package com.adultswim.rickandmorty.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Getter
@Setter
public class Characters {

    private Info info;
    private List<Result> results = null;

    @JsonCreator
    public Characters(@JsonProperty("info")  Info info, @JsonProperty("results") List<Result> results) {
        this.info = info;
        this.results = results;
    }
}


