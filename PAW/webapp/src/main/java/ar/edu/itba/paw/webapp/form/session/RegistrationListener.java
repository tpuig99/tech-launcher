package ar.edu.itba.paw.webapp.form.session;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;


@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService service;

    @Autowired
    MessageSource messageSource;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        if(!event.isResend()){
            service.createVerificationToken(user, token);
        }else{
            service.generateNewVerificationToken(user,token);
        }
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mailSender.setJavaMailProperties(prop);
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("confirmemailonly", "pawserver");
            }
        });
        mailSender.setSession(session);
        String recipientAddress = user.getMail();
        String subject = messageSource.getMessage("email.subject",new Object[]{}, Locale.getDefault());

        String confirmationUrl
                = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
        String message = messageSource.getMessage("email.body",new Object[]{}, Locale.getDefault());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("confirmemailonly@gmail.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(email);
    }
}