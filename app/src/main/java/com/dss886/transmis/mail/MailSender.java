package com.dss886.transmis.mail;

import android.widget.Toast;
import com.dss886.transmis.base.App;
import com.dss886.transmis.utils.Tags;
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

    private static final String SENDER_NAME = "Transmis";

    private Executor mExecutor;
    private String host;
    private String email;
    private String password;
    private String receiveAddress;
    private Session session;

    public MailSender() {
        this.mExecutor = Executors.newSingleThreadExecutor();
        this.host = App.me().sp.getString(Tags.SP_MAIL_HOST, null);
        String port = App.me().sp.getString(Tags.SP_MAIL_PORT, null);
        this.email = App.me().sp.getString(Tags.SP_MAIL_SEND_MAIL, null);
        this.password = App.me().sp.getString(Tags.SP_MAIL_SEND_PASSWORD, null);
        this.receiveAddress = App.me().sp.getString(Tags.SP_MAIL_RECEIVE_MAIL, null);

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", port);
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            Toast.makeText(App.me(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                transport.connect(host, email, password);
                transport.sendMessage(msg, new Address[] { new InternetAddress(receiveAddress) });
                transport.close();
            } catch (MessagingException | UnsupportedEncodingException e) {
                Toast.makeText(App.me(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}
