package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.EmailService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender = new JavaMailSenderImpl();

    @Autowired
    private UserService service;

    @Override
    public void sendEmailConfirmation(User user,String appUrl){
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);
        try{
        Transport.send(registrationMessage(user.getMail(),appUrl,token));}
        catch (Exception e){

        }
        /*String recipientAddress = user.getMail();
        String subject = "Registration Confirmation";
        String confirmationUrl = appUrl + "/regitrationConfirm.html?token=" + token;
        String message = "Click here to confirm your account!";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);

        mailSender.send(email);*/
    }

    private Message registrationMessage(String mail, String appUrl, String token) throws MessagingException {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("confirmemailonly", "pawserver");
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("confirmemailonly@gmail.com"));
        InternetAddress[] auxi = InternetAddress.parse(mail);
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(mail));
        message.setSubject("Registration Confirmation");
        System.out.println(auxi[0].getAddress());
        String confirmationUrl = appUrl + "/regitrationConfirm.html?token=" + token;
        String initMessage = "Click here to confirm your account!";

        String msg = initMessage + "\r\n" + "http://localhost:8080" + confirmationUrl;

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
        return message;
    }

}
