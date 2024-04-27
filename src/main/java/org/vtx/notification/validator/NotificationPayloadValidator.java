package org.vtx.notification.validator;

import org.vtx.notification.payload.Notification;

/**
 * Interface for validating notification payloads.
 *
 * <p>This interface defines methods to validate whether a notification payload is valid
 * and to perform validation on a notification.</p>
 *
 * @param <T> The type of the notification.
 * @author Chanthavithou
 */
public interface NotificationPayloadValidator<T extends Notification> {
    /**
     * Checks if the given notification payload is valid.
     *
     * @param notification The notification to check.
     * @return {@code true} if the notification payload is valid, {@code false} otherwise.
     */
    boolean isValid(T notification);

    /**
     * Validates the given notification payload.
     *
     * @param notification The notification to validate.
     */
    void validate(T notification);
}
