
package com.dictionary.my.mydictionary.data.cloud.pojo.meaning;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeaningSkyEng {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("wordId")
    @Expose
    private Integer wordId;

    private Integer difficultyLevel;

    @SerializedName("partOfSpeechCode")
    @Expose
    private String partOfSpeechCode;
    @SerializedName("prefix")
    @Expose
    private String prefix;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("soundUrl")
    @Expose
    private String soundUrl;
    @SerializedName("transcription")
    @Expose
    private String transcription;

    private String properties;
    private String updatedAt;
    private Object mnemonics;

    @SerializedName("translation")
    @Expose
    private Translation translation;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("definition")
    @Expose
    private Definition definition;
    @SerializedName("examples")
    @Expose
    private List<Example> examples = null;
    @SerializedName("meaningsWithSimilarTranslation")
    @Expose
    private List<MeaningsWithSimilarTranslation> meaningsWithSimilarTranslation = null;

    private List<String> alternativeTranslations = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public String getPartOfSpeechCode() {
        return partOfSpeechCode;
    }

    public void setPartOfSpeechCode(String partOfSpeechCode) {
        this.partOfSpeechCode = partOfSpeechCode;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Definition getDefinition() {
        return definition;
    }

    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    public List<MeaningsWithSimilarTranslation> getMeaningsWithSimilarTranslation() {
        return meaningsWithSimilarTranslation;
    }

    public void setMeaningsWithSimilarTranslation(List<MeaningsWithSimilarTranslation> meaningsWithSimilarTranslation) {
        this.meaningsWithSimilarTranslation = meaningsWithSimilarTranslation;
    }


}
