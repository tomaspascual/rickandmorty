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
public class Info {
    private Integer count;
    private Integer pages;
    private String next;
    private Object prev;

    @JsonCreator
    public Info(@JsonProperty("count") Integer count, @JsonProperty("pages") Integer pages, @JsonProperty("next") String next, @JsonProperty("prev") Object prev) {
        this.count = count;
        this.pages = pages;
        this.next = next;
        this.prev = prev;
    }
}
