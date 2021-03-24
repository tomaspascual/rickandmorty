package com.adultswim.rickandmorty.component;

import com.adultswim.rickandmorty.entity.SpecificCharacters;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.internal.ThreadLocalRandom;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.List;
import java.util.Set;


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

        Iterable<String> allowedCiphers = List.of("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384");


        // Webclient that trusts any external SSL certificate
        SslContext sslContext = SslContextBuilder
                .forClient()
                .protocols("SSLv3","TLSv1","TLSv1.1","TLSv1.2")
                .ciphers(allowedCiphers)
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        HttpClient httpClient = HttpClient.create().secure(ssl -> { ssl.sslContext(sslContext); });
        ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);
        WebClient wc =  WebClient
                .builder()
                .baseUrl(charactersUrl + "/" + String.join(",", charactersList))
                .clientConnector(httpConnector)
                .build();

        return wc
                .get()
                //.accept(MediaType.APPLICATION_JSON)
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



}
