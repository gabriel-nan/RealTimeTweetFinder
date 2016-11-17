package com.gabinan.rttweetfinder.api;

import com.gabinan.rttweetfinder.tweetservice.Tweet;
import com.gabinan.rttweetfinder.tweetservice.TweetService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller
 *
 * @author Gabriel Nan
 */
@RestController
public class Controller {

    @Autowired
    private TweetService tweetService;

    /**
     *
     * @param filter is mandatory, represents the string to filter the tweets by
     * @param nbOfTweets number of tweets to return
     * @return A set of Set<Tweet>(nbOfTweets)
     */
    @RequestMapping(value = "/getmesometweets", method = RequestMethod.GET)
    public Set<Tweet> getTwitterSet(@RequestParam(value = "filter") String filter, @RequestParam(value = "tweets", defaultValue = "10") String nbOfTweets) {

        return tweetService.getTweets(filter, Integer.parseInt(nbOfTweets));

    }

}
