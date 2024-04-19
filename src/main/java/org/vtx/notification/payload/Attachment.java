package org.vtx.notification.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {
    private String filename;
    private String mimetype;
    private byte[] content;
}
