package se.mojujo.blogservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.mojujo.blogservice.config.RabbitConfig;
import se.mojujo.blogservice.exception.BlogPostUpdateException;
import se.mojujo.blogservice.service.BlogPostService;
import se.mojujo.blogservice.util.LogUtil;

import java.util.Map;
import java.util.UUID;

@Service
public class UsernameChangedListener {

    private static final Logger logger = LoggerFactory.getLogger(UsernameChangedListener.class);

    private final BlogPostService blogPostService;

    @Autowired
    public UsernameChangedListener(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @RabbitListener(queues = RabbitConfig.USERNAME_CHANGED_QUEUE)
    public void handleUsernameChangedEvent(Map<String, Object> message) {
        try {
            UUID userId = UUID.fromString((String) message.get("userId"));
            String newUsername = (String) message.get("newUsername");

            blogPostService.updatePostAuthor(userId, newUsername);

            LogUtil.info(logger,
                    "POST_AUTHOR_USERNAME_CHANGED",
                    "Successfully changed author username",
                    "userId", userId, "newUsername", newUsername);

        } catch (Exception e) {
            throw new BlogPostUpdateException("Failed to update author username(s)");
        }
    }
}
