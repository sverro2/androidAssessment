package nl.seventho.androidassessment;

/**
 * Created by Sven Brettschneider on 8-3-2016.
 */
public class PokemonListItem {
    private String id;
    private String name;

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
        //This only works with pokemon names > 2 chars
        if(name.length() > 1){
            char first = Character.toUpperCase(name.charAt(0));
            this.name = first + name.substring(1);
        }else{
            this.name = name;
        }
    }

    @Override
    public String toString(){
        return id;
    }

}
