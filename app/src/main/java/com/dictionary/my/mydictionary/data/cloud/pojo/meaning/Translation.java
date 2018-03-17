
package com.dictionary.my.mydictionary.data.cloud.pojo.meaning;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Translation {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("note")
    @Expose
    private Object note;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getNote() {
        return note;
    }

    public void setNote(Object note) {
        this.note = note;
    }

}
