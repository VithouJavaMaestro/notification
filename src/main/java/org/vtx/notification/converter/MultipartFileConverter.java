package org.vtx.notification.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.vtx.notification.exception.ConverterException;
import org.vtx.notification.payload.Attachment;

import java.io.IOException;
import java.util.function.Function;

/**
 * A converter class for converting MultipartFile objects to Attachment objects.
 *
 * <p>This class extends {@link AbstractConversionHandler} and implements the conversion logic
 * in the {@code convertInternal} method.</p>
 *
 * <p>The conversion criteria can be provided through a custom function or a default check
 * that ensures the MultipartFile is not empty.</p>
 *
 * <p>The conversion process involves extracting metadata such as filename and MIME type
 * from the MultipartFile, and reading its content to create an Attachment object.</p>
 * @author Chanthavithou
 */
@Slf4j
public class MultipartFileConverter
        extends AbstractConversionHandler<MultipartFile, Attachment> {

    /**
     * Constructs a MultipartFileConverter with the specified conversion function.
     *
     * @param function The function to determine if the conversion is possible.
     */
    public MultipartFileConverter(Function<MultipartFile, Boolean> function) {
        super(function);
    }

    /**
     * Constructs a MultipartFileConverter with a default conversion function.
     * The default function checks if the MultipartFile is not empty.
     */
    public MultipartFileConverter() {
        super(file -> !file.isEmpty());
    }


    /**
     * Converts a MultipartFile object to an Attachment object.
     *
     * @param multipartFile The MultipartFile object to convert.
     * @return The converted Attachment object.
     * @throws ConverterException if an error occurs during the conversion process.
     */
    @Override
    public Attachment convertInternal(MultipartFile multipartFile) {
        final var attachment = new Attachment();
        try {
            attachment.setFilename(multipartFile.getOriginalFilename());
            attachment.setMimetype(multipartFile.getContentType());
            attachment.setContent(multipartFile.getBytes());
            return attachment;
        } catch (IOException e) {
            log.error("An error occurred while converting multipart file to attachment", e);
            throw new ConverterException(e.getMessage());
        }
    }
}

