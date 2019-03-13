package ir.parser;


import irpagu.object.Document;

import javax.print.Doc;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


//Parse the documents
public class XMLParse {
    List<Document> documentList;
    irpagu.object.Document document ;
    public XMLParse(){
        document = null;
        documentList = new ArrayList<>();
    }

    //do Parse method
    public List<Document> parse(int number) throws FileNotFoundException {
        documentList = new ArrayList<Document>();
        String name = "reuters21578/reut2-0" + ((number>9)?number:"0"+number) + ".sgm" ;
        UrlManager urlManager = new UrlManager();
        File file = new  File(urlManager.getPath(name));
        Scanner scanner = new Scanner(file);
        System.out.println(scanner.nextLine());
        int current = 1;

        while (scanner.hasNext()){
            String str = scanner.next();
            String reg1 = "<TITLE>*";
            String reg2 = "*</TITLE>";
            Pattern pattern1 = Pattern.compile(reg1);
            if(str.equals("<REUTERS")){
                System.out.println((number*1000 + current )+" Starting New Document...");
                document = new Document(0,"","");

                current = current+1;
            }else if (str.matches("NEWID=\"(.*)")){
                String[] strArray = str.split("\"");
                document.setDocId(Integer.parseInt(strArray[1]));
            }
            else if(str.equals("<DATE>")){
                String str1 = scanner.next();
                String str2 = scanner.next();
                String[] str3 = str2.split("</DATE>");
                document.setDate(str1 + " " + str3[0]);
                System.out.println("Set Date To Document"+str1 + " " + str3[0]);
            }else if(str.matches("<TITLE>(.*)")){
                System.out.println("Reading Title ... ");
                String str1 = "";
                if(!str.equals("<TITLE>")){
                    str1 = str.split("<TITLE>")[1];
                }

                String str2 = scanner.next();
                while (!str2.matches("(.*)</TITLE>(.*)")){
                    str1 = str1 + " " + str2;
                    str2 = scanner.next();
                }
                if(!(str2.equals("</TITLE>"))){
                    str1 = str1 + " " + str2.split("</TITLE>")[0];
                }

                document.setDocTitle(str1);
                System.out.println(str1);
            }else if(str.matches("(.*)<TITLE>(.*)")){
                System.out.println("Reading Title ... ");
                String str1 = "";
                if(str.matches("(.*)<TITLE>")){
                    str1 = "";
                }
                else if(!str.equals("<TITLE>")){
                    str1 = str.split("<TITLE>")[1];
                }

                String str2 = scanner.next();
                while (!str2.matches("(.*)</TITLE>(.*)")){
                    str1 = str1 + " " + str2;
                    str2 = scanner.next();
                }
                if(!(str2.equals("</TITLE>"))){
                    str1 = str1 + " " + str2.split("</TITLE>")[0];
                }

                document.setDocTitle(str1);
                System.out.println(str1);
            } else if(str.matches("</DATELINE><BODY>(.*)")) {
                System.out.println("Reading Body ... ");
                String str1 = str.split("</DATELINE><BODY>")[1];
                String str2 = scanner.next();
                while (!str2.matches("(.*)</BODY></TEXT>")) {
                    str1 = str1 + " " + str2;
                    str2 = scanner.next();
                }
                str1 = str1 + " " + str2.split("</BODY></TEXT>")[0];
                document.setDocBody(str1);
                System.out.println(str1);
            }else if(str.matches("<TOPICS><D>(.*)</D></TOPICS>")){
                System.out.println("Reading Topic ... ");
                String str1 = str.split("<TOPICS><D>")[1];
                str1 = str1.split("</D></TOPICS>")[0];
                str1 = str1.replace("</D><D>"," - ");
                document.setTopic(str1);
                System.out.println(str1);
            }else if(str.matches("<PLACES><D>(.*)</D></PLACES>")) {
                System.out.println("Reading place ... ");
                String str1 = str.split("<PLACES><D>")[1];
                str1 = str1.split("</D></PLACES>")[0];
                str1 = str1.replace("</D><D>"," - ");
                document.setPlace(str1);
                System.out.println(str1);
            }else if(str.matches("<PEOPLE>(.*)</PEOPLE>")){
                String str1 = str.split("<PEOPLE>")[0];
                str1 = str1.split("</PEOPLE>")[0];
                str1 = str1.replace("</D><D>"," - ");
                document.setPeaple(str1);
                System.out.println(" ‌* " + str1);

            }else if(str.matches("<ORGS>(.*)</ORGS>")){
                String str1 = str.split("<ORGS>")[0];
                str1 = str1.split("</ORGS>")[0];
                str1 = str1.replace("</D><D>"," - ");
                document.setOrg(str1);
                System.out.println(" ‌* " + str1);
            }else if(str.matches("<EXCHANGES>(.*)</EXCHANGES>")){
                String str1 = str.split("<EXCHANGES>")[0];
                str1 = str1.split("</EXCHANGES>")[0];
                str1 = str1.replace("</D><D>"," - ");
                document.setExchanges(str1);
                System.out.println(" ‌* " + str1);

            }else if(str.matches("<COMPANIES>(.*)</COMPANIES>")){
                String str1 = str.split("<COMPANIES>")[0];
                str1 = str1.split("</COMPANIES>")[0];
                str1 = str1.replace("</D><D>"," - ");
                document.setCompany(str1);
                System.out.println(" ‌* " + str1);
            }else if (str.equals("</REUTERS>")){
                System.out.print("Reading "+(number*1000+current)+"th Doument is Successfully end \n\n\n\n");
                document.setFile(number);
                documentList.add(document);
            }
        }
        return documentList;

    }


}