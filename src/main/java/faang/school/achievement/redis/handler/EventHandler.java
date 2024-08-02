package faang.school.achievement.redis.handler;

import org.springframework.scheduling.annotation.Async;

public interface EventHandler<E> {
    @Async(value = "taskExecutor")
    void handleEvent(E event);
}
