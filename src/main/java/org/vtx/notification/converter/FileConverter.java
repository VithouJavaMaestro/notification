package org.vtx.notification.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.vtx.notification.exception.ConverterException;
import org.vtx.notification.payload.Attachment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Function;

@Slf4j
public class FileConverter extends AbstractConversionHandler<File, Attachment> {

    public FileConverter(Function<File, Boolean> function) {
        super(function);
    }

    public FileConverter() {
        super(file -> file.isFile() && file.canRead() && file.exists());
    }

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
