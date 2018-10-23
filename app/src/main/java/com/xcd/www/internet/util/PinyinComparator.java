package com.xcd.www.internet.util;

import com.xcd.www.internet.model.ContactModel;

import java.util.Comparator;

/**
 * Created by gs on 2018/10/18.
 */

public class PinyinComparator implements Comparator<ContactModel> {

    public int compare(ContactModel o1, ContactModel o2) {
        if (o1.getLetters().equals("@")
                || o2.getLetters().equals("#")) {
            return -1;
        } else
            if (o1.getLetters().equals("#")
                || o2.getLetters().equals("@")) {
            return 1;
        } else {
            return o1.getLetters().compareTo(o2.getLetters());
        }
    }

}
