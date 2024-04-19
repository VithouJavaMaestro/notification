package org.vtx.notification.converter;

import io.micrometer.common.lang.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.vtx.notification.exception.ConverterException;
import org.vtx.notification.payload.Attachment;

@Slf4j
public class ResourceConverter implements Converter<List<Resource>, List<Attachment>> {
    private Function<List<Resource>, Boolean> function;

    public ResourceConverter(@Nullable Function<List<Resource>, Boolean> function) {
        if (Objects.isNull(function)) {
            defaultValidation();
        } else {
            this.function = function;
        }
    }

    public ResourceConverter() {
        defaultValidation();
    }

    private void defaultValidation() {
        this.function = (resources -> {
            boolean isResourceEmpty = CollectionUtils.isEmpty(resources);
            boolean isResourceConvertable = resources.stream().allMatch(resource ->
                    resource.isReadable() && resource.exists());
            return !isResourceEmpty && isResourceConvertable;
        });
    }

    @Override
    public List<Attachment> convert(List<Resource> resources) {
        if (!canConvert(resources)) {
            return Collections.emptyList();
        }
        final var attachments = new ArrayList<Attachment>();
        for (Resource resource : resources) {
            final var attachment = new Attachment();
            try {
                var mimeType = new Tika().detect(resource.getInputStream());
                attachment.setContent(resource.getContentAsByteArray());
                attachment.setFilename(resource.getFilename());
                attachment.setMimetype(mimeType);
                attachments.add(attachment);
            } catch (IOException exception) {
                log.error(exception.getMessage(), exception);
                throw new ConverterException(exception.getMessage(), exception);
            }
        }
        return attachments;
    }

    @Override
    public boolean canConvert(List<Resource> resources) {
        return function.apply(resources);
    }
}

