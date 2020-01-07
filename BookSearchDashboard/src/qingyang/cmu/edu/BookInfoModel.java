package qingyang.cmu.edu;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientURI;
import org.bson.Document;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class BookInfoModel {
    public void pushToMongo(LogBean log) {
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://username:password@cluster0-n4dkr.mongodb.net/test?retryWrites=true&w=majority");

        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection sampleCollection selected successfully");

        Document document = new Document("device", log.getDeviceInfo())
                .append("search", log.getSearchTerm()).append("time", log.getTimestamp())
                .append("api", log.getBookInfo().getSearchLink()).append("reply", log.getBookInfo().toString())
                .append("response_time", log.getResponseTime());
        collection.insertOne(document);
        System.out.println("Document inserted successfully");

        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
    }


    public BookBean searchBook(String searchTerm) throws IOException {
        searchTerm = searchTerm.replace(" ", "&");
        String addr = "https://www.googleapis.com/books/v1/volumes?q=" + searchTerm;
        JSONObject doc = readJsonFromUrl(addr);
        if (doc.isEmpty()) {
            return null; // no books found
        } else {
            JSONArray books= (JSONArray) doc.get("items");
            // Only get the information of the first book
            JSONObject o = (JSONObject) books.get(0);
            JSONObject volumeInfo = (JSONObject) o.get("volumeInfo");
            String title = volumeInfo.get("title").toString();
            String authors = volumeInfo.get("authors").toString().replace("[", "")
                    .replace("]", "").replace("\"", "");
            String publisher = volumeInfo.get("publisher").toString();
            String imageLink = ((JSONObject)volumeInfo.get("imageLinks")).get("thumbnail").toString().replace("\\", "");
            BookBean book = new BookBean(title, authors, publisher, imageLink, addr);

            return book;
        }
    }

    private JSONObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(jsonText);
            return json;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
