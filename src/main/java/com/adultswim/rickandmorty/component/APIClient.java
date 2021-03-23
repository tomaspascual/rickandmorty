package com.adultswim.rickandmorty.component;

import com.adultswim.rickandmorty.entity.Characters;
import com.adultswim.rickandmorty.entity.SpecificCharacters;
import io.netty.util.internal.ThreadLocalRandom;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Component
public class APIClient {

    @Value( "${rickandmorty.url.characters}" )
    public String charactersUrl;

    @Value( "${rickandmorty.url.numCharacters}" )
    public int numCharacters;

    @Autowired
    PopularityComparator popularityComparator;

    static final Log logger = LogFactory.getLog(APIClient.class);



    public Mono<List<SpecificCharacters>> retrieveUpTo5CharacterOrderedByPopularity(Set excludedCharacters) {


                    int numberOfCharactersToShow = ThreadLocalRandom.current().nextInt(1, 6);
                    String[] charactersList = new String[numberOfCharactersToShow];

                    int fullFilled = 0;
                    while (fullFilled<numberOfCharactersToShow) {
                        String possibleCharacterToShow = ThreadLocalRandom.current().nextInt(1, numCharacters)+"";
                        if (!excludedCharacters.contains(possibleCharacterToShow)) {
                            charactersList[fullFilled] = possibleCharacterToShow;
                            fullFilled++;
                        }
                    }



                    return WebClient
                            .builder()
                            .baseUrl(charactersUrl + "/" + String.join(",", charactersList))
                            .build()
                            .get()
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToFlux(SpecificCharacters.class)
                            .map(specificCharacters -> {
                                specificCharacters.setPopularityIndex(popularityComparator.popularityCalculator(specificCharacters));
                                return specificCharacters;
                            })
                            .log() //remove this in a production environment obviously please
                            .onErrorResume(ex -> {
                                logger.error(ex);
                                return Mono.empty();
                            })
                            .collectSortedList(popularityComparator.reversed());

    }






}
