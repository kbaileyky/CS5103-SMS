package edu.utsa.cs.smsmessenger.activity;

import java.text.SimpleDateFormat;

import edu.utsa.cs.smsmessenger.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import edu.utsa.cs.smsmessenger.adapter.MessageContainerAdapter;
import edu.utsa.cs.smsmessenger.model.ConversationPreview;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;

public class ViewMessageActivity extends Activity {

	private TableRow rootTable;
	private TextView txtMsgBody;
	private MessageContainer currentMessage;
	private SmsMessageHandler smsMessageHandler;
	private Context context;
	// Touch event related variables
	int touchState;
	final int IDLE = 0;
	final int TOUCH = 1;
	final int PINCH = 2;
	float dist0, distCurrent;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation_to_message_item);

		Bundle extras = getIntent().getExtras();

		currentMessage = new MessageContainer();

		currentMessage.setBody(extras.getString("msgBody"));
		currentMessage.setDate(extras.getLong("timeAndDate"));
		currentMessage.setContactId(extras
				.getInt(SmsMessageHandler.COL_NAME_CONTACT_ID));
		currentMessage.setPhoneNumber(extras
				.getString(SmsMessageHandler.COL_NAME_PHONE_NUMBER));
		currentMessage.setType(extras.getString("msgType"));

		updateUI();

		distCurrent = 1; // Dummy default distance
		dist0 = 1; // Dummy default distance

		rootTable.setOnTouchListener(MyOnTouchListener);

		touchState = IDLE;

		Log.d("ViewMessageActivity",
				"view Message Activity: " + currentMessage.getBody() + " from "
						+ currentMessage.getContactId());

		return;
	}

	private void updateUI() {

		// Temporary - replace with contact name
		setTitle(currentMessage.getPhoneNumber());

		txtMsgBody = (TextView) findViewById(R.id.msgBodyTextView);
		txtMsgBody.setText(currentMessage.getBody());

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		TextView txtTimeAndDate = (TextView) findViewById(R.id.msgDateTextView);
		txtTimeAndDate.setText(sdf.format(currentMessage.getDate()));

		rootTable = (TableRow) findViewById(R.id.tableRow1);
		// tableRow1

		// Find the root view
		View root = rootTable.getRootView();

		if (currentMessage.getType().equals(SmsMessageHandler.MSG_TYPE_IN)) {
			root.setBackgroundColor(Color.parseColor("#dddddd"));
		} else {
			root.setBackgroundColor(Color.parseColor("#019192"));
		}

		return;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_msg_menu_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Toast msg;

		switch (item.getItemId()) {
		case R.id.action_forward:
			msg = Toast.makeText(this, "Forward...", Toast.LENGTH_LONG);
			msg.show();

			Intent forwardIntent = new Intent(this,
					NewConversationActivity.class);

			forwardIntent.putExtra("fwdBody", currentMessage.getBody());

			startActivity(forwardIntent);

			break;
		case R.id.action_reply:
			msg = Toast.makeText(this, "Reply...", Toast.LENGTH_LONG);
			msg.show();

			Intent replyIntent = new Intent(this, NewConversationActivity.class);

			replyIntent.putExtra("replyContact",
					currentMessage.getPhoneNumber());
			startActivity(replyIntent);
			break;
		// case R.id.action_delete:
		// try{
		// AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		// this);
		// final CharSequence[] items = { "Delete", "Cancel" };
		// alertDialogBuilder.setItems(items,
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// switch (which) {
		// case 0:
		// MessageContainer[] msgArr = { currentMessage };
		// DeleteMessageFromDbTask deleteThread = new DeleteMessageFromDbTask();
		// deleteThread.execute(msgArr);
		// break;
		// case 1:
		// dialog.cancel();
		// break;
		// }
		// }
		// });
		// AlertDialog alertDialog = alertDialogBuilder.create();
		// alertDialog.show();
		// }catch(Exception x) {
		// Log.d("ViewMessageActivity", "Threw Error on delete:" +
		// x.getMessage());
		// }

		}

		return super.onOptionsItemSelected(item);
	}

	private void updateFontSize() {
		float newsize;
		float curScale = distCurrent / dist0;
		if (curScale < 0.1) {
			curScale = 0.1f;
		}

		newsize = (12 * curScale);

		if (newsize < 9) {
			newsize = 9;
		}

		if (newsize > 24) {
			newsize = 24;
		}

		txtMsgBody.setTextSize(newsize);
	}

	OnTouchListener MyOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			// TODO Auto-generated method stub

			float distx, disty;

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				// A pressed gesture has started, the motion contains the
				// initial starting location.
				touchState = TOUCH;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				// A non-primary pointer has gone down.
				touchState = PINCH;

				// Get the distance when the second pointer touch
				distx = event.getX(0) - event.getX(1);
				disty = event.getY(0) - event.getY(1);
				dist0 = FloatMath.sqrt(distx * distx + disty * disty);

				break;
			case MotionEvent.ACTION_MOVE:
				// A change has happened during a press gesture (between
				// ACTION_DOWN and ACTION_UP).

				if (touchState == PINCH) {
					// Get the current distance
					distx = event.getX(0) - event.getX(1);
					disty = event.getY(0) - event.getY(1);
					distCurrent = FloatMath.sqrt(distx * distx + disty * disty);

					updateFontSize();
				}

				break;
			case MotionEvent.ACTION_UP:
				// A pressed gesture has finished.

				touchState = IDLE;
				break;
			case MotionEvent.ACTION_POINTER_UP:
				// A non-primary pointer has gone up.
				touchState = TOUCH;
				break;
			}

			return true;
		}

	};

	private class DeleteMessageFromDbTask extends
			AsyncTask<MessageContainer, Void, Void> {
		@Override
		protected Void doInBackground(MessageContainer... objects) {
			if (context != null) {
				for (MessageContainer msg : objects) {
					smsMessageHandler.deleteMessage(msg);
				}
				smsMessageHandler.close();
			}
			return null;
		}
	}

}
