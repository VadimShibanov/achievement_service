package faang.school.achievement.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.LikeEventDto;
import faang.school.achievement.redis.handler.LikeEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {
    private final List<LikeEventHandler> handlers;
    private final ObjectMapper objectMapper;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikeEventDto likeEvent;
        try {
            likeEvent = objectMapper.readValue(message.getBody(), LikeEventDto.class);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Received message decoding failed: %s", e));
        }

        handlers.forEach(handler -> handler.handleEvent(likeEvent));
    }
}
