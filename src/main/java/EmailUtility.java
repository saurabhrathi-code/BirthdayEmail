import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class EmailUtility {


    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public void setMailServerProperties() {

        String emailPort = "587"; // gmail's smtp port
        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port",emailPort);
        emailProperties.put("mail.smtp.auth","true");
        emailProperties.put("mail.smtp.starttls.enable","true");
    }


    public void createEmailMessage(String body) throws MessagingException {
        String [] toEmail = {BirthdayUtility.ToemailAddress};
        String emailSubject = "Happy Birthday";
        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);
        for (int i =0; i<toEmail.length; i++) {
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail[i]));
        }
        emailMessage.setSubject(emailSubject);
        MimeMultipart multipart = new MimeMultipart("related");
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = " <html> \n" +
                "<body> \n" +
                "<img src=\"cid:image\"> </img> \n" +
                "</br> \n" +
                "</br> \n" +
                "<table class\"x_m_-4302907314109865712gmail-MsoNormalTable\" border\"0\" cellspacing= \"1\"  cellpa>" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td width=\"709\" colspan=\"2\" valign=\"top\">"+
                "</body> \n" +
                "</html> \n";

        messageBodyPart.setContent(htmlText, "text/html");
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();

        String headerImageFilePath = System.getProperty("user.dir") + File.separator + "src" + File.separator
                + "main" + File.separator + "resources" + File.separator + "image.png";

        String cakeImageFilePath = System.getProperty("user.dir") + File.separator + "src" + File.separator
                + "main" + File.separator + "resources" + File.separator + "BirthCake.png";

        DataSource fsd = new FileDataSource(headerImageFilePath);
        messageBodyPart.setDataHandler(new DataHandler(fsd));
        messageBodyPart.addHeader("Content-ID", "<image>");
        multipart.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();
        DataSource fsd1 = new FileDataSource(cakeImageFilePath);
        messageBodyPart.setDataHandler(new DataHandler(fsd1));
        messageBodyPart.addHeader("Content-ID", "<image1>");
        multipart.addBodyPart(messageBodyPart);
        emailMessage.setContent(multipart);
    }


    public void sendEmail() throws AddressException, MessagingException {
        String emailHost = "smtp.gmail.com";
        String fromUser = "saurabhss.rathi@gmail.com"; //just the id alone without @gmail.com
        String fromUserEmailPassword = "kbsuibibd";
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully");

    }

/*
    public String readEmailFromHtml(String filePath, Map<String, String> input) {
        String msg = readContentFromFile(filePath);
        try {
            Set<Map.Entry<String, String>> entries = input.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return msg;
    }

    public String readContentFromFile(String fileName) {
        StringBuffer content = new StringBuffer();

        try {
            //use buffering, reading one line at a time
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                    content.append(System.getProperty("line.separator"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
*/
}
