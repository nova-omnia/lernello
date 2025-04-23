package ch.nova_omnia.lernello.service;

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

    @Value("${spring.mail.username}")
    private String sender;

    // TODO correct parameters and message will be defined with the create learning kit issue
    public String sendLearningKitInvitation(User user) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(user.getUsername());
            mailMessage.setText("Hello " + user.getUsername() + ",\n\n" + "Your password is: " + user.getPassword());
            mailMessage.setSubject("Test Mail");

            javaMailSender.send(mailMessage);
            return "Test Mail Sent Successfully...";
        } catch (Exception e) {
            return "Error while Sending Test Mail";
        }
    }
}
