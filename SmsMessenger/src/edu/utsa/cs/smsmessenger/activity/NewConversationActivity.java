package edu.utsa.cs.smsmessenger.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.adapter.AutoContactFillAdapter;
import edu.utsa.cs.smsmessenger.model.ContactContainer;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.ContactsUtil;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is the Activity that allows the users to start a new conversations
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class NewConversationActivity extends Activity {

	public static final int CONTACT_REQUEST_CODE = 101;
	public static final int SCHEDULE_REQUEST_CODE = 102;
	private AutoCompleteTextView newRecipientTextView;
	private EditText newMessageEditText;
	private TextView newMessageCharCountTextView;
	private ImageButton sendNewMessageButton;
	private CheckBox scheduleMessageCheckBox;
	private TextView scheduleMessageTextView;
	private Calendar scheduleMessageDate;
	private SimpleDateFormat dateTimeSdf;

	private OnClickListener addNewRecipientOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
			startActivityForResult(intent, CONTACT_REQUEST_CODE);
		}
	};

	private boolean sent = false;

	private OnClickListener sendNewMessageOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			System.out.println("onClick send New Message!!!");
			String number = newRecipientTextView.getText().toString();
			String message = newMessageEditText.getText().toString();

			if (ContactsUtil.isAPhoneNumber(number)) {
				handleSmsMessageSend(number, message);
			} else {
				String phoneNumber = ContactsUtil.getPhoneNumberByContactName(
						getActivity().getContentResolver(), number);
				if (phoneNumber != null) {
					if (!sent) {
						handleSmsMessageSend(phoneNumber, message);
					}
					Log.d("NewConversationActivity", "" + sent);
				}
			}
		}
	};

	private Activity actvty = this;

	private OnFocusChangeListener recipientTextFieldFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			if (!newRecipientTextView.isFocused()) {
				if (newRecipientTextView.getText().length() == 0) {
					sendNewMessageButton.setEnabled(false);
				} else {
					if (ContactsUtil.isAValidPhoneNumber(actvty
							.getContentResolver(), newRecipientTextView
							.getText().toString())) {
						if (ContactsUtil.isAPhoneNumber(newRecipientTextView
								.getText().toString())) {
							ContactContainer contact = ContactsUtil
									.getContactByPhoneNumber(actvty
											.getContentResolver(),
											newRecipientTextView.getText()
													.toString());

							newRecipientTextView.setText(contact
									.getDisplayName() != null ? contact
									.getDisplayName() : contact
									.getPhoneNumber());
						}
						sendNewMessageButton.setEnabled(true);
					} else {
						sendNewMessageButton.setEnabled(false);
						newRecipientTextView.setText("");
						newRecipientTextView.post(new Runnable() {
							@Override
							public void run() {
								newRecipientTextView.requestFocus();
							}

						});

					}
				}
			}
		}

	};

	private class SaveNewDraftoDbTask extends
			AsyncTask<MessageContainer, Void, MessageContainer> {
		@Override
		protected MessageContainer doInBackground(MessageContainer... objects) {
			MessageContainer message = null;
			for (MessageContainer msg : objects) {
				message = msg;
				getSmsMessageHandler().deleteNewDraftMessage();
				getSmsMessageHandler().saveSmsToDB(msg);
			}
			getSmsMessageHandler().close();
			return message;
		}

		@Override
		protected void onPostExecute(MessageContainer result) {
			// getActivity().finish();
		}
	}

	private class SaveNewMessageToDbStartConversationTask extends
			AsyncTask<MessageContainer, Void, MessageContainer> {
		@Override
		protected MessageContainer doInBackground(MessageContainer... objects) {
			MessageContainer message = null;
			for (MessageContainer msg : objects) {
				message = msg;
				getSmsMessageHandler().deleteNewDraftMessage();
				getSmsMessageHandler().saveSmsToDB(msg);
			}
			getSmsMessageHandler().close();
			return message;
		}

		@Override
		protected void onPostExecute(MessageContainer result) {
			// Clear input fields
			newMessageEditText.setText("");
			newRecipientTextView.setText("");

			// Send SMS Message
			if (result.getType().equals(SmsMessageHandler.MSG_TYPE_OUT))
				sendSmsMessage(result);
			else if(result.getType().equals(SmsMessageHandler.MSG_TYPE_SCHEDULED))
				scheduleMessage(result);

			Intent coversationIntent = new Intent(getContext(),
					ConversationActivity.class);
			coversationIntent.putExtra(SmsMessageHandler.COL_NAME_PHONE_NUMBER,
					result.getPhoneNumber());
			coversationIntent.putExtra(SmsMessageHandler.COL_NAME_CONTACT_ID,
					result.getContactId());
			getContext().startActivity(coversationIntent);
			getActivity().finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_conversation);

		dateTimeSdf = new SimpleDateFormat(getResources().getString(
				R.string.date_time_format2),
				getResources().getConfiguration().locale);

		scheduleMessageTextView = (TextView) findViewById(R.id.msgScheduleTextView);
		scheduleMessageCheckBox = (CheckBox) findViewById(R.id.msgSchedulecheckBox);
		newRecipientTextView = (AutoCompleteTextView) findViewById(R.id.newMsgRecipientAutoCompleteTextView);
		newRecipientTextView.setAdapter(new AutoContactFillAdapter(this));
		newRecipientTextView
				.setOnFocusChangeListener(recipientTextFieldFocusListener);

		newMessageEditText = (EditText) findViewById(R.id.newMsgTextEditText);

		final String charCountFormat = getResources().getString(
				R.string.text_ratio);
		newMessageCharCountTextView = (TextView) findViewById(R.id.msgCharCountTextView);
		newMessageCharCountTextView.setText(String.format(charCountFormat, 0,
				SmsMessageHandler.SMS_MESSAGE_LENGTH));
		newMessageEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String message = newMessageEditText.getText().toString();
				newMessageCharCountTextView.setText(String.format(
						charCountFormat, message.length(),
						SmsMessageHandler.SMS_MESSAGE_LENGTH));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		scheduleMessageCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (arg1) {
							startSheduleMessageActivity();
						} else
							sendNewMessageButton
									.setImageResource(R.drawable.send_msg_icon);

					}
				});

		ImageButton newRecipientButton = (ImageButton) findViewById(R.id.newMsgAddRecipientImageButton);
		sendNewMessageButton = (ImageButton) findViewById(R.id.newMsgSendImageButton);

		newRecipientButton.setOnClickListener(addNewRecipientOnClickListener);
		sendNewMessageButton.setOnClickListener(sendNewMessageOnClickListener);
		sendNewMessageButton.setEnabled(false);

		// If the message is being forwarded
		if (getIntent().hasExtra("fwdBody")) {
			newMessageEditText.setText(getIntent().getExtras().getString(
					"fwdBody"));
		}

		if (getIntent().hasExtra("replyContact")) {
			newRecipientTextView.setText(getIntent().getExtras().getString(
					"replyContact"));
		}
		MessageContainer draft = GetNewDraft();
		if (draft != null) {
			newMessageEditText.setText(draft.getBody());
			newRecipientTextView.setText(draft.getPhoneNumber());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("NewConversationActivity", "onActivityResult");
		Log.d("NewConversationActivity", "Result Code: " + requestCode);
		if (requestCode == SCHEDULE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				sendNewMessageButton.setImageResource(R.drawable.send_timed_msg_icon);
				scheduleMessageDate = (Calendar) data
						.getSerializableExtra(ScheduleMessageActivity.SCHEDULE_RESQUEST_DATE_KEY);
				scheduleMessageTextView.setText(String.format(
						this.getResources().getString(
								R.string.schedule_date_checkbox_label),
						dateTimeSdf.format(scheduleMessageDate.getTime())));
			} else {
				sendNewMessageButton.setImageResource(R.drawable.send_msg_icon);
				scheduleMessageCheckBox.setChecked(false);
			}
		} else if (requestCode == CONTACT_REQUEST_CODE) {
			if (data != null) {
				Uri uri = data.getData();

				if (uri != null) {
					Cursor c = null;
					try {
						c = getContentResolver()
								.query(uri,
										new String[] {
												ContactsContract.CommonDataKinds.Phone.NUMBER,
												ContactsContract.Contacts.DISPLAY_NAME },
										null, null, null);

						if (c != null && c.moveToFirst()) {
							String number = c.getString(0);
							String type = c.getString(1);
							SetContact(type, number);
						}
					} finally {
						if (c != null) {
							c.close();
						}
					}
				}
			}
		}
	}

	public void SetContact(String name, String number) {
		newRecipientTextView.setText(name);
	}

	public void handleSmsMessageSend(final String phoneNumber,
			final String message) {
		String strippedNumber = ContactsUtil
				.getStrippedPhoneNumber(phoneNumber);
		sendNewMessageButton.setEnabled(false);
		if (message.length() > SmsMessageHandler.SMS_MESSAGE_LENGTH) {
			Toast.makeText(
					this,
					String.format(
							getResources().getString(
									R.string.message_length_error),
							SmsMessageHandler.SMS_MESSAGE_LENGTH),
					Toast.LENGTH_LONG).show();
			sendNewMessageButton.setEnabled(true);
			return;
		}
		MessageContainer messageContainer = new MessageContainer(
				SmsMessageHandler.MSG_TYPE_OUT);
		ContactContainer contact = ContactsUtil.getContactByPhoneNumber(
				getContentResolver(), strippedNumber);
		messageContainer.setContactId(contact.getId());
		messageContainer.setPhoneNumber(strippedNumber);
		messageContainer.setBody(message);
		messageContainer.setDate(Calendar.getInstance().getTimeInMillis());
		messageContainer.setStatus(SmsMessageHandler.SMS_PENDING);

		if (scheduleMessageCheckBox.isChecked()) {
			if (scheduleMessageDate != null
					&& scheduleMessageDate.after(Calendar.getInstance())) {
				// Save Message in DB then send
				messageContainer.setDate(scheduleMessageDate.getTimeInMillis());
				messageContainer.setType(SmsMessageHandler.MSG_TYPE_SCHEDULED);
				messageContainer.setStatus(SmsMessageHandler.SMS_SCHEDULED);
			}
			else
			{
				Toast.makeText(this, getResources().getString(R.string.error_date_in_past), Toast.LENGTH_LONG).show();
				return;
			}
		}
		// Save Message in DB then send
		MessageContainer[] msgArr = { messageContainer };
		SaveNewMessageToDbStartConversationTask saveThread = new SaveNewMessageToDbStartConversationTask();
		saveThread.execute(msgArr);
	}

	public void sendSmsMessage(MessageContainer messageContainer) {

		// Intent for send
		Intent sentIntent = new Intent("edu.utsa.cs.smsmessenger.SMS_SENT");
		sentIntent.putExtra("edu.utsa.cs.smsmessenger.MessageContainer",
				messageContainer);

		PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, (int)messageContainer.getId(),
				sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Intent for delivery
//		Intent deliveredIntent = new Intent(
//				"edu.utsa.cs.smsmessenger.SMS_DELIVERED");
//		deliveredIntent.putExtra("edu.utsa.cs.smsmessenger.MessageContainer",
//				messageContainer);
//		PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this,
//				0, deliveredIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Send Message
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(messageContainer.getPhoneNumber(), null,
				messageContainer.getBody(), sentPendingIntent, null);
	}


	private SmsMessageHandler getSmsMessageHandler() {
		return SmsMessageHandler.getInstance(this);
	}

	private Context getContext() {
		return this;
	}

	private NewConversationActivity getActivity() {
		return this;
	}

	private MessageContainer GetNewDraft() {
		String selectString = SmsMessageHandler.COL_NAME_STATUS + " = ?";
		String[] selectArgs = { SmsMessageHandler.SMS_NEW_DRAFT };
		String sortOrder = SmsMessageHandler.COL_NAME_DATE + " DESC";

		ArrayList<MessageContainer> newDraftList = getSmsMessageHandler()
				.getSmsMessages(selectString, selectArgs, sortOrder,
						SmsMessageHandler.MSG_TYPE_DRAFT);

		// There should only be one or zero, so let's grab the first
		if (newDraftList.size() > 0) {
			Log.d("NewConversationActivity",
					"draftList size:" + newDraftList.size());
			Log.d("NewConversationActivity", "draftList 0 phoneNumber:"
					+ newDraftList.get(0).getPhoneNumber());
			Log.d("NewConversationActivity", "draftList 0 message:"
					+ newDraftList.get(0).getBody());
			return newDraftList.get(0);
		}
		return null;
	}

	private void OnUserLeavesActivity() {

		Log.d("NewConversationActivity", "OnUserLeavesActivity");
		String messageBody = newMessageEditText.getText().toString().trim();
		String phoneNumber = newRecipientTextView.getText().toString().trim();

		if (!messageBody.isEmpty() || !phoneNumber.isEmpty()) {
			if (!messageBody.isEmpty() && !phoneNumber.isEmpty())
				sendNewMessageButton.setEnabled(true);
			// Save message as new draft
			MessageContainer message = new MessageContainer();
			message.setBody(messageBody);
			message.setType(SmsMessageHandler.MSG_TYPE_DRAFT);
			message.setStatus(SmsMessageHandler.SMS_NEW_DRAFT);
			message.setPhoneNumber(phoneNumber);
			message.setDate(Calendar.getInstance().getTimeInMillis());

			MessageContainer[] msgArr = { message };
			SaveNewDraftoDbTask saveThread = new SaveNewDraftoDbTask();
			saveThread.execute(msgArr);
		}
		else
			getSmsMessageHandler().deleteNewDraftMessage();
	}

	public void startSheduleMessageActivity() {
		Intent intent = new Intent(getContext(), ScheduleMessageActivity.class);
		if(scheduleMessageDate!=null)
			intent.putExtra(ScheduleMessageActivity.PASSED_SCHEDULE_DATE_KEY, scheduleMessageDate.getTimeInMillis());
		else
			intent.putExtra(ScheduleMessageActivity.PASSED_SCHEDULE_DATE_KEY, -1L);
		startActivityForResult(intent, SCHEDULE_REQUEST_CODE);
	}

	@Override
	protected void onPause() {
		Log.d("NewConversationActivity", "onPause");
		OnUserLeavesActivity();
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		Log.d("NewConversationActivity", "onBackPressed");
		OnUserLeavesActivity();
		finish();
		super.onBackPressed();
	}

	public void scheduleMessage(MessageContainer message)
	{
		if(message.getType().equals(SmsMessageHandler.MSG_TYPE_SCHEDULED))
		{
			AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent("edu.utsa.cs.smsmessenger.SMS_SCHEDULED");
			PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)message.getId(), intent, 0);
			alarmMgr.set(AlarmManager.RTC_WAKEUP, message.getDate(), pendingIntent);
		}
	}
	

}
