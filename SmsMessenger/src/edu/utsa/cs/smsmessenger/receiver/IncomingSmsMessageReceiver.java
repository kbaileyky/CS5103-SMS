package edu.utsa.cs.smsmessenger.receiver;

import edu.utsa.cs.smsmessenger.activity.ConversationsListActivity;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/** IncomingSmsMessageReceiver listens for incoming Sms messages and brocasts intents to activities
 * 
 * @author mmadrigal
 *
 */
public class IncomingSmsMessageReceiver extends BroadcastReceiver {

	private SmsMessageHandler smsHandler;

	@Override
	public void onReceive(final Context context, Intent intent) {
		final Bundle bundle = intent.getExtras();
		try{
            if (bundle != null) {
                 
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                 
                for (int i = 0; i < pdusObj.length; i++) {
                     
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    
                    MessageContainer msg = new MessageContainer();
                    msg.setPhoneNumber(currentMessage.getDisplayOriginatingAddress());
                    msg.setBody(currentMessage.getDisplayMessageBody());
                    msg.setDate(currentMessage.getTimestampMillis());
                                        
                    // TODO - lookup contact by phone number
                    
                    Log.d("IncomingSMSReceiver", "phoneNumber: "+ msg.getPhoneNumber() + "; message: " + msg.getBody());

                    Toast toast = Toast.makeText(context, "New message from: "+ msg.getPhoneNumber(), Toast.LENGTH_LONG);
                    toast.show();
                    
                    //TODO - save message, should be done with async task
                    getSmsMessageHandler(context).saveIncomingSmsToDB(msg);
                    
                }
            }
		}
		catch(Exception e){
			Log.e("IncomingSMSReceiver", "Exception in onReceive(): " + e);
		}
		
		getSmsMessageHandler(context).close();
		
        //Stop broadcast - Warning, this will prevent native sms messenger app from handling sms message
        //this.abortBroadcast();

		//Delay broadcast to give system time to insert new message
		Handler handler = new Handler();   
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				Intent newMsgIntent = new Intent(SmsMessageHandler.NEW_MSG_INTENT);
				context.sendBroadcast(newMsgIntent);
			}
		}, 3000);
	}
	private SmsMessageHandler getSmsMessageHandler(Context context)
	{
		if(smsHandler == null)
			smsHandler = new SmsMessageHandler(context);
		return smsHandler;
	}
}
