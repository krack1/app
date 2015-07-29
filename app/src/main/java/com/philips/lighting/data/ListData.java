package com.philips.lighting.data;

import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;

public class ListData {


    public Drawable mIcon;


    public String mTitle;


    public String mDate;


    public static final Comparator<ListData> ALPHA_COMPARATOR = new Comparator<ListData>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(ListData mListDate_1, ListData mListDate_2) {
            return sCollator.compare(mListDate_1.mTitle, mListDate_2.mTitle);
        }
    };
}
