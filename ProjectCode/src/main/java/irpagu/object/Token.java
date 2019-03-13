package irpagu.object;

import ir.Search.SearchTable;

import java.util.ArrayList;
import java.util.List;

public class Token {
    public String string;
    public int id;
    public int dF;
    public int cf;
    public TokenNode nodes;
    int docId;

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public Token(int docId){
        nodes = new TokenNode(docId);
    }
    public Token(){
        nodes = null;
    }

    public Token(int docId,String string){
        this.string = string;
        this.nodes = null;
        this.docId = docId;

    }

    public void updateTokenNode(int docId){
        TokenNode tokenNode = this.nodes;
        while (tokenNode.next!=null){
            tokenNode = tokenNode.next;
        }
        tokenNode.next = new TokenNode(docId);
    }

    public void updateLastNodeTfUpdate() {
        TokenNode tokenNode = this.nodes;
        while (tokenNode.next!=null){
            tokenNode = tokenNode.next;
        }
        tokenNode.tf++;
    }

    public void addNode(int docId , int tf){
        if (this.nodes==null){
            System.out.println("23456");
            this.nodes = new TokenNode(docId);
        }else {
            TokenNode node = this.nodes;
            while (node.next != null){
                node = node.next;
            }
            node.next = new TokenNode(docId);
            node.next.tf = tf;
        }
    }

    public void printNodes() {
        TokenNode tokenNode = this.nodes;
        while (tokenNode!=null){
            System.out.print("["+tokenNode.docId + "," + tokenNode.tf + "]");
            tokenNode = tokenNode.next;
        }
    }

    public java.lang.String printTF() {
        TokenNode tokenNode = this.nodes;
        java.lang.String str = "";
        while (tokenNode!=null){
            str = str + id+","+tokenNode.docId+","+tokenNode.tf+"\n";
            tokenNode = tokenNode.next;
        }
        return str;
    }

    public List<SearchTable> getSearchTable() {
        if (this.nodes==null){
            return null;
        }else {
            List<SearchTable> searchTableList = new ArrayList<SearchTable>();
            TokenNode node = this.nodes;
            while (node!=null){
                double tf = (1 + Math.log10(node.tf));
                double idf = Math.log10(Document.DOCUMENT_COUNT/this.dF);
                double weight = tf * idf;
                System.out.println(idf);
                if (weight!=0){
                    SearchTable searchTable = new SearchTable(this.id,node.docId,weight);
                    searchTableList.add(searchTable);

                }
                node = node.next;
            }

            return searchTableList;
        }
    }

    public class TokenNode{
        public int docId;
        public int tf;
        public TokenNode next;
        public TokenNode(int docId){
            next = null;
            this.tf = 1;
            this.docId = docId;
        }
    }

    public static List<Token> sortTokenList(List<Token> tokenList){
        int docId = 0;
        for (int i=1;i<tokenList.size();i++){
            if (tokenList.get(i).getDocId()>docId){
                docId++;
            }
            int j = i;
            while (j>docId && tokenList.get(j).string.compareTo(tokenList.get(j-1).string)<0){
                Token token = tokenList.get(j);
                tokenList.set(j,tokenList.get(j-1));
                tokenList.set(j-1,token);
                j--;
            }
            System.out.println(i);
        }
        return tokenList;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
