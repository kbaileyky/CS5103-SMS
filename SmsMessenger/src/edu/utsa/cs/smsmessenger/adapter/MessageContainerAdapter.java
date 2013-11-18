package edu.utsa.cs.smsmessenger.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.activity.ViewMessageActivity;
import edu.utsa.cs.smsmessenger.model.ContactContainer;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;

/**
 * This class is used to adapter and fill a ListView with an ArrayList of
 * MessageContainer objects
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class MessageContainerAdapter extends ArrayAdapter<MessageContainer> {

	private Context context;
	private ArrayList<MessageContainer> objects;
	private SimpleDateFormat sdf;
	private SmsMessageHandler smsMessageHandler;
	private ContactContainer contact;

	private class DeleteMessageFromDbTask extends
			AsyncTask<MessageContainer, Void, Void> {
		@Override
		protected Void doInBackground(MessageContainer... objects) {
			if (context != null) {
				for (MessageContainer msg : objects) {
					getSmsMessageHandler().deleteMessage(msg);
				}
				getSmsMessageHandler().close();
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

	private class UpdateMessageDbTask extends
			AsyncTask<MessageContainer, Void, Void> {
		@Override
		protected Void doInBackground(MessageContainer... objects) {
			if (context != null) {
				for (MessageContainer msg : objects) {
					getSmsMessageHandler().updateSmsMessage(msg);
				}
				getSmsMessageHandler().close();
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

	public MessageContainerAdapter(Context context, int textViewResourceId,
			ContactContainer contact, ArrayList<MessageContainer> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
		this.contact = contact;
		this.sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MessageContainer message = objects.get(position);

		int layoutResourceId = message.getType().equals(
				SmsMessageHandler.MSG_TYPE_IN) ? R.layout.conversation_from_message_item
				: R.layout.conversation_to_message_item;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Get the need view items
		convertView = inflater.inflate(layoutResourceId, parent, false);
		ImageView msgImageView = (ImageView) convertView
				.findViewById(R.id.msgImageView);
		TextView msgBodyTextView = (TextView) convertView
				.findViewById(R.id.msgBodyTextView);
		TextView msgDateTextView = (TextView) convertView
				.findViewById(R.id.msgDateTextView);

		msgBodyTextView.setText(message.getBody());

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(message.getDate());
		msgDateTextView.setText(sdf.format(cal.getTime()));

		if (message.getType().equals(SmsMessageHandler.MSG_TYPE_IN)) {
			if (contact.getDisplayName() != null) {
				if (contact.getPhotoUri() != null) {
					msgImageView.setImageURI(Uri.parse(contact.getPhotoUri()));
					if (msgImageView.getDrawable() == null)
						msgImageView.setImageResource(R.drawable.hg_contact);
				} else
					msgImageView.setImageResource(R.drawable.hg_contact);
			} else
				msgImageView.setImageResource(R.drawable.hg_new_contact);

			msgBodyTextView
					.setBackgroundResource(R.drawable.speech_bubble_left);

		} else {
			msgImageView.setImageResource(R.drawable.me_icon);
			msgBodyTextView
					.setBackgroundResource(R.drawable.speech_bubble_right);

		}

		// Mark all messages as read since activity will open
		if (!message.isRead()) {
			message.setRead(true);
			MessageContainer[] msgArr = { message };
			UpdateMessageDbTask updateThread = new UpdateMessageDbTask();
			updateThread.execute(msgArr);
		}

		final MessageContainer finalMessage = message;
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Intent viewMsgIntent = new Intent(context,
							ViewMessageActivity.class);

					// store message details in
					viewMsgIntent.putExtra(
							SmsMessageHandler.COL_NAME_PHONE_NUMBER,
							finalMessage.getPhoneNumber());
					viewMsgIntent.putExtra(
							SmsMessageHandler.COL_NAME_CONTACT_ID,
							finalMessage.getContactId());
					viewMsgIntent.putExtra("contactName",
							finalMessage.getPhoneNumber());
					viewMsgIntent.putExtra("timeAndDate",
							finalMessage.getDate());
					viewMsgIntent.putExtra("msgBody", finalMessage.getBody());
					viewMsgIntent.putExtra("msgType", finalMessage.getType());
					viewMsgIntent.putExtra("msgID", finalMessage.getId());
					viewMsgIntent.putExtra("contactURI", contactUri);

					context.startActivity(viewMsgIntent);

				} catch (Exception x) {
					finalMessage.setBody(x.getMessage());
				}

			}
		});
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);

				// If contact is invalid, add option to create contact for phone
				// number
				final CharSequence[] items;
				if (finalMessage.getContactId() != -1
						|| finalMessage.getType() == SmsMessageHandler.MSG_TYPE_OUT)
					items = new CharSequence[] { "Delete", "Cancel" };
				else
					items = new CharSequence[] { "Add to contacts", "Delete",
							"Cancel" };

				alertDialogBuilder.setItems(items,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									if (finalMessage.getContactId() != -1
											|| finalMessage.getType() == SmsMessageHandler.MSG_TYPE_OUT) {
										MessageContainer[] msgArr = { finalMessage };
										DeleteMessageFromDbTask deleteThread = new DeleteMessageFromDbTask();
										deleteThread.execute(msgArr);
									} else {
										Intent intent = new Intent(
												Intent.ACTION_INSERT);
										intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
										intent.putExtra(
												ContactsContract.Intents.Insert.PHONE,
												finalMessage.getPhoneNumber());
										context.startActivity(intent);
									}
									break;
								case 1:
									if (finalMessage.getContactId() != -1
											|| finalMessage.getType() == SmsMessageHandler.MSG_TYPE_OUT) {
										dialog.cancel();
									} else {
										MessageContainer[] msgArr = { finalMessage };
										DeleteMessageFromDbTask deleteThread = new DeleteMessageFromDbTask();
										deleteThread.execute(msgArr);
									}
									break;
								case 2:
									dialog.cancel();
									break;
								}
							}
						});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				return true;
			}
		});
		return convertView;
	}

	public void setItems(ArrayList<MessageContainer> items) {
		this.objects = items;
	}

	public ArrayList<MessageContainer> getItems() {
		return this.objects;
	}

	public int getCount() {
		return objects.size();
	}

	public MessageContainer getItem(int position) {
		if (position > 0 && position < objects.size())
			return objects.get(position);
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	private SmsMessageHandler getSmsMessageHandler() {
		if (smsMessageHandler == null)
			smsMessageHandler = new SmsMessageHandler(context);
		return smsMessageHandler;
	}

}
