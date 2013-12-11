package edu.utsa.cs.smsmessenger.test;

import java.util.List;

import edu.utsa.cs.smsmessenger.util.ContactsUtil;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import android.test.AndroidTestCase;

/**
 * This is the JUnit Test Class for SmsMessageHandler.
 * 
 * @author Emilio Mercado
 * @version 1.0
 * @since 1.0
 * 
 */

public class SmsMessageHandlerTest extends AndroidTestCase {

	public void testGetInstance() {
		SmsMessageHandler smsMessageHandlerA = SmsMessageHandler.getInstance(getContext());
		SmsMessageHandler smsMessageHandlerB = SmsMessageHandler.getInstance(getContext());
		
		assertEquals(smsMessageHandlerA,smsMessageHandlerB);
	}
}
