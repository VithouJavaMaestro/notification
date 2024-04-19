package org.vtx.notification.listener;

import org.vtx.notification.context.NotificationContext;
import org.vtx.notification.payload.Notification;

public interface NotificationListener {
   <T extends Notification> void afterExecuting(NotificationContext notificationContext, T notification);

    <T extends Notification> void beforeExecuting(NotificationContext notificationContext, T notification);
}
