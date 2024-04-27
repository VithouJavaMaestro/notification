package org.vtx.notification.validator;

import org.vtx.notification.payload.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Aggregator for validating notification payloads.
 *
 * <p>This class aggregates multiple {@link NotificationPayloadValidator} instances
 * and provides a method to validate a notification against all registered validators.</p>
 * @author Chanthavithou
 */
public class NotificationValidatorAggregator {
    /**
     * The list of notification payload validators.
     */
    private final List<NotificationPayloadValidator<Notification>> notificationPayloadValidators;

    /**
     * Constructs a NotificationValidatorAggregator with the specified list of notification payload validators.
     *
     * @param notificationPayloadValidators The list of notification payload validators.
     */
    public NotificationValidatorAggregator(List<NotificationPayloadValidator<Notification>> notificationPayloadValidators) {
        this.notificationPayloadValidators = notificationPayloadValidators;
    }

    /**
     * Constructs a NotificationValidatorAggregator with an empty list of notification payload validators.
     */
    public NotificationValidatorAggregator() {
        this.notificationPayloadValidators = new ArrayList<>();
    }

    /**
     * Adds a notification payload validator to the aggregator.
     *
     * @param notificationPayloadValidator The notification payload validator to add.
     */
    public void add(NotificationPayloadValidator<Notification> notificationPayloadValidator) {
        Objects.requireNonNull(notificationPayloadValidator);
        this.notificationPayloadValidators.add(notificationPayloadValidator);
    }

    /**
     * Validates a notification against all registered validators.
     *
     * @param notification The notification to validate.
     */
    public void validate(Notification notification) {
        for (NotificationPayloadValidator<Notification> notificationPayloadValidator : notificationPayloadValidators) {
            notificationPayloadValidator.validate(notification);
        }
    }
}
