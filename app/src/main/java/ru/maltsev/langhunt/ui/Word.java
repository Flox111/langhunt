package ru.maltsev.langhunt.ui;

public class Word {
    private Long wordId;

    private String nativeTranslated;

    private String translated;

    private Long setId;

    public Word(Long wordId, String nativeTranslated, String translated) {
        this.wordId = wordId;
        this.nativeTranslated = nativeTranslated;
        this.translated = translated;
    }

    public Word(String nativeTranslated, String translated, Long setId) {
        this.nativeTranslated = nativeTranslated;
        this.translated = translated;
        this.setId = setId;
    }

    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public String getNativeTranslated() {
        return nativeTranslated;
    }

    public void setNativeTranslated(String nativeTranslated) {
        this.nativeTranslated = nativeTranslated;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }

    public Long getSetId() {
        return setId;
    }

    public void setSetId(Long setId) {
        this.setId = setId;
    }
}
