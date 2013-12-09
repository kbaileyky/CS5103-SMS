package edu.utsa.cs.smsmessenger.receiver;

import java.util.Calendar;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class DeliveredSmsMessageReceiver extends BroadcastReceiver {

	private SmsMessageHandler smsMessageHandler;
	private Context context;
	private class UpdateMessagesInDbTask extends AsyncTask<MessageContainer, Void, Void> {
		@Override
		protected Void doInBackground(MessageContainer... messages) {
			if (context != null) {
				for (MessageContainer msg : messages) {
					getSmsMessageHandler(context).updateSmsMessage(msg);
				}
				getSmsMessageHandler(context).close();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent updateSentMessageIntent = new Intent(
					SmsMessageHandler.UPDATE_MSG_INTENT);
			context.sendBroadcast(updateSentMessageIntent);
		}
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		final Bundle bundle = intent.getExtras();
		MessageContainer message = (MessageContainer) bundle
				.getSerializable("edu.utsa.cs.smsmessenger.MessageContainer");

		switch (getResultCode()) {
		case Activity.RESULT_OK:
			//message.setDate(Calendar.getInstance().getTimeInMillis());
			message.setStatus(SmsMessageHandler.SMS_DELIVERED);
			updateDatabase(context, message, context.getResources().getString(R.string.sms_delivered));
			break;
		case Activity.RESULT_CANCELED:
			//message.setDate(Calendar.getInstance().getTimeInMillis());
			message.setStatus(SmsMessageHandler.SMS_FAILED);
			updateDatabase(context, message, context.getResources().getString(R.string.sms_not_delivered));
			break;
		}
	}

	private SmsMessageHandler getSmsMessageHandler(Context context) {
		if (smsMessageHandler == null)
			smsMessageHandler = new SmsMessageHandler(context);
		return smsMessageHandler;
	}
	private void updateDatabase(Context context, MessageContainer message,
			String displayMessage) {

		MessageContainer[] msgArr = { message };
		UpdateMessagesInDbTask saveThread = new UpdateMessagesInDbTask();
		saveThread.execute(msgArr);
		
		Toast.makeText(context, displayMessage, Toast.LENGTH_SHORT).show();
	}
}
