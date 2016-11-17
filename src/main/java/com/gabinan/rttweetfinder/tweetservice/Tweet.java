package com.gabinan.rttweetfinder.tweetservice;

import lombok.Getter;
import org.json.JSONObject;

/**
 * Defining how a tweet looks like.
 * This class can be expanded easily w/ other tweet info.
 * 
 * @author Gabriel Nan
 */

public class Tweet {
    
 @Getter  private final String userName;
 @Getter  private final String tweet;
 
 public Tweet(String userName, String tweet){
     this.tweet = tweet;
     this.userName = userName;
 }

 /**
  * 
  * @return Json like string with username and tweet keys. 
  */
    @Override
    public String toString() {
        return new JSONObject().put("Username", this.userName).put("Tweet", this.tweet).toString();
    }
 

}
