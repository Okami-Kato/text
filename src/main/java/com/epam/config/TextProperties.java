package com.epam.config;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("text")
@Getter
@Setter
public class TextProperties {

    List<String> filePaths;
}
