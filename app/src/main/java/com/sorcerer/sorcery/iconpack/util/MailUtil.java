package com.sorcerer.sorcery.iconpack.util;

import com.sorcerer.sorcery.iconpack.models.MailSenderInfo;

/**
 * Created by Sorcerer on 2016/4/24.
 */
public class MailUtil {
    public static final String MAIL_SERVER_HOST_163 = "smtp.163.com";
    public static final String MAIL_SERVER_PORT_163 = "25";

    public static String MAIL_ADDRESS_FEEDBACK_163;
    public static String MAIL_PASSWORD_FEEDBACK_163;

    public static String MAIL_ADDRESS_SORCERER_SORCERERXW;
    public static String MAIL_ADDRESS_FEEDBACK_SORCERERXW;

    public interface SendMailCallback {
        void onSuccess();

        void onFail();
    }

    public static MailSenderInfo generateMailSenderInfo(String content,
                                                 String mailServerHost,
                                                 String mailServerPort,
                                                 Boolean validate,
                                                 String userName,
                                                 String password,
                                                 String fromAddress,
                                                 String toAddress,
                                                 String subject) {
        MailSenderInfo mailSenderInfo = new MailSenderInfo();
        mailSenderInfo.setContent(content);
        mailSenderInfo.setMailServerHost(mailServerHost);
        mailSenderInfo.setMailServerPort(mailServerPort);
        mailSenderInfo.setValidate(validate);
        mailSenderInfo.setUserName(userName);
        mailSenderInfo.setPassword(password);
        mailSenderInfo.setFromAddress(fromAddress);
        mailSenderInfo.setToAddress(toAddress);
        mailSenderInfo.setSubject(subject);
        return mailSenderInfo;
    }

    public static void send(MailSenderInfo mailSenderInfo,
                     SendMailCallback callback) {
        try {
            SimpleMailSender sms = new SimpleMailSender();
            sms.sendTextMail(mailSenderInfo);
            if (callback != null) {
                callback.onSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail();
        }
    }
}
