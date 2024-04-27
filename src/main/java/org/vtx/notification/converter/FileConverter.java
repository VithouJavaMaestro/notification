package org.vtx.notification.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.vtx.notification.exception.ConverterException;
import org.vtx.notification.payload.Attachment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Function;

/**
 * A converter class for converting File objects to Attachment objects.
 *
 * <p>This class extends {@link AbstractConversionHandler} and implements the conversion logic
 * in the {@code convertInternal} method.</p>
 *
 * <p>The conversion criteria can be provided through a custom function or a default check
 * that ensures the file is a regular file, readable, and exists.</p>
 *
 * <p>The conversion process involves reading the contents of the file, detecting its MIME type,
 * and creating an Attachment object with the file's metadata and content.</p>
 * @author Chanthavithou
 */
@Slf4j
public class FileConverter extends AbstractConversionHandler<File, Attachment> {

    /**
     * Constructs a FileConverter with the specified conversion function.
     *
     * @param function The function to determine if the conversion is possible.
     */
    public FileConverter(Function<File, Boolean> function) {
        super(function);
    }

    /**
     * Constructs a FileConverter with a default conversion function.
     * The default function checks if the file is a regular file, readable, and exists.
     */
    public FileConverter() {
        super(file -> file.isFile() && file.canRead() && file.exists());
    }

    /**
     * Converts a File object to an Attachment object.
     *
     * @param file The File object to convert.
     * @return The converted Attachment object.
     * @throws ConverterException if an error occurs during the conversion process.
     */
    @Override
    public Attachment convertInternal(File file) throws ConverterException {

        try {

            try (var fis = new FileInputStream(file)) {

                var bytes = fis.readAllBytes();
                var mimeType = new Tika().detect(file);

                final var attachment = new Attachment();
                attachment.setFilename(file.getName());
                attachment.setContent(bytes);
                attachment.setMimetype(mimeType);

                return attachment;
            }

        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
            throw new ConverterException(exception.getMessage(), exception);
        }
    }
}
