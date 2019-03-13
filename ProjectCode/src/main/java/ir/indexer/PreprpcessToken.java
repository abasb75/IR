package ir.indexer;

import ir.parser.UrlManager;
import irpagu.object.Document;
import irpagu.object.Token;

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PreprpcessToken {
    int numberBeforStopWord;
    int numberAfterStopWord;
    int numberBeforeChar;
    int numberBeforStemming;
    int numberAfterStemming;
    int numberAfterChars;
    String[] stopWords;
    List<Document> documentList;
    List<Token> tokensList;

    public PreprpcessToken() throws FileNotFoundException {
        this.documentList = new ArrayList<Document>();
        this.tokensList = new ArrayList<Token>();
        this.numberBeforStopWord = 0;
        this.numberBeforStemming = 0;
        this.numberAfterStemming = 0;
        this.numberBeforeChar = 0;
        this.numberAfterStopWord = 0;
        this.numberAfterChars = 0;
        this.stopWords = this.loadStopWords();
    }

    //load from database and save tokens in database
    public List<Token> indexer(List<Document> documentList, JProgressBar jProgressBar) throws FileNotFoundException {
        for (int i=0;i<documentList.size();i++){
            Document document = documentList.get(i);
            document = toLowerCase(document);
            this.documentList.add(document);
        }
        System.out.println(this.documentList.size());
        int counter = documentList.size()/25;
        if (jProgressBar!=null){
            jProgressBar.setValue(55);
        }

        for (int i=0;i<this.documentList.size();i++){
            String sql = "INSERT INTO wordTokens(tokenString,tokenDocId) VALUES";

            if (jProgressBar!=null) {
                jProgressBar.setValue(55 + (i / 759));
            }
            Document document = this.documentList.get(i);
            String[] strTemp = document.getDocBody().split(" ");
            this.numberBeforeChar = this.numberBeforeChar+strTemp.length;
            sql = t‫‪okenizing‬‬(document.getDocBody(),document.getDocId(),sql);
            strTemp = document.getDocTitle().split(" ");
            this.numberBeforeChar = this.numberBeforeChar+strTemp.length;
            sql = t‫‪okenizing‬‬(document.getDocTitle(),document.getDocId(),sql);
            if(document.getCompany()!=null && !document.getCompany().equals("")){
                strTemp = document.getCompany().split(" ");
                this.numberBeforeChar = this.numberBeforeChar+strTemp.length;
                sql = t‫‪okenizing‬‬(document.getCompany().toLowerCase().replace(" - "," "),document.getDocId(),sql);
            }
            if(document.getPlace()!=null && !document.getPlace().equals("")){
                strTemp = document.getPlace().split(" ");
                this.numberBeforeChar = this.numberBeforeChar+strTemp.length;
                sql = t‫‪okenizing‬‬(document.getPlace().toLowerCase().replace(" - "," "),document.getDocId(),sql);
            }
            if(document.getTopic()!=null && !document.getTopic().equals("")){
                strTemp = document.getTopic().split(" ");
                this.numberBeforeChar = this.numberBeforeChar+strTemp.length;
                sql = t‫‪okenizing‬‬(document.getTopic().toLowerCase().replace(" - "," "),document.getDocId(),sql);
            }

        }
        if (jProgressBar!=null) {
            jProgressBar.setValue(80);
            try {
                Path currentRelativePath = Paths.get("");
                String s = currentRelativePath.toAbsolutePath().toString();
                File file = new File(s+"/txtFiles/data.txt");
                if (!file.exists()) {
                    file.createNewFile();
                    System.out.println("creating df file ... ");
                }else{
                    file.delete();
                    System.out.println("updating file...");
                    file.createNewFile();

                }
                BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
                String str = this.numberBeforeChar+"";
                buffer.write("Before Delete Characters :"+str+"\n");
                str = this.numberAfterChars+"";
                buffer.write("After Delete Characters :"+str+"\n");
                str = this.numberBeforStopWord+"";
                buffer.write("Befor Delete StopWord :"+str+"\n");
                str = this.numberAfterStopWord+"";
                buffer.write("After Delete StopWord :"+str+"\n");
                str = this.numberBeforStemming+"";
                buffer.write("Before Stemming:"+str+"\n");
                str = this.numberAfterStemming+"";
                buffer.write("After Stemming:"+str+"\n");

                buffer.close();





                System.out.println("finish writing to df file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        return tokensList;

    }

    //toLowerCase
    public Document toLowerCase(Document document){
        System.out.println(document.getDocId());
        document.setDocTitle(document.getDocTitle().toLowerCase());
        document.setDocBody(document.getDocBody().toLowerCase());
        if (document.getCompany() != null){
            document.setCompany(document.getCompany().toLowerCase());
        }
        if(document.getPlace() != null){
            document.setPlace(document.getPlace().toLowerCase());
        }
        //document.setDate(document.getDate().toLowerCase());
        if(document.getTopic() != null){
            document.setTopic(document.getTopic().toLowerCase());
        }
        if (document.getExchanges()!=null){
            document.setExchanges(document.getExchanges().toLowerCase());
        }
        if (document.getOrg()!=null){
            document.setOrg(document.getOrg());
        }
        if (document.getPeaple()!=null){
            document.setPeaple(document.getPeaple().toLowerCase());
        }

        return document;
    }

    // get text and do t‫‪okenizing‬‬
    private String t‫‪okenizing‬‬(String inputString,int docId,String sql) throws FileNotFoundException {
        System.out.println("Starting Text Preproccesing ...");

        String[] strs = inputString.split(" ");
        inputString = "";
        for(int i=0;i<strs.length;i++){
            strs[i] = removeDOtt(strs[i]);
            if(strs[i].length()>0){
                this.numberAfterChars++;
                inputString = inputString + strs[i] + " ";
            }
        }

        inputString = inputString.replace("'nt"," not");
        inputString = inputString.replace("'m"," am");
        inputString = inputString.replace("n't"," not");
        inputString = inputString.replace("'ll"," will");
        inputString = inputString.replace("' "," ");
        inputString = inputString.replace("'r"," are");
        inputString = inputString.replace("'v"," have");
        inputString = inputString.replace("'s"," ");
        inputString = inputString.replace("'l","");
        inputString = inputString.replace("'"," ");
        inputString = inputString.replace("-" ," ");
        inputString = inputString.replace("'s" ,"");
        inputString = inputString.replace("'u.s." ,"us");
        inputString = inputString.replace("'u.s.a" ,"usa");
        inputString = inputString.replace("'u.k." ,"uk");
        inputString = inputString.replace("'&#127;" ,"");
        inputString = inputString.replace("&" ," ");
        inputString = inputString.replace("\"" ," ");
        inputString = inputString.replace("=" ," ");
        inputString = inputString.replace("/" ," ");
        inputString = inputString.replace(";" ," ");
        inputString = inputString.replace("#" ," ");
        String[] strTemp = inputString.split(" ");
        this.numberBeforStopWord = this.numberBeforStopWord + strTemp.length;
        inputString = removeStopWord(inputString);
        strTemp = inputString.split(" ");
        this.numberAfterStopWord = this.numberAfterStopWord + strTemp.length;

        String str1 = inputString;
        String[] str = str1.split(" ");
        String str2 = "";
        for (int i = 0; i < str.length; i++) {
            if(str[i].matches("(.+).(.+)")){
                str[i]=str[i].replace(".","");
            }

            if(str[i].length()>0){
                this.numberBeforStemming = this.numberBeforStemming+1;
                str[i] = stemming(str[i]);
                this.numberAfterStemming = this.numberAfterStemming+1;
                str[i] = removeDOtt(str[i]);
                str[i] = deleteOneCharacter(str[i]);
                str[i] = checkFirstChar(str[i]);
                if (str[i].length() > 0) {
                    Token token = new Token(docId,str[i]);
                    //addToTokenList(token);
                    tokensList.add(token);
                }
            }
            else{
                System.out.print("\n ------> " + str[i]);
            }
        }
        return "";
    }

    private void addToTokenList(Token token) {
        if (tokensList.size()!=0){
            int min=0,max=tokensList.size()-1;
            int i = (max + min)/2;
            while (max>=min){
                if (token.string.compareTo(tokensList.get(i).string)<0){
                        max = i-1;
                        i = (max+min)/2;
                }else if (token.string.compareTo(tokensList.get(i).string)>0){
                        min = i+1;
                        i = (max+min)/2;
                }else {
                    break;
                }
                System.out.println("++");
            }
            int k=tokensList.size();
            for (int j=i;j<tokensList.size();j++){
                if(!token.string.equals(tokensList.get(j).string)){
                    k=j+1;
                    break;
                }
            }
            if (k==tokensList.size()){
                tokensList.add(token);
            }else {
                tokensList.add(k,token);
            }
        }else {
            tokensList.add(token);
        }
    }

    private String stemming(String str){
        Stermmer stermmer = new Stermmer();
        Token token = new Token();
        token.string = str;
        return stermmer.doStermm(token);
    }

    private String deleteOneCharacter(String str) {
        if(str.length()==1){
            return "";
        }else {
            return str;
        }
    }

    //remove character in last index of token if character is not letter or numeric
    public String removeDOtt(String str) {
        if(str.length()==0) {
            return "";
        }
        int numberFirst = str.charAt(str.length()-1);
        //System.out.println(Character.charCount(numberFirst));
        if(((numberFirst>=48 && numberFirst<=57)||(numberFirst>=65 && numberFirst<=90)||(numberFirst>=97 && numberFirst<=122))){
            return str;
        }else {
            String str2 = "";
            for(int i=0;i<str.length()-1;i++){
                str2 = str2 + str.charAt(i);
            }
            str = str2;
            return removeDOtt(str);

        }

    }

    //remove stopword from text
    public String removeStopWord(String str) throws FileNotFoundException {

        str = " " + str + " ";
    //    System.out.println(str);
        for(int i=0;i<this.stopWords.length;i++){
            str = str.replace(" "+this.stopWords[i]+" "," ");
            str = str.replace(" "+this.stopWords[i]+". "," ");
        }
        str = str.replaceFirst(" ","");
    //    System.out.println(str);
        if(str.charAt(str.length()-1)==' '){
            String str2 = "";
            for(int i=0;i<str.length()-1;i++){
                str2 = str2 + str.charAt(i);
            }
            str = str2;
        }
        System.out.println(str);
        return str;
    }


    //read stopword from stopword.txt file
    public String[] loadStopWords() throws FileNotFoundException{
        UrlManager urlManager = new UrlManager();
        String url = urlManager.getPath("stopwords.txt");
        System.out.println(url);
        String str = "";
        File file = new File(url);
        Scanner scanner = new Scanner(file);
        str=scanner.next();
        while (scanner.hasNext()){
            str = str + " " + scanner.next();
        }
    //    System.out.println(str);
        return str.split(" ");
    }


    //remove token if it is not began from a numeric of letter
    public String checkFirstChar(String str){
        if(str.length() == 0){
            return "";
        }
        int numberFirst = str.charAt(0);
        if((numberFirst>=48 && numberFirst<=57)||(numberFirst>=65 && numberFirst<=90)||(numberFirst>=97 && numberFirst<=122)){
            return str;
        }
        return "";
    }
}
