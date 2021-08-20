package com.laynya.tenshinhan;

import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class OAuth
{
  static ConfigurationBuilder cb = new ConfigurationBuilder();
  
  Twitter OAuth;
  Properties properties = new Properties();
  RequestToken reqToken;

  OAuth() throws TwitterException {
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey("IiLQgteC8DAzhTqX6bGNsGK8g")
      .setOAuthConsumerSecret("LjTZ78z7eJqbpHWrXbp0yNYlbAozATUcIkpekHnRVsFXGudjCk")
      .setOAuthAccessToken(null)
      .setOAuthAccessTokenSecret(null);
    
    this.OAuth = (new TwitterFactory(cb.build())).getInstance();
    this.reqToken = this.OAuth.getOAuthRequestToken();
  }
  
  public void Auth() throws IOException {
    String url = this.reqToken.getAuthorizationURL();
    Desktop desktop = Desktop.getDesktop();
    
    try {
      URI uri = new URI(url);
      desktop.browse(uri);
    } catch (URISyntaxException ex) {
      Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
  }
  
  public void secondAuth(String pin) throws IOException, TwitterException {
    AccessToken accessToken = this.OAuth.getOAuthAccessToken(this.reqToken, pin);
    storeAccessToken(this.OAuth.verifyCredentials().getId(), accessToken);
  }

  
  private void storeAccessToken(long useId, AccessToken accessToken) {
    this.properties.setProperty("useId", String.valueOf(useId));
    this.properties.setProperty("accessToken", String.valueOf(accessToken.getToken()));
    this.properties.setProperty("accessTokenSecret", String.valueOf(accessToken.getTokenSecret()));
    
    try {
      this.properties.store(new FileOutputStream("conf.properties"), "OAuth");
    } catch (FileNotFoundException ex) {
      Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (IOException ex) {
      Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    
    MainWindow.exit();
  }
}