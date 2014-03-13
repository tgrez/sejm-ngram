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
}
