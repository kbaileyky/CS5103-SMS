package edu.utsa.cs.smsmessenger.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewMessageActivity extends Activity{

	private MessageContainer currentMessage;
	private SmsMessageHandler smsMessageHandler;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation_to_message_item);

		Bundle extras = getIntent().getExtras();
	
		currentMessage= new MessageContainer();
		
		currentMessage.setBody(extras.getString("msgBody"));
		currentMessage.setDate(extras.getLong("timeAndDate"));
		currentMessage.setContactId(extras.getInt("contactName"));
		
		updateUI();
		
	//	ImageButton newRecipientButton = (ImageButton) findViewById(R.id.newMsgAddRecipientImageButton);
	//	ImageButton sendNewMessageButton = (ImageButton) findViewById(R.id.newMsgSendImageButton);
		return;
	}

	private void updateUI(){
		
		//Temporary - replace with contact name
		//TODO Get contact
		setTitle(Integer.toString(currentMessage.getContactId()));
		
		TextView txtMsgBody = (TextView)findViewById(R.id.msgBodyTextView);
		txtMsgBody.setText(currentMessage.getBody());
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		TextView txtTimeAndDate = (TextView)findViewById(R.id.msgDateTextView);
		txtTimeAndDate.setText(sdf.format(currentMessage.getDate()));
		
		return;
	}

}
