package faang.school.achievement.event;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class MentorshipStartEvent {

    private long mentorId;

    private long menteeId;
}
