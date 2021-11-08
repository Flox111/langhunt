package ru.maltsev.langhunt.ui;

public class Card {
    private Long cardId;

    private String nativeTranslated;

    private String translated;

    private Long setId;

    public Card(Long cardId, String nativeTranslated, String translated) {
        this.cardId = cardId;
        this.nativeTranslated = nativeTranslated;
        this.translated = translated;
    }

    public Card(String nativeTranslated, String translated, Long setId) {
        this.nativeTranslated = nativeTranslated;
        this.translated = translated;
        this.setId = setId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
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
