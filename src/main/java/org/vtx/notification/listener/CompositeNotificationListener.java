package org.vtx.notification.listener;

import java.util.Collections;
import java.util.LinkedList;
import org.vtx.notification.context.NotificationContext;
import org.vtx.notification.payload.Notification;

public class CompositeNotificationListener implements NotificationListener {
    private final LinkedList<NotificationListener> listeners = new LinkedList<>();

    public void register(NotificationListener notificationListener) {
        this.listeners.add(notificationListener);
    }


    @Override
    public <T extends Notification> void afterExecuting(NotificationContext notificationContext, T notification) {
        Collections.reverse(listeners);
        for (NotificationListener notificationListener : listeners) {
            notificationListener.afterExecuting(notificationContext, notification);
        }
    }

    @Override
    public <T extends Notification> void beforeExecuting(NotificationContext notificationContext, T notification) {
        for (NotificationListener notificationListener : listeners) {
            notificationListener.beforeExecuting(notificationContext, notification);
        }
    }
}
