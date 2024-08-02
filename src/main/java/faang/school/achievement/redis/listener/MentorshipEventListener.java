package faang.school.achievement.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.MentorshipStartEvent;
import faang.school.achievement.redis.handler.SenseiAchievementHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MentorshipEventListener implements MessageListener {

    private final ObjectMapper objectMapper;

    private final SenseiAchievementHandler senseiAchievementHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipStartEvent mentorshipStartEvent = objectMapper.readValue(message.getBody(), MentorshipStartEvent.class);
            senseiAchievementHandler.handleEvent(mentorshipStartEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
