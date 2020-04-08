package com.company.safekyc;

<<<<<<< HEAD
=======
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
>>>>>>> fdb9163e536ed3ca9a5322ce24c08445b802f623
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@EntityScan("com.company.safekyc")
<<<<<<< HEAD
=======
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
>>>>>>> fdb9163e536ed3ca9a5322ce24c08445b802f623
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}