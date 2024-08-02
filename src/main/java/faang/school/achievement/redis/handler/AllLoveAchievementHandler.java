package faang.school.achievement.redis.handler;

import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AllLoveAchievementHandler extends LikeEventHandler {
    @Value("${achievement.all-love.title}")
    private String achievementTitle;
    private final AchievementCache achievementCache;

    public AllLoveAchievementHandler(AchievementService achievementService, AchievementCache achievementCache) {
        super(achievementService);
        this.achievementCache = achievementCache;
    }

    @PostConstruct
    public void setUp() {
        achievement = achievementCache.get(achievementTitle);
    }


    @Override
    protected void tryGiveAchievement(AchievementProgress achievementProgress) {
        long userId = achievementProgress.getUserId();

        if (achievementService.hasAchievement(userId, achievement.getId()) ||
                achievementProgress.getCurrentPoints() < achievement.getPoints()) {
            return;
        }

        achievementService.giveAchievement(achievement, userId);
        log.info("User with id: {} received achievement {}", userId, achievement.getTitle());
    }

    @Override
    public void createProgressIfNecessary(long userId) {
        achievementService.createProgressIfNecessary(userId, achievement.getId());
    }
}
