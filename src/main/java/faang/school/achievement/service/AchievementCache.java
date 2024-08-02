package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    private Map<String, Achievement> achievementMap;

    @PostConstruct
    public void setUp() {
        achievementMap = achievementRepository.findAll().stream()
                .collect(Collectors.toMap(Achievement::getTitle, Function.identity()));
    }

    public Achievement get(String title) {
        return achievementMap.get(title);
    }
}
