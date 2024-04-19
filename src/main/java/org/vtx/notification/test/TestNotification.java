package org.vtx.notification.test;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.vtx.notification.converter.ResourceConverter;
import org.vtx.notification.payload.SMTPNotification;
import org.vtx.notification.service.SMTPNotificationProvider;

@Component
@RequiredArgsConstructor
public class TestNotification implements InitializingBean {
    private final SMTPNotificationProvider smtpNotificationProvider;

    @Override
    public void afterPropertiesSet() {
//        new JobBuilder()
//                .flow()
//                .build();
    }
}
