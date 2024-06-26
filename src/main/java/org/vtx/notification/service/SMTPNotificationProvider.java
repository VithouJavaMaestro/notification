package org.vtx.notification.service;

import jakarta.activation.DataHandler;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.util.CollectionUtils;
import org.vtx.notification.context.NotificationContext;
import org.vtx.notification.converter.StringToAddressConverter;
import org.vtx.notification.payload.SMTPNotification;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

/**
 * A concrete implementation of {@link AbstractNotificationService} for sending SMTP notifications.
 *
 * <p>This class handles the execution of SMTP notifications by preparing and sending MimeMessage objects
 * via a JavaMailSender. It overrides the {@code doExecute} method to define the SMTP notification sending logic.</p>
 *
 * @author Chanthavithou
 */
@RequiredArgsConstructor
public class SMTPNotificationProvider extends AbstractNotificationService<SMTPNotification> {

    /**
     * The JavaMailSender used for sending MimeMessage objects.
     */
    private final JavaMailSender javaMailSender;

    /**
     * Executes the SMTP notification sending logic.
     *
     * @param notificationContext The context of the notification execution.
     * @param smtpNotification    The SMTP notification to execute.
     */
    @Override
    protected void doSend(NotificationContext notificationContext, SMTPNotification smtpNotification) {
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

                        final MimeBodyPart mimeBodyPart = getMimeBodyPart(
                                attachment.getFilename(), attachment.getMimetype(), Part.ATTACHMENT, attachment.getContent()
                        );

                        MimeMultipart mimeMultipart = new MimeMultipart();
                        mimeMultipart.addBodyPart(mimeBodyPart);

                        mimeMessage.setContent(mimeMultipart, attachment.getMimetype());
                    }

                }

                if (!CollectionUtils.isEmpty(smtpNotification.getInlines())) {

                    for (var inline : smtpNotification.getInlines()) {

                        final MimeBodyPart mimeBodyPart = getMimeBodyPart(
                                inline.getFilename(), inline.getMimetype(), Part.INLINE, inline.getContent()
                        );

                        mimeBodyPart.setContentID(inline.getContentId());

                        MimeMultipart mimeMultipart = new MimeMultipart();
                        mimeMultipart.addBodyPart(mimeBodyPart);

                        mimeMessage.setContent(mimeMultipart, inline.getMimetype());
                    }

                }
            }
        };

        javaMailSender.send(mimeMessagePreparator);
    }

    private MimeBodyPart getMimeBodyPart(String filename, String mimeType, String part, byte[] content) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();

        mimeBodyPart.setDisposition(part);
        mimeBodyPart.setFileName(filename);
        var byteArrayDataSource = new ByteArrayDataSource(content, mimeType);
        mimeBodyPart.setDataHandler(new DataHandler(byteArrayDataSource));

        return mimeBodyPart;
    }
}
