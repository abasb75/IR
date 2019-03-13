//بسم الله الرحمن الرحیم

import gui.SearchForm;
import gui.ShowDocumentForm;
import gui.SpalshForm;
import ir.Search.QueryToken;
import ir.Search.Search;
import ir.indexer.InvertedIndex;
import ir.indexer.PreprpcessToken;
import ir.Search.SearchResultDocument;
import ir.Search.SearchTable;
import ir.parser.XMLParse;
import irpagu.object.Document;
import irpagu.object.Token;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        SpalshForm splashForm = new SpalshForm();
        splashForm.setVisible(true);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        splashForm.setBackgroundImage();
        splashForm.revalidate();

        List<Document> documentList = new ArrayList<Document>();
        XMLParse xmlParse = new XMLParse();
        for(int i = 0 ; i<=21 ; i++){
            List<Document> documents = xmlParse.parse(i);
            for (int j=0;j<documents.size();j++){
                documentList.add(documents.get(j));
            }
            documents = null;
            splashForm.jProgressBar.setValue(2*i);
        }



        System.out.println("Count of documnts is : " + documentList.size());
        //RemoveSpamDocument
        splashForm.progressDetail.setText("در حال حذف اسپم ... ");
        documentList = Document.removingSpamDocment(documentList);
        splashForm.jProgressBar.setValue(50);
        System.out.println("Count of documnts is : " + documentList.size());

        //Save Document in a file
        splashForm.progressDetail.setText("در حال نوشتن اسناد در فایل متنی ... ");
        Document.saveDocumentsInFile(documentList);
        splashForm.jProgressBar.setValue(53);

        //process on word an Save on DataBase
        splashForm.progressDetail.setText("در حال پیش پردازش متن ... ");
        PreprpcessToken prep = new PreprpcessToken();
        List<Token> tokenList = prep.indexer(documentList,splashForm.jProgressBar);
        System.out.println(tokenList.size());
        Comparator<Token> c = new Comparator<Token>() {
            @Override
            public int compare(Token token, Token t1) {
                return token.string.compareTo(t1.string);
            }
        };

        //Sort The List
        tokenList.sort(c);
        System.out.println(tokenList.size());


        splashForm.jProgressBar.setValue(81);
        splashForm.progressDetail.setText("در حال ساخت شاخض معکوس ... ");
        //create invertedIndex
        InvertedIndex invertedIndex = new InvertedIndex(tokenList);
        invertedIndex.doIndexing(splashForm.jProgressBar);
        System.out.println(invertedIndex.list.size());

        splashForm.progressDetail.setText("تمام کردن پردازش ... ");
        //print and cteate .txt file
        invertedIndex.printWorld();
        splashForm.jProgressBar.setValue(100);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        splashForm.dispose();
        //ALREADY  TO  SEARCH
        SearchForm searchForm = new SearchForm();
        searchForm.setVisible(true);

        searchForm.doSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                searchForm.model.removeAllElements();
                String query = searchForm.getSearchQuery.getText();
                if (query.equals("")){
                    return;
                }
                try {
                    List<Token> tokenList = doPreprocessOnQuery(query);
                    if (tokenList.size()==0){
                        return;
                    }
                    tokenList = Search.getTokenId(tokenList);
                    tokenList = Search.getTokenTfs(tokenList);
                    List<QueryToken> queryTokenList = Search.doTfForQuery(tokenList);
                    if (queryTokenList.size()==0){
                        return;
                    }


                    List<SearchTable> searchTableList = Search.createSearchTable(tokenList);
                    if (searchTableList.size()==0){
                        return;
                    }
                    List<SearchResultDocument> resultDocuments = Search.doTfIdfSearch(queryTokenList,searchTableList,tokenList);
                    resultDocuments = Document.readDocumentHeader(resultDocuments);
                    for (int i=0;i<resultDocuments.size();i++){
                        searchForm.model.addElement("   "+resultDocuments.get(i).docId + " : " + resultDocuments.get(i).title);
                    }
                    List<SearchResultDocument> finalResultDocuments = resultDocuments;
                    searchForm.listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    searchForm.listView.addListSelectionListener(new ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent listSelectionEvent) {
                            searchForm.listView.removeListSelectionListener(this::valueChanged);
                            SearchResultDocument document = finalResultDocuments.get(listSelectionEvent.getLastIndex());
                            ShowDocumentForm documentForm = null;
                            try {
                                documentForm = new ShowDocumentForm(document.docId,document.fileId,document.title);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            documentForm.setVisible(true);
                        }

                    });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    public static List<Token> doPreprocessOnQuery(String query) throws FileNotFoundException {
        Document document = new Document(-1,"",query.toLowerCase());
        PreprpcessToken preprpcessToken = new PreprpcessToken();
        List<Document> documentList = new ArrayList<Document>();
        documentList.add(document);
        return preprpcessToken.indexer(documentList,null);
    }
}
