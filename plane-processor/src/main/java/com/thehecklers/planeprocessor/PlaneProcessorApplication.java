package com.thehecklers.planeprocessor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SpringBootApplication
public class PlaneProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaneProcessorApplication.class, args);
    }

}

@Configuration
class PlaneFilter {
/*
    @Bean
    Function<Flux<Aircraft>, Flux<EssentialAircraft>> transformAC() {
        return aircraftFlux -> aircraftFlux
                .map(ac -> new EssentialAircraft(
                    ac.getCallsign(),
                    ac.getReg(),
                    ac.getType()))
                .log();
    }
*/

    @Bean
    Function<List<Aircraft>, List<EssentialAircraft>> transformAC() {
        return iterableAC -> {
            List<EssentialAircraft> eACs = new ArrayList<>();

            iterableAC.forEach(ac -> eACs.add(new EssentialAircraft(
                    ac.getCallsign(),
                    ac.getReg(),
                    ac.getType())));

            eACs.forEach(System.out::println);

            return eACs;
        };
    }
}


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
class Aircraft {
    private Long id;
    private String callsign, reg, flightno, type;
    private int altitude, heading, speed;
    private double lat, lon;
}

@Data
class EssentialAircraft {
    private String callsign, reg, type;
    private String mfr;

    public EssentialAircraft(String callsign, String reg, String type) {
        this.callsign = callsign;
        this.reg = reg;
        this.type = type;

        setMfr();
    }

    private void setMfr() {
        mfr = switch (type.substring(0, 2)) {
            case "A2", "A3" -> "Airbus";
            case "BE", "B3" -> "Beechcraft";
            case "B7" -> "Boeing";
            case "CR", "E7" -> "Embraer";
            case "C1", "C2", "C7", "T2" -> "Cessna";
            case "LJ" -> "Learjet";
            case "MD" -> "McDonnell Douglas";
            case "PA" -> "Piper";
            default -> "Other";
        };

        if (mfr.equals("Other")) {
            System.out.println("Type: " + type);
        }
    }
}