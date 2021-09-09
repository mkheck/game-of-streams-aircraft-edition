package com.thehecklers.planesource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.PollableBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@SpringBootApplication
public class PlaneSourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaneSourceApplication.class, args);
	}

}

@Configuration
class PositionFeeder {
	private final WebClient client = WebClient.create("http://localhost:7634/aircraft");

	@PollableBean
	Supplier<Flux<Aircraft>> sendAC() {
		return () -> client.get()
				.retrieve()
				.bodyToFlux(Aircraft.class)
				.filter(aircraft -> !aircraft.getReg().isEmpty())
				.log();
	}

/*
	@Bean
	Supplier<List<Aircraft>> sendAC() {
		return () -> {
			List<Aircraft> aircraftList = client.get()
					.retrieve()
					.bodyToFlux(Aircraft.class)
					.filter(aircraft -> !aircraft.getReg().isEmpty())
			.toStream().collect(Collectors.toList());

			//aircraftList.forEach(System.out::println);

			return aircraftList;
		};
	}
*/
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Aircraft {
	private Long id;
	private String callsign, reg, flightno, type;
	private int altitude, heading, speed;
	private double lat, lon;
}
