package edu.utsa.cs.messenger.activity;

import edu.utsa.cs.messenger.R;
import edu.utsa.cs.messenger.adapter.ConversationListItemAdapter;
import edu.utsa.cs.messenger.util.ConversationListItem;
import edu.utsa.cs.messenger.util.MessageContainer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConversationsListActivity extends Activity {
	
	public static final int NEW_MESSAGE_REQUEST_CODE = 1;
	public static final String NEW_MESSAGE_RECEVIED = "edu.utsa.cs.messenger.NEW_MESSAGE_RECEVIED";
	private ListView conversationListView;
	private HashMap<String, ArrayList<MessageContainer>> messageMap;
	private ArrayList<ConversationListItem> conversationArrayList;
	private ConversationListItemAdapter conversationArrayListAdapter;
	
	private BroadcastReceiver newMsgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equalsIgnoreCase(NEW_MESSAGE_RECEVIED)){  
            	// TODO - probably a bad idea to fetch all message again.
            	// perhaps we should just update the messageMap and conversation list view.
            	fetchAndStoreSMSMessages();
            	generateConversationList();
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversations);

		registerNewMsgReceiver();
		
		conversationListView = (ListView)findViewById(R.id.conversationsListView);
		fetchAndStoreSMSMessages();
    	generateConversationList();
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversation_list_menu, menu);
		
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Toast msg;
		
		switch(item.getItemId())
		{
			case R.id.action_new_message:
				msg = Toast.makeText(this, "New Message...", Toast.LENGTH_LONG);
				msg.show();

				Intent intent = new Intent(this, NewMessageActivity.class);
				startActivityForResult(intent, NEW_MESSAGE_REQUEST_CODE);
				
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void registerNewMsgReceiver()
	{
        IntentFilter filter = new IntentFilter();
        filter.addAction(NEW_MESSAGE_RECEVIED);
        registerReceiver(newMsgReceiver, filter);
	}
	public void fetchAndStoreSMSMessages()
	{
		messageMap = new HashMap<String, ArrayList<MessageContainer>>();
		//Cursor cursor = getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
		
		
		
		cursor.moveToFirst();
		final Calendar cal = Calendar.getInstance();
		do{
			try{
				for(int x = 0; x < cursor.getColumnCount(); x++)
				{
					Log.d("Test", cursor.getColumnName(x) );
				}
				int threadId = cursor.getInt(cursor.getColumnIndex((MessageContainer.MSG_DB_COL_THREAD_ID)));
				String address = cursor.getString(cursor.getColumnIndex((MessageContainer.MSG_DB_COL_ADDRESS)));
				int person = cursor.getInt(cursor.getColumnIndex((MessageContainer.MSG_DB_COL_PERSON)));
				cal.setTimeInMillis(cursor.getLong(cursor.getColumnIndex((MessageContainer.MSG_DB_COL_DATE))));
				Date date = cal.getTime();
				cal.setTimeInMillis(cursor.getLong(cursor.getColumnIndex((MessageContainer.MSG_DB_COL_DATE_SENT))));
				Date dateSent = cal.getTime();
				String subject = cursor.getString(cursor.getColumnIndex(MessageContainer.MSG_DB_COL_SUBJECT));
				String body = cursor.getString(cursor.getColumnIndex(MessageContainer.MSG_DB_COL_BODY));
				boolean read = cursor.getInt(cursor.getColumnIndex(MessageContainer.MSG_DB_COL_READ)) == 0 ? false : true;
				
				MessageContainer newMsg = new MessageContainer(threadId, address, person, date, dateSent, subject, body, read);
				
				//Store items by address (phone number)
				if(messageMap.containsKey(address)) { 
					ArrayList<MessageContainer> list = messageMap.get(address);
					list.add(newMsg);
				}
				else
				{
					ArrayList<MessageContainer> list = new ArrayList<MessageContainer>();
					list.add(newMsg);
					messageMap.put(address, list);
				}
				//TODO - lookup contact information based on number.
				
				String logString = "Message from " + address + ": " + body;
				Log.i("ConversationsListActivity", logString);
				//msg = Toast.makeText(this, logString, Toast.LENGTH_LONG);
				//msg.show();
			}
			catch(Exception e){
				Log.d("ConversationsListActivity", "Exception occurred in fetchAndStoreSMSMessages(): " + e);
			}
		}while(cursor.moveToNext());
		
		
	}
	public void generateConversationList()
	{
		Toast msg = Toast.makeText(this, "generateConversationList()", Toast.LENGTH_LONG);
		msg.show();
		
		conversationArrayList = new ArrayList<ConversationListItem> ();

		System.out.println(conversationArrayList);
		System.out.println(messageMap);
		Log.d("ConversationsListActivity", "conversationArrayList size: " + (conversationArrayList.size()));
		Log.d("ConversationsListActivity", "messageMap size: " + (messageMap.size()));
		
		int count = 0;
		for (Map.Entry<String, ArrayList<MessageContainer>> entry : messageMap.entrySet()) {
			ArrayList<MessageContainer> list = entry.getValue();
			
			//I am making the assumption that the first message is the most recent
			MessageContainer recentMsg = list.get(0);
			Log.d("ConversationsListActivity", "count: " + (count++));
			conversationArrayList.add(new ConversationListItem(recentMsg.getAddress(), recentMsg.getBody(), null, recentMsg.getDate(), recentMsg.isRead()));
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
		Log.d("ConversationsListActivity", "conversationArrayList size: " + (conversationArrayList.size()));
		Log.d("ConversationsListActivity", "messageMap size: " + (messageMap.size()));
		if(conversationArrayListAdapter == null)
		{
			msg = Toast.makeText(this, "conversationArrayListAdapter == null", Toast.LENGTH_LONG);
			msg.show();
			conversationArrayListAdapter = new ConversationListItemAdapter(this, conversationArrayList, R.layout.conversation_list_item, 
					R.id.contactImageView, R.id.contactNameTextView, R.id.conversationPreviewTextView);
			conversationListView.setAdapter(conversationArrayListAdapter);
		}
		else
		{
			msg = Toast.makeText(this, "conversationArrayListAdapter != null", Toast.LENGTH_LONG);
			msg.show();
			Log.d("ConversationsListActivity", "conversationArrayList size: " + (conversationArrayList.size()));
			conversationArrayListAdapter.clear();
			conversationArrayListAdapter.addAll(conversationArrayList);
			conversationArrayListAdapter.notifyDataSetInvalidated();
		}
	}
	public void simulateConversationList()
	{
/*		ConversationListItem[] items = new ConversationListItem[10];
		items[0] = new ConversationListItem("Bob", "hello", "");
		items[1] = new ConversationListItem("Tom", "Your order is complete", "");
		items[2] = new ConversationListItem("Frank Doe", "BBQ this weekend!", "");
		items[3] = new ConversationListItem("John Doe", "Your payment has been received.", "");
		items[4] = new ConversationListItem("Sally", "hi :)", "");
		items[5] = new ConversationListItem("Bob", "hello", "");
		items[6] = new ConversationListItem("Tom", "Your order is complete", "");
		items[7] = new ConversationListItem("Frank Doe", "BBQ this weekend!", "");
		items[8] = new ConversationListItem("John Doe", "Your payment has been received.", "");
		items[9] = new ConversationListItem("Sally", "hi :)", "");


		ConversationListItemAdapter adapter = new ConversationListItemAdapter(this, R.layout.conversation_list_item, items);
		
		conversationListView.setAdapter(adapter);*/
	}

}
