package com.company.safekyc;

import com.company.safekyc.security.JwtAuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@EnableAsync
@SpringBootApplication
@EntityScan(basePackageClasses = {
        SafeKYCApplication.class,
        Jsr310Converters.class
})
public class SafeKYCApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafeKYCApplication.class, args);
    }

    @PostConstruct
    void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

}
