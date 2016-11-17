package com.gabinan.rttweetfinder.tweetservice;

import com.gabinan.rttweetfinder.config.GlobalConfig;
import com.gabinan.rttweetfinder.subscribers.RxSubscriber;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.log4j.Log4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Service which does all the work of connecting to stream and building tweets.
 *
 * @author Gabriel Nan
 */
@Service
@Log4j
public class TweetService {

    private final GlobalConfig config;
    private final RxSubscriber processor;
    private final String TWEETS_FILE;

    //Credentials for connecting to twitter stream
    private final String CONSUMER_KEY;
    private final String CONSUMER_SECRET;
    private final String ACCES_TOKEN;
    private final String ACCES_SECRET;
    private final String StreamApiURL;

    @Autowired
    public TweetService(GlobalConfig config, RxSubscriber processor) {
        this.config = config;
        this.processor = processor;

        this.CONSUMER_KEY = this.config.get("consumerKey");
        this.CONSUMER_SECRET = this.config.get("consumerSecret");
        this.ACCES_TOKEN = this.config.get("accesToken");
        this.ACCES_SECRET = this.config.get("accesSecret");
        this.TWEETS_FILE = this.config.get("tweetFile");
        this.StreamApiURL = this.config.get("streamApiURL");

    }

    /**
     * Connects to twitter stream, filters tweets and returns them in a set.
     * Also appends the tweets to a file using a reactive approach.
     * 
     * @param filter - String to filter tweets by
     * @param nbOfTweets - number of tweets to return
     * @return Set<Tweet>(nbOfTweets))
     */
    public Set<Tweet> getTweets(String filter, int nbOfTweets) {

        Set<Tweet> tweetSet = new HashSet<>(nbOfTweets);

        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(this.CONSUMER_KEY, this.CONSUMER_SECRET);
        consumer.setTokenWithSecret(this.ACCES_TOKEN, this.ACCES_SECRET);
        HttpGet request = new HttpGet(StreamApiURL);

        try {
            consumer.sign(request);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            createTweetFile();
            writeTweetsToFile(br, nbOfTweets, filter);

            br.lines().parallel().filter(line -> line.contains("created_at"))
                    .map(line -> new JSONObject(line))
                    .map(json -> buildTweet(json))
                    .filter(tweet -> StringUtils.containsIgnoreCase(tweet.getTweet(), filter))
                    .limit(nbOfTweets)
                    .forEach(tweet -> tweetSet.add(tweet));

        } catch (Exception ex) {
            myLogger.error("Error in getTweets method. Possible issues: Connecting to twitter stream, processing twitter stream or creating tweet file.");
            myLogger.error(ex.getMessage());
        }

        return tweetSet;
    }

    /**
     * Twitter sends tweets as a Json Object. 
     * Decode Json and build tweet.
     * 
     * @param json - Stream tweet as Json object
     * @return Tweet
     */
    private Tweet buildTweet(JSONObject json) {
        String tweet = "";
        String userName = "";
        try {
            tweet = new String(json.getString("text").getBytes("UTF-8"), "UTF-8").replace("\n", "");
            JSONObject user = json.getJSONObject("user");
            userName = new String(user.getString("screen_name").getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            myLogger.error("Couldn't build tweet. Here's the raw json tweet: " +json);
            myLogger.error(ex.getMessage());
        }

        return new Tweet(userName, tweet);
    }

    /**
     * Reactive magic happens here.
     * Defined the observable and observer.
     * 
     * @param br BufferedReader from the Twitter Stream
     * @param nbOfTweets to limit
     * @param filter 
     */
    private void writeTweetsToFile(BufferedReader br, int nbOfTweets, String filter) {
        Observable twitterResponse = Observable.from(br.lines()::iterator)
                .filter(line -> line.contains("created_at"))
                .map(line -> new JSONObject(line))
                .map(json -> buildTweet(json))
                .filter(tweet -> StringUtils.containsIgnoreCase(tweet.getTweet(), filter))
                .limit(nbOfTweets);

        twitterResponse.subscribe(processor);
    }

    /**
     * Make sure the tweets file exists.
     * 
     * @throws IOException 
     */
    private void createTweetFile() throws IOException {
        File file = new File(TWEETS_FILE);

        if (!file.exists()) {
            Files.createDirectories(Paths.get(file.getParent()));
            Files.createFile(Paths.get(TWEETS_FILE));
        }
    }

}
