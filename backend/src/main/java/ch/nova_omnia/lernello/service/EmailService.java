package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.LearningKit;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.user.User;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

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
     */
    public void sendLearningKitInvitation(User user, LearningKit learningKit) {
        try {
            MimeMessage invitationEmail = formatHtmlInvite(user, learningKit);
            javaMailSender.send(invitationEmail);

            log.info("Sent learning kit invitation to {}", user.getUsername());
        } catch (Exception ex) {
            log.error("Failed to send learning kit invitation to {}: {}",
                user.getUsername(), ex.getMessage(), ex);
            throw new RuntimeException("Failed to send email", ex);
        }
    }

    private MimeMessage formatHtmlInvite(User user, LearningKit learningKit) throws MessagingException {

        String kitUrl = "https://lernello.vercel.app/learningkit/" + learningKit.getUuid();

        String html = createHtmlContent(learningKit, kitUrl);
        // Fallback to plain text if HTML is not supported
        String text = createPlainTextContent(learningKit, kitUrl);

        MimeMessage mime = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setFrom(sender);
        helper.setTo(user.getUsername());
        helper.setSubject("Learning Kit Invitation: " + learningKit.getName());
        helper.setText(text, html);

        return mime;
    }

    private String createHtmlContent(LearningKit learningKit, String kitUrl) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <body style="font-family:Arial, sans-serif; line-height:1.55; color:#202022;">
              <h2 style="color:#2563eb; margin-top:0;">
                🚀 You’re invited to <strong>%s</strong>!
              </h2>
              <p style="font-size:15px;">%s</p>
              <table style="margin:20px 0;">
                <tr><td><strong>Deadline&nbsp;</strong></td><td>%s</td></tr>
                <tr><td><strong>Context&nbsp;</strong></td><td>%s</td></tr>
              </table>
              <p>
                <a href="%s" style="background:#2563eb; color:#ffffff; padding:12px 24px; border-radius:6px; text-decoration:none; font-weight:600;">Open learning kit</a>
              </p>
              <p style="font-size:12px; color:#6b7280;">
                If the button doesn’t work, copy this link into your browser:<br>
                <a href="%s" style="color:#2563eb;">%s</a>
              </p>
            </body>
            </html>
            """, learningKit.getName(), learningKit.getDescription(), learningKit.getDeadlineDate(), learningKit.getContext(), kitUrl, kitUrl, kitUrl);
    }

    private String createPlainTextContent(LearningKit learningKit, String kitUrl) {
        return String.format(
            "You’ve been invited to the learning kit: %s%n%n" +
                "%s%n%n" +
                "Deadline: %s%n" +
                "Context: %s%n%n" +
                "Open the kit → %s",
            learningKit.getName(),
            learningKit.getDescription(),
            learningKit.getDeadlineDate(),
            learningKit.getContext(),
            kitUrl
        );
    }
}
