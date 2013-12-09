package edu.utsa.cs.smsmessenger.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.adapter.ConversationPreviewAdapter;
import edu.utsa.cs.smsmessenger.adapter.MessageContainerAdapter;
import edu.utsa.cs.smsmessenger.adapter.MessageSearchContainerAdapter;
import edu.utsa.cs.smsmessenger.model.ContactContainer;
import edu.utsa.cs.smsmessenger.model.ConversationPreview;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.ContactsUtil;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;

public class ScheduledMessageList extends Activity implements
SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	private ListView scheduledMessageListView;
	private ConversationPreviewAdapter scheduledMessagePreviewAdapter;
	private SmsMessageHandler smsMessageHandler;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduled_message_list);
		fillScheduledMessageList();
	}


	@Override
	protected void onResume() {
		super.onResume();
		fillScheduledMessageList();
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
			Intent conversationIntent = new Intent(this, ConversationsListActivity.class);
			conversationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(conversationIntent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	//
	// This method fetches closes all notifications for this app
	//
//	private void closeExistingNotifications() {
//		NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		notificationmanager.cancelAll();
//	}
	
	//
	// This method fetches the latest conversation preview lists from the app
	// message database.
	//
	private void fillScheduledMessageList() {

		
		HashMap<String, ConversationPreview> convPrevMap = getSmsMessageHandler()
				.getConversationPreviewItmes(this);

		ArrayList<ConversationPreview> convPrevArrayList = new ArrayList<ConversationPreview>();

		for (Map.Entry<String, ConversationPreview> entry : convPrevMap
				.entrySet()) 
			convPrevArrayList.add(entry.getValue());

		//Sort array list
		Collections.sort(convPrevArrayList, Collections.reverseOrder());
		
		if (convPrevArrayList.size() > 0) {
			scheduledMessagePreviewAdapter = new ConversationPreviewAdapter(this,
					R.layout.conversations_list_item, convPrevArrayList);
			getScehduledMessageListView().setAdapter(scheduledMessagePreviewAdapter);
		} else {
			if (getScehduledMessageListView().getAdapter() != null) {
				ConversationPreviewAdapter emptyAdapter = new ConversationPreviewAdapter(
						this, R.layout.conversations_list_item,
						new ArrayList<ConversationPreview>());
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
