package com.company.safekyc.config;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kemal  {

    public static void main(String[] args) {

        List<String> items = new ArrayList<>();
        List<String> query = new ArrayList<>();
        items.add("5");
        items.add("aab");
        items.add("a");
        items.add("bcd");
        items.add("awe");
        items.add("bbbbbu");
        items.add("2");
        items.add("2-3");
        items.add("4-5");


        List<Integer> x = new Kemal().hasVowels(items, query);
        System.out.println(x);

        System.out.println();

    }


    public List<Integer> hasVowels(List<String> strArr, List<String> query) {
        List<String> vovels = new ArrayList<>();
        List<String> quer = new ArrayList<>();
        vovels.add("a");
        vovels.add("e");
        vovels.add("i");
        vovels.add("o");
        vovels.add("u");

        List<Integer> result = new ArrayList<>();


        for (String item : strArr) {
            if(item.contains("-"))
                quer.add(item);
        }
        query = quer;

        for (String item : query) {
            if(!item.contains("-"))
                continue;

            Integer startIndex = new Integer(item.substring(0, item.indexOf("-")));
            Integer endIndex = new Integer(item.substring(item.indexOf("-") + 1));
            Integer sum = 0;

            for (int i = startIndex; i <= endIndex; i++) {
                String text = strArr.get(i);

                char startChar = text.charAt(0);
                char endChar = text.charAt(text.length() - 1);

                if(vovels.contains(String.valueOf(startChar)) && vovels.contains(String.valueOf(endChar)))
                    sum++;

            }
            result.add(sum);
        }
        return result;
    }
}
