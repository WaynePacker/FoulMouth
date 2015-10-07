package com.imy320.foultmouth.personaldigitaldairy;

//TODO: if this does not connect to SMTP add the following line to manifest :
//<uses-permission android:name="android.permission.INTERNET" />

import android.database.Cursor;
import android.os.AsyncTask;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import java.security.Security;
import java.util.Properties;

public class EmailHandler extends javax.mail.Authenticator
{
    private String mailhost = "smtp.gmail.com";
    private String user = "personaldigitaldiary@gmail.com";
    private String password = "digitaldiary123";
    private String subject = "Daily Reminder";

    private Session session;
    private Cursor cursor;

    static
    {
        Security.addProvider(new JSSEProvider());
    }

    public EmailHandler(Cursor _cursor)
    {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol","smtp");
        props.setProperty("mail.host",mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");//465,587
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
        cursor = _cursor;
    }

    protected PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String recipient)
    {
        final String rec = recipient;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

        try
        {
            String body = getBodyText();
            final MimeMessage message = new MimeMessage(session);
            DataSource dataSrc =  new ByteArrayDataSource(body.getBytes(), "text/plain");
            DataHandler handler = new DataHandler(dataSrc);
            message.setSender(new InternetAddress(user));
            message.setSubject(subject);
            message.setDataHandler(handler);

            if (rec.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(rec));

            Transport.send(message);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

            }
        });
    }

    private String getBodyText()
    {
        String bodyText = "";
        if(cursor.moveToFirst())
        {
            do {
                String title, time, details;
                title = cursor.getString(0);
                time = cursor.getString(2);
                details = cursor.getString(3);

                bodyText += time + ": " + title + " - " + details + ". \n";
            } while (cursor.moveToNext());
        }
        return bodyText;
    }
}
