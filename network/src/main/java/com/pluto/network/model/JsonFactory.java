package com.pluto.network.model;

import com.google.gson.Gson;

public class JsonFactory {

    private static JsonFactory instance = new JsonFactory();
    private Gson gson;
    private JsonFactory(){
        gson = new Gson();
    }

    public static JsonFactory getInstance(){
        return instance;
    }
    public Gson getGson(){
        return gson;
    }
}
