package com.adultswim.rickandmorty.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
@Getter
@Setter
public class Location {

    private String name;
    private String url;

    @JsonCreator
    public Location(@JsonProperty("name") String name, @JsonProperty("url") String url) {
        this.name = name;
        this.url = url;
    }

}
