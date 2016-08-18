package hello;

import org.junit.Test;
import static org.junit.Assert.*;

public class HelloControllerTest {
	@Test
	public void testSomeLibraryMethod() {
		HelloController classUnderTest = new HelloController();
		assertEquals("Greetings from Spring Boot!", classUnderTest.index());
	}
}
