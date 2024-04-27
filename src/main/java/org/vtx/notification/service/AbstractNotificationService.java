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

/**
 * An abstract implementation of the {@link NotificationProvider} interface for handling notifications.
 *
 * <p>This class provides a framework for executing notifications and allows customization
 * through subclassing and method overrides.</p>
 *
 * @param <T> The type of the notification.
 * @author Chanthavithou
 */
public abstract class AbstractNotificationService<T extends Notification> implements NotificationProvider<T> {
    /** The logger for logging notification-related messages and errors. */
    protected static final Log logger = LogFactory.getLog(AbstractNotificationService.class);

    /** The aggregator for validation of notifications. */
    private NotificationValidatorAggregator notificationValidatorAggregator;

    /** The composite notification listener to handle notification execution events. */
    private final CompositeNotificationListener<T> compositeNotificationListener = new CompositeNotificationListener<>();

    /**
     * Sets the notification validator aggregator.
     *
     * @param notificationValidatorAggregator The notification validator aggregator to set.
     */
    public void setNotificationValidatorAggregator(NotificationValidatorAggregator notificationValidatorAggregator) {
        if (notificationValidatorAggregator != null) {
            this.notificationValidatorAggregator = notificationValidatorAggregator;
        }
    }

    /**
     * Sets the notification listeners.
     *
     * @param notificationListeners The notification listeners to set.
     */
    public void setNotificationListener(NotificationListener<T>... notificationListeners) {
        for (NotificationListener<T> notificationListener : notificationListeners) {
            this.compositeNotificationListener.register(notificationListener);
        }
    }

    /**
     * Sends a notification.
     *
     * @param notification The notification to send.
     */
    @Override
    public void send(T notification) {
        if (logger.isDebugEnabled()) {
            logger.debug("Notification execution started");
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
                logger.debug("Notification execution completed");
            }

        } catch (RuntimeException throwable) {
            logger.error("An error occurred while executing notification", throwable);
            notificationContext.setNotificationStatus(NotificationStatus.FAILED);
            notificationContext.setFailedException(throwable);
            compositeNotificationListener.afterExecuting(notificationContext, notification);
        }
    }

    /**
     * Executes the notification.
     *
     * @param notificationContext The context of the notification execution.
     * @param notification        The notification to execute.
     */
    protected abstract void doExecute(NotificationContext notificationContext, T notification);
}
