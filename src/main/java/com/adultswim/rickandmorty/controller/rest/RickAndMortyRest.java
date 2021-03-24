package com.adultswim.rickandmorty.controller.rest;

import com.adultswim.rickandmorty.component.APIClient;
import com.adultswim.rickandmorty.entity.SpecificCharacters;
import com.adultswim.rickandmorty.entity.UserRequest;
import com.adultswim.rickandmorty.repository.InteractionsRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping(value = "/api")
public class RickAndMortyRest {

    @Autowired
    APIClient apiClient;

    @Autowired
    InteractionsRepository interactionsRepository;

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");


    static final Log logger = LogFactory.getLog(RickAndMortyRest.class);

    @GetMapping(value="/rickandmorty")
    public Mono<List<SpecificCharacters>> retrieveUpTo5CharacterOrderedByPopularity(@RequestParam Optional<List<String>> excluded) throws SSLException {

        // lets store the request parameters (empty if no exclusion has been described)
        String requestId = sdf1.format(new Timestamp(System.currentTimeMillis()));
        if (!excluded.isEmpty()) {
            interactionsRepository.save(new UserRequest(requestId, String.join(",", excluded.get())));
        } else {
            interactionsRepository.save(new UserRequest(requestId, ""));
        }


        if (excluded.isPresent()) {
            return apiClient.retrieveUpTo5CharacterOrderedByPopularity(Set.copyOf(excluded.get()));
        } else {
            return apiClient.retrieveUpTo5CharacterOrderedByPopularity(Set.of());
        }

    }


}
