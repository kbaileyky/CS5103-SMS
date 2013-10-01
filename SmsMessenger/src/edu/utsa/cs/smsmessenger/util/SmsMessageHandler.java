package edu.utsa.cs.smsmessenger.util;

import edu.utsa.cs.smsmessenger.model.MessageContainer;

/** SmsMessageHandler contains static methods to interfaces with the systems database
 * 
 * @author mmadrigal
 *
 */
public class SmsMessageHandler {

    public static final String SMS_DRAFT = "SMS_DRAFT";
    public static final String SMS_SENT = "SMS_SENT";
    public static final String SMS_DELIVERED = "SMS_DELIVERED";
    public static final String SMS_FAILED = "SMS_FAILED";
    public static final String SMS_RECEIVED = "SMS_RECEIVED";

	public static void saveOutgoingSmsToDB(MessageContainer message)
	{
		
	}

}
