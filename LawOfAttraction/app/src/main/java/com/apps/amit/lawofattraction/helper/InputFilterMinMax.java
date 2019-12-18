package com.apps.amit.lawofattraction.helper;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

/**
 * Created by amit on 10/2/18.
 */

public class InputFilterMinMax implements InputFilter {

    private int min;
    private int max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min,max,input)) {
                return null;
            }
        } catch (NumberFormatException e) {
            Log.e("NumberFormatException", e.getLocalizedMessage());
        }
        return  "";
    }

    private  boolean isInRange(int a , int b ,int c) {
        return b > a ? c  >= a && c <= b : c >= b && c <= a;
    }
}

