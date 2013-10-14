package edu.utsa.cs.smsmessenger.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.adapter.ConversationPreviewAdapter;
import edu.utsa.cs.smsmessenger.model.ConversationPreview;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This class is the Activity that shows a preview list of all conversations
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class ConversationsListActivity extends Activity {

	private ListView conversationsListView;
	private ConversationPreviewAdapter conversationPreviewAdapter;
	private SmsMessageHandler smsMessageHandler;

	private BroadcastReceiver newMsgReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equalsIgnoreCase(SmsMessageHandler.UPDATE_MSG_INTENT)) {
				fillConversationsList();
			}
			Log.d("ConversationsListActivity - BroadcastReceiver",
					"New intent received.");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversations_list);
		registerNewMsgReceiver();
		fillConversationsList();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(newMsgReceiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		registerNewMsgReceiver();
		super.onResume();
		fillConversationsList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversations_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Toast msg;

		switch (item.getItemId()) {
		case R.id.action_new_message:
			msg = Toast.makeText(this, "New Message...", Toast.LENGTH_LONG);
			msg.show();

			Intent intent = new Intent(this, NewConversationActivity.class);
			startActivity(intent);

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void fillConversationsList() {
		Log.d("ConversationsListActivity", "fillConversation()");
		HashMap<String, ConversationPreview> convPrevMap = getSmsMessageHandler()
				.getConversationPreviewItmes();
		Log.d("ConversationsListActivity", "convPrevMap: " + convPrevMap);

		ArrayList<ConversationPreview> convPrevArrayList = new ArrayList<ConversationPreview>();

		for (Map.Entry<String, ConversationPreview> entry : convPrevMap
				.entrySet()) {
			Log.d("ConversationsListActivity", "Iterate Map");
			convPrevArrayList.add(entry.getValue());
		}
		if (convPrevArrayList.size() > 0) {
			conversationPreviewAdapter = new ConversationPreviewAdapter(this,
					R.layout.conversations_list_item, convPrevArrayList);
			Log.d("ConversationsListActivity", "conversationPreviewAdapter: "
					+ conversationPreviewAdapter);
			Log.d("ConversationsListActivity", "conversationListView: "
					+ getConversationListView());
			getConversationListView().setAdapter(conversationPreviewAdapter);
		} else {
			if (getConversationListView().getAdapter() != null) {
				ConversationPreviewAdapter emptyAdapter = new ConversationPreviewAdapter(
						this, R.layout.conversations_list_item,
						new ArrayList<ConversationPreview>());
				getConversationListView().setAdapter(emptyAdapter);
			}
		}
	}

	private SmsMessageHandler getSmsMessageHandler() {
		if (smsMessageHandler == null)
			smsMessageHandler = new SmsMessageHandler(this);
		return smsMessageHandler;
	}

	public void registerNewMsgReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(SmsMessageHandler.UPDATE_MSG_INTENT);
		registerReceiver(newMsgReceiver, filter);
	}

	public ListView getConversationListView() {
		if (conversationsListView == null)
			conversationsListView = (ListView) findViewById(R.id.conversationsListView);
		return conversationsListView;
	}
}
