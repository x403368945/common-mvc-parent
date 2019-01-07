package com.demo.config;
//
//import com.demo.config.enums.Mail;
//import com.utils.util.Charsets;
//import com.utils.util.Num;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
///**
// */
////, repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class
//@Configuration
//public class MailConfig {
//    @Value("${mail.host}")
//    private String host;
//    @Value("${mail.port}")
//    private String port;
//    @Value("${mail.username}")
//    private String username;
//    @Value("${mail.password}")
//    private String password;
//
//    @Bean
//    public JavaMailSender mailSender() throws Exception {
//        final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        {
//            final MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
//            mailSSLSocketFactory.setTrustAllHosts(true);
//            final Properties props = new Properties();
//            props.put("mail.smtp.ssl.enable", "true");
//            props.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.timeout", "20000");
//            javaMailSender.setJavaMailProperties(props);
//        }
//        javaMailSender.setDefaultEncoding(Charsets.UTF_8.displayName());
//        javaMailSender.setHost(Mail.HOST.value);
//        javaMailSender.setPort(Num.of(Mail.PORT.value).intValue());
//        javaMailSender.setUsername(Mail.USERNAME.value);
//        javaMailSender.setPassword(Mail.PASSWORD.value);
//        return javaMailSender;
//    }
//}