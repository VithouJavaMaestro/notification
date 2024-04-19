package org.vtx.notification.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.vtx.notification.exception.ConverterException;
import org.vtx.notification.payload.Attachment;

@Slf4j
public class MultipartFileConverter
        implements Converter<List<MultipartFile>, List<Attachment>> {
    private Function<List<MultipartFile>, Boolean> function;

    public MultipartFileConverter(Function<List<MultipartFile>, Boolean> function) {
        if (Objects.isNull(function)) {
            defaultValidation();
        } else {
            this.function = function;
        }
    }

    public MultipartFileConverter() {
        defaultValidation();
    }

    private void defaultValidation() {
        this.function = (multipartFiles -> {
            boolean isMultipartFilesEmpty = CollectionUtils.isEmpty(multipartFiles);
            boolean isMultipartFileIsConvertable = multipartFiles.stream().allMatch(Predicate.not(MultipartFile::isEmpty));
            return !isMultipartFilesEmpty && isMultipartFileIsConvertable;
        });
    }

    @Override
    public List<Attachment> convert(List<MultipartFile> multipartFiles) {
        if (!canConvert(multipartFiles)) {
            return Collections.emptyList();
        }
        final var attachments = new ArrayList<Attachment>();
        multipartFiles.stream()
                .filter(file -> file != null && !file.isEmpty())
                .forEach(
                        multipartFile -> {
                            final var attachment = new Attachment();
                            try {
                                attachment.setFilename(multipartFile.getOriginalFilename());
                                attachment.setMimetype(multipartFile.getContentType());
                                attachment.setContent(multipartFile.getBytes());
                                attachments.add(attachment);
                            } catch (IOException e) {
                                log.error(e.getMessage(), e);
                                throw new ConverterException(e.getMessage());
                            }
                        });
        return attachments;
    }

    @Override
    public boolean canConvert(List<MultipartFile> multipartFiles) {
        return function.apply(multipartFiles);
    }
}

