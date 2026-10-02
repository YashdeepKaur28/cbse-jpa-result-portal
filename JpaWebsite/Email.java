package my;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Email {
    private static final String FROM_EMAIL   = "yashdeepkaur20133@gmail.com";
    private static final String APP_PASSWORD = "bqny jrur ebse sjam";

    public static void sendRegistrationEmail(String toEmail, String username) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Registration Successful - CBSE Result System");

            String body = "Dear " + username + ",\n\n"
                    + "Thank you for registering with the CBSE Student Result Management System.\n"
                    + "Your account has been created successfully.\n\n"
                    + "Best Regards,\nAdmin Team";
            message.setText(body);
            Transport.send(message);
        } catch (Exception e) {
            System.err.println("Error sending registration email to " + toEmail + ": " + e.getMessage());
        }
    }
}