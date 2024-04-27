package org.vtx.notification.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SMTPNotification implements Notification {

    private String from;
    private Set<String> recipients;
    private Set<String> bcc;
    private Set<String> replyTo;
    private Date sentDate;
    private String subject;
    private String description;
    private String body;
    private Set<String> ccs;
    private String fileName;
    private List<Attachment> attachments = new ArrayList<>();
    private List<Inline> inlines = new ArrayList<>();
    private String mimeType;
    private boolean isBodyAsHtml;

    public void addAttachment(Attachment attachment) {
        Objects.requireNonNull(attachment);
        this.attachments.add(attachment);
    }
}
