package edu.utsa.cs.smsmessenger.activity;

import edu.utsa.cs.smsmessenger.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/** ConversationsListActivity is the Activity that shows a preview list of all conversations
 * 
 * @author mmadrigal
 *
 */
public class ConversationsListActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversations_list);
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
		
		switch(item.getItemId())
		{
			case R.id.action_new_message:
				msg = Toast.makeText(this, "New Message...", Toast.LENGTH_LONG);
				msg.show();

				Intent intent = new Intent(this, NewConversationActivity.class);
				startActivity(intent);
				
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
