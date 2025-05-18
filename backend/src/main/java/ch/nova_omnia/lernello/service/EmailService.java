package ch.nova_omnia.lernello.service;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final String CHARSET = StandardCharsets.UTF_8.name();

    @Value("${spring.mail.username}")
    private String sender;

    public void sendLearningKitInvitation(User user, LearningKit learningKit) {
        try {
            String plainPassword = null;
            if (!user.isChangedPassword()) {
                plainPassword = userService.generateRandomPassword();
            }

            MimeMessage mime = buildMimeMessage(
                    user.getUsername(), "Learning‑Kit Invitation: " + learningKit.getName(), buildLearningKitTextContent(user, learningKit, plainPassword), buildLearningKitHtmlContent(user, learningKit, plainPassword)
            );

            javaMailSender.send(mime);
            log.info("Sent learning kit invitation to {}", user.getUsername());

            if (plainPassword != null) {
                user.setChangedPassword(false);
                user.setPassword(passwordEncoder.encode(plainPassword));
                userRepository.save(user);
            }
        } catch (Exception ex) {
            log.error("Failed to send learning kit invitation to {}: {}", user.getUsername(), ex.getMessage(), ex);
            throw new RuntimeException("Failed to send email", ex);
        }
    }

    public void sendNewLoginData(User user) {
        try {
            String plainPassword = userService.generateRandomPassword();
            MimeMessage mime = buildMimeMessage(
                    user.getUsername(), "New Login Data", builtResetPasswordTextContent(user.getUsername(), plainPassword), buildResetPasswordHtmlContent(user.getUsername(), plainPassword)
            );

            javaMailSender.send(mime);
            log.info("Sent new login data to {}", user.getUsername());

            user.setPassword(passwordEncoder.encode(plainPassword));
            user.setChangedPassword(false);
            userRepository.save(user);
        } catch (Exception ex) {
            log.error("Failed to send new login data to {}: {}", user.getUsername(), ex.getMessage(), ex);
            throw new RuntimeException("Failed to send email", ex);
        }
    }

    private MimeMessage buildMimeMessage(String to, String subject, String text, String html) throws MessagingException {
        MimeMessage mime = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, CHARSET);
        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, html);
        return mime;
    }

    private String buildResetPasswordHtmlContent(String username, String pwd) {
        return baseHtmlWrapper("""
                    <h2 style="color:#2563eb; margin-top:0;">Welcome, %s</h2>
                    <p style="font-size:15px;">You can now log in to Lernello with your new password.</p>
                    <p style="margin-top:30px;">
                      <strong>🔑 First‑login password:</strong><br>
                      <code style="background:#f1f5f9; padding:4px 8px; border-radius:4px;">%s</code>
                    </p>
                """.formatted(username, pwd));
    }

    private String builtResetPasswordTextContent(String username, String pwd) {
        return """
                    Welcome, %s

                    You can now log in to Lernello with your new password.

                    First‑login password: %s
                """.formatted(username, pwd);
    }

    private String buildLearningKitHtmlContent(User user, LearningKit kit, String pwd) {
        String kitUrl = "https://lernello.vercel.app/learningkit/" + kit.getUuid();
        String passwordBlock = pwd != null ? """
                    <p style="margin-top:30px;">
                      <strong>🔑 First‑login password:</strong><br>
                      <code style="background:#f1f5f9; padding:4px 8px; border-radius:4px;">%s</code>
                    </p>
                """.formatted(pwd) : "";

        return baseHtmlWrapper("""
                    <h2 style="color:#2563eb; margin-top:0;">🚀 Hello, %s – You're invited to <strong>%s</strong>!</h2>
                    <p style="font-size:15px;">%s</p>
                    <table style="margin:20px 0;">
                      <tr><td><strong>Deadline&nbsp;</strong></td><td>%s</td></tr>
                      <tr><td><strong>Context&nbsp;</strong></td><td>%s</td></tr>
                    </table>
                    <p>
                      <a href="%s" style="background:#2563eb; color:#ffffff; padding:12px 24px;
                          border-radius:6px; text-decoration:none; font-weight:600;">
                        Open learning kit
                      </a>
                    </p>
                    %s
                    <p style="font-size:12px; color:#6b7280;">
                      If the button doesn’t work, copy this link into your browser:<br>
                      <a href="%s" style="color:#2563eb;">%s</a>
                    </p>
                """.formatted(user.getUsername(), kit.getName(), kit.getDescription(), kit.getDeadlineDate(), kitUrl, passwordBlock, kitUrl, kitUrl));
    }

    private String buildLearningKitTextContent(User user, LearningKit kit, String pwd) {
        String pwdLine = pwd != null ? "\n\nFirst‑login password: " + pwd : "";
        return """
                    Hello, %s – You’ve been invited to the learning kit: %s

                    %s

                    Deadline: %s
                    Context : %s

                    Open the kit → %s%s
                """.formatted(user.getUsername(), kit.getName(), kit.getDescription(), kit.getDeadlineDate(), "https://lernello.vercel.app/learningkit/" + kit.getUuid(), pwdLine);
    }

    private String baseHtmlWrapper(String bodyContent) {
        return """
                    <!DOCTYPE html>
                    <html>
                    <body style="font-family:Arial, sans-serif; line-height:1.55; color:#202022;">
                      %s
                    </body>
                    </html>
                """.formatted(bodyContent);
    }
}
