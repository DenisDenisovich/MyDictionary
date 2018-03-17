
package com.dictionary.my.mydictionary.data.cloud.pojo.meaning;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Definition {

    @SerializedName("text")
    @Expose
    private String text;

    private String soundUrl;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
