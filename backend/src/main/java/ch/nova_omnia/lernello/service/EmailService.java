package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.LearningKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);


    @Value("${spring.mail.username}")
    private String sender;

    /**
     * Sends an email invitation to a user for a learning kit.
     *
     * @param user        The user to whom the invitation is sent.
     * @param learningKit The learning kit for which the invitation is sent.
     * @return true if the email was sent successfully, false otherwise.
     */
    public boolean sendLearningKitInvitation(User user, LearningKit learningKit) {
        try {
            SimpleMailMessage mailMessage = formatMailMessageLKInvite(user, learningKit);

            javaMailSender.send(mailMessage);
            log.info("Sent learning kit invitation to {}", user.getUsername());

            return true;
        } catch (Exception ex) {
            log.error("Failed to send learning kit invitation to {}: {}",
                user.getUsername(), ex.getMessage(), ex);
            return false;
        }
    }

    private SimpleMailMessage formatMailMessageLKInvite(User user, LearningKit learningKit) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Learning Kit Invitation");
        String body = """
        You have been invited to join the learning kit: %s.
        Description: %s
        Deadline: %s
        Context: %s
        """.formatted(
                learningKit.getName(),
                learningKit.getDescription(),
                learningKit.getDeadlineDate(),
                learningKit.getContext()
        );
        mailMessage.setText(body);

        return mailMessage;
    }
}
