package com.victorfigma.fintrack;

import java.util.Comparator;

public class StringIntPair {

    public String code;
    public int qtty;

    public StringIntPair(String code) {
        this.code = code;
    }

    public StringIntPair(String code, int qtty) {
        this.code = code;
        this.qtty = qtty;
    }


    public static class StringIntPairComparator implements Comparator<StringIntPair> {
        @Override
        public int compare(StringIntPair o1, StringIntPair o2) {
            return o1.code.compareTo(o2.code);
        }
    }
}

