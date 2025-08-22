package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.repositories.BannedWordRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BannedWordService {
    private final BannedWordRepository bannedWordRepository;
    private Set<String> bannedWords = new HashSet<>();

    @PostConstruct
    private void init() {
        bannedWords = bannedWordRepository.findAll()
                .stream()
                .map(w->w.getWord().toLowerCase())
                .collect(Collectors.toSet());
    }

    public boolean containsWord(String text) {
        String[] tokens = text.toLowerCase().split("\\s+");
        for (String token : tokens) {
            if (bannedWords.contains(token)) {
                return true;
            }
        }
        return false;
    }
}
