package ir.parser;

import java.net.URL;

public class UrlManager {
    public UrlManager(){

    }
    public static String getPath(String res){
        URL url = UrlManager.class.getClassLoader().getResource(res);
        return url.getPath();
    }

}
