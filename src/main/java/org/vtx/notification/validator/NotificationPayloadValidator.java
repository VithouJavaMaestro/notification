package org.vtx.notification.validator;

import org.vtx.notification.payload.Notification;

public interface NotificationPayloadValidator<T extends Notification> {
    boolean isValid(T notification);

    void validate(T notification);
}
