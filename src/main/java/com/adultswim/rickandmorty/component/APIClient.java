package com.adultswim.rickandmorty.component;

import com.adultswim.rickandmorty.entity.Characters;
import com.adultswim.rickandmorty.entity.SpecificCharacters;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.internal.ThreadLocalRandom;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SSLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;



@Component
public class APIClient {

    @Value("${rickandmorty.url.characters}")
    public String charactersUrl;

    @Value("${rickandmorty.url.numCharacters}")
    public int numCharacters;

    @Autowired
    PopularityComparator popularityComparator;

    static final Log logger = LogFactory.getLog(APIClient.class);


    public Mono<List<SpecificCharacters>> retrieveUpTo5CharacterOrderedByPopularity(Set excludedCharacters) throws SSLException {

        int numberOfCharactersToShow = ThreadLocalRandom.current().nextInt(1, 6);
        String[] charactersList = new String[numberOfCharactersToShow];

        // Lets select which characters will be show
        int fullFilled = 0;
        while (fullFilled < numberOfCharactersToShow) {
            String possibleCharacterToShow = ThreadLocalRandom.current().nextInt(1, numCharacters) + "";
            // consider exclusions
            if (!excludedCharacters.contains(possibleCharacterToShow)) {
                charactersList[fullFilled] = possibleCharacterToShow;
                fullFilled++;
            }
        }

        logger.info("PATH: " + String.join(",", charactersList));

        WebClient wc =  WebClient
                .builder()
                .baseUrl(charactersUrl + "/" + String.join(",", charactersList))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT_CHARSET, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .exchangeStrategies(ExchangeStrategies.builder().codecs(this::acceptedCodecs).build())
                .build();

        return wc
                .get()
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
                .collectSortedList(popularityComparator.reversed()); // The Comparator will order based in the popularity index declared inside

    }


    private void acceptedCodecs(ClientCodecConfigurer clientCodecConfigurer) {
        clientCodecConfigurer.customCodecs().register(new Jackson2JsonEncoder(new ObjectMapper(), MediaType.APPLICATION_JSON));
        clientCodecConfigurer.customCodecs().register(new Jackson2JsonDecoder(new ObjectMapper(), MediaType.TEXT_PLAIN));
    }



}
