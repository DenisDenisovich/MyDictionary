
package com.dictionary.my.mydictionary.data.entites.skyengapi.meaning;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeaningsWithSimilarTranslation {

    @SerializedName("meaningId")
    @Expose
    private Integer meaningId;
    @SerializedName("frequencyPercent")
    @Expose
    private String frequencyPercent;
    @SerializedName("partOfSpeechAbbreviation")
    @Expose
    private String partOfSpeechAbbreviation;
    @SerializedName("translation")
    @Expose
    private Translation translation;

    public Integer getMeaningId() {
        return meaningId;
    }

    public void setMeaningId(Integer meaningId) {
        this.meaningId = meaningId;
    }

    public String getFrequencyPercent() {
        return frequencyPercent;
    }

    public void setFrequencyPercent(String frequencyPercent) {
        this.frequencyPercent = frequencyPercent;
    }

    public String getPartOfSpeechAbbreviation() {
        return partOfSpeechAbbreviation;
    }

    public void setPartOfSpeechAbbreviation(String partOfSpeechAbbreviation) {
        this.partOfSpeechAbbreviation = partOfSpeechAbbreviation;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

}
