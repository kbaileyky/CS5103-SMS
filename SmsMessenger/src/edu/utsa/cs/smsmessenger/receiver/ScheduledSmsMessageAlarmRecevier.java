package edu.utsa.cs.smsmessenger.receiver;

import java.util.ArrayList;
import java.util.Calendar;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * This class listens for Alarms for scheduled SMS messages and broadcasts
 * intents to activities.
 * 
 * @author Michael Madrigal
 * @version 1.1
 * @since 1.1
 * 
 */
public class ScheduledSmsMessageAlarmRecevier extends BroadcastReceiver {

	private SmsMessageHandler smsMessageHandler;
	private Context context;

	private class SaveNewMessageToDbTask extends
			AsyncTask<MessageContainer, Void, MessageContainer> {
		@Override
		protected MessageContainer doInBackground(MessageContainer... objects) {
			MessageContainer message = null;
			for (MessageContainer msg : objects) {
				message = msg;
				getSmsMessageHandler(context).deleteMessage(msg);
				msg.setType(SmsMessageHandler.MSG_TYPE_OUT);
				msg.setStatus(SmsMessageHandler.SMS_PENDING);
				getSmsMessageHandler(context).saveSmsToDB(msg);
			}
			getSmsMessageHandler(context).close();
			return message;
		}

		@Override
		protected void onPostExecute(MessageContainer message) {

			Intent sentIntent = new Intent("edu.utsa.cs.smsmessenger.SMS_SENT");
			sentIntent.putExtra("edu.utsa.cs.smsmessenger.MessageContainer",
					message);

			PendingIntent sentPendingIntent = PendingIntent.getBroadcast(
					context, (int) message.getId(), sentIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			// Send Message
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(message.getPhoneNumber(), null,
					message.getBody(), sentPendingIntent, null);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;

		Log.d("ScheduledSmsMessageAlarmRecevier", "onReceive() called at "
				+ Calendar.getInstance().getTime());
		String selectString = SmsMessageHandler.COL_NAME_DATE + " < ?";
		String[] selectArgs = { String.valueOf(Calendar.getInstance()
				.getTimeInMillis()) };
		String sortOrder = SmsMessageHandler.COL_NAME_DATE + " ASC";

		ArrayList<MessageContainer> messageList = getSmsMessageHandler(context)
				.getSmsMessages(selectString, selectArgs, sortOrder,
						SmsMessageHandler.MSG_TYPE_SCHEDULED);
		if (messageList.size() > 0)
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.sms_sending_scheduled_message),
					Toast.LENGTH_LONG).show();
		for (MessageContainer message : messageList) {
			MessageContainer[] msgArr = { message };
			SaveNewMessageToDbTask saveThread = new SaveNewMessageToDbTask();
			saveThread.execute(msgArr);
		}
	}

	private SmsMessageHandler getSmsMessageHandler(Context context) {
		if (smsMessageHandler == null)
			smsMessageHandler = new SmsMessageHandler(context);
		return smsMessageHandler;
	}

}
