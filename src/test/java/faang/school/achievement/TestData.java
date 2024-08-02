package faang.school.achievement;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import lombok.Getter;

@Getter
public class TestData {
    public static final long USER_ID = 1L;
    public static final int CURRENT_POINTS = 1001;
    public static final int ACHIEVEMENT_ID = 1;
    public static final int ACHIEVEMENT_POINTS = 1000;
    private final Achievement achievement;
    private final UserAchievement userAchievement;
    private final AchievementProgress achievementProgress;

    public TestData() {
        achievement = createAchievement();
        userAchievement = createUserAchievement();
        achievementProgress = createAchievementProgress();
    }

    private AchievementProgress createAchievementProgress() {
        return AchievementProgress.builder()
                .userId(USER_ID)
                .achievement(achievement)
                .currentPoints(CURRENT_POINTS)
                .build();
    }

    private UserAchievement createUserAchievement() {
        return UserAchievement.builder()
                .userId(USER_ID)
                .achievement(achievement)
                .build();
    }

    private Achievement createAchievement() {
        return Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .description("description")
                .title("title")
                .points(ACHIEVEMENT_POINTS)
                .build();
    }
}
