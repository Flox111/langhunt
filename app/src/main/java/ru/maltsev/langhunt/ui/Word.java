package ru.maltsev.langhunt.ui;

public class Word {
    private Long wordId;

    private String nativeTranslated;

    private String translated;

    public Word(Long wordId, String nativeTranslated, String translated) {
        this.wordId = wordId;
        this.nativeTranslated = nativeTranslated;
        this.translated = translated;
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
}
