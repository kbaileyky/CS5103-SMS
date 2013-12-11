package edu.utsa.cs.smsmessenger.adapter;

import java.util.ArrayList;
import java.util.List;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.activity.ConversationActivity;
import edu.utsa.cs.smsmessenger.model.ContactContainer;
import edu.utsa.cs.smsmessenger.model.ConversationPreview;
import edu.utsa.cs.smsmessenger.util.AppConstants;
import edu.utsa.cs.smsmessenger.util.ContactsUtil;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class is used to adapter and fill a ListView with an ArrayList of
 * ConversationPreview objects
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class ConversationPreviewAdapter extends
		ArrayAdapter<ConversationPreview> {

	private Context context;
	private int layoutResourceId;
	private ArrayList<ConversationPreview> objects;
	private Animation clickAnimation;
	private Animation deleteAnimation;

	public ConversationPreviewAdapter(Context context, int layoutResourceId,
			List<ConversationPreview> objects) {
		super(context, layoutResourceId, objects);

		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.objects = new ArrayList<ConversationPreview>();
		this.objects.addAll(objects);

		clickAnimation = AnimationUtils.loadAnimation(context,
				R.anim.click_animation);
		deleteAnimation = AnimationUtils.loadAnimation(context,
				R.anim.delete_animation);
	}

	private class DeleteConversationFromDbTask extends
			AsyncTask<ConversationPreview, Void, Void> {
		@Override
		protected Void doInBackground(ConversationPreview... objects) {
			if (context != null) {
				for (ConversationPreview conv : objects) {
					getSmsMessageHandler().deleteConversation(
							conv.getPhoneNumber());
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);
		}

		// Get the need view items
		ImageView contactImageView = (ImageView) convertView
				.findViewById(R.id.contactImageView);
		TextView contactNameTextView = (TextView) convertView
				.findViewById(R.id.contactNameTextView);
		TextView previewTextView = (TextView) convertView
				.findViewById(R.id.conversationPreviewTextView);
		TextView countTextView = (TextView) convertView
				.findViewById(R.id.notReadCountTextView);

		// Get the ConversationPreview object
		ConversationPreview preview = objects.get(position);
		ContactContainer contact = ContactsUtil.getContactByPhoneNumber(
				context.getContentResolver(), preview.getPhoneNumber());

		// Set the view item values
		previewTextView.setText(preview.getPreviewText());
		countTextView.setText(String.valueOf(preview.getNotReadCount()));

		// if all messages in Conversation are read, hide the not read count
		// TextView
		if (preview.getNotReadCount() < 1)
			countTextView.setVisibility(View.INVISIBLE);

		if (contact.getDisplayName() != null) {
			contactNameTextView.setText(contact.getDisplayName());
			if (contact.getPhotoUri() != null) {
				contactImageView.setImageURI(Uri.parse(contact.getPhotoUri()));
				if (contactImageView.getDrawable() == null)
					contactImageView.setImageResource(R.drawable.hg_contact);
			} else
				contactImageView.setImageResource(R.drawable.hg_contact);
		} else {
			contactNameTextView.setText(contact.getPhoneNumber());
			contactImageView.setImageResource(R.drawable.hg_new_contact);
		}

		final ConversationPreview finalPreview = preview;
		final ContactContainer finalContact = contact;
		final TextView finalCountTextView = countTextView;
		final View finalConvertView = convertView;

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(AppConstants.VIBRATION_SHORT_PULSE);
				
				Handler handler = new Handler();

				finalConvertView.startAnimation(clickAnimation);
				handler.postDelayed(
					new Runnable(){

						@Override
						public void run() {		

							finalCountTextView.setVisibility(View.INVISIBLE);
							Intent intent = new Intent(context, ConversationActivity.class);
							intent.putExtra(SmsMessageHandler.COL_NAME_PHONE_NUMBER,
									finalPreview.getPhoneNumber());
							intent.putExtra(SmsMessageHandler.COL_NAME_CONTACT_ID,
									finalPreview.getContactId());
							context.startActivity(intent);
						}
						
					}
					, AppConstants.DELAY_FOR_CLICK_ANIMATION);
			}
		});
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

						// If contact is invalid, add option to create contact
						// for phone
						// number
						final CharSequence[] items;
						if (finalContact.getDisplayName() != null)
							items = new CharSequence[] {
									String.format(context.getResources()
											.getString(
													R.string.action_call,
													finalContact
															.getDisplayName())),
									context.getResources()
											.getString(
													R.string.action_delete_conversation),
									context.getResources().getString(
											R.string.decline_desicion) };
						else
							items = new CharSequence[] {
									String.format(context.getResources()
											.getString(
													R.string.action_call,
													finalContact
															.getPhoneNumber())),
									context.getResources().getString(
											R.string.action_add_to_contacts),
									context.getResources()
											.getString(
													R.string.action_delete_conversation),
									context.getResources().getString(
											R.string.decline_desicion) };

						// Create alert dialog options to Add phone number to
						// contacts,
						// Delete conversation, or Cancel
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
											if (finalContact.getDisplayName() != null) {

												Handler dHandler = new Handler();

												finalConvertView.startAnimation(deleteAnimation);
												dHandler.postDelayed(
													new Runnable(){

														@Override
														public void run() {	
															finalConvertView.setVisibility(View.INVISIBLE);
															ConversationPreview[] convArr = { finalPreview };
															DeleteConversationFromDbTask deleteThread = new DeleteConversationFromDbTask();
															deleteThread.execute(convArr);				
														}
														
													}
													, AppConstants.DELAY_FOR_DELETE_ANIMATION);
											} else {
												Intent intent = new Intent(
														Intent.ACTION_INSERT);
												intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
												intent.putExtra(
														ContactsContract.Intents.Insert.PHONE,
														finalPreview
																.getPhoneNumber());
												context.startActivity(intent);
											}
											break;
										case 2:
											if (finalContact.getDisplayName() != null) {
												dialog.cancel();
											} else {

												Handler dHandler = new Handler();

												finalConvertView.startAnimation(deleteAnimation);
												dHandler.postDelayed(
													new Runnable(){

														@Override
														public void run() {	
															finalConvertView.setVisibility(View.INVISIBLE);
															ConversationPreview[] convArr = { finalPreview };
															DeleteConversationFromDbTask deleteThread = new DeleteConversationFromDbTask();
															deleteThread.execute(convArr);				
														}
														
													}
													, AppConstants.DELAY_FOR_DELETE_ANIMATION);
											}
											break;
										case 3:
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

	public void setItems(ArrayList<ConversationPreview> items) {
		this.objects = items;
	}

	public ArrayList<ConversationPreview> getItems() {
		return this.objects;
	}

	public int getCount() {
		return objects.size();
	}

	public ConversationPreview getItem(int position) {
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
