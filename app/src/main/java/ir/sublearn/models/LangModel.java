package ir.sublearn.models;

public class LangModel {

    private final String name, code, image;

    public LangModel(String name, String code, String image) {
        this.name = name;
        this.code = code;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getShowName() {
        return getName() + " (" + getCode() + ")";
    }

    public String getImage() {
        return image;
    }

    public String getCode() {
        return code;
    }
}
