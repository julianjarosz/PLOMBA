package pl.warszawa.plomba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlombaApplication {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		SpringApplication.run(PlombaApplication.class, args);
	}

	public static boolean isWorking() {
		return true;
	}
}
