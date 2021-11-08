package ru.maltsev.langhunt.ui;

public class SetWords {

    private Long setId;

    private String title;

    private Long userId;

    public SetWords(Long setId, String title, Long userId) {
        this.setId = setId;
        this.title = title;
        this.userId = userId;
    }

    public Long getSetId() {
        return setId;
    }

    public void setSetId(Long setId) {
        this.setId = setId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
