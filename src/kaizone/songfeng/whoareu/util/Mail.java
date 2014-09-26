
package kaizone.songfeng.whoareu.util;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {

    public static void send(String addr, String pass, String host, String toAddr, String subject, String text)
            throws AddressException,
            MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");// 发送邮件协议
        properties.setProperty("mail.smtp.auth", "true");// 需要验证
        properties.setProperty("mail.debug", "true");// 设置debug模式 后台输出邮件发送的过程
        Session session = Session.getDefaultInstance(properties);
        session.setDebug(true);// debug模式

        Message messgae = new MimeMessage(session);
        messgae.setFrom(new InternetAddress(addr));// 设置发送人
        messgae.setSubject(subject);// 设置邮件主题
        messgae.setText(text);// 设置邮件内容
        // 发送邮件
        Transport tran = session.getTransport();
        // tran.connect("smtp.sohu.com", 25, "wuhuiyao@sohu.com",
        // "xxxx");//连接到新浪邮箱服务器
        tran.connect(host, 587, addr, pass);// 连接到新浪邮箱服务器
        // tran.connect("smtp.qq.com", 25, "Michael8@qq.vip.com",
        // "xxxx");//连接到QQ邮箱服务器
        tran.sendMessage(messgae, new Address[] {
                new InternetAddress(toAddr)
        });// 设置邮件接收人
        tran.close();
    }

    public static Store imap(String user, String pass, String host) throws MessagingException {

        String url = "imap://" + user + ":" + pass + "@" + host;
        Properties properties = System.getProperties();
        properties.setProperty("mail.imap.ssl.enable", "true");
        properties.setProperty("mail.imap.ssl.trust", "*");
        Session session = Session.getDefaultInstance(properties);
        URLName urln = new URLName(url);
        Store store = session.getStore(urln);
        store.connect();
        return store;

    }

    public static void resolve(Store store) throws MessagingException {
        Folder folder = store.getDefaultFolder();
        Folder[] sub = folder.list();
        for (Folder f : sub) {
            f.open(Folder.READ_ONLY);
            System.out.println(f.getName());
            System.out.println("count:" + f.getMessageCount());

            Message[] msgs = f.getMessages();
            int i = 0;
            for (Message msg : msgs) {
                System.out.println(i++);
                System.out.println("subject = " + msg.getSubject());

                dumpPart("*", msg);
                System.out.println("----------------------------------------------");
            }
        }
    }

    protected static void dumpPart(String prefix, Part p) {
        try {
            System.out.println(prefix + "----------------");
            System.out.println(prefix +
                    "Content-Type: " + p.getContentType());
            System.out.println(prefix +
                    "Class: " + p.getClass().toString());

            Object o = p.getContent();
            if (o == null) {
                System.out.println(prefix + "Content:  is null");
            } else {
                System.out.println(prefix +
                        "Content: " + o.getClass().toString());
            }

            if (o instanceof Multipart) {
                String newpref = prefix + "\t";
                Multipart mp = (Multipart) o;

                int count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    dumpPart(newpref, mp.getBodyPart(i));
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException ioex) {
            System.out.println("Cannot get content" + ioex.getMessage());
        }
    }

}
