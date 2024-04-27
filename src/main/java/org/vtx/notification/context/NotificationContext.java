package org.vtx.notification.context;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;
import org.vtx.notification.payload.Notification;
import org.vtx.notification.status.NotificationStatus;

/**
 * Represents the context for a notification, encapsulating various details such as status, timing, context information,
 * and any encountered exceptions during processing.
 *
 * @author Chanthavithou
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationContext {
    /** The status of the notification. */
    private NotificationStatus notificationStatus;

    /** The start time of the notification. */
    private Date startTime;

    /** The end time of the notification. */
    private Date endTime;

    /** Additional context information for the notification. */
    private Map<String, Object> context;

    /** The exception encountered during notification processing. */
    private Throwable failedException;

    /**
     * Adds a key-value pair to the context map.
     *
     * @param key   The key to add to the context.
     * @param value The value associated with the key.
     * @throws IllegalArgumentException if the key is null or empty.
     */
    public void add(String key, Object value) {
        Objects.requireNonNull(value);
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        this.context.put(key, value);
    }
}
