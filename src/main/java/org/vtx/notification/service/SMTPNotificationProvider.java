package org.vtx.notification.service;

import jakarta.activation.DataHandler;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.util.CollectionUtils;
import org.vtx.notification.context.NotificationContext;
import org.vtx.notification.converter.StringToAddressConverter;
import org.vtx.notification.payload.SMTPNotification;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@RequiredArgsConstructor
public class SMTPNotificationProvider extends AbstractNotificationService<SMTPNotification> {

    private final JavaMailSender javaMailSender;

    @Override
    protected void doExecute(NotificationContext notificationContext, SMTPNotification smtpNotification) {
        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(@NonNull MimeMessage mimeMessage) throws Exception {

                StringToAddressConverter stringToAddressConverter = new StringToAddressConverter();

                if (!CollectionUtils.isEmpty(smtpNotification.getCcs())) {
                    Address[] ccs = stringToAddressConverter.converts(smtpNotification.getCcs())
                            .toArray(new Address[]{});
                    mimeMessage.setRecipients(Message.RecipientType.CC, ccs);
                }

                if (!CollectionUtils.isEmpty(smtpNotification.getBcc())) {
                    Address[] bcc = stringToAddressConverter.converts(smtpNotification.getBcc())
                            .toArray(new Address[]{});
                    mimeMessage.setRecipients(Message.RecipientType.BCC, bcc);
                }

                if (Objects.isNull(smtpNotification.getSentDate())) {
                    mimeMessage.setSentDate(Date.from(Instant.now()));
                } else {
                    mimeMessage.setSentDate(smtpNotification.getSentDate());
                }

                if (!CollectionUtils.isEmpty(smtpNotification.getReplyTo())) {
                    Address[] replyTo = stringToAddressConverter.converts(smtpNotification.getReplyTo())
                            .toArray(new Address[]{});
                    mimeMessage.setReplyTo(replyTo);
                }

                Address[] recipients = stringToAddressConverter.converts(smtpNotification.getRecipients())
                        .toArray(Address[]::new);

                mimeMessage.setText(smtpNotification.getBody());
                mimeMessage.setSubject(smtpNotification.getSubject());
                mimeMessage.setFrom(smtpNotification.getFrom());
                mimeMessage.setRecipients(Message.RecipientType.TO, recipients);

                if (!CollectionUtils.isEmpty(smtpNotification.getAttachments())) {
                    for (var attachment : smtpNotification.getAttachments()) {

                        MimeBodyPart mimeBodyPart = new MimeBodyPart();

                        mimeBodyPart.setDisposition(Part.ATTACHMENT);
                        mimeBodyPart.setFileName(attachment.getFilename());
                        var byteArrayDataSource = new ByteArrayDataSource(attachment.getContent(), attachment.getMimetype());
                        mimeBodyPart.setDataHandler(new DataHandler(byteArrayDataSource));

                        MimeMultipart mimeMultipart = new MimeMultipart();
                        mimeMultipart.addBodyPart(mimeBodyPart);

                        mimeMessage.setContent(mimeMultipart);
                    }
                }
            }
        };

        javaMailSender.send(mimeMessagePreparator);
    }
}
