//بسم الله الرحمن الرحیم
package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SpalshForm extends JFrame {
    JLabel godName ;
    JLabel background;
    public JProgressBar jProgressBar;
    public JLabel copyRigth,progressDetail;
    public SpalshForm(){
        godName = new JLabel("بسم الله الرحمن الرحیم",SwingConstants.CENTER);
        initialForm();
        this.add(godName);
        godName.setBounds(50,150,400,50);
        godName.setFont(FontManager.createFont("BNaznnBd.ttf",28f));
    }
    void initialForm(){
        this.setLayout(null);
        this.setSize(500,350);
        this.setResizable(false);
        this.setUndecorated(true);
        this.getContentPane().setBackground(Color.white);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }

    public void setBackgroundImage(){
        jProgressBar = new JProgressBar();
        jProgressBar.setMinimum(0);
        jProgressBar.setMaximum(100);
        jProgressBar.setBackground(new Color(0,0,0,0));
        jProgressBar.setBorder(BorderFactory.createEmptyBorder());
        jProgressBar.setBorderPainted(false);
        jProgressBar.setBounds(0,300,500,3);
        jProgressBar.setForeground(new Color(52,152,219));
        //jProgressBar.setForeground(Color.white);


        this.godName.setVisible(false);
        this.getContentPane().setVisible(false);
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        ImageIcon img = new ImageIcon(s+"/image/background.jpg");
        background = new JLabel("",img,JLabel.LEFT);
        background.setBounds(0,0,500,350);
        background.setVisible(true);

        copyRigth = new JLabel("عباس باقری",SwingConstants.CENTER);
        copyRigth.setFont(FontManager.createFont("BNaznnBd.ttf",13f));
        copyRigth.setForeground(new Color(124, 124, 124));
        copyRigth.setBounds(200,315,100,20);

        progressDetail = new JLabel("در حال خواندن اسناد از فایل ... ",SwingConstants.RIGHT);
        progressDetail.setFont(FontManager.createFont("BNazanin.ttf",12f));
        progressDetail.setBounds(0,285,475,15);


        this.add(progressDetail);
        this.add(copyRigth);
        this.add(jProgressBar);
        this.add(background);
        jProgressBar.setValue(0);


        this.getContentPane().setVisible(true);

    }
}
