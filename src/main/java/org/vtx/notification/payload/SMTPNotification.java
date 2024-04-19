package org.vtx.notification.payload;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SMTPNotification implements Notification {

    private String from;
    private Set<String> recipients;
    private Set<String> bcc;
    private String replyTo;
    private Date sentDate;
    private String subject;
    private String description;
    private String body;
    private Set<String> ccs;
    private String fileName;
    private List<Attachment> attachments;
    private Map<String, byte[]> inlines = new HashMap<>();
    private String mimeType;
    private boolean isBodyAsHtml;

    public void addAttachment(Attachment attachment) {
        Objects.requireNonNull(attachment);
        this.attachments.add(attachment);
    }
}
