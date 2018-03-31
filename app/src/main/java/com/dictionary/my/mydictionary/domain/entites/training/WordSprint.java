package com.dictionary.my.mydictionary.domain.entites.training;

import com.dictionary.my.mydictionary.domain.entites.dictionary.Word;

/**
 * Created by luxso on 19.02.2018.
 */

public class WordSprint extends Word {
    private boolean rightFlag;

    public boolean isRightFlag() {
        return rightFlag;
    }

    public void setRightFlag(boolean rightFlag) {
        this.rightFlag = rightFlag;
    }
}
