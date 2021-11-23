package main;

import java.util.ArrayList;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *  Class SendEmail for send mails for friends by using smtp.gmail host
 * @version 1.0
 * @since   2021-10-18
 */

public class SendEmail {
    /**
     * Function to send mail to friend
     *
     * @param FileName First name of friend
     * @param email    Email address sent mail to
     * @param subject   Header of mail
     * @param text      Base of mail
     * @param path      Path for Picture
     */
    public static void SendBirthdaySMS(String email, String subject, String text, String path, String FileName) {

        final String username = "timofei190612@gmail.com";
        final String password = "tima5193192";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
            message.setSubject(subject);

            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText(text);

            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            DataSource source = new FileDataSource(path);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(FileName);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Message sent successfully");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
    public static void SendHolidaySMS(ArrayList<String> emails, String subject, String text, String path, String FileName) {

        final String username = "timofei190612@gmail.com";
        final String password = "tima5193192";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            InternetAddress[] recipientAddress = new InternetAddress[emails.size()];
            int counter = 0;
            for (String recipient : emails) {
                recipientAddress[counter] = new InternetAddress(recipient.trim());
                counter++;
            }
            message.setRecipients(Message.RecipientType.TO, recipientAddress);
            message.setSubject(subject);

            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText(text);

            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            DataSource source = new FileDataSource(path);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(FileName);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Message sent successfully for all recipients");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}
