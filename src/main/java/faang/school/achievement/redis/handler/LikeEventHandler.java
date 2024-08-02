package faang.school.achievement.redis.handler;

import faang.school.achievement.dto.LikeEventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class LikeEventHandler implements EventHandler<LikeEventDto> {
    protected final AchievementService achievementService;
    protected Achievement achievement;


    public void handleEvent(LikeEventDto event) {
        log.info("Received an like event from like_topic for user with id: {}", event.getAuthorId());

        createProgressIfNecessary(event.getAuthorId());

        AchievementProgress achievementProgress = achievementService
                .incrementProgressPoints(event.getAuthorId(), achievement.getId());

        log.info("All like achievements progresses have been incremented");

        tryGiveAchievement(achievementProgress);
    }

    protected abstract void tryGiveAchievement(AchievementProgress achievementProgress);

    protected abstract void createProgressIfNecessary(long userId);
}
