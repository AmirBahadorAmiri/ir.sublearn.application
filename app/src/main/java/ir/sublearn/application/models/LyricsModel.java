package ir.sublearn.application.models;

public class LyricsModel {
    private String en, fa;
    private int pos;

    public LyricsModel(String en, String fa, int pos) {
        this.en = en;
        this.fa = fa;
        this.pos = pos;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
