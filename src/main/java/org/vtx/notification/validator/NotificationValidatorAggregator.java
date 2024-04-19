package org.vtx.notification.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.vtx.notification.payload.Notification;

public class NotificationValidatorAggregator {
    private final List<NotificationPayloadValidator<Notification>> notificationPayloadValidators;

    public NotificationValidatorAggregator(List<NotificationPayloadValidator<Notification>> notificationPayloadValidators) {
        this.notificationPayloadValidators = notificationPayloadValidators;
    }

    public NotificationValidatorAggregator() {
        this.notificationPayloadValidators = new ArrayList<>();
    }

    public void add(NotificationPayloadValidator<Notification> notificationPayloadValidator) {
        Objects.requireNonNull(notificationPayloadValidator);
        this.notificationPayloadValidators.add(notificationPayloadValidator);
    }

    public void validate(Notification notification) {
        for (NotificationPayloadValidator<Notification> notificationPayloadValidator : notificationPayloadValidators) {
            notificationPayloadValidator.validate(notification);
        }
    }
}
