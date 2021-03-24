package com.adultswim.rickandmorty.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Getter
@Setter
@Entity
@AllArgsConstructor
public class UserRequest {

    @Id
    private String id;
    private String request;

    public UserRequest() {
    }
}
