package tools;

import datastructures.LinkedList;

import java.util.regex.Pattern;

public class SplitString {
    public static LinkedList<String> split(String string, String separator){
        String[] splitted = string.split(Pattern.quote(separator));

        LinkedList<String> list = new LinkedList<String>();

        for (Integer i = 0; i < splitted.length; i++) {
            list.insertAtFirst(splitted[i]);
        }

        return list;
    }
}
