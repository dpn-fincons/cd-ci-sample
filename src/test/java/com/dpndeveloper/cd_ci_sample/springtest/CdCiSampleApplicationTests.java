package com.dpndeveloper.cd_ci_sample.springtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CdCiSampleApplicationTests {

    @Value("${spring.application.name}")
    private String applicationName;

    @Test
    void contextLoads() {
        assertThat(applicationName).isEqualTo("Test for Jenkins and Docker");
    }

}
