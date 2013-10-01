package edu.utsa.cs.smsmessenger.activity;

import java.util.Calendar;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/** NewConversationActivity is the Activity that allows the users to start a new conversations
 * 
 * @author mmadrigal
 *
 */
public class NewConversationActivity extends Activity {

    
	private AutoCompleteTextView newRecipientTextView;
	private EditText newMessageEditText;
	
	private OnClickListener addNewRecipientOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}};
	
	private OnClickListener sendNewMessageOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			//TODO - input should be a contact, and not limited to a number.
			//Also should try to resolve contact and show message if bad input
			String number = newRecipientTextView.getText().toString();
			String message = newMessageEditText.getText().toString();
			sendSmsMessage(number, message);
		}};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_conversation);
		
		newRecipientTextView = (AutoCompleteTextView)findViewById(R.id.newMsgTextEditText);
		newMessageEditText = (EditText)findViewById(R.id.newMsgRecipientAutoCompleteTextView);
		
		ImageButton newRecipientButton = (ImageButton)findViewById(R.id.newMsgAddRecipientImageButton);
		ImageButton sendNewMessageButton = (ImageButton)findViewById(R.id.newMsgSendImageButton);
		
		newRecipientButton.setOnClickListener(addNewRecipientOnClickListener);
		sendNewMessageButton.setOnClickListener(sendNewMessageOnClickListener);
	}
	
	public void sendSmsMessage(final String phoneNumber, final String message)
	{
		final MessageContainer messageContainer = new MessageContainer();
		messageContainer.setPhoneNumber(phoneNumber);
		messageContainer.setBody(message);
		
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SmsMessageHandler.SMS_SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SmsMessageHandler.SMS_DELIVERED), 0);
 
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SmsMessageHandler.SMS_SENT));
 
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                        messageContainer.setDate(Calendar.getInstance().getTime());
                        messageContainer.setStatus(SmsMessageHandler.SMS_DELIVERED);
                        SmsMessageHandler.saveOutgoingSmsToDB( messageContainer );
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;                        
                }
            }
        }, new IntentFilter(SmsMessageHandler.SMS_DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
	}
}
