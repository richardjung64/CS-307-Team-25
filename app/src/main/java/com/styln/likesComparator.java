package com.styln;

import com.amazonaws.models.nosql.ClothingDO;

import java.util.Comparator;

/**
 * Created by agcy on 4/27/17.
 */

public class likesComparator implements Comparator<ClothingDO> {
    @Override
    public int compare(ClothingDO c1, ClothingDO c2){
        return c1.getClothingLikes().compareTo(c2.getClothingLikes());
    }
}