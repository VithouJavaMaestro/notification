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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationContext {
    private NotificationStatus notificationStatus;
    private Date startTime;
    private Date endTime;
    private Map<String, Object> context;
    private Throwable failedException;

    public void add(String key, Object value) {
        Objects.requireNonNull(value);
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        this.context.put(key, value);
    }
}
