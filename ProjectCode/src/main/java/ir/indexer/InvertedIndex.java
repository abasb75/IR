package ir.indexer;

import irpagu.object.Document;
import irpagu.object.Token;
import irpagu.object.Token.TokenNode;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class InvertedIndex {

    int max=1,min=1,sum=0;
    List<Token> tokenList ;
    public List<Token> list;
    public InvertedIndex(List<Token> tokenList){
        this.tokenList = tokenList;
        list = new ArrayList<Token>();
    }


    //CREATE INVETED INDEX Method
    public void doIndexing(JProgressBar jProgressBar){
        String temp = "";
        int tokenId = 1;
        Token token = null;
        int tempdocId = 0;
        int ctr = 0;
        for (int i=0;i<tokenList.size();i++){
            System.out.println("Indexing " + ctr*100/tokenList.size() + "%");
            if (jProgressBar!=null){
                jProgressBar.setValue(81+(ctr*10/tokenList.size()));
            }
            ctr++;
            int id = i+1;
            //  System.out.print(id);
            String tokenString = tokenList.get(i).string;
            //  System.out.print(tokenString);
            int docId = tokenList.get(i).getDocId();
            // System.out.println(docId);

            if(tokenString.equals(temp)){
                token.cf++;
                if(docId == tempdocId){
                    token.updateLastNodeTfUpdate();
                }else{
                    token.updateTokenNode(docId);
                    tempdocId = docId;
                    token.dF++;
                }
                token.cf++;

            }
            else{
                if(token!=null){
                    if(token.dF>max){
                        this.max=token.dF;
                    }else if(token.dF<min){
                        this.min=token.dF;
                    }
                    this.sum = this.sum + token.dF;

                    list.add(token);
                }
                token = null;
                temp = tokenString;
                token = new Token(docId);
                token.id = tokenId;
                tokenId++;
                token.string = tokenString;
                token.cf = 1;
                token.dF = 1;
                tempdocId = docId;
            }
        }
    }

    //create .txt files df and tf and more ...
    public void printWorld(){

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        try {
            File file = new File(s+"/txtFiles/max-min-avg-of-dF.txt");
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("creating df file ... ");
            }else{
                file.delete();
                System.out.println("updating file...");
                file.createNewFile();

            }
            BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
            int avg  =  this.sum/list.size();
            String str3445 = "max :"+(this.max+"") + " \n"+"min :" + this.min +"\n" + "avg :"+avg;
            buffer.write(str3445);
            buffer.close();





            System.out.println("finish writing to df file");
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            File file = new File(s+"/txtFiles/df.txt");
            if (file.exists()){
                file.delete();
                System.out.println("updating file...");
                file.createNewFile();

            }
            BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
            for (int i=0;i<list.size();i++){
                Token token = list.get(i);
                System.out.print("\" " + token.string+" \" , ");
                System.out.print(" " + token.id+" , ");
                System.out.print(" " + token.dF+" , ==>> ");
                token.printNodes();
                System.out.println("");
                buffer.write("\""+token.string + "\","+token.id+","+token.dF+"\n");
            }
            buffer.close();





            System.out.println("finish writing to df file");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File file = new File(s+"/txtFiles/tf.txt");
            if (file.exists()){
                file.delete();
                System.out.println("updating tf file...");
                file.createNewFile();

            }
            BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
            for (int i=0;i<list.size();i++){
                Token token = list.get(i);
                buffer.write(token.printTF());
            }
            buffer.close();





            System.out.println("finish writing to df file");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
