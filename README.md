# Real Time Tweet Finder

This small project connects to a Twitter sample stream, that streams in real time max. 60 tweets/second.
The connection to the stream is made using personal twitter dev credentials.
(You can get a free twitter dev account with your regular twitter account - for more info visit MyApps section: https://dev.twitter.com/streaming/overview)

Spring Boot made easy the interogation of real time tweets through a REST API which returns the tweets as a JSON Array.
The API can be accesed (by default on localhost port 8080) at http://localhost:8080/getmesometweets?filter=&tweets=, where the "filter" parameter represents a string in the tweet to find (not case sensitive) and the "tweets" parameter is the number of tweets to return.

Once a request is sent, the app connects to the twitter stream and using Java 8 Stream filtering and mapping, returns the requested real-time tweets.
Also i use a JavaRX Observable created from the stream, the Observer subscribed to this Observable appends the filtered tweets into a file (path defined in config) in an asynchronous manner.

This app was made to compare the Java 8 Parallel Stream execution and the parallel asynchronous task execution of ReactiveX.

The HTTP Post method may be slower than appending to a local file, but you can switch the functionalities to see who wins this battle for yourself :)
