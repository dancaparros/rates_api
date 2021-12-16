package io.ratesapi.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "framework")
public class FrameworkConfiguration {

    @NotBlank
    String ratesapiUrl;
    boolean consoleRequestLogging;

}
