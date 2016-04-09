package nl.seventho.androidassessment;

/**
 * Created by Sven Brettschneider on 9-4-2016.
 */
public class Pokemon {
    private String id;
    private String name;
    private String height;
    private String baseExp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBaseExp() {
        return baseExp;
    }

    public void setBaseExp(String baseExp) {
        this.baseExp = baseExp;
    }
}
