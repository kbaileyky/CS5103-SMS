package edu.utsa.cs.smsmessenger.adapter;

import java.util.ArrayList;
import java.util.List;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.activity.ConversationActivity;
import edu.utsa.cs.smsmessenger.model.ConversationPreview;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;

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
	private SmsMessageHandler smsMessageHandler;

	public ConversationPreviewAdapter(Context context, int layoutResourceId,
			List<ConversationPreview> objects) {
		super(context, layoutResourceId, objects);

		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.objects = new ArrayList<ConversationPreview>();
		this.objects.addAll(objects);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);
		}
		ImageView contactImageView = (ImageView) convertView
				.findViewById(R.id.contactImageView);
		TextView contactNameTextView = (TextView) convertView
				.findViewById(R.id.contactNameTextView);
		TextView previewTextView = (TextView) convertView
				.findViewById(R.id.conversationPreviewTextView);
		TextView countTextView = (TextView) convertView
				.findViewById(R.id.notReadCountTextView);

		ConversationPreview preview = objects.get(position);
		contactNameTextView.setText(preview.getContactName());
		
		previewTextView.setText(preview.getPreviewText());
		countTextView.setText(""+preview.getNotReadCount());

		if(preview.getNotReadCount()<1)
			countTextView.setVisibility(View.INVISIBLE);
		
		if(preview.getContactImgUri()!=null)
		{
			contactImageView.setImageURI(Uri.parse( preview.getContactImgUri() ));
		}
		else
		{
			contactImageView.setImageResource(R.drawable.hg_new_contact);
		}

		final ConversationPreview finalPreview = preview;
		final TextView finalCountTextView = countTextView;
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finalCountTextView.setVisibility(View.INVISIBLE);
				Intent intent = new Intent(context, ConversationActivity.class);
				intent.putExtra(SmsMessageHandler.COL_NAME_PHONE_NUMBER,
						finalPreview.getPhoneNumber());
				intent.putExtra(SmsMessageHandler.COL_NAME_CONTACT_ID,
						finalPreview.getContactId());
				context.startActivity(intent);
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
									ConversationPreview[] convArr = { finalPreview };
									DeleteConversationFromDbTask deleteThread = new DeleteConversationFromDbTask();
									deleteThread.execute(convArr);
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
		if (smsMessageHandler == null)
			smsMessageHandler = new SmsMessageHandler(context);
		return smsMessageHandler;
	}
}
