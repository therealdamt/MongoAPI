package xyz.damt.mognoapi;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.damt.mognoapi.api.MongoAPI;

public class Mongo extends JavaPlugin {

    private MongoAPI mongoAPI;

    @Override
    public void onEnable() {
        this.mongoAPI = new MongoAPI();
        this.mongoAPI.connect("url", "database", "collection name"); //First Usage
        this.mongoAPI.connect("username", "database", "password", "collection name", "server address", 27017); //Second Usage
    }


}
