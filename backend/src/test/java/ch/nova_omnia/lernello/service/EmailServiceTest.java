package ch.nova_omnia.lernello.service;

import ch.nova_omnia.lernello.model.data.LearningKit;
import ch.nova_omnia.lernello.model.data.user.Role;
import ch.nova_omnia.lernello.model.data.user.User;
import ch.nova_omnia.lernello.repository.UserRepository;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepo;
    @Mock
    private UserService userService;

    @InjectMocks
    private EmailService emailService;

    private User testUser;
    private LearningKit testKit;

    @BeforeEach
    void setup() {
        testUser = new User("newuser@example.com", "First", "Last", "initialPwd", "en", Role.TRAINEE);
        testUser.setChangedPassword(false);
        testKit = new LearningKit("Java 101");
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        try {
            setSenderField();
        } catch (Exception e) {
            throw new RuntimeException("Failed to set sender field", e);
        }

    }

    private void setSenderField() throws NoSuchFieldException, IllegalAccessException {
        Field senderField = EmailService.class.getDeclaredField("sender");
        senderField.setAccessible(true);
        senderField.set(emailService, "mocked-sender@example.com");
    }


    @Test
    void sendLearningKitInvitation_shouldGenerateAndSavePasswordForNewUser() throws Exception {
        String generatedPlainPwd = "TempPass123!";
        when(passwordEncoder.encode(any())).thenAnswer(inv -> "ENCODED_" + inv.getArgument(0));
        when(userService.generateRandomPassword()).thenReturn(generatedPlainPwd);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendLearningKitInvitation(testUser, testKit);

        verify(userService).generateRandomPassword();
        verify(passwordEncoder).encode(generatedPlainPwd);
        assertThat(testUser.isChangedPassword()).isFalse();
        assertThat(testUser.getPassword()).isEqualTo("ENCODED_" + generatedPlainPwd);
        verify(userRepo).save(testUser);
        verify(mailSender).send((MimeMessage) argThat(mime -> {
            try {
                MimeMessage mm = (MimeMessage) mime;
                MimeMessageHelper helper = new MimeMessageHelper(mm);
                String to = helper.getMimeMessage().getAllRecipients()[0].toString();
                String subject = helper.getMimeMessage().getSubject();
                return to.equals(testUser.getUsername())
                    && subject.contains(testKit.getName());
            } catch (Exception e) {
                return false;
            }
        }));
    }

    @Test
    void sendLearningKitInvitation_shouldNotOverwritePasswordForUserWhoAlreadyChangedIt() throws Exception {
        testUser.setChangedPassword(true);

        emailService.sendLearningKitInvitation(testUser, testKit);

        verify(userService, never()).generateRandomPassword();
        verify(passwordEncoder, never()).encode(anyString());
        verify(mailSender).send(any(MimeMessage.class));
        verify(userRepo, never()).save(any());
    }

    @Test
    void sendLearningKitInvitation_onEmailFailure_shouldThrowRuntimeException() {
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(MimeMessage.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> emailService.sendLearningKitInvitation(testUser, testKit));
        assertThat(ex.getMessage()).contains("Failed to send email");
        verify(userRepo, never()).save(any());
    }
}
