package org.vtx.notification.service;

import org.vtx.notification.payload.Notification;

public interface NotificationProvider<T extends Notification> {
    void send(T notification);
}
