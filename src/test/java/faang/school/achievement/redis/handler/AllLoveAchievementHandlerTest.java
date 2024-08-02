package faang.school.achievement.redis.handler;

import faang.school.achievement.TestData;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AllLoveAchievementHandlerTest {
    @InjectMocks
    private AllLoveAchievementHandler achievementHandler;
    @Mock
    private AchievementService achievementService;

    private Achievement achievement;
    private UserAchievement userAchievement;
    private AchievementProgress achievementProgress;


    @BeforeEach
    public void setUp() {
        var testData = new TestData();
        achievement = testData.getAchievement();
        userAchievement = testData.getUserAchievement();
        achievementProgress = testData.getAchievementProgress();

        changeField(achievementHandler, "achievementTitle", achievement.getTitle(), false);
        changeField(achievementHandler, "achievement", achievement, true);
    }

    @Nested
    class PositiveTests {
        @Test
        public void tryGiveAchievementTest() {
            var achievementArgumentCaptor = ArgumentCaptor.forClass(Achievement.class);
            when(achievementService.hasAchievement(anyLong(), anyLong())).thenReturn(false);

            achievementHandler.tryGiveAchievement(achievementProgress);

            verify(achievementService).giveAchievement(achievementArgumentCaptor.capture(), eq(userAchievement.getUserId()));
            assertEquals(achievement, achievementArgumentCaptor.getValue());
        }

        @Test
        public void createProcessIfNecessary() {
            achievementHandler.createProgressIfNecessary(1L);

            verify(achievementService).createProgressIfNecessary(1L, 1L);
        }
    }

    @Nested
    class NegativeTests {
        @DisplayName("should not give achievement when user already has this achievement")
        @Test
        public void tryReGiveAchievementTest() {
            when(achievementService.hasAchievement(anyLong(), anyLong())).thenReturn(true);

            achievementHandler.tryGiveAchievement(achievementProgress);

            verify(achievementService, times(0))
                    .giveAchievement(any(Achievement.class), eq(userAchievement.getUserId()));
        }

        @DisplayName("should not give achievement when achievement points aren't enough")
        @Test
        public void tryGiveAchievementWithNoEnoughPointsTest() {
            when(achievementService.hasAchievement(anyLong(), anyLong())).thenReturn(false);
            achievementProgress.setCurrentPoints(0);

            achievementHandler.tryGiveAchievement(achievementProgress);

            verify(achievementService, times(0))
                    .giveAchievement(any(Achievement.class), eq(userAchievement.getUserId()));
        }
    }

    public static void changeField(Object obj, String fieldName, Object newValue, boolean fromSuperclass) {
        try {
            Field field;
            if (fromSuperclass) {
                field = obj.getClass().getSuperclass().getDeclaredField(fieldName);
            } else {
                field = obj.getClass().getDeclaredField(fieldName);
            }

            field.setAccessible(true);

            field.set(obj, newValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}