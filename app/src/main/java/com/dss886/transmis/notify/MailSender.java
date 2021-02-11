package com.dss886.transmis.notify;

import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.utils.Tags;
import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by dss886 on 2017/6/28.
 */
public class MailSender extends javax.mail.Authenticator {

    private final Executor mExecutor;
    private final String host;
    private final String email;
    private final String password;
    private final String name;
    private final String receiveAddress;
    private final Session session;

    public MailSender() {
        this.mExecutor = Executors.newSingleThreadExecutor();
        this.host = App.sp.getString(Tags.SP_MAIL_HOST, null);
        String port = App.sp.getString(Tags.SP_MAIL_PORT, null);
        this.email = App.sp.getString(Tags.SP_MAIL_SEND_MAIL, null);
        this.password = App.sp.getString(Tags.SP_MAIL_SEND_PASSWORD, null);
        this.name = App.sp.getString(Tags.SP_MAIL_SEND_NAME, App.me().getString(R.string.app_name));
        this.receiveAddress = App.sp.getString(Tags.SP_MAIL_RECEIVE_MAIL, null);

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
                msg.setFrom(new InternetAddress(email, name));

                Transport transport = session.getTransport();
                transport.connect(host, email, password);
                transport.sendMessage(msg, new Address[] { new InternetAddress(receiveAddress) });
                transport.close();
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        });
    }
}
