package org.vtx.notification.listener;

import org.vtx.notification.context.NotificationContext;
import org.vtx.notification.payload.Notification;

import java.util.Collections;
import java.util.LinkedList;

/**
 * A composite implementation of the {@link NotificationListener} interface.
 *
 * <p>This class allows multiple notification listeners to be registered and
 * delegates notification execution events to all registered listeners.</p>
 * @param <T> The type of the notification.
 * @author Chanthavithou
 */
public class CompositeNotificationListener<T extends Notification> implements NotificationListener<T> {
    /**
     * The list of registered notification listeners.
     */
    private final LinkedList<NotificationListener<T>> listeners = new LinkedList<>();

    /**
     * Registers a notification listener to receive notification events.
     *
     * @param notificationListener The notification listener to register.
     */
    public void register(NotificationListener<T> notificationListener) {
        this.listeners.add(notificationListener);
    }

    /**
     * Notifies all registered listeners after executing a notification.
     * This method invokes the {@code afterExecuting} method of each registered listener
     * in reverse order of registration.
     *
     * @param notificationContext The context of the notification execution.
     * @param notification        The executed notification.
     */
    @Override
    public void afterExecuting(NotificationContext notificationContext, T notification) {
        Collections.reverse(listeners);
        for (NotificationListener<T> notificationListener : listeners) {
            notificationListener.afterExecuting(notificationContext, notification);
        }
    }

    /**
     * Notifies all registered listeners before executing a notification.
     * This method invokes the {@code beforeExecuting} method of each registered listener
     * in the order of registration.
     *
     * @param notificationContext The context of the notification execution.
     * @param notification        The notification about to be executed.
     */
    @Override
    public void beforeExecuting(NotificationContext notificationContext, T notification) {
        for (NotificationListener<T> notificationListener : listeners) {
            notificationListener.beforeExecuting(notificationContext, notification);
        }
    }
}