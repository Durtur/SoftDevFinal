package utils;

import javafx.scene.control.TextField;

/**
 *
 * @author Patrick Ott
 * class that does not allow numbers in textfield
 */
public class FullNameTextField extends TextField {
    
    @Override
    public void replaceText(int i, int i1, String string) {
        if (string.matches("[a-zA-Z]")) {
            super.replaceText(i, i1, string);
        }
    }
    
    @Override
    public void replaceSelection(String string) {
        super.replaceSelection(string);
    }
}
