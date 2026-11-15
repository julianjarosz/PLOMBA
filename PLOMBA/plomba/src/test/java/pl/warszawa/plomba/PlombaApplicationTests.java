package pl.warszawa.plomba;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlombaApplicationTests {

  @Test
  void contextLoads() {}

  @Test
  void isWorkingShouldReturnTrue() {
    assertTrue(PlombaApplication.isWorking());
  }
}
