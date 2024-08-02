package faang.school.achievement.service;

import faang.school.achievement.TestData;
import faang.school.achievement.exception.DataNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {
    @InjectMocks
    private AchievementService achievementService;
    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    private Achievement achievement;
    private UserAchievement userAchievement;
    private AchievementProgress achievementProgress;


    @BeforeEach
    public void setUp() {
        var testData = new TestData();
        achievement = testData.getAchievement();
        userAchievement = testData.getUserAchievement();
        achievementProgress = testData.getAchievementProgress();
    }


    @Nested
    class PositiveTests {
        @Test
        void hasAchievementTest() {
            when(userAchievementRepository.existsByUserIdAndAchievementId(anyLong(), anyLong())).thenReturn(true);

            boolean actualResult = achievementService.hasAchievement(1L, 1L);

            verify(userAchievementRepository).existsByUserIdAndAchievementId(anyLong(), anyLong());
            assertTrue(actualResult);
        }

        @Test
        void getAchievementTest() {
            var expectedResult = new Achievement();

            when(achievementRepository.findById(anyLong())).thenReturn(Optional.of(expectedResult));

            Achievement actualResult = assertDoesNotThrow(() -> achievementService.getAchievement(1L));

            assertEquals(actualResult, expectedResult);
        }

        @Test
        void incrementProgressPointsTest() {
            when(achievementProgressRepository.findByUserIdAndAchievementId(anyLong(), anyLong()))
                    .thenReturn(Optional.of(achievementProgress));
            when(achievementProgressRepository.save(any(AchievementProgress.class)))
                    .thenReturn(achievementProgress);

            AchievementProgress actualResult = assertDoesNotThrow(() -> achievementService
                    .incrementProgressPoints(1L, 1L));

            assertEquals(11, actualResult.getCurrentPoints());
        }

        @Test
        void createProgressIfNecessaryTest() {
            achievementService.createProgressIfNecessary(1L, 1L);

            verify(achievementProgressRepository).createProgressIfNecessary(anyLong(), anyLong());
        }

        @Test
        void giveAchievementTest() {
            var achievementArgumentCaptor = ArgumentCaptor.forClass(UserAchievement.class);

            achievementService.giveAchievement(achievement, 1L);

            verify(userAchievementRepository).save(achievementArgumentCaptor.capture());
            assertEquals(userAchievement, achievementArgumentCaptor.getValue());
        }
    }

    @Nested
    class NegativeTests {
        @Test
        void getAchievementTest() {
            when(achievementRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(DataNotFoundException.class, () -> achievementService.getAchievement(1L));
        }

        @Test
        void incrementProgressPointsTest() {
            when(achievementProgressRepository.findByUserIdAndAchievementId(anyLong(), anyLong()))
                    .thenReturn(Optional.empty());

            assertThrows(DataNotFoundException.class, () -> achievementService
                    .incrementProgressPoints(1L, 1L));

            verify(achievementProgressRepository, times(0)).save(any(AchievementProgress.class));
        }
    }
}