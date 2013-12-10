package edu.utsa.cs.smsmessenger.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.adapter.MessageSearchContainerAdapter;
import edu.utsa.cs.smsmessenger.adapter.ScheduledMessageContainerAdapter;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;

/**
 * This class is the Activity that shows a preview list of all scheduled
 * messages
 * 
 * @author Kendall Bailey
 * @version 1.1
 * @since 1.1
 * 
 */
public class ScheduledMessageList extends Activity implements
		SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	private ListView scheduledMessageListView;
	private ScheduledMessageContainerAdapter scheduledMessagePreviewAdapter;
	private SmsMessageHandler smsMessageHandler;

	private BroadcastReceiver updateMsgReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equalsIgnoreCase(SmsMessageHandler.UPDATE_MSG_INTENT)) {
				fillScheduledMessageList();
			}
			Log.d("ConversationsListActivity - BroadcastReceiver",
					"New intent received.");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduled_message_list);
		registerUpdateMsgReceiver();
		fillScheduledMessageList();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(updateMsgReceiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerUpdateMsgReceiver();
		fillScheduledMessageList();
	}

	public void registerUpdateMsgReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(SmsMessageHandler.UPDATE_MSG_INTENT);
		registerReceiver(updateMsgReceiver, filter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scheduled_message_list, menu);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(
				R.id.action_message_search).getActionView();
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);

		searchView.setOnQueryTextListener(this);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_new_message:
			Intent newConversationintent = new Intent(this,
					NewConversationActivity.class);
			startActivity(newConversationintent);

			break;
		case R.id.action_settings:
			Intent settingsIntent = new Intent(this, AppSettingsActivity.class);
			startActivity(settingsIntent);
			break;
		case R.id.action_conversation_list:
			Intent conversationIntent = new Intent(this,
					ConversationsListActivity.class);
			conversationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(conversationIntent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void fillScheduledMessageList() {

		String sortOrder = SmsMessageHandler.COL_NAME_DATE + " ASC";

		ArrayList<MessageContainer> messageList = getSmsMessageHandler()
				.getSmsMessages(null, null, sortOrder,
						SmsMessageHandler.MSG_TYPE_SCHEDULED);

		Log.d("ScheduledMessageList", "Size of scheduled messages: "
				+ messageList.size());

		if (messageList.size() > 0) {
			scheduledMessagePreviewAdapter = new ScheduledMessageContainerAdapter(
					this, R.layout.to_message_search_item, messageList);

			getScehduledMessageListView().setAdapter(
					scheduledMessagePreviewAdapter);
		} else {
			if (getScehduledMessageListView().getAdapter() != null) {
				ScheduledMessageContainerAdapter emptyAdapter = new ScheduledMessageContainerAdapter(
						this, R.layout.to_message_search_item,
						new ArrayList<MessageContainer>());
				getScehduledMessageListView().setAdapter(emptyAdapter);
			}
		}

	}

	private SmsMessageHandler getSmsMessageHandler() {
		if (smsMessageHandler == null)
			smsMessageHandler = new SmsMessageHandler(this);
		return smsMessageHandler;
	}

	public ListView getScehduledMessageListView() {
		if (scheduledMessageListView == null)
			scheduledMessageListView = (ListView) findViewById(R.id.scheduledMessageListView);
		return scheduledMessageListView;
	}

	private void searchForMessage(String query) {
		ArrayList<MessageContainer> msgList = getSmsMessageHandler()
				.queryMessages(query);
		getSmsMessageHandler().close();

		MessageSearchContainerAdapter messageSearchContainerAdapter = new MessageSearchContainerAdapter(
				this, R.layout.conversation_from_message_item, msgList);

		getScehduledMessageListView().setAdapter(messageSearchContainerAdapter);
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (newText != null && newText.length() > 0)
			searchForMessage(newText);
		else
			fillScheduledMessageList();
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onClose() {
		fillScheduledMessageList();
		return false;
	}
}
