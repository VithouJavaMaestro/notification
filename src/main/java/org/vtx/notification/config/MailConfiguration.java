package org.vtx.notification.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.vtx.notification.payload.NotificationType;
import org.vtx.notification.service.SMTPNotificationProvider;

import java.util.Properties;

/**
 * Configuration class for setting up email functionality.
 * This class configures the JavaMailSender based on provided properties.
 * @author Chanthavithou THEN
 */
@Configuration
@Import({SMTPNotificationProvider.class})
@EnableConfigurationProperties({MailProperties.class})
public class MailConfiguration {

    /**
     * Configures the JavaMailSender bean if the mail provider is SMTP and no existing bean is found.
     *
     * @param properties The properties containing mail configuration details.
     * @return Configured JavaMailSender bean.
     */
    @Bean
    @ConditionalOnProperty(prefix = "vtx.mail", name = "provider", havingValue = NotificationType.SMTP)
    @ConditionalOnMissingBean
    public JavaMailSender configureJavaMailSender(MailProperties properties) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(properties.getHost());
        mailSender.setPort(properties.getPort());

        mailSender.setUsername(properties.getUsername());
        mailSender.setPassword(properties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
