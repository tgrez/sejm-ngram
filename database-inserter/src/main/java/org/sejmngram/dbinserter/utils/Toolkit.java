package org.sejmngram.dbinserter.utils;

/**
 * Created by michalsiemionczyk on 05/03/14.
 */
public class Toolkit {


    /** Returns size of string in bytes*/
    public static int getStringSizeInBytes( String s ){
//        Minimum String memory usage (bytes) = 8 * (int) ((((no chars) * 2) + 45) / 8)
        return 8 * (int) (((( s.length()) * 2) + 45) / 8);

    }

    public static String replaceDiacrytics(String text){
        text = text.toLowerCase();
        text = text.replace('ą', 'a');
        text = text.replace('ć', 'c');
        text = text.replace('ę', 'e');
        text = text.replace('ł', 'l');
        text = text.replace('ń', 'n');
        text = text.replace('ó', 'o');
        text = text.replace('ś', 's');
        text = text.replace('ź', 'z');
        text = text.replace('ż', 'z');

        return text;
    }
}
