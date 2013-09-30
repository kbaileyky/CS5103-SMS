package edu.utsa.cs.smsmessenger.activity;

import edu.utsa.cs.smsmessenger.R;
import android.app.Activity;
import android.os.Bundle;

/** ConversationActivity is the Activity that shows all messages in a single conversation
 * 
 * @author mmadrigal
 *
 */
public class ConversationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);
	}
}
