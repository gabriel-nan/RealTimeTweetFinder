package com.gabinan.rttweetfinder.subscribers;

import com.gabinan.rttweetfinder.config.GlobalConfig;
import com.gabinan.rttweetfinder.tweetservice.Tweet;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import rx.Subscriber;

/**
 * JavaRX Subscriber that appends the tweets to a file.
 * 
 * @author Gabriel Nan
 */
@Log4j
public class RxSubscriber extends Subscriber {

    private GlobalConfig config;
    private BufferedWriter bw;

    @Autowired
    public RxSubscriber(GlobalConfig config) {
        this.config = config;

        try {
            this.bw = new BufferedWriter(new FileWriter(new File(this.config.get("tweetFile")), true));
        } catch (IOException ex) {
            myLogger.error("Tweets File broken or does not exist!");
            myLogger.error(ex.getMessage());
        }

    }

    /**
     * Unsubscribe from the Observable, flush and close File Writer when finished.
     */
    @Override
    public void onCompleted() {
        this.unsubscribe();

        try {
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            myLogger.error(ex.getMessage());
        }

    }

    @Override
    public void onError(Throwable thrwbl) {
        myLogger.error("Couldn't complete writing tweets to file due to unexpected error.");
        myLogger.error(thrwbl.getMessage());
    }

    
    /**
     * Cast the object to a tweet and append it to the file.
     * 
     * @param t - Tweet 
     */
    @Override
    public void onNext(Object t) {
        Tweet tweet = (Tweet) t;
        try {
            bw.append(tweet.toString());
            bw.append(System.lineSeparator());
        } catch (IOException ex) {
            myLogger.error("Error while appending to tweets file!");
            myLogger.error(ex.getMessage());
        }
    }

}
