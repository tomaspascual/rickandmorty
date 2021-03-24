package com.adultswim.rickandmorty.repository;


import com.adultswim.rickandmorty.entity.UserRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface InteractionsRepository extends CrudRepository<UserRequest, String> {


}