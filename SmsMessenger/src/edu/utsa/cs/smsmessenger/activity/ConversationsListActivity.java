package edu.utsa.cs.smsmessenger.activity;

import edu.utsa.cs.smsmessenger.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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
		getMenuInflater().inflate(R.menu.conversations, menu);
		return true;
	}

}
