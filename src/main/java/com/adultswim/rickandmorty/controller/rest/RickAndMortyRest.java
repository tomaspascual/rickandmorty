package com.adultswim.rickandmorty.controller.rest;

import com.adultswim.rickandmorty.component.APIClient;
import com.adultswim.rickandmorty.entity.SpecificCharacters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping(value = "/api")
public class RickAndMortyRest {

    @Autowired
    APIClient apiClient;

    static final Log logger = LogFactory.getLog(RickAndMortyRest.class);

    @GetMapping(value="/rickandmorty")
    public Mono<List<SpecificCharacters>> retrieveUpTo5CharacterOrderedByPopularity(@RequestParam Optional<List<String>> excluded) throws SSLException {

        if (excluded.isPresent()) {
            return apiClient.retrieveUpTo5CharacterOrderedByPopularity(Set.copyOf(excluded.get()));
        } else {
            return apiClient.retrieveUpTo5CharacterOrderedByPopularity(Set.of());
        }

    }


}
