package org.vtx.notification.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.util.CollectionUtils;
import org.vtx.notification.exception.ConverterException;
import org.vtx.notification.payload.Attachment;

@Slf4j
public class FileConverter implements Converter<List<File>, List<Attachment>> {
    private Function<List<File>, Boolean> function;

    public FileConverter(Function<List<File>, Boolean> function) {
        if (Objects.isNull(function)) {
            defaultValidation();
        } else {
            this.function = function;
        }
    }

    public FileConverter() {
        defaultValidation();
    }

    private void defaultValidation() {
        this.function = (files -> {
            boolean isFilesEmpty = CollectionUtils.isEmpty(files);
            boolean isFileReadable = files.stream().allMatch(file -> file.isFile() && file.canRead() && file.exists());
            return !isFilesEmpty && isFileReadable;
        });
    }

    @Override
    public List<Attachment> convert(List<File> files) throws ConverterException {
        if (canConvert(files)) {
            return Collections.emptyList();
        }
        final var attachments = new ArrayList<Attachment>();
        for (File eachFile : files) {
            try {
                try (var fis = new FileInputStream(eachFile)) {
                    var bytes = fis.readAllBytes();
                    var mimeType = new Tika().detect(eachFile);
                    final var attachment = new Attachment();
                    attachment.setFilename(eachFile.getName());
                    attachment.setContent(bytes);
                    attachment.setMimetype(mimeType);
                    attachments.add(attachment);
                }
            } catch (IOException exception) {
                log.error(exception.getMessage(), exception);
                throw new ConverterException(exception.getMessage(), exception);
            }
        }
        return attachments;
    }

    @Override
    public boolean canConvert(List<File> files) {
        return function.apply(files);
    }
}
