package hello;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class HelloController {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "Greetings from Spring Boot!";
	}

}