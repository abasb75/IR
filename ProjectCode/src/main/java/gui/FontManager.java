//بسم الله الرحمن الرحیم
package gui;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontManager {
    public static Font createFont(String fontName,float size){
        String fontUrl = FontManager.class.getClassLoader().getResource("fonts/"+fontName).getPath();
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontUrl)).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            return customFont;
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
