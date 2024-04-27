package org.vtx.notification.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vtx.notification.context.NotificationContext;
import org.vtx.notification.listener.CompositeNotificationListener;
import org.vtx.notification.listener.NotificationListener;
import org.vtx.notification.payload.Notification;
import org.vtx.notification.status.NotificationStatus;
import org.vtx.notification.validator.NotificationValidatorAggregator;

import java.time.Instant;
import java.util.Date;

public abstract class AbstractNotificationService<T extends Notification> implements NotificationProvider<T> {
    protected static final Log logger = LogFactory.getLog(AbstractNotificationService.class);
    private NotificationValidatorAggregator notificationValidatorAggregator;
    private final CompositeNotificationListener compositeNotificationListener = new CompositeNotificationListener();

    public void setNotificationValidatorAggregator(NotificationValidatorAggregator notificationValidatorAggregator) {
        if (notificationValidatorAggregator != null) {
            this.notificationValidatorAggregator = notificationValidatorAggregator;
        }
    }

    public void setNotificationListener(NotificationListener... notificationListeners) {
        for (NotificationListener notificationListener : notificationListeners) {
            this.compositeNotificationListener.register(notificationListener);
        }
    }

    @Override
    public void send(T notification) {

        if (logger.isDebugEnabled()) {
            logger.debug("Mail notification start executing");
        }

        final NotificationContext notificationContext = new NotificationContext();
        notificationContext.setNotificationStatus(NotificationStatus.STARTING);
        notificationContext.setStartTime(Date.from(Instant.now()));

        if (notificationValidatorAggregator != null) {
            notificationValidatorAggregator.validate(notification);
        }

        try {

            compositeNotificationListener.beforeExecuting(notificationContext, notification);

            doExecute(notificationContext, notification);

            notificationContext.setNotificationStatus(NotificationStatus.COMPLETED);
            compositeNotificationListener.afterExecuting(notificationContext, notification);

            notificationContext.setEndTime(Date.from(Instant.now()));

            if (logger.isDebugEnabled()) {
                logger.debug("Mail notification processing end");
            }

        } catch (RuntimeException throwable) {
            logger.error("An error occurred while executing notification", throwable);
            notificationContext.setNotificationStatus(NotificationStatus.FAILED);
            notificationContext.setFailedException(throwable);
            compositeNotificationListener.afterExecuting(notificationContext, notification);
        }
    }

    protected abstract void doExecute(NotificationContext notificationContext, T notification);
}
