package com.laynya.tenshinhan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationContext;

public class Twit
{
  static ConfigurationBuilder cb = new ConfigurationBuilder();
  
  Properties properties = new Properties();
  Configuration conf = ConfigurationContext.getInstance();
  OAuthAuthorization oauth = new OAuthAuthorization(this.conf);

  Twitter twitter;

  public Twit() throws FileNotFoundException, IOException {
    cb.setDebugEnabled(false)
      .setOAuthConsumerKey("IiLQgteC8DAzhTqX6bGNsGK8g")
      .setOAuthConsumerSecret("LjTZ78z7eJqbpHWrXbp0yNYlbAozATUcIkpekHnRVsFXGudjCk")
      .setOAuthAccessToken(null)
      .setOAuthAccessTokenSecret(null);
    
    loadToken();
    
    this.twitter = (new TwitterFactory(cb.build())).getInstance();
  }
  
  public void update(String text) throws TwitterException {
    Status status = this.twitter.updateStatus(text);
  }
  
  public void update(String text, Long id) throws TwitterException {
    StatusUpdate su = new StatusUpdate(text);
    su.setInReplyToStatusId(id.longValue());
    Status status = this.twitter.updateStatus(su);
  }
  
  public void favorite(Long id) throws TwitterException {
    Status status = this.twitter.createFavorite(id.longValue());
  }
  
  public void retweet(Long id) throws TwitterException {
    Status status = this.twitter.retweetStatus(id.longValue());
  }
  
  public void remove(Long id) throws TwitterException {
    Status status = this.twitter.destroyStatus(id.longValue());
  }
  
  public void removeFav(Long id) throws TwitterException {
    Status status = this.twitter.destroyFavorite(id.longValue());
  }
  
  public String getScreenName() throws TwitterException {
    return this.twitter.getScreenName();
  }
  
  public String getScreenName(long id) throws TwitterException {
    Status status = this.twitter.showStatus(id);
    return status.getUser().getScreenName();
  }
  
  public ResponseList<Status> getHomeTimeline() throws TwitterException{
      return this.twitter.getHomeTimeline();
  }
  
  public static AccessToken loadAccessToken() {
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream("conf.properties"));
    } catch (IOException ex) {
      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return new AccessToken(properties.getProperty("accessToken"), properties.getProperty("accessTokenSecret"));
  }
  
  public static void loadToken() {
    if ((new File("conf.properties")).exists()) {
      AccessToken accessToken = loadAccessToken();
      
      cb.setDebugEnabled(false)
        .setOAuthConsumerKey("IiLQgteC8DAzhTqX6bGNsGK8g")
        .setOAuthConsumerSecret("LjTZ78z7eJqbpHWrXbp0yNYlbAozATUcIkpekHnRVsFXGudjCk")
        .setOAuthAccessToken(accessToken.getToken())
        .setOAuthAccessTokenSecret(accessToken.getTokenSecret());
    } 
  }
}