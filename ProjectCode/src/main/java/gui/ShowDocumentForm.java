package gui;

import ir.parser.UrlManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShowDocumentForm extends JFrame {
    public ShowDocumentForm(int documentId,int fileId,String documentTitle) throws FileNotFoundException {
        this.setLayout(null);
        this.setSize(680,450);
        this.getContentPane().setBackground(new Color(245,245,245));
        this.setResizable(false);
        String documentBodyText = getBodyOfText(documentId,fileId);
        JLabel title = new JLabel(documentTitle);
        JTextArea documentBody = new JTextArea(documentBodyText);
        title.setBounds(20,5,640,50);
        documentBody.setBounds(25,60,630,300);
        title.setFont(FontManager.createFont("Roboto-Black.ttf",16f));
        documentBody.setMargin(new Insets(5,5,5,5));
        documentBody.setLineWrap(true);
        this.add(title);
        this.add(documentBody);
    }
    private String getBodyOfText(int documentId, int fileId) throws FileNotFoundException {
        String name = "reuters21578/reut2-0" + ((fileId>9)?fileId:"0"+fileId) + ".sgm" ;
        UrlManager urlManager = new UrlManager();
        File file = new  File(urlManager.getPath(name));
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()){
            String str = scanner.next();
            if (str.matches("NEWID=\"(.*)")){
                String[] strArray = str.split("\"");
                if (Integer.parseInt(strArray[1])==documentId){
                    System.out.println(strArray[1]);
                    String str1 = scanner.next();
                    while (!str1.matches("</DATELINE><BODY>(.*)")){
                        str1 = scanner.next();
                    }
                    System.out.println("Reading Body ... ");
                    String str10 = str1.split("</DATELINE><BODY>")[1];
                    String str2 = scanner.next();
                    while (!str2.matches("(.*)</BODY></TEXT>")) {
                        str10 = str10 + " " + str2;
                        str2 = scanner.next();
                    }
                    str10 = str10 + " " + str2.split("</BODY></TEXT>")[0];
                    return str10;
                }
            }
        }

        return "";
    }
}
