package ir.Search;

public class SearchTable {
    public double tokenId;
    public int docId;
    public double weight;
    public SearchTable(double tokenId,int docId,double weight){
        this.docId = docId;
        this.tokenId = tokenId;
        this.weight = weight;
    }
}
