package utils;

import javafx.scene.control.TextField;

/**
 *
 * @author Patrick Ott
 * class that does not allow letters in textfield
 */
public class NumberTextField extends TextField {
    
    @Override
    public void replaceText(int i, int i1, String string) {
        if (string.matches("[0-9]")) {
            super.replaceText(i, i1, string);
        }
    }
    
    @Override
    public void replaceSelection(String string) {
        super.replaceSelection(string);
    }
}
