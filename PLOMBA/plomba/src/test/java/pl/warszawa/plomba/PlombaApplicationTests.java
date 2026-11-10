package pl.warszawa.plomba;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PlombaApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void isWorkingShouldReturnTrue() {
		assertTrue(PlombaApplication.isWorking());
	}
}
