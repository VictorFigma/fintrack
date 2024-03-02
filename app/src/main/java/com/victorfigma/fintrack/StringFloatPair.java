package com.victorfigma.fintrack;

import java.util.Comparator;

public class StringFloatPair {

    public String code;
    public float qtty;

    public StringFloatPair(String code) {
        this.code = code;
    }

    public StringFloatPair(String code, float qtty) {
        this.code = code;
        this.qtty = qtty;
    }

    public static class StringFloatPairComparator implements Comparator<StringFloatPair> {
        @Override
        public int compare(StringFloatPair o1, StringFloatPair o2) {
            return o1.code.compareTo(o2.code);
        }
    }
}

