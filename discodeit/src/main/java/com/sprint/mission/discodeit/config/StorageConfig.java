package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.storage.LocalBinaryContentStorage;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    public LocalBinaryContentStorage localBinaryContentStorage(
            @Value("${discodeit.storage.local.root-dir:./storage}") String rootDir) {
        return new LocalBinaryContentStorage(Paths.get(rootDir));
    }
}
