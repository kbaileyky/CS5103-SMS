package edu.utsa.cs.smsmessenger.receiver;

import java.util.Calendar;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import edu.utsa.cs.smsmessenger.util.AppConstants;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * This class listens for sent SMS messages and broadcasts intents to
 * activities.
 * 
 * @author Michael Madrigal
 * @version 1.1
 * @since 1.1
 * 
 */
public class SentSmsMessageReceiver extends BroadcastReceiver {

	private Context context;
	

	private class UpdateMessagesInDbTask extends
			AsyncTask<MessageContainer, Void, Void> {
		@Override
		protected Void doInBackground(MessageContainer... messages) {
			if (context != null) {
				for (MessageContainer msg : messages) {
					getSmsMessageHandler(context).updateSmsMessage(msg);
				}
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
		Log.d("SentSmsMessageReceiver", "OnReceive getResultCode: "
				+ getResultCode());
		this.context = context;
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);


		final Bundle bundle = intent.getExtras();
		MessageContainer message = (MessageContainer) bundle
				.getSerializable("edu.utsa.cs.smsmessenger.MessageContainer");

		switch (getResultCode()) {
		case Activity.RESULT_OK:
			message.setDate(Calendar.getInstance().getTimeInMillis());
			message.setStatus(SmsMessageHandler.SMS_SENT);
			updateDatabase(context, message,
					context.getResources().getString(R.string.sms_message_sent));
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			v.vibrate(AppConstants.VIBRATION_ERROR_PULSE, -1); //-1 means only repeat once
			message.setDate(Calendar.getInstance().getTimeInMillis());
			message.setStatus(SmsMessageHandler.SMS_FAILED);
			updateDatabase(context, message,
					context.getResources().getString(R.string.sms_send_failed));
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			v.vibrate(AppConstants.VIBRATION_ERROR_PULSE, -1); //-1 means only repeat once
			message.setDate(Calendar.getInstance().getTimeInMillis());
			message.setStatus(SmsMessageHandler.SMS_FAILED);
			updateDatabase(context, message,
					context.getResources().getString(R.string.sms_no_service));
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			v.vibrate(AppConstants.VIBRATION_ERROR_PULSE, -1); //-1 means only repeat once
			message.setDate(Calendar.getInstance().getTimeInMillis());
			message.setStatus(SmsMessageHandler.SMS_FAILED);
			updateDatabase(context, message,
					context.getResources().getString(R.string.sms_no_signal));
			break;
		}
	}

	private SmsMessageHandler getSmsMessageHandler(Context context) {
		return SmsMessageHandler.getInstance(context);
	}

	private void updateDatabase(Context context, MessageContainer message,
			String displayMessage) {

		MessageContainer[] msgArr = { message };
		UpdateMessagesInDbTask saveThread = new UpdateMessagesInDbTask();
		saveThread.execute(msgArr);

		Toast.makeText(context, displayMessage, Toast.LENGTH_SHORT).show();
	}
}
