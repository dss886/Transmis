package com.dss886.transmis.mail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dss886 on 2017/6/28.
 */
public class MailSender extends javax.mail.Authenticator {

    private static final String MAIL_HOST = "smtp.qq.com";
    private static final String SENDER_NAME = "Transmis";
    private Executor mExecutor;
    private String email;
    private String password;
    private String receiveAddress;
    private Session session;

    public MailSender(String email, String password, String receiveAddress) {
        this.email = email;
        this.password = password;
        this.receiveAddress = receiveAddress;
        this.mExecutor = Executors.newSingleThreadExecutor();

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", MAIL_HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        session = Session.getDefaultInstance(props, this);
    }

    public void send(String title, String content) {
        mExecutor.execute(() -> {
            try {
                Message msg = new MimeMessage(session);
                msg.setSubject(title);
                msg.setText(content);
                msg.setFrom(new InternetAddress(email, SENDER_NAME));

                Transport transport = session.getTransport();
                transport.connect(MAIL_HOST, email, password);
                transport.sendMessage(msg, new Address[] { new InternetAddress(receiveAddress) });
                transport.close();
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }
}
