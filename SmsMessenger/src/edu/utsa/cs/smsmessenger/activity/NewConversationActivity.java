package edu.utsa.cs.smsmessenger.activity;

import edu.utsa.cs.smsmessenger.R;
import android.app.Activity;
import android.os.Bundle;

/** NewConversationActivity is the Activity that allows the users to start a new conversations
 * 
 * @author mmadrigal
 *
 */
public class NewConversationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_conversation);
	}
}
