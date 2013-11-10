package edu.utsa.cs.smsmessenger.activity;

import java.text.SimpleDateFormat;

import edu.utsa.cs.smsmessenger.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TableRow;
import android.widget.TextView;
import edu.utsa.cs.smsmessenger.model.MessageContainer;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;


public class ViewMessageActivity extends Activity{

	private TableRow rootTable;
	private TextView txtMsgBody;
	private MessageContainer currentMessage;
	private SmsMessageHandler smsMessageHandler;
	 //Touch event related variables
	 int touchState;
	 final int IDLE = 0;
	 final int TOUCH = 1;
	 final int PINCH = 2;
	 float dist0, distCurrent;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation_to_message_item);

		Bundle extras = getIntent().getExtras();
	
		currentMessage= new MessageContainer();
		
		currentMessage.setBody(extras.getString("msgBody"));
		currentMessage.setDate(extras.getLong("timeAndDate"));
		currentMessage.setContactId(extras.getInt("contactName"));
		
		updateUI();
		
		
        distCurrent = 1; //Dummy default distance
        dist0 = 1;   //Dummy default distance
        
        rootTable.setOnTouchListener(MyOnTouchListener);
        
        touchState = IDLE;
		
		
		Log.d("ViewMessageActivity", "view Message Activity: " + currentMessage.getBody() + " from " + currentMessage.getContactId());
		
	//	ImageButton newRecipientButton = (ImageButton) findViewById(R.id.newMsgAddRecipientImageButton);
	//	ImageButton sendNewMessageButton = (ImageButton) findViewById(R.id.newMsgSendImageButton);
		return;
	}

	private void updateUI(){
		
		//Temporary - replace with contact name
		//TODO Get contact
		setTitle(Integer.toString(currentMessage.getContactId()));
		
		txtMsgBody = (TextView)findViewById(R.id.msgBodyTextView);
		txtMsgBody.setText(currentMessage.getBody());
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		TextView txtTimeAndDate = (TextView)findViewById(R.id.msgDateTextView);
		txtTimeAndDate.setText(sdf.format(currentMessage.getDate()));
		

		rootTable = (TableRow)findViewById(R.id.tableRow1);
		 //tableRow1
		
		 
		 
		return;
	}

	
	private void updateFontSize() {
		float newsize;
		float curScale = distCurrent/dist0;
	     if (curScale < 0.1){
	         curScale = 0.1f; 
	        }
	     
	     newsize = (12 * curScale);
	     
	     if(newsize < 9){
	    	 newsize = 9;
	     }
	     
	     if(newsize > 24){
	    	 newsize = 24;
	     }
	     
	     txtMsgBody.setTextSize(newsize);
	}
	
	 OnTouchListener MyOnTouchListener = new OnTouchListener(){


	  @Override
	  public boolean onTouch(View view, MotionEvent event) {
	   // TODO Auto-generated method stub
	   
	   float distx, disty;
	   
	   switch(event.getAction() & MotionEvent.ACTION_MASK){
	   case MotionEvent.ACTION_DOWN:
	    //A pressed gesture has started, the motion contains the initial starting location.
	    touchState = TOUCH;
	    break;
	   case MotionEvent.ACTION_POINTER_DOWN:
	    //A non-primary pointer has gone down.
	    touchState = PINCH;
	    
	    //Get the distance when the second pointer touch
	    distx = event.getX(0) - event.getX(1);
	    disty = event.getY(0) - event.getY(1);
	    dist0 = FloatMath.sqrt(distx * distx + disty * disty);

	    break;
	   case MotionEvent.ACTION_MOVE:
	    //A change has happened during a press gesture (between ACTION_DOWN and ACTION_UP).
	    
	    if(touchState == PINCH){      
	     //Get the current distance
	     distx = event.getX(0) - event.getX(1);
	     disty = event.getY(0) - event.getY(1);
	     distCurrent = FloatMath.sqrt(distx * distx + disty * disty);

	     updateFontSize();
	    }
	    
	    break;
	   case MotionEvent.ACTION_UP:
	    //A pressed gesture has finished.
		   
	    touchState = IDLE;
	    break;
	   case MotionEvent.ACTION_POINTER_UP:
	    //A non-primary pointer has gone up.
	    touchState = TOUCH;
	    break;
	   }
	   
	   return true;
	  }


	 };
	
}
