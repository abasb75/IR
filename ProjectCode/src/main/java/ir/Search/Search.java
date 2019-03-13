package ir.Search;

import irpagu.object.Document;
import irpagu.object.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Search {
    public static List<Token> getTokenId(List<Token> tokenList) throws FileNotFoundException {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        File file = new File(s+"/txtFiles/df.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            String str = scanner.nextLine();
            String[] strings = str.split(",");
            for (int i=0;i<tokenList.size();i++){
                if (strings[0].equals("\""+tokenList.get(i).string+"\"")){
                    Token token = tokenList.get(i);
                    token.id = Integer.parseInt(strings[1]);
                    token.dF = Integer.parseInt(strings[2]);
                }
            }
        }
        scanner.close();
        return tokenList;
    }
    public static List<Token> getTokenTfs(List<Token> tokenList) throws FileNotFoundException {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        File file = new File(s+"/txtFiles/tf.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            String str = scanner.nextLine();
            String[] strings = str.split(",");
            for (int i=0;i<tokenList.size();i++){
                if (tokenList.get(i).id == Integer.parseInt(strings[0])){
                    Token token = tokenList.get(i);
                    token.addNode(Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
                }
            }
        }
        scanner.close();
        return tokenList;
    }
    public static List<SearchTable> createSearchTable(List<Token> tokenList){
        List<SearchTable> searchTableList = new ArrayList<SearchTable>();
        for (int i= 0;i<tokenList.size(); i++ ){
            Token token = tokenList.get(i);
            List<SearchTable> currentSearchTable = token.getSearchTable();
            if (currentSearchTable!=null){
                for (int j=0;j<currentSearchTable.size();j++){
                    searchTableList.add(currentSearchTable.get(j));
                }
            }
        }
        return searchTableList;
    }
    public static List<QueryToken> doTfForQuery(List<Token> tokenList){
        List<QueryToken> queryTokenList = new ArrayList<QueryToken>();
        queryTokenList.add(new QueryToken(tokenList.get(0).id,1));
        for (int i=1;i<tokenList.size();i++){
            int isContain = -1;
            for (int j=0;j<queryTokenList.size();j++){
                if (queryTokenList.get(j).tokenId == tokenList.get(i).id){
                    isContain=j;
                    break;
                }
            }
            if (isContain==-1){
                queryTokenList.add(new QueryToken(tokenList.get(i).id,1));
            }else {
                queryTokenList.get(isContain).tf++;
                tokenList.remove(i);
                i--;
            }
        }
        return queryTokenList;
    }
    public static List<SearchResultDocument> doTfIdfSearch(List<QueryToken> queryTokenList, List<SearchTable> searchTableList,List<Token> tokenList) {
        double score[] = new double[Document.MAX_DOCUMENT_ID+1];
        double Length[] = new double[Document.MAX_DOCUMENT_ID+1];
        for (int i=0;i<score.length;i++){
            score[i] = 0.0;
            Length[i] = 0;
        }
        for (int i=0;i<queryTokenList.size();i++){
            double queryTokenDf = 0;
            for (int j=0;j<tokenList.size();j++){
                if (queryTokenList.get(i).tokenId == tokenList.get(j).id){
                    queryTokenDf = tokenList.get(j).dF;
                }
            }

            double queryTokenWeight = (1+Math.log10(queryTokenList.get(i).tf))*(Math.log10(Document.DOCUMENT_COUNT/queryTokenDf));
            System.out.println(Math.log10(queryTokenDf)+">>>>>>>>>>>>>>>>>><<<<<<<<<<<<<>>>>><<<<>");
            for (int j =0;j<searchTableList.size();j++){
                if (queryTokenList.get(i).tokenId == searchTableList.get(j).tokenId){
                    score[searchTableList.get(j).docId] += (queryTokenWeight*searchTableList.get(j).weight);
                }
            }
        }
        Length[searchTableList.get(0).docId] += searchTableList.get(0).weight*searchTableList.get(0).weight;
        for (int i=1;i<searchTableList.size();i++){
            if (searchTableList.get(i).docId == searchTableList.get(i-1).docId){
                Length[searchTableList.get(i).docId] += searchTableList.get(i).weight*searchTableList.get(i).weight;

            }else {
                Length[searchTableList.get(i).docId] = searchTableList.get(i).weight*searchTableList.get(i).weight;
            }
        }
        List<SearchResultDocument> list = new ArrayList<SearchResultDocument>();
        int counter = 0;
        for (int i=0;i<score.length;i++){
            if (Length[i]!=0){
                System.out.println(Length[i] + "<<<<<<<<<<<<<<<<<<<<<<<<<<"+i+">>>>>>>>>>>>>>>>>>>>"+score[i]);
                score[i] = score[i]/(Math.sqrt(Length[i]));
                System.out.println(Length[i] + "<<<<<<<<<<<<<<<<<<<<<<<<<<"+i+">>>>>>>>>>>>>>>>>>>>"+score[i]);
                if (score[i]!=0){
                    list.add(new SearchResultDocument(i,score[i]));
                }
            }
        }
        score = null;
        Length = null;
        list = sortList(list);
        return list;
    }
    private static List<SearchResultDocument> sortList(List<SearchResultDocument> list) {
        for (int i=1;i<list.size();i++){
            int j=i;
            while (j>0 && list.get(j).score > list.get(j-1).score){
                SearchResultDocument current = list.get(j);
                list.set(j,list.get(j-1));
                list.set(j-1,current);
                j--;
            }
        }
        return list;
    }
}
