package com.example.demo.entity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Translation {
    private String id;
    private JsonObject metadata;
    private JsonArray results;
    private String word;
}
