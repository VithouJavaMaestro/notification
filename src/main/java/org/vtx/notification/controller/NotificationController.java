package org.vtx.notification.controller;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vtx.notification.converter.ResourceConverter;
import org.vtx.notification.listener.BeforeExecutionListener;
import org.vtx.notification.payload.SMTPNotification;
import org.vtx.notification.service.SMTPNotificationProvider;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final SMTPNotificationProvider smtpNotificationProvider;

    @GetMapping
    public void send() {
        final var smtpNotification = new SMTPNotification();
        smtpNotification.setFrom("thenchanthavithou@gmail.com");
        smtpNotification.setRecipients(Set.of("kimnguon.lim@allweb.com.kh"));
        smtpNotification.setBody("Hello");
        smtpNotification.setSubject("00000000000000000000000");

        final var path = "C:\\Users\\Chanthavithou THEN\\Documents\\notification\\src\\main\\resources\\attachment\\kimnguon.png";

        final var file = new FileSystemResource(path);

        final var resourceConverter = new ResourceConverter();

        smtpNotification.setAttachments(resourceConverter.convert(List.of(file)));


        smtpNotificationProvider.setNotificationListener(new BeforeExecutionListener());
        smtpNotificationProvider.send(smtpNotification);
    }
}
