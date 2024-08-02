package faang.school.achievement.config.redis;

import faang.school.achievement.redis.listener.LikeEventListener;
import faang.school.achievement.redis.listener.MentorshipEventListener;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Setter
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisConfig {
    private int port;
    private String host;
    private Channels channels;
    private final LikeEventListener likeEventListener;

    private final MentorshipEventListener mentorshipEventListener;


    @Bean
    RedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public MessageListenerAdapter likeEventListenerAdapter() {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public MessageListenerAdapter mentorshipEventListenerAdapter() {
        return new MessageListenerAdapter(mentorshipEventListener);
    }

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(channels.getLike());
    }

    @Bean
    public ChannelTopic mentorshipTopic() {
        return new ChannelTopic(channels.getMentorship());
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        var container = new RedisMessageListenerContainer();

        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(likeEventListenerAdapter(), likeTopic());
        container.addMessageListener(mentorshipEventListenerAdapter(), mentorshipTopic());

        return container;
    }

    /**
     * В этом классе хранятся названия всех топиков (каналов) редиса,
     * получаемые из application.yaml по пути spring.data.redis.channels
     */
    @Data
    private static class Channels {
        private String like;
        private String mentorship;
    }
}
