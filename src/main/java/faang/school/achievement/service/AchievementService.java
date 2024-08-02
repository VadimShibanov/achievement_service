package faang.school.achievement.service;

import faang.school.achievement.exception.DataNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;


    @Transactional(readOnly = true)
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional(readOnly = true)
    public Achievement getAchievement(long achievementId) {
        return achievementRepository
                .findById(achievementId)
                .orElseThrow(getAchievementNotFoundExceptionSupplier(achievementId));
    }

    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional
    public AchievementProgress incrementProgressPoints(long userId, long achievementId) {
        AchievementProgress userAchievementProgress = achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(getAchievementNotFoundExceptionSupplier(achievementId));

        userAchievementProgress.increment();
        return achievementProgressRepository.save(userAchievementProgress);
    }

    @Transactional
    public void giveAchievement(Achievement achievement, long userId) {
        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();

        userAchievementRepository.save(userAchievement);
    }

    @Transactional(readOnly = true)
    public AchievementProgress getProgress(long mentorId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(mentorId, achievementId).orElseThrow(
                getAchievementNotFoundExceptionSupplier(achievementId));
    }

    private Supplier<DataNotFoundException> getAchievementNotFoundExceptionSupplier(long achievementId) {
        return () -> {
            String message = String.format("Achievement with id: %s not found", achievementId);
            return new DataNotFoundException(message);
        };
    }
}