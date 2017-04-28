package com.styln;

import android.util.Log;

import com.amazonaws.models.nosql.PostTableDO;

import java.text.ParseException;
import java.util.Comparator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kevin Jiang on 4/28/2017.
 */

public class ListComparer implements Comparator<PostTableDO> {
    @Override
    public int compare(PostTableDO c1, PostTableDO c2){
        String aa = c1.getPostDate();
        String bb = c2.getPostDate();
        Date a= new Date();
        Date b = a;
        SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd HH:mm:ss", Locale.US);
        try {
            a = df.parse(aa);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try
        {
            b = df.parse(bb);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return a.compareTo(b);

    }
}