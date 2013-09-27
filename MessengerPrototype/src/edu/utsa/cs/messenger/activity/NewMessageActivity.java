package edu.utsa.cs.messenger.activity;

import edu.utsa.cs.messenger.R;
import edu.utsa.cs.messenger.R.id;
import edu.utsa.cs.messenger.R.layout;
import edu.utsa.cs.messenger.R.menu;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewMessageActivity extends Activity {

	private ImageButton addRecipientBtn;
	private ImageButton sendMessageBtn;
	private ImageButton attachFileBtn;
	private AutoCompleteTextView recipientTextView;
	private EditText messageEditTextView;

	private OnClickListener addRecipientOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	private OnClickListener sendMessageOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String number = recipientTextView.getText().toString();
			String message = messageEditTextView.getText().toString();
			sendSMSMessage(number, message);
		}
	};
	private OnClickListener attachFileOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_new);
		
		addRecipientBtn = (ImageButton)findViewById(R.id.newMsgAddRecipientImageButton);
		sendMessageBtn = (ImageButton)findViewById(R.id.newMsgSendImageButton);
		attachFileBtn = (ImageButton)findViewById(R.id.newMsgAttachImageButton);
		recipientTextView = (AutoCompleteTextView)findViewById(R.id.newMsgRecipientAutoCompleteTextView);
		messageEditTextView = (EditText) findViewById(R.id.newMsgTextEditText);
		
		addRecipientBtn.setOnClickListener(addRecipientOnClickListener);
		sendMessageBtn.setOnClickListener(sendMessageOnClickListener);
		attachFileBtn.setOnClickListener(attachFileOnClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_message_menu, menu);
		
		return true;
	}
	
	public void sendSMSMessage(String phoneNumber, String message)
	{
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
 
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
        
        //TODO - add to database.
	}

}
