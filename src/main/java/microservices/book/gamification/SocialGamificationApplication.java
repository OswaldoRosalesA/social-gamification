package microservices.book.gamification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SocialGamificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialGamificationApplication.class, args);
	}

}
