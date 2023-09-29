package local.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	FeignClient feignClient;
	@Test
	void contextLoads() {
	}

	@Test
	public void testLongTask() {
		StepVerifier.create(feignClient.longTask())
				.expectNext("OK1")
				.expectComplete();

	}

}
