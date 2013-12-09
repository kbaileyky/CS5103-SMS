package edu.utsa.cs.smsmessenger.activity;

import java.util.ArrayList;
import java.util.Calendar;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.adapter.MessageContainerAdapter;
import edu.utsa.cs.smsmessenger.model.ContactContainer;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.ContactsUtil;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is the Activity that shows all messages in a single conversation
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class ConversationActivity extends Activity {

	private String contactPhoneNumber;
	private long contactId;
	private ListView conversationListView;
	private SmsMessageHandler smsMessageHandler;
	private MessageContainerAdapter messageContainerAdapter;
	private EditText messageEditText;
	private TextView messageCharCountTextView;
	private ImageButton sendMessageImageButton;
	private ContactContainer contact;

	private BroadcastReceiver newMsgReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equalsIgnoreCase(SmsMessageHandler.UPDATE_MSG_INTENT)) {
				fillConversationListView();
			}
			Log.d("ConversationsListActivity - BroadcastReceiver",
					"New intent received.");
		}
	};

	private OnClickListener sendMessageOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String message = messageEditText.getText().toString();
			message = message.trim();
			if (!message.isEmpty()) {
				handleSmsMessageSend(contactPhoneNumber, message);
			}
		}
	};

	private class SaveNewDraftoDbTask extends
			AsyncTask<MessageContainer, Void, MessageContainer> {
		@Override
		protected MessageContainer doInBackground(MessageContainer... objects) {
			MessageContainer message = null;
			String selectString = SmsMessageHandler.COL_NAME_PHONE_NUMBER
					+ " = ? AND " + SmsMessageHandler.COL_NAME_STATUS + " = ? ";
			String[] selectArgs = {
					ContactsUtil.getStrippedPhoneNumber(contact
							.getPhoneNumber()), SmsMessageHandler.SMS_DRAFT };

			for (MessageContainer msg : objects) {
				message = msg;
				getSmsMessageHandler().deleteDraftMessage(selectString,
						selectArgs);
				getSmsMessageHandler().saveSmsToDB(msg);
			}
			getSmsMessageHandler().close();
			return message;
		}
	}

	private class SaveNewMessageToDbTask extends
			AsyncTask<MessageContainer, Void, MessageContainer> {
		@Override
		protected MessageContainer doInBackground(MessageContainer... objects) {
			MessageContainer message = null;
			for (MessageContainer msg : objects) {
				message = msg;
				getSmsMessageHandler().saveSmsToDB(msg);
			}
			getSmsMessageHandler().close();
			return message;
		}

		@Override
		protected void onPostExecute(MessageContainer result) {

			// Send SMS Message
			sendSmsMessage(result);

			Intent newMsgIntent = new Intent(
					SmsMessageHandler.UPDATE_MSG_INTENT);
			getContext().sendBroadcast(newMsgIntent);
			
			//Enable send button
			//sendMessageImageButton.setEnabled(true);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);

		Bundle extras = getIntent().getExtras();
		contactPhoneNumber = extras
				.getString(SmsMessageHandler.COL_NAME_PHONE_NUMBER);
		contactId = extras.getLong(SmsMessageHandler.COL_NAME_CONTACT_ID);

		contact = ContactsUtil.getContactByPhoneNumber(
				this.getContentResolver(), contactPhoneNumber);

		setTitle(contact.getDisplayName() != null ? contact.getDisplayName()
				: contactPhoneNumber);

		messageEditText = (EditText) findViewById(R.id.msgEditText);

		final String charCountFormat = getResources().getString(
				R.string.text_ratio);
		messageCharCountTextView = (TextView) findViewById(R.id.msgCharCountTextView);
		messageCharCountTextView.setText(String.format(charCountFormat, 0,
				SmsMessageHandler.SMS_MESSAGE_LENGTH));
		messageEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				String message = messageEditText.getText().toString();
				messageCharCountTextView.setText(String.format(charCountFormat,
						message.length(), SmsMessageHandler.SMS_MESSAGE_LENGTH));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}
		});
		sendMessageImageButton = (ImageButton) findViewById(R.id.sendMsgImageButton);
		sendMessageImageButton.setOnClickListener(sendMessageOnClickListener);

		registerNewMsgReceiver();
		fillConversationListView();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(newMsgReceiver);
		OnUserLeavesActivity();
		super.onPause();
	}

	@Override
	protected void onResume() {
		registerNewMsgReceiver();
		fillConversationListView();
		MessageContainer draft = GetDraft();
		if (draft != null)
			messageEditText.setText(draft.getBody());
		super.onResume();
	}

	private void closeExistingNotifications() {
		// Close notifications for this user
		NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationmanager.cancel((int) contactId);
	}

	public void fillConversationListView() {
		closeExistingNotifications();
		MessageContainer.groupContainsPendingMessage = false;
		ArrayList<MessageContainer> msgList = getSmsMessageHandler()
				.getConversationWithUser(contactPhoneNumber);
		getSmsMessageHandler().close();

		messageContainerAdapter = new MessageContainerAdapter(this,
				R.layout.conversation_from_message_item, contact, msgList);
		Log.d("ConversationActivity", "messageContainerAdapter: "
				+ messageContainerAdapter);
		Log.d("ConversationActivity", "conversationListView: "
				+ getConversationListView());
		getConversationListView().setAdapter(messageContainerAdapter);

		scrollListViewToBottom();
		
		if(MessageContainer.groupContainsPendingMessage)
			sendMessageImageButton.setEnabled(false);
		else
			sendMessageImageButton.setEnabled(true);
			

	}

	private void scrollListViewToBottom() {
		getConversationListView().setSelection(
				getConversationListView().getCount() - 1);
	}

	public ListView getConversationListView() {
		if (conversationListView == null) {
			conversationListView = (ListView) findViewById(R.id.conversationListView);

			conversationListView
					.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
			conversationListView.setStackFromBottom(true);
		}
		return conversationListView;
	}

	public SmsMessageHandler getSmsMessageHandler() {
		if (smsMessageHandler == null)
			smsMessageHandler = new SmsMessageHandler(this);
		return smsMessageHandler;
	}

	public void registerNewMsgReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(SmsMessageHandler.UPDATE_MSG_INTENT);
		registerReceiver(newMsgReceiver, filter);
	}

	public void handleSmsMessageSend(final String phoneNumber,
			final String message) {

		sendMessageImageButton.setEnabled(false);
		if (message.length() > SmsMessageHandler.SMS_MESSAGE_LENGTH) {
			Toast.makeText(
					this,
					String.format(
							getResources().getString(
									R.string.message_length_error),
							SmsMessageHandler.SMS_MESSAGE_LENGTH),
					Toast.LENGTH_LONG).show();
			sendMessageImageButton.setEnabled(true);
			return;
		}

		final MessageContainer messageContainer = new MessageContainer(
				SmsMessageHandler.MSG_TYPE_OUT);
		messageContainer.setPhoneNumber(phoneNumber);
		messageContainer.setBody(message);
		messageContainer.setDate(Calendar.getInstance().getTimeInMillis());
		messageContainer.setStatus(SmsMessageHandler.SMS_PENDING);

		MessageContainer[] msgArr = { messageContainer };
		SaveNewMessageToDbTask saveThread = new SaveNewMessageToDbTask();
		saveThread.execute(msgArr);
		messageEditText.setText("");
	}

	public void sendSmsMessage(MessageContainer messageContainer) {
		
		// Intent for send
		Intent sentIntent = new Intent("edu.utsa.cs.smsmessenger.SMS_SENT");
		sentIntent.putExtra("edu.utsa.cs.smsmessenger.MessageContainer", messageContainer);
		
		PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0,
				sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Intent for delivery
		Intent deliveredIntent = new Intent(
				"edu.utsa.cs.smsmessenger.SMS_DELIVERED");
		deliveredIntent.putExtra("edu.utsa.cs.smsmessenger.MessageContainer", messageContainer);
		
		PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this,
				0, deliveredIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Send Message
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(messageContainer.getPhoneNumber(), null,
				messageContainer.getBody(), sentPendingIntent,
				null);

	}

	private Context getContext() {
		return this;
	}

	private void OnUserLeavesActivity() {

		String messageBody = messageEditText.getText().toString().trim();

		if (!messageBody.isEmpty()) {
			// Save message as new draft
			MessageContainer message = new MessageContainer();
			message.setBody(messageBody);
			message.setType(SmsMessageHandler.MSG_TYPE_DRAFT);
			message.setStatus(SmsMessageHandler.SMS_DRAFT);
			message.setPhoneNumber(contact.getPhoneNumber());
			if (contact.getDisplayName() != null)
				message.setId(contact.getId());
			message.setDate(Calendar.getInstance().getTimeInMillis());

			MessageContainer[] msgArr = { message };
			SaveNewDraftoDbTask saveThread = new SaveNewDraftoDbTask();
			saveThread.execute(msgArr);
		}
	}

	private MessageContainer GetDraft() {
		String selectString = SmsMessageHandler.COL_NAME_PHONE_NUMBER
				+ " = ? AND " + SmsMessageHandler.COL_NAME_STATUS + " = ? ";
		String[] selectArgs = {
				ContactsUtil.getStrippedPhoneNumber(contact.getPhoneNumber()),
				SmsMessageHandler.SMS_DRAFT };
		String sortOrder = SmsMessageHandler.COL_NAME_DATE + " DESC";

		ArrayList<MessageContainer> newDraftList = getSmsMessageHandler()
				.getSmsMessages(selectString, selectArgs, sortOrder,
						SmsMessageHandler.MSG_TYPE_DRAFT);

		// There should only be one or zero, so let's grab the first
		if (newDraftList.size() > 0) {
			Log.d("ConversationActivity",
					"draftList size:" + newDraftList.size());
			Log.d("ConversationActivity", "draftList 0 phoneNumber:"
					+ newDraftList.get(0).getPhoneNumber());
			Log.d("ConversationActivity", "draftList 0 message:"
					+ newDraftList.get(0).getBody());
			return newDraftList.get(0);
		}
		return null;
	}

}
