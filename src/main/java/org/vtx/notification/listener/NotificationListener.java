package org.vtx.notification.listener;

import org.vtx.notification.context.NotificationContext;
import org.vtx.notification.payload.Notification;

/**
 * Interface for listening to notification execution events.
 *
 * <p>This interface defines methods to be implemented by classes that wish to receive
 * notifications before and after their execution.</p>
 *
 * @param <T> The type of the notification.
 * @author Chanthavithou
 */
public interface NotificationListener<T extends Notification> {
    /**
     * Called after executing a notification.
     *
     * @param notificationContext The context of the notification execution.
     * @param notification        The executed notification.
     */
    void afterExecuting(NotificationContext notificationContext, T notification);

    /**
     * Called before executing a notification.
     *
     * @param notificationContext The context of the notification execution.
     * @param notification        The notification about to be executed.
     */
    void beforeExecuting(NotificationContext notificationContext, T notification);
}
