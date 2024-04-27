package org.vtx.notification.service;

import org.vtx.notification.payload.Notification;

/**
 * Interface for providing notifications.
 *
 * <p>This interface defines a method to send a notification of type T.</p>
 *
 * @param <T> The type of the notification.
 * @author Chanthavithou
 */
public interface NotificationProvider<T extends Notification> {
    /**
     * Sends the given notification.
     *
     * @param notification The notification to send.
     */
    void send(T notification);
}
