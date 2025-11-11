package ir.sublearn.models;

public class ConversationModel {
    private String text, languageCode;
    private int viewType;

    public ConversationModel() {
    }

    public ConversationModel(String text, String languageCode, int viewType) {
        this.text = text;
        this.languageCode = languageCode;
        this.viewType = viewType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
