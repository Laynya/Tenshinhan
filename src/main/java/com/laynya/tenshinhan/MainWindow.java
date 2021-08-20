package com.laynya.tenshinhan;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
//import twitter4j.StallWarning;
import twitter4j.Status;
//import twitter4j.StatusListener;
//import twitter4j.StreamListener;
import twitter4j.TwitterException;
//import twitter4j.TwitterStream;
//import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class MainWindow extends JFrame {
  String version = "0.1";
  static MainWindow mainWindow;
  static Setting settingWindow;
  Map<String, Long> tlmap = new HashMap<>();
  Map<Long, String> repIdmap = new HashMap<>();
  Twit twit = new Twit();
  boolean replyStatus = false;
  boolean decorated = true;
  long replyId;
  Point location = new Point();
  int mouseX = 0;
  int mouseY = 0;
  
  static boolean auth = false;
  boolean AlwaysOnTop = false;
  static ConfigurationBuilder cb = new ConfigurationBuilder();

  private JButton jButton1;
  private JList<String> jList1;
  private JScrollPane jScrollPane1;
  private JTextField jTextField1;
  
  public static void main(String[] args) throws TwitterException, IOException {
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Windows".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        } 
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
        }
      } 
    } catch (ClassNotFoundException|InstantiationException|IllegalAccessException|javax.swing.UnsupportedLookAndFeelException ex) {
      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 

    settingWindow = new Setting();
    
    cb.setDebugEnabled(false)
      .setOAuthConsumerKey("IiLQgteC8DAzhTqX6bGNsGK8g")
      .setOAuthConsumerSecret("LjTZ78z7eJqbpHWrXbp0yNYlbAozATUcIkpekHnRVsFXGudjCk")
      .setOAuthAccessToken(null)
      .setOAuthAccessTokenSecret(null);
    
    loadToken();
    
    mainWindow = new MainWindow();
    
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Rectangle desktopBounds = env.getMaximumWindowBounds();
    mainWindow.setBounds(desktopBounds.width - mainWindow.getWidth(), desktopBounds.height - mainWindow.getHeight(), mainWindow.getWidth(), mainWindow.getHeight());
    
    EventQueue.invokeLater(() -> mainWindow.setVisible(true));
  }

  public MainWindow() throws TwitterException, IOException {
    setUndecorated(this.decorated);

    initComponents();

    this.jList1.setModel(new DefaultListModel<>());
    timelineStreaming();
  }

  private void initComponents() {
    this.jScrollPane1 = new JScrollPane();
    this.jList1 = new JList<>();
    this.jTextField1 = new JTextField();
    this.jButton1 = new JButton();
    
    setDefaultCloseOperation(3);
    setTitle("Tenshinhan v." + this.version);
    
    this.jList1.setSelectionMode(0);
    this.jList1.addMouseMotionListener(new MouseMotionAdapter() {
          @Override
          public void mouseDragged(MouseEvent evt) {
            MainWindow.this.jList1MouseDragged(evt);
          }
        });
    this.jList1.addMouseListener(new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent evt) {
            MainWindow.this.jList1MousePressed(evt);
          }
        });
    this.jList1.addKeyListener(new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent evt) {
              try {
                  MainWindow.this.jList1KeyPressed(evt);
              } catch (URISyntaxException ex) {
                  Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
        });
    this.jScrollPane1.setViewportView(this.jList1);
    
    this.jTextField1.addMouseMotionListener(new MouseMotionAdapter() {
          @Override
          public void mouseDragged(MouseEvent evt) {
            MainWindow.this.jTextField1MouseDragged(evt);
          }
        });
    this.jTextField1.addMouseListener(new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent evt) {
            MainWindow.this.jTextField1MousePressed(evt);
          }
        });
    this.jTextField1.addKeyListener(new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent evt) {
            MainWindow.this.jTextField1KeyPressed(evt);
          }
        });
    
    this.jButton1.setText("Send");
    this.jButton1.setCursor(new Cursor(0));
    this.jButton1.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent evt) {
            MainWindow.this.jButton1MouseClicked(evt);
          }
        });
    
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout
        .createParallelGroup(GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addComponent(this.jTextField1, -1, 352, 32767)
          .addGap(0, 0, 0)
          .addComponent(this.jButton1))
        .addComponent(this.jScrollPane1));
    
    layout.setVerticalGroup(layout
        .createParallelGroup(GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
            .addComponent(this.jTextField1, -2, -1, -2)
            .addComponent(this.jButton1))
          .addGap(0, 0, 0)
          .addComponent(this.jScrollPane1, -1, 279, 32767)));

    pack();
  }

  private void jButton1MouseClicked(MouseEvent evt) {
    tweetUpdate(this.jTextField1.getText());
    this.jTextField1.setText("");
  }

  private void jTextField1KeyPressed(KeyEvent evt) {
    int keycode = evt.getKeyCode();
    int mod = evt.getModifiersEx();
    if (keycode == 10 && (mod & 0x80) != 0) {
      tweetUpdate(this.jTextField1.getText());
      this.jTextField1.setText("");
    } 
  }

  private void jList1KeyPressed(KeyEvent evt) throws URISyntaxException {
    int keycode = evt.getKeyCode();
    int mod = evt.getModifiersEx();
    
    @SuppressWarnings("UnusedAssignment")
    String tweetText = "";
    DefaultListModel<String> listModel = (DefaultListModel)this.jList1.getModel();
    try {
      if ((mod & 0x80) != 0) {
        long tweetId; Pattern pattern; Matcher matcher; Pattern pattern2; Rectangle size; Matcher matcher2; switch (keycode) {
          case 70:
            tweetId = (this.tlmap.get(this.jList1.getSelectedValue()));
            this.twit.favorite(tweetId);
            this.tlmap.remove(this.jList1.getSelectedValue());
            this.tlmap.put("☆ " + (String)this.jList1.getSelectedValue(), tweetId);
            listModel.set(this.jList1.getSelectedIndex(), "☆ " + (String)this.jList1.getSelectedValue());
            break;
          case 84:
            tweetId = (this.tlmap.get(this.jList1.getSelectedValue()));
            this.twit.retweet(tweetId);
            this.tlmap.remove(this.jList1.getSelectedValue());
            this.tlmap.put("[RT] " + (String)this.jList1.getSelectedValue(), tweetId);
            listModel.set(this.jList1.getSelectedIndex(), "[RT] " + (String)this.jList1.getSelectedValue());
            break;
          case 82:
            this.replyStatus = true;
            this.replyId = (this.tlmap.get(this.jList1.getSelectedValue()));
            this.jTextField1.setText(this.repIdmap.get(this.replyId));
            this.jTextField1.requestFocusInWindow();
            break;
          case 88:
            tweetId = (this.tlmap.get(this.jList1.getSelectedValue()));
            pattern = Pattern.compile("☆ ");
            matcher = pattern.matcher(this.jList1.getSelectedValue());
            if (matcher.find()) {
              this.twit.removeFav(tweetId);
              tweetText = ((String)this.jList1.getSelectedValue()).replace("☆ ", "");
              this.tlmap.remove(this.jList1.getSelectedValue());
              this.tlmap.put(tweetText, tweetId);
              listModel.set(this.jList1.getSelectedIndex(), tweetText); break;
            } 
            pattern2 = Pattern.compile("(@" + this.twit.getScreenName() + ")");
            matcher2 = pattern2.matcher(this.jList1.getSelectedValue());
            if (matcher2.find()) {
              this.twit.remove(tweetId);
              this.tlmap.remove(this.jList1.getSelectedValue());
              listModel.remove(this.jList1.getSelectedIndex());
            } 
            break;
          
          case 79:
            tweetId = (this.tlmap.get(this.jList1.getSelectedValue()));
            try {
              Desktop desktop = Desktop.getDesktop();
              
              desktop.browse(new URI("https://twitter.com/" + this.twit.getScreenName(tweetId) + "/status/" + tweetId));
            }
            catch (URISyntaxException|IOException ex) {
              ex.printStackTrace();
            } 
            break;
          case 81:
            System.exit(0);
            break;
          case 80:
            this.AlwaysOnTop = !this.AlwaysOnTop;
            mainWindow.setAlwaysOnTop(this.AlwaysOnTop);
            break;
          case 83:
            settingWindow.setVisible(true);
            break;
          case 87:
            size = new Rectangle(mainWindow.getX(), mainWindow.getY(), mainWindow.getWidth(), mainWindow.getHeight());
            if (this.decorated) {
              mainWindow.dispose();
              
              mainWindow.setUndecorated(false);
              
              mainWindow.setVisible(true);
              this.decorated = false; break;
            } 
            mainWindow.dispose();
            mainWindow.setBounds(size);
            mainWindow.setUndecorated(true);
            mainWindow.setVisible(true);
            this.decorated = true;
            break;
        } 
      
      } 
    } catch (TwitterException ex) {
      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
    } 
  }

  private void jList1MouseDragged(MouseEvent evt) {
    moveForm();
  }

  private void jList1MousePressed(MouseEvent evt) {
    getMouselocation();
  }

  private void jTextField1MouseDragged(MouseEvent evt) {
    moveForm();
  }

  private void jTextField1MousePressed(MouseEvent evt) {
    getMouselocation();
  }

  private void timelineStreaming() throws TwitterException, IOException {
    /*
    StatusListener listener = new StatusListener()
      {
        public void onStatus(Status status) {
          MainWindow.this.addTimeline(status.getUser().getName() + "(@" + status.getUser().getScreenName() + ")" + " : " + status.getText());
          MainWindow.this.tlmap.put(status.getUser().getName() + "(@" + status.getUser().getScreenName() + ")" + " : " + status.getText(), Long.valueOf(status.getId()));
          Pattern pattern = Pattern.compile("@[\\w]+");
          Matcher matcher = pattern.matcher(status.getText());
          StringBuilder idList = new StringBuilder();
          while (matcher.find()) {
            try {
              if (!matcher.group().equals("@" + MainWindow.this.twit.getScreenName()))
                idList.append(matcher.group()).append(" "); 
            } catch (TwitterException ex) {
              Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
            } 
          } 
          MainWindow.this.repIdmap.put(Long.valueOf(status.getId()), "@" + status.getUser().getScreenName() + " " + idList.toString());
        }

        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

        public void onScrubGeo(long userId, long upToStatusId) {}

        public void onStallWarning(StallWarning warning) {}

        public void onException(Exception ex) {}
      };
    TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
    TwitterStream twitterStream = tf.getInstance();
    twitterStream.addListener((StreamListener)listener);
    twitterStream.user();
    */
    
    System.out.println("Showing home timeline.");
    List<Status> statuses = twit.getHomeTimeline();
    refreshTimeline(statuses);
  }
  
  public void refreshTimeline(List statuses){
      statuses.forEach(new Consumer<Status>() {
          @Override
          public void accept(Status status) {
              //System.out.println(status.getUser().getName() + ":" +
              //        status.getText());
              
              MainWindow.this.addTimeline(status.getUser().getName() + "(@" + status.getUser().getScreenName() + ")" + " : " + status.getText());
              MainWindow.this.tlmap.put(status.getUser().getName() + "(@" + status.getUser().getScreenName() + ")" + " : " + status.getText(), status.getId());
              Pattern pattern = Pattern.compile("@[\\w]+");
              Matcher matcher = pattern.matcher(status.getText());
              StringBuilder idList = new StringBuilder();
              while (matcher.find()) {
                  try {
                      if (!matcher.group().equals("@" + MainWindow.this.twit.getScreenName()))
                          idList.append(matcher.group()).append(" ");
                  } catch (TwitterException ex) {
                      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
                  }
              }
              MainWindow.this.repIdmap.put(status.getId(), "@" + status.getUser().getScreenName() + " " + idList.toString());
          }
      });
  }

  private void addTimeline(String str) {
    DefaultListModel<String> listModel = (DefaultListModel)this.jList1.getModel();
    
    listModel.add(0, str);
  }

  private void tweetUpdate(String str) {
    try {
      if (this.replyStatus && this.replyId == (this.tlmap.get(this.jList1.getSelectedValue()))) {
        this.twit.update(str, this.replyId);
        this.replyStatus = false;
      } else {
        this.twit.update(str);
      } 
    } catch (TwitterException ex) {
      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
    } 
  }

  private void getMouselocation() {
    PointerInfo pi = MouseInfo.getPointerInfo();
    Point pt = pi.getLocation();
    SwingUtilities.convertPointFromScreen(pt, this);
    this.mouseX = pt.x;
    this.mouseY = pt.y;
  }

  private void moveForm() {
    PointerInfo pi = MouseInfo.getPointerInfo();
    Point pt = pi.getLocation();
    mainWindow.setLocation(pt.x - this.mouseX, pt.y - this.mouseY);
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
      
      auth = true;
    } else {
      settingWindow.setVisible(true);
    } 
  }
  
  public static void exit() {
    System.exit(0);
  }
}
