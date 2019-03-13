//بسم الله الرحمن الرحیم

package irpagu.object;


import ir.Search.SearchResultDocument;
import ir.parser.UrlManager;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Document {
    public static int MAX_DOCUMENT_ID = 21578;
    public static int DOCUMENT_COUNT = 18982;
    public Document(int docId,String docTitle,String docBody){
        this.docId = docId;
        this.docTitle = docTitle;
        this.docBody = docBody;
        this.company = null;
        this.peaple = null;
        this.place = null;
        this.date = null;
        this.exchanges = null;
        this.topic = null;
        this.org = null;
        this.oldId = 0;
        this.file=-1;


    }
    private int docId;
    private int file;

    public static List<SearchResultDocument> readDocumentHeader(List<SearchResultDocument> resultDocuments) throws FileNotFoundException {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        File file = new File(s+"/txtFiles/document.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            String s1 = scanner.nextLine();
            String[] strings = s1.split(",");
            int documentId = Integer.parseInt(strings[0]);
            for (int i=0;i<resultDocuments.size();i++){
                if (resultDocuments.get(i).docId == documentId){
                    resultDocuments.get(i).title = strings[2];
                    resultDocuments.get(i).fileId = Integer.parseInt(strings[1]);
                }
            }
        }
        return resultDocuments;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int file) {
        this.file = file;
    }

    private String docTitle;
    private String docBody;
    private String place;
    private String date;
    private String peaple;
    private String company;
    private String exchanges;
    private String topic;
    private String org;
    private int oldId;

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getDocBody() {
        return docBody;
    }

    public void setDocBody(String docBody) {
        this.docBody = docBody;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPeaple() {
        return peaple;
    }

    public void setPeaple(String peaple) {
        this.peaple = peaple;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExchanges() {
        return exchanges;
    }

    public void setExchanges(String exchanges) {
        this.exchanges = exchanges;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    //REMOVE SPAM DOCUMENT METHOD
    public static List<Document> removingSpamDocment(List<Document> documentList){
        for (int i= 0;i<documentList.size();i++){
            if (documentList.get(i).getDocBody().equals("")){
                documentList.remove(i);
                i--;
            }
        }
        return documentList;
    }


    //SAVE DOCUMENT IN A TXT FILE
    public static void saveDocumentsInFile(List<Document> documentList){
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        File file = new File(s+"/txtFiles/document.txt");
        try {
            if (file.exists()){
                file.delete();
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (int  i = 0;i<documentList.size();i++){
                bw.write(documentList.get(i).getDocId()+","+documentList.get(i).getFile()+","+documentList.get(i).getDocTitle()+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

