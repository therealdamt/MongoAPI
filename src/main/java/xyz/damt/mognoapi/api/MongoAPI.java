package xyz.damt.mognoapi.api;


import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoWriteException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoAPI {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> collection;


    public void connect(String username, String database, String password, String collectionName, String serverAddress, int port) {

        System.setProperty("DEBUG.GO", "true");
        System.setProperty("DB.TRACE", "true");
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.WARNING);

        try {
            MongoCredential mongoCredential = MongoCredential.createCredential(username, database, password.toCharArray());
            mongoClient = new MongoClient(new ServerAddress(serverAddress, port), Collections.singletonList(mongoCredential));
            mongoDatabase = mongoClient.getDatabase(database);
            collection = mongoDatabase.getCollection(collectionName);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "The MongoDB database has connected!");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There was an error connecting to the MongoDatabase and that is: ");
            e.printStackTrace();
        }
    }

    public void connect(String url, String database, String collectionName) {

        System.setProperty("DEBUG.GO", "true");
        System.setProperty("DB.TRACE", "true");
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.WARNING);

        try {
            com.mongodb.client.MongoClient client = MongoClients.create(url);
            mongoDatabase = client.getDatabase(database);
            collection = mongoDatabase.getCollection(collectionName);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "The MongoDB database has connected!");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There was an error connecting to the MongoDatabase and that is: ");
            e.printStackTrace();
        }
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public Document getDocument(String key, Object value) {
        Document filter = collection.find(new Document(key, value)).first();
        return filter;
    }

    public void updateDocument(Document document, String key, Object value) {
        if (document != null) {
            Bson updatedValue = new Document(key, value);
            Bson updateOperation = new Document("$set", updatedValue);
            collection.updateOne(document, updateOperation);
        } else {
            return;
        }

    }

    public void createCollection(String collectionName) {
        if (mongoDatabase.getCollection(collectionName) != null) {
            return;
        }
        mongoDatabase.createCollection(collectionName);
    }

    public void createDocument(String key, String value) {
        try {
            Document document = new Document(key, value);
            collection.insertOne(document);
        } catch (MongoWriteException e) {
            System.out.println("That document already exists!");
        }
    }

}
