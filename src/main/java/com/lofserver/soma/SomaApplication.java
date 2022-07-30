package com.lofserver.soma;

import com.lofserver.soma.component.CrawlComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SomaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SomaApplication.class, args);
	}
}
