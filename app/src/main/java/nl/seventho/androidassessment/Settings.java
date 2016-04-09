package nl.seventho.androidassessment;

import android.content.SharedPreferences;

/**
 * Created by Sven Brettschneider on 9-4-2016.
 */
public class Settings {

    private boolean idEnabled;
    private boolean heightEnabled;
    private boolean base_expEnabled;
    private String sender;

    public Settings(){

    }

    public Settings(SharedPreferences prefs){
        idEnabled = prefs.getBoolean("switch_id", true);
        heightEnabled = prefs.getBoolean("switch_height", true);
        base_expEnabled = prefs.getBoolean("switch_base_xp", true);
        sender = prefs.getString("friends_list", "Friend");
    }


    public boolean isIdEnabled() {
        return idEnabled;
    }

    public void setIdEnabled(boolean idEnabled) {
        this.idEnabled = idEnabled;
    }

    public boolean isHeightEnabled() {
        return heightEnabled;
    }

    public void setHeightEnabled(boolean heightEnabled) {
        this.heightEnabled = heightEnabled;
    }

    public boolean isBase_expEnabled() {
        return base_expEnabled;
    }

    public void setBase_expEnabled(boolean base_expEnabled) {
        this.base_expEnabled = base_expEnabled;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
