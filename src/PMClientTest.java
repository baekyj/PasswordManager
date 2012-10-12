import static org.junit.Assert.*;

import org.junit.Test;


public class PMClientTest {
	PMClient pc = new PMClient();
	@Test
	public void testGetRest() {
	    assertEquals("-bbb vvv", pc.getRest("aaa -bbb vvv"));
	}
	@Test
	public void testGetRest2() {
		assertEquals("-ccc eee -o iii", pc.getRest("bbb -ccc eee -o iii"));
	}
	@Test
	public void testGetRest3() {
		assertEquals("-o aaa -i eee -k jjj", pc.getRest("bbb -o aaa -i eee -k jjj"));
	}
	@Test
	public void testChkSep() {
		assertEquals("2", pc.chkSep("C:\\temp", "\\"));
	}
}
