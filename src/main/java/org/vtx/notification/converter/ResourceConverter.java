package org.vtx.notification.converter;

import io.micrometer.common.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.vtx.notification.exception.ConverterException;
import org.vtx.notification.payload.Attachment;

import java.io.IOException;
import java.util.function.Function;

@Slf4j
public class ResourceConverter extends AbstractConversionHandler<Resource, Attachment> {
    private Function<Resource, Boolean> function;

    public ResourceConverter(@Nullable Function<Resource, Boolean> function) {
        super(function);
    }

    public ResourceConverter() {
        super(resource -> resource.isReadable() && resource.exists());
    }


    @Override
    public Attachment convertInternal(Resource resource) {
        try {
            final var attachment = new Attachment();
            var mimeType = new Tika().detect(resource.getInputStream());
            attachment.setContent(resource.getContentAsByteArray());
            attachment.setFilename(resource.getFilename());
            attachment.setMimetype(mimeType);
            return attachment;
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
            throw new ConverterException(exception.getMessage(), exception);
        }
    }
}

