package com.philips.lighting.data;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by jaeho on 2015-08-04.
 */
public class ListData_checkbox {

    public Drawable mIcon;

    public String mTitle;

    public CheckBox mCheck;


    public static final Comparator<ListData> ALPHA_COMPARATOR = new Comparator<ListData>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(ListData mListDate_1, ListData mListDate_2) {
            return sCollator.compare(mListDate_1.mTitle, mListDate_2.mTitle);
        }
    };
}
