package com.adultswim.rickandmorty.component;

import com.adultswim.rickandmorty.entity.Characters;
import com.adultswim.rickandmorty.entity.SpecificCharacters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class PopularityComparator implements Comparator<SpecificCharacters> {

    @Value("${rickandmorty.url.numCharacters}")
    public int numCharacters;

    @Override
    public int compare(SpecificCharacters firstCharacter, SpecificCharacters secondCharacter) {
        return Integer.compare(popularityCalculator(firstCharacter), popularityCalculator(secondCharacter));
    }

    public int popularityCalculator(SpecificCharacters character) {
        // To make it easier lets consider popularity index is simply calculated based on the amount of episodes it appears into
        // This assumption considers popularity index can be, obviously, repeated across all characters several times (per index).
        return character.getEpisode().size();
    }

}
