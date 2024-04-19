package org.vtx.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.util.ByteArrayDataSource;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.vtx.notification.context.NotificationContext;
import org.vtx.notification.payload.SMTPNotification;

@RequiredArgsConstructor
public class SMTPNotificationProvider extends AbstractNotificationService<SMTPNotification> {
    private final JavaMailSender javaMailSender;

    @Override
    protected void doExecute(NotificationContext notificationContext, SMTPNotification smtpNotification) {
        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    javaMailSender.createMimeMessage(), true, StandardCharsets.UTF_8.name()
            );

            String[] recipients = smtpNotification.getRecipients().toArray(new String[]{});

            if (!CollectionUtils.isEmpty(smtpNotification.getCcs())) {
                var ccs = smtpNotification.getCcs().toArray(new String[]{});
                mimeMessageHelper.setCc(ccs);
            }

            if (!CollectionUtils.isEmpty(smtpNotification.getBcc())) {
                var bcc = smtpNotification.getCcs().toArray(new String[]{});
                mimeMessageHelper.setBcc(bcc);
            }

            if (Objects.isNull(smtpNotification.getSentDate())) {
                mimeMessageHelper.setSentDate(Date.from(Instant.now()));
            } else {
                mimeMessageHelper.setSentDate(smtpNotification.getSentDate());
            }

            if (StringUtils.hasText(smtpNotification.getReplyTo())) {
                mimeMessageHelper.setReplyTo(smtpNotification.getReplyTo());
            }

            mimeMessageHelper.setText(smtpNotification.getBody());
            mimeMessageHelper.setSubject(smtpNotification.getSubject());
            mimeMessageHelper.setFrom(smtpNotification.getFrom());
            mimeMessageHelper.setTo(recipients);

            if (!CollectionUtils.isEmpty(smtpNotification.getAttachments())) {
                for (var attachment : smtpNotification.getAttachments()) {
                    var byteArrayDataSource = new ByteArrayDataSource(attachment.getContent(), attachment.getMimetype());
                    mimeMessageHelper.addAttachment(attachment.getFilename(), byteArrayDataSource);
                }
            }

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            logger.error("An error occurred while sending mail with smtp notification", e);
        }
    }
}
