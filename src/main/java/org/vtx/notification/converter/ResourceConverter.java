package org.vtx.notification.converter;

import io.micrometer.common.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.vtx.notification.exception.ConverterException;
import org.vtx.notification.payload.Attachment;

import java.io.IOException;
import java.util.function.Function;

/**
 * A converter class for converting Spring Resource objects to Attachment objects.
 *
 * <p>This class extends {@link AbstractConversionHandler} and implements the conversion logic
 * in the {@code convertInternal} method.</p>
 *
 * <p>The conversion criteria can be provided through a custom function or a default check
 * that ensures the Resource is readable and exists.</p>
 *
 * <p>The conversion process involves reading the content of the Resource, detecting its MIME type,
 * and creating an Attachment object with the Resource's metadata and content.</p>
 * @author Chanthavithou
 */
@Slf4j
public class ResourceConverter extends AbstractConversionHandler<Resource, Attachment> {
    /** The function to determine if the conversion is possible. */
    private Function<Resource, Boolean> function;

    /**
     * Constructs a ResourceConverter with the specified conversion function.
     *
     * @param function The function to determine if the conversion is possible.
     */
    public ResourceConverter(@Nullable Function<Resource, Boolean> function) {
        super(function);
    }

    /**
     * Constructs a ResourceConverter with a default conversion function.
     * The default function checks if the Resource is readable and exists.
     */
    public ResourceConverter() {
        super(resource -> resource.isReadable() && resource.exists());
    }

    /**
     * Converts a Resource object to an Attachment object.
     *
     * @param resource The Resource object to convert.
     * @return The converted Attachment object.
     * @throws ConverterException if an error occurs during the conversion process.
     */
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

