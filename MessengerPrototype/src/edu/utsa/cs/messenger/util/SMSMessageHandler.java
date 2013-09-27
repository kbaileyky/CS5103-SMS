package edu.utsa.cs.messenger.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SMSMessageHandler {

	public void getMessageHistory(Context context)
	{
		HashMap<String, ArrayList<MessageContainer>>messageMap = new HashMap<String, ArrayList<MessageContainer>>();
		Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
		
		cursor.moveToFirst();
		final Calendar cal = Calendar.getInstance();
		do{
			try{
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
}
