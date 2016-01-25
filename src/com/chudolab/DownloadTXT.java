package com.chudolab;

import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by Chudo on 18.01.2016.
 */
public class DownloadTXT {

    public ArrayList<Double> fileRead(String filepath) {
        String data;
        FileInputStream inFile = null;
        ArrayList<Double> arrayOfData;
        try {
            inFile = new FileInputStream(filepath);
            byte[] str;
            str = new byte[inFile.available()];
            inFile.read(str);
            data = new String(str);
            String[] strings = data.split(",");

            if (strings[0].charAt(0) == '[') {
                String s = strings[0].substring(1);
                strings[0] = s;
            }
            String lastString = strings[strings.length - 1];
            if (lastString.charAt((lastString.length()) - 2) == ']') {
                String s = lastString.substring(0, lastString.length() - 2);
                strings[strings.length - 1] = s;
            }
            arrayOfData = new ArrayList<>();

            for (int i = 0; i < strings.length; i++) {
                arrayOfData.add(Double.parseDouble(strings[i]));
            }

            return arrayOfData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
