package com.alok.aws.s3.refresh.poller;

import com.alok.aws.s3.refresh.AppType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.apache.ProxyConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.time.Duration;

@Slf4j
@Configuration
public class ApplicationConfiguration {

    private S3Client s3Client = S3Client.builder()
            .region(Region.AP_SOUTH_1)
            .credentialsProvider(ProfileCredentialsProvider.create("yahoo"))
            .httpClient(ApacheHttpClient.builder()
                    .socketTimeout(Duration.ofSeconds(20))
                    .connectionTimeout(Duration.ofSeconds(5))
                    // you may start forward HTTP proxy by following - https://github.com/alokkusingh/squid-proxy.git
                    .proxyConfiguration(ProxyConfiguration.builder()
                            .endpoint(URI.create("http://127.0.0.1:3128"))
                            .build()
                    )
                    .build()
            )
            .build();

    @Bean
    S3RefreshPoller s3RefreshPoller(
            @Value("${aws.s3.refresh.bucket-name}") String bucketName,
            @Value("${refresh.poller.interval}") Long pollingInterval,
            @Value("${refresh.poller.initial-delay}") Long pollingInitialDelay

    ) {
        S3RefreshPoller s3RefreshPoller = S3RefreshPoller.builder()
                .setAppType(AppType.APPA)
                .setBucketName(bucketName)
                .setS3Client(s3Client)
                .setPollingInterval(pollingInterval)
                .setPollingInitialDelay(pollingInitialDelay)
                .build();

//        s3RefreshPoller.registerListener(
//                () -> {
//                   log.info("Refresh is called");
//                    try {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//        );

        return s3RefreshPoller;
    }
}
