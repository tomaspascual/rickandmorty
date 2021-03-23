package com.adultswim.rickandmorty.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
public class SpecificCharacters {


    private Integer id;
    private String name;
    private Integer popularityIndex;
    private String status;
    private String species;
    private String type;
    private String gender;
    private Origin origin;
    private Location location;
    private String image;
    private List<String> episode = null;
    private String url;
    private String created;

    @JsonCreator
    public SpecificCharacters(@JsonProperty("id") Integer id, @JsonProperty("name") String name, @JsonProperty("status") String status, @JsonProperty("species") String species, @JsonProperty("type") String type, @JsonProperty("gender") String gender, @JsonProperty("origin") Origin origin, @JsonProperty("location") Location location, @JsonProperty("image") String image, @JsonProperty("episode") List<String> episode, @JsonProperty("url") String url, @JsonProperty("created") String created) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.species = species;
        this.type = type;
        this.gender = gender;
        this.origin = origin;
        this.location = location;
        this.image = image;
        this.episode = episode;
        this.url = url;
        this.created = created;
    }

    @Override
    public String toString() {
        return id + " = " + name;
    }


}
