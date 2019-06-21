package com.mail.service;

import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by baiyc
 * 2019/5/30/030 15:49
 * Description：
 */
@Service
public class MailService {

    //邮件的发送者
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private MailProperties mailProperties;
    //注入MailSender
    @Autowired
    private JavaMailSender mailSender;

    //发送邮件的模板引擎
    @Autowired
    private FreeMarkerConfigurer configurer;

    /**
     * @param params       发送邮件的主题对象 object
     * @param title        邮件标题
     * @param templateName 模板名称
     * @param to 收件人地址
     * @param receipt 是否需要回执
     * @throws MessagingException
     * @throws Exception
     */
    public void sendMessageMail(Object params, String title, String templateName, String to, boolean receipt) throws Exception{
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        if(receipt) {
            mimeMessage.setHeader("Disposition-Notification-To", "1");
        }
        mimeMessage.setContentID(System.currentTimeMillis()+"");
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(InternetAddress.parse(to));
        helper.setSubject("【" + title + "-" + LocalDate.now() + " " + LocalTime.now().withNano(0) + "】");//邮件标题
        Map<String, Object> model = new HashMap<>();
        model.put("params", params);
        Template template = configurer.getConfiguration().getTemplate(templateName);
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        helper.setText(text, true);
        System.out.println(mimeMessage.getContentID());
        mailSender.send(mimeMessage);
    }

    /**
     * Description:
     * params:[]
     * return:void
     */
    public void receiveMail() throws Exception {
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";//ssl加密,jdk1.8无法使用
        // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", mailProperties.getProtocol());
        props.setProperty("mail.imap.host", mailProperties.getHost());
        props.setProperty("mail.imap.port", mailProperties.getPort());
        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.imap.socketFactory.port", mailProperties.getPort());

        // 创建Session实例对象
        Session session = Session.getInstance(props);

        // 创建IMAP协议的Store对象
        Store store = session.getStore(mailProperties.getProtocol());

        // 连接邮件服务器
        store.connect(mailProperties.getUsername(), mailProperties.getPassword());

        // 获得收件箱
        Folder folder = store.getFolder("INBOX");
        // 以读写模式打开收件箱
        folder.open(Folder.READ_WRITE);

        // 获得收件箱的邮件列表
        Message[] messages = folder.getMessages();

        // 打印不同状态的邮件数量
        System.out.println("收件箱中共" + messages.length + "封邮件!");
        System.out.println("收件箱中共" + folder.getUnreadMessageCount() + "封未读邮件!");
        System.out.println("收件箱中共" + folder.getNewMessageCount() + "封新邮件!");
        System.out.println("收件箱中共" + folder.getDeletedMessageCount() + "封已删除邮件!");

        System.out.println("------------------------开始解析邮件----------------------------------");


        int total = folder.getMessageCount();
        System.out.println("-----------------您的邮箱共有邮件：" + total + " 封--------------");
        // 得到收件箱文件夹信息，获取邮件列表
        Message[] msgs = folder.getMessages();
        System.out.println("\t收件箱的总邮件数：" + msgs.length);
        System.out.println(msgs[5].getContent());
        /*for (int i = 0; i < total; i++) {
            Message a = msgs[i];
            //   获取邮箱邮件名字及时间

            System.out.println(a.getReplyTo());

            System.out.println("==============");
                System.out.println(a.getSubject() + "   接收时间：" + a.getReceivedDate().toLocaleString()+"  contentType()" +a.getContentType());
        }*/
        System.out.println("\t未读邮件数：" + folder.getUnreadMessageCount());
        System.out.println("\t新邮件数：" + folder.getNewMessageCount());
        System.out.println("----------------End------------------");



        // 关闭资源
        folder.close(false);
        store.close();
    }
}
