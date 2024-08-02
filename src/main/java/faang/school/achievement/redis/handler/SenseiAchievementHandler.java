package faang.school.achievement.redis.handler;

import faang.school.achievement.event.MentorshipStartEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SenseiAchievementHandler implements EventHandler<MentorshipStartEvent> {

    @Value("${achievement.sensei.title}")
    private String achievementName;

    private static final int POINTS_TO_SENSEI = 30;

    private final AchievementService achievementService;

    private final AchievementCache achievementCache;

    @Override
    public void handleEvent(MentorshipStartEvent event) {
        Achievement achievement = achievementCache.get(achievementName);

        if (!achievementService.hasAchievement(event.getMentorId(), achievement.getId())) {
            achievementService.createProgressIfNecessary(event.getMentorId(), achievement.getId());
        }

        AchievementProgress progress = achievementService.getProgress(event.getMentorId(), achievement.getId());
        if (progress.getCurrentPoints() < POINTS_TO_SENSEI) {
            progress = achievementService.incrementProgressPoints(event.getMentorId(), achievement.getId());
        }

        if (progress.getCurrentPoints() == achievement.getPoints()) {
            achievementService.giveAchievement(achievement, event.getMentorId());
        }
    }
}
