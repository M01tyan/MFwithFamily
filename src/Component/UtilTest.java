package Component;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilTest {
	@Test
	public void testEmailValidation() {
		boolean result = Util.emailValidation("");
		assertFalse("failure emailValidation not success", result);
	}

}
