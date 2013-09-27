package edu.utsa.cs.messenger.receiver;

import edu.utsa.cs.messenger.activity.ConversationsListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class IncomingSMSReceiver extends BroadcastReceiver {
	
	private SmsManager smsManager;

	@Override
	public void onReceive(final Context context, Intent intent) {
		final Bundle bundle = intent.getExtras();
		try{
            if (bundle != null) {
                 
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                 
                for (int i = 0; i < pdusObj.length; i++) {
                     
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    
                    // TODO - lookup contact by phone number
                    Log.i("IncomingSMSReceiver", "phoneNumber: "+ phoneNumber + "; message: " + message);

                    Toast toast = Toast.makeText(context, "New message from: "+ phoneNumber, Toast.LENGTH_LONG);
                    toast.show();
                    
                    //TODO - save message
                    
                }
            }
		}
		catch(Exception e){
			Log.e("IncomingSMSReceiver", "Exception in onReceive(): " + e);
		}
        //Stop broadcast - Warning, this will prevent native sms messenger app from handling sms message
        //this.abortBroadcast();

		//Delay broadcast to give system time to insert new message
		Handler handler = new Handler();   
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				Intent newMsgIntent = new Intent(ConversationsListActivity.NEW_MESSAGE_RECEVIED);
				context.sendBroadcast(newMsgIntent);
			}
		}, 1500);
	}
	public SmsManager getSMSManager()
	{
		if(smsManager==null)
			smsManager = SmsManager.getDefault();
		return smsManager;
	}

}
