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
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.activity.ViewMessageActivity;
import edu.utsa.cs.smsmessenger.model.ContactContainer;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.AppConstants;
import edu.utsa.cs.smsmessenger.util.ContactsUtil;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;

/**
 * This class is used to adapter and fill a ListView with an ArrayList of
 * MessageContainer objects used in the search message list
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class MessageSearchContainerAdapter extends
		ArrayAdapter<MessageContainer> {

	private Context context;
	private ArrayList<MessageContainer> objects;
	private SimpleDateFormat sdf;
	private Animation clickAnimation;
	private Animation deleteAnimation;


	private class DeleteMessageFromDbTask extends
			AsyncTask<MessageContainer, Void, Void> {
		@Override
		protected Void doInBackground(MessageContainer... objects) {
			if (context != null) {
				for (MessageContainer msg : objects) {
					getSmsMessageHandler().deleteMessage(msg);
				}
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

	public MessageSearchContainerAdapter(Context context,
			int textViewResourceId, ArrayList<MessageContainer> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
		this.sdf = new SimpleDateFormat(context.getResources().getString(
				R.string.date_time_format), context.getResources()
				.getConfiguration().locale);

		clickAnimation = AnimationUtils.loadAnimation(context,
				R.anim.click_animation);
		deleteAnimation = AnimationUtils.loadAnimation(context,
				R.anim.delete_animation);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MessageContainer message = objects.get(position);

		// Log.d("MessageContainerAdapter", "getView() messageType: " +
		// message.getType());
		int layoutResourceId = message.getType().equals(
				SmsMessageHandler.MSG_TYPE_IN) ? R.layout.from_message_search_item
				: R.layout.to_message_search_item;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(layoutResourceId, parent, false);
		ImageView msgImageView = (ImageView) convertView
				.findViewById(R.id.msgImageView);
		TextView msgSenderTextView = (TextView) convertView
				.findViewById(R.id.msgSenderTextView);
		TextView msgBodyTextView = (TextView) convertView
				.findViewById(R.id.msgBodyTextView);
		TextView msgDateTextView = (TextView) convertView
				.findViewById(R.id.msgDateTextView);

		ContactContainer contact = new ContactContainer();
		contact = ContactsUtil.getContactByPhoneNumber(
				context.getContentResolver(), message.getPhoneNumber());

		msgSenderTextView.setText(message.getType().equals(
				SmsMessageHandler.MSG_TYPE_OUT) ? context.getResources()
				.getString(R.string.self_reference)
				: contact.getDisplayName() != null ? contact.getDisplayName()
						: message.getPhoneNumber());
		msgBodyTextView.setText(message.getBody());
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(message.getDate());
		msgDateTextView.setText(sdf.format(cal.getTime()));

		if (message.getType() == SmsMessageHandler.MSG_TYPE_IN) {
			if (contact.getDisplayName() != null) {
				if (contact.getPhotoUri() != null) {
					msgImageView.setImageURI(Uri.parse(contact.getPhotoUri()));
					if (msgImageView.getDrawable() == null)
						msgImageView.setImageResource(R.drawable.hg_contact);
				} else
					msgImageView.setImageResource(R.drawable.hg_contact);
			} else
				msgImageView.setImageResource(R.drawable.hg_new_contact);
		} else
			msgImageView.setImageResource(R.drawable.me_icon);

		final MessageContainer finalMessage = message;
		final View finalConvertView = convertView;
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(AppConstants.VIBRATION_SHORT_PULSE);
				
				Handler handler = new Handler();
				finalConvertView.startAnimation(clickAnimation);
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
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
						viewMsgIntent.putExtra("msgBody",
								finalMessage.getBody());
						viewMsgIntent.putExtra("msgType",
								finalMessage.getType());
						viewMsgIntent.putExtra("msgID", finalMessage.getId());

						context.startActivity(viewMsgIntent);
					}

				}, AppConstants.DELAY_FOR_CLICK_ANIMATION);
			}
		});
		final ContactContainer finalContact = contact;
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {

				Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(AppConstants.VIBRATION_SHORT_PULSE);
				
				Handler handler = new Handler();
				finalConvertView.startAnimation(clickAnimation);
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						final CharSequence[] items = {
								String.format(context
										.getResources()
										.getString(
												R.string.action_call,
												finalContact.getDisplayName() != null ? finalContact
														.getDisplayName()
														: finalContact
																.getPhoneNumber())),
								context.getResources().getString(
										R.string.action_delete_message),
								context.getResources().getString(
										R.string.decline_desicion) };
						alertDialogBuilder.setItems(items,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										switch (which) {
										case 0:
											Intent callIntent = new Intent(
													Intent.ACTION_DIAL,
													Uri.parse("tel:"
															+ finalContact
																	.getPhoneNumber()));
											context.startActivity(callIntent);
											break;
										case 1:

											finalConvertView.startAnimation(deleteAnimation);
											Handler dhandler = new Handler();
											
											dhandler.postDelayed(
												new Runnable(){

													@Override
													public void run() {	
														finalConvertView.setVisibility(View.INVISIBLE);
														MessageContainer[] msgArr = { finalMessage };
														DeleteMessageFromDbTask deleteThread = new DeleteMessageFromDbTask();
														deleteThread.execute(msgArr);						
													}
													
												}
												, AppConstants.DELAY_FOR_DELETE_ANIMATION);
											break;
										case 2:
											dialog.cancel();
											break;
										}
									}
								});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}

				}, AppConstants.DELAY_FOR_CLICK_ANIMATION);

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
		return SmsMessageHandler.getInstance(context);
	}
}
