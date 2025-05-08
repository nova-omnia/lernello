package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.model.data.user.User;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
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
            String plainPassword = null;
            if (!user.isChangedPassword()) {
                plainPassword = generateRandomPassword();
                user.setPassword(passwordEncoder.encode(plainPassword));
                userRepository.save(user);
            }

            MimeMessage mime = buildInviteMail(user, learningKit, plainPassword);
            javaMailSender.send(mime);

            log.info("Sent learning kit invitation to {}", user.getUsername());
        } catch (Exception ex) {
            log.error("Failed to send learning kit invitation to {}: {}",
                user.getUsername(), ex.getMessage(), ex);
            throw new RuntimeException("Failed to send email", ex);
        }
    }

    private String generateRandomPassword() {
        SecureRandom rng = new SecureRandom();
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rng.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private MimeMessage buildInviteMail(User user, LearningKit kit, String plainPassword)
        throws MessagingException {

        String kitUrl = "https://lernello.vercel.app/learningkit/" + kit.getUuid();
        boolean firstLogin = plainPassword != null;

        String htmlBody = createHtmlContent(kit, kitUrl, plainPassword, firstLogin);
        String textBody = createPlainTextContent(kit, kitUrl, plainPassword, firstLogin);

        MimeMessage mime = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
            mime, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setFrom(sender);
        helper.setTo(user.getUsername());
        helper.setSubject("Learning‑Kit Invitation: " + kit.getName());
        helper.setText(textBody, htmlBody);

        return mime;
    }

    private String createHtmlContent(LearningKit kit, String kitUrl,
                                     String pwd, boolean firstLogin) {

        String pwdBlock = firstLogin
            ? """
                  <p style="margin-top:30px;">
                    <strong>🔑 First‑login password:</strong><br>
                    <code style="background:#f1f5f9; padding:4px 8px; border-radius:4px;">%s</code>
                  </p>
                  """.formatted(pwd)
            : "";

        return """
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
            </body>
            </html>
            """.formatted(
            kit.getName(), kit.getDescription(), kit.getDeadlineDate(),
            kit.getContext(), kitUrl, pwdBlock, kitUrl, kitUrl
        );
    }

    private String createPlainTextContent(LearningKit kit, String kitUrl,
                                          String pwd, boolean firstLogin) {

        String pwdLine = firstLogin
            ? "\n\nFirst‑login password: " + pwd
            : "";

        return """
            You’ve been invited to the learning kit: %s

            %s

            Deadline: %s
            Context : %s

            Open the kit → %s%s
            """.formatted(
            kit.getName(), kit.getDescription(), kit.getDeadlineDate(),
            kit.getContext(), kitUrl, pwdLine
        );
    }
}
