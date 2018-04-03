/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class KronutoluParser {

    public static String parse(int i) {
        String s = String.valueOf(i);
        String[] chopped = chopUp(s);
        s = chopped[0];
        for (int j = 1; j < chopped.length; j++) {
            s += "," + chopped[j];
        }

        return s;
    }
    
     public static String parse(String s) {
       
        String[] chopped = chopUp(s);
        s = chopped[0];
        for (int j = 1; j < chopped.length; j++) {
            s += "," + chopped[j];
        }

        return s;
    }


    public static int unParse(String s) {
        String sb = "";
   
        for (int i = 0; i <  s.length(); i++) {
            if(!s.substring(i,i+1).equals(".")){
                sb+=s.substring(i,i+1);
            }
        }

        return Integer.valueOf(sb);
    }

    private static String[] chopUp(String s) {
        int klukka = 0;
        int staerd = s.length() / 3;
        if (s.length() % 3 != 0) {
            staerd++;
        }
        int j = staerd - 1;
        int byrjun;
        String[] chopped = new String[staerd];
        for (int i = s.length(); i > 0; i--) {
            if (klukka % 3 == 0) {
                byrjun = i - 3;
                if (byrjun < 0) {
                    byrjun = 0;
                }
                chopped[j] = s.substring(byrjun, i);
                j--;
            }
            klukka++;
        }
        return chopped;

    }

    public static void main(String[] args) {
        System.out.println(parse(500000000)); //500.000.000
    }

}
