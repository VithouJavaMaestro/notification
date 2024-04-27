package org.vtx.notification.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.vtx.notification.exception.ConverterException;
import org.vtx.notification.payload.Attachment;

import java.io.IOException;
import java.util.function.Function;

@Slf4j
public class MultipartFileConverter
        extends AbstractConversionHandler<MultipartFile, Attachment> {

    public MultipartFileConverter(Function<MultipartFile, Boolean> function) {
        super(function);
    }

    public MultipartFileConverter() {
        super(file -> !file.isEmpty());
    }


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

