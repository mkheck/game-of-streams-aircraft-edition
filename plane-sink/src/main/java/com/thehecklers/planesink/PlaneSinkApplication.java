package com.thehecklers.planesink;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@SpringBootApplication
public class PlaneSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaneSinkApplication.class, args);
	}

}

@Configuration
class PlaneLogger {
	@Bean
	Consumer<Flux<EssentialAircraft>> logIt() {
		return aircraftFlux -> aircraftFlux.subscribe(System.out::println);
	}

/*
	@Bean
	Consumer<List<EssentialAircraft>> logIt() {
		return eACs -> eACs.forEach(System.out::println);
	}
*/
}

@Data
@AllArgsConstructor
class EssentialAircraft {
	private final String callsign, reg, type, mfr;
}
