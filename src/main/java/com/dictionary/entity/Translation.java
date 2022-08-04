package com.dictionary.entity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Translation{
    private String id;
    private JsonObject metadata;
    private JsonArray results;
    private String word;
}
