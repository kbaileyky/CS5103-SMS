package edu.utsa.cs.smsmessenger.activity;

import java.util.ArrayList;
import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.adapter.MessageSearchContainerAdapter;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * This class is the Activity that shows search results for messages.
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class SearchActivity extends ListActivity {

	private SmsMessageHandler smsMessageHandler;
	private MessageSearchContainerAdapter messageSearchContainerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_search_list);

		Log.d("SearchActivity", "onCreate");
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.d("SearchActivity", "onNewIntent");
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		Log.d("SearchActivity", "handleIntent");
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			searchForMessage(query);
		}
	}

	private void searchForMessage(String query) {
		ArrayList<MessageContainer> msgList = getSmsMessageHandler()
				.queryMessages(query);
		getSmsMessageHandler().close();

		messageSearchContainerAdapter = new MessageSearchContainerAdapter(this,
				R.layout.conversation_from_message_item, msgList);

		this.getListView().setAdapter(messageSearchContainerAdapter);
		this.getListView().setSelection(
				messageSearchContainerAdapter.getCount() - 1);
	}

	private SmsMessageHandler getSmsMessageHandler() {
		if (smsMessageHandler == null)
			smsMessageHandler = new SmsMessageHandler(this);
		return smsMessageHandler;
	}
}
