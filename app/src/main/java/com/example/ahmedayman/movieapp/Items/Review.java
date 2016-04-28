package com.example.ahmedayman.movieapp.Items;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Eng AhmedAyman on 4/14/2016.
 */

public class Review {

    private String id;
    private String author;
    private String content;

    public Review() {

    }

    public Review(JSONObject trailer) throws JSONException {
        this.id = trailer.getString("id");
        this.author = trailer.getString("author");
        this.content = trailer.getString("content");
    }

    public String getId() { return id; }

    public String getAuthor() { return author; }

    public String getContent() { return content; }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
