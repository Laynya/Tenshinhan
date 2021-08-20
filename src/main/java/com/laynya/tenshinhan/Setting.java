package com.laynya.tenshinhan;

import java.awt.Button;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import twitter4j.TwitterException;

public class Setting
  extends JFrame
{
  static boolean visible = false;
  boolean authSuccess = false;
  OAuth twitterOAuth;
  private Button button1;
  private Button button2;
  
  public Setting() throws TwitterException {
    this.twitterOAuth = new OAuth();
    initComponents();
    setDefaultCloseOperation(2);
  }
  
  private Label label1;
  private Label label2;
  private Label label3;
  private Label label4;
  private TextArea textArea1;
  private TextField textField1;
  
  private void initComponents() {
    this.label2 = new Label();
    this.label1 = new Label();
    this.textField1 = new TextField();
    this.button1 = new Button();
    this.label3 = new Label();
    this.label4 = new Label();
    this.textArea1 = new TextArea();
    this.button2 = new Button();
    
    this.label2.setText("label2");
    
    setDefaultCloseOperation(3);
    
    this.label1.setText("OAuth認証");
    
    this.button1.setLabel("コード取得");
    this.button1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            Setting.this.button1ActionPerformed(evt);
          }
        });
    
    this.label3.setText("その他設定は準備中です。。。");
    
    this.label4.setText("キーボードショートカット一覧");
    
    this.textArea1.setEditable(false);
    this.textArea1.setName("");
    this.textArea1.setText("Ctrl + F : 選択中のツイートをふぁぼります。\nCtrl + T : 選択中のツイートをRTします。\nCtrl + R : 選択中のツイートにリプライをします。\nCtrl + X : 選択中のツイートを削除します。(ふぁぼの場合はふぁぼ、RTの場合はRTを削除)\nCtrl + O : 選択中のツイートをブラウザでオープンします。\nCtrl + W : ウィンドウの枠を表示(リサイズとかできるよ！)\nCtrl + P : ウィンドウを最前面に表示\nCtrl + S : 設定画面(この画面)を表示\nCtrl + Q : プログラムを終了します。(現在リスト上でのみ有効)");
    
    this.button2.setLabel("OK");
    this.button2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            Setting.this.button2ActionPerformed(evt);
          }
        });
    
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout
        .createParallelGroup(GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addContainerGap()
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
              .addGap(0, 0, 32767)
              .addComponent(this.button2, -2, -1, -2))
            .addComponent(this.textArea1, -1, -1, 32767)
            .addGroup(layout.createSequentialGroup()
              .addComponent(this.label1, -2, -1, -2)
              .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
              .addComponent(this.textField1, -1, -1, 32767)
              .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
              .addComponent(this.button1, -2, 66, -2))
            .addGroup(layout.createSequentialGroup()
              .addComponent(this.label3, -2, -1, -2)
              .addGap(0, 208, 32767))
            .addComponent(this.label4, -1, 380, 32767))
          .addContainerGap()));
    
    layout.setVerticalGroup(layout
        .createParallelGroup(GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addContainerGap()
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
            .addComponent(this.button1, -2, -1, -2)
            .addComponent(this.textField1, -2, -1, -2)
            .addComponent(this.label1, -2, -1, -2))
          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
          .addComponent(this.label3, -2, 20, -2)
          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
          .addComponent(this.label4, -2, -1, -2)
          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
          .addComponent(this.textArea1, -2, 178, -2)
          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
          .addComponent(this.button2, -2, -1, -2)
          .addContainerGap()));

    pack();
  }
  
  private void button1ActionPerformed(ActionEvent evt) {
    if (this.textField1.getText().equals("")) {
      try {
        this.twitterOAuth.Auth();
      } catch (IOException ex) {
        Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, (String)null, ex);
      } 
      this.button1.setLabel("認証");
    } else {
      try {
        this.twitterOAuth.secondAuth(this.textField1.getText());
      } catch (TwitterException|IOException ex) {
        Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, (String)null, ex);
      } 
      this.button1.setLabel("コード取得");
    } 
  }

  private void button2ActionPerformed(ActionEvent evt) {
    setVisible(false);
  }

  public static void main(String[] args) {
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        } 
      } 
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (InstantiationException ex) {
      Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 

    EventQueue.invokeLater(() -> {
          
          try {
            (new Setting()).setVisible(visible);
          } catch (TwitterException ex) {
            Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
          } 
        });
  }
}