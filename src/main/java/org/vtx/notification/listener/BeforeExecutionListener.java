package org.vtx.notification.listener;

import lombok.extern.slf4j.Slf4j;
import org.vtx.notification.context.NotificationContext;
import org.vtx.notification.payload.Notification;

@Slf4j
public class BeforeExecutionListener implements NotificationListener {
    @Override
    public <T extends Notification> void afterExecuting(NotificationContext notificationContext, T notification) {
        log.error("Executing me after");
    }

    @Override
    public <T extends Notification> void beforeExecuting(NotificationContext notificationContext, T notification) {
        log.error("Executing me before");
    }
}
