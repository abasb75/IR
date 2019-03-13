package ir.Search;


public class SearchResultDocument {
    public int docId;
    public double score;
    public int fileId;
    public String title;
    public SearchResultDocument(int docId,double score){
        this.score = score;
        this.docId = docId;
    }
}
