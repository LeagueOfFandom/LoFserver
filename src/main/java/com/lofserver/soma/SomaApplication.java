package com.lofserver.soma;

import com.lofserver.soma.dto.UserTeamInfoListDto;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserTeamlistRepository;
import com.lofserver.soma.service.LofService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SomaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SomaApplication.class, args);
	}
	// gitlab 수정확인
}
