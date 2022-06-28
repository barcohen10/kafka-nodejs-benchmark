package application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/")
	public String index() {
		return "Greetings!";
	}

	@GetMapping("/isAlive")
	public Boolean isAlive() {
		return true;
	}
}
