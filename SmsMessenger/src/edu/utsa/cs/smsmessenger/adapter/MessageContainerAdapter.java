package edu.utsa.cs.smsmessenger.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.activity.ConversationActivity;
import edu.utsa.cs.smsmessenger.activity.NewConversationActivity;
import edu.utsa.cs.smsmessenger.activity.ViewMessageActivity;
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

	public MessageContainerAdapter(Context context, int textViewResourceId,
			ArrayList<MessageContainer> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
		this.sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MessageContainer message = objects.get(position);

		// Log.d("MessageContainerAdapter", "getView() messageType: " +
		// message.getType());
		int layoutResourceId = message.getType().equals(
				SmsMessageHandler.MSG_TYPE_IN) ? R.layout.conversation_from_message_item
				: R.layout.conversation_to_message_item;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		
		if(message.getType().equals(SmsMessageHandler.MSG_TYPE_IN)){
			//convertView.setBackgroundColor(Color.parseColor("#C6E093"));	
			convertView.setBackgroundColor( context.getResources().getColor(R.color.RowColor1));	
		} else {
			//convertView.setBackgroundColor(Color.parseColor("#ABDA4E"));	
			convertView.setBackgroundColor(context.getResources().getColor(R.color.RowColor2));	
		}

		// TODO - mark view to indicate if message has not been read

		final MessageContainer finalMessage = message;
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO launch Message Activitys
				try {
					
					Intent viewMsgIntent = new Intent(context, ViewMessageActivity.class);
					
					//store message details in 
					viewMsgIntent.putExtra(SmsMessageHandler.COL_NAME_PHONE_NUMBER,
							finalMessage.getPhoneNumber());
					viewMsgIntent.putExtra(SmsMessageHandler.COL_NAME_CONTACT_ID,
							finalMessage.getContactId());
					viewMsgIntent.putExtra("contactName", finalMessage.getPhoneNumber());
					viewMsgIntent.putExtra("timeAndDate", finalMessage.getDate());
					viewMsgIntent.putExtra("msgBody", finalMessage.getBody());
					viewMsgIntent.putExtra("msgType", finalMessage.getType());
					
					context.startActivity(viewMsgIntent);
				
				} catch(Exception x){
					finalMessage.setBody(x.getMessage());
				}
				
			}
		});
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
				final CharSequence[] items = { "Delete", "Cancel" };
				alertDialogBuilder.setItems(items,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									MessageContainer[] msgArr = { finalMessage };
									DeleteMessageFromDbTask deleteThread = new DeleteMessageFromDbTask();
									deleteThread.execute(msgArr);
									break;
								case 1:
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
