package edu.utsa.cs.smsmessenger.receiver;

import java.util.ArrayList;
import java.util.Calendar;

import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * This class listens for incoming SMS messages and broadcasts intents to
 * activities.
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class IncomingSmsMessageReceiver extends BroadcastReceiver {

	private SmsMessageHandler smsMessageHandler;
	private Context context;

	private class SaveNewMessagesToDbTask extends AsyncTask<Object, Void, Void> {
		@Override
		protected Void doInBackground(Object... objects) {
			if (context != null) {
				for (Object msg : objects) {
					getSmsMessageHandler(context).saveSmsToDB(
							(MessageContainer) msg);
				}
				getSmsMessageHandler(context).close();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent newMsgIntent = new Intent(
					SmsMessageHandler.UPDATE_MSG_INTENT);
			context.sendBroadcast(newMsgIntent);
		}
	}

	@Override
	public void onReceive(final Context context, Intent intent) {
		this.context = context;
		final Bundle bundle = intent.getExtras();
		ArrayList<MessageContainer> newMsgList = new ArrayList<MessageContainer>();
		try {
			if (bundle != null) {
				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);

					MessageContainer msg = new MessageContainer(
							SmsMessageHandler.MSG_TYPE_IN);
					msg.setPhoneNumber(currentMessage
							.getDisplayOriginatingAddress());
					msg.setBody(currentMessage.getDisplayMessageBody());

					// This is how it should be done, but emulator times are all
					// messed up
					// Calendar cal = Calendar.getInstance();
					// cal.setTimeInMillis(currentMessage.getTimestampMillis());
					// msg.setDate(cal.getTimeInMillis());

					// This is the way to correct emulator times
					msg.setDate(Calendar.getInstance().getTimeInMillis());

					// TODO - lookup contact by phone number

					Log.d("IncomingSMSReceiver",
							"phoneNumber: " + msg.getPhoneNumber()
									+ "; message: " + msg.getBody());

					Toast toast = Toast.makeText(context, "New message from: "
							+ msg.getPhoneNumber(), Toast.LENGTH_LONG);
					toast.show();

					newMsgList.add(msg);
				}
			}
		} catch (Exception e) {
			Log.e("IncomingSMSReceiver", "Exception in onReceive(): " + e);
		}

		Log.e("IncomingSMSReceiver", "New Msg List Size: " + newMsgList.size());

		SaveNewMessagesToDbTask saveThread = new SaveNewMessagesToDbTask();
		saveThread.execute(newMsgList.toArray());
	}

	private SmsMessageHandler getSmsMessageHandler(Context context) {
		if (smsMessageHandler == null)
			smsMessageHandler = new SmsMessageHandler(context);
		return smsMessageHandler;
	}
}
