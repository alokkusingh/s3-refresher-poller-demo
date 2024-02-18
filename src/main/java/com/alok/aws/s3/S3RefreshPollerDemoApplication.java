package com.alok.aws.s3;

import com.alok.aws.s3.refresh.poller.S3RefreshPoller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class S3RefreshPollerDemoApplication implements ApplicationRunner {

    @Autowired
    private S3RefreshPoller s3RefreshPoller;

    public static void main(String[] args) {
        SpringApplication.run(S3RefreshPollerDemoApplication.class, args);
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        s3RefreshPoller.registerListener(
                () -> {
                    log.info("Refresh Called!");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
