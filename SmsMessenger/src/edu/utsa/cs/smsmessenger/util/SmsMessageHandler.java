package edu.utsa.cs.smsmessenger.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ToggleButton;
import edu.utsa.cs.smsmessenger.model.ConversationPreview;
import edu.utsa.cs.smsmessenger.model.MessageContainer;

/** SmsMessageHandler contains methods to interfaces with the systems database
 * 
 * @author mmadrigal
 *
 */
public class SmsMessageHandler extends SQLiteOpenHelper {
	
	public static final String NEW_MSG_INTENT = "edu.utsa.cs.smsmessenger.NEW_MSG";

	public static final String SMS_DRAFT = "SMS_DRAFT";
    public static final String SMS_SENT = "SMS_SENT";
    public static final String SMS_DELIVERED = "SMS_DELIVERED";
    public static final String SMS_FAILED = "SMS_FAILED";
    public static final String SMS_RECEIVED = "SMS_RECEIVED";
    
    public static final String DB_NAME = "cs5103messenger.db";
    public static final int DB_VERSION = 1;
    
    public static final String OUTGOING_TBL = "outgoing";
    public static final String INCOMING_TBL = "incoming";
    
    public static final String MSG_TYPE_OUT = "OUTGOING";
    public static final String MSG_TYPE_IN  = "INCOMING";
    
    public static final String COL_NAME_ID = "id";
    public static final String COL_NAME_PHONE_NUMBER = "phoneNumber";
    public static final String COL_NAME_CONTACT_ID = "contactId";
    public static final String COL_NAME_DATE = "date";
    public static final String COL_NAME_SUBJECT = "subject";
    public static final String COL_NAME_BODY = "body";
    public static final String COL_NAME_READ = "read";
    public static final String COL_NAME_STATUS = "status";
    public static final String COL_NAME_TYPE = "status";
    
    public static final String CREATE_SMS_MSG_TABLE = 
    		"CREATE TABLE %s (" + COL_NAME_ID + " INTEGER PRIMARY KEY, " +
    		COL_NAME_PHONE_NUMBER + " TEXT, " + COL_NAME_CONTACT_ID + " INTEGER, " + COL_NAME_DATE + " INTEGER, " +
    		COL_NAME_SUBJECT + " TEXT, " + COL_NAME_BODY + " TEXT, " + COL_NAME_READ + " INTEGER, " + COL_NAME_STATUS + " TEXT)";
    
    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS %s";

    
    public SmsMessageHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(String.format(CREATE_SMS_MSG_TABLE, OUTGOING_TBL));
		db.execSQL(String.format(CREATE_SMS_MSG_TABLE, INCOMING_TBL));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		//TODO - this will delete old table on upgrade
		db.execSQL(String.format(DELETE_TABLE, OUTGOING_TBL));
		db.execSQL(String.format(DELETE_TABLE, INCOMING_TBL));
		db.execSQL(String.format(CREATE_SMS_MSG_TABLE, OUTGOING_TBL));
		db.execSQL(String.format(CREATE_SMS_MSG_TABLE, INCOMING_TBL));
	}

	public long saveOutgoingSmsToDB(MessageContainer message)
	{
		message.setType(MSG_TYPE_OUT);
		message.setRead(true); //User always reads his own messages
		return saveSmsToDB(message, OUTGOING_TBL);
	}
	
	public long saveIncomingSmsToDB(MessageContainer message)
	{
		message.setType(MSG_TYPE_IN);
		return saveSmsToDB(message, INCOMING_TBL);
	}
	public long saveSmsToDB(MessageContainer message, String table)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(COL_NAME_PHONE_NUMBER, message.getPhoneNumber());
		values.put(COL_NAME_CONTACT_ID, message.getContactId());
		values.put(COL_NAME_DATE, message.getDate());
		values.put(COL_NAME_SUBJECT, message.getSubject());
		values.put(COL_NAME_BODY, message.getBody());
		values.put(COL_NAME_READ, message.isRead()?1:0);
		values.put(COL_NAME_STATUS, message.getStatus());
		values.put(COL_NAME_TYPE, message.getType());

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(table, null, values);
		
		return newRowId;
	}
	
	public int updateSmsMessage(MessageContainer message, String table)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		// New value for one column
		ContentValues values = new ContentValues();
		values.put(COL_NAME_PHONE_NUMBER, message.getPhoneNumber());
		values.put(COL_NAME_CONTACT_ID, message.getContactId());
		values.put(COL_NAME_DATE, message.getDate());
		values.put(COL_NAME_SUBJECT, message.getSubject());
		values.put(COL_NAME_BODY, message.getBody());
		values.put(COL_NAME_READ, message.isRead()?1:0);
		values.put(COL_NAME_STATUS, message.getStatus());
		values.put(COL_NAME_TYPE, message.getType());

		// Which row to update, based on the ID
		String selection = COL_NAME_ID + " = ?";
		String[] selectionArgs = { Long.toString(message.getId()) };

		int count = db.update(table, values, selection, selectionArgs);
		
		return count;
	}
	
	public ArrayList<MessageContainer> getSmsMessages(String selection, String[] selectionArgs, String sortOrder, String table)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
			COL_NAME_ID,
			COL_NAME_PHONE_NUMBER,
			COL_NAME_CONTACT_ID,
			COL_NAME_DATE,
			COL_NAME_SUBJECT,
			COL_NAME_BODY,
			COL_NAME_READ,
			COL_NAME_STATUS,
			COL_NAME_TYPE
	    };

		// How you want the results sorted in the resulting Cursor
		//String sortOrder = COL_NAME_DATE + " DESC";

		Cursor c = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
		
		ArrayList<MessageContainer> msgList = new ArrayList<MessageContainer>();
		c.moveToFirst();
		do{
			//Store in List
			MessageContainer msg = new MessageContainer();
			msg.setId(c.getLong(c.getColumnIndex(COL_NAME_ID)));
			msg.setPhoneNumber(c.getString(c.getColumnIndex(COL_NAME_PHONE_NUMBER)));
			msg.setContactId(c.getInt(c.getColumnIndex(COL_NAME_CONTACT_ID)));
			msg.setDate(c.getLong(c.getColumnIndex(COL_NAME_DATE)));
			msg.setSubject(c.getString(c.getColumnIndex(COL_NAME_SUBJECT)));
			msg.setBody(c.getString(c.getColumnIndex(COL_NAME_BODY)));
			msg.setRead(c.getInt(c.getColumnIndex(COL_NAME_READ))==0?false:true);
			msg.setStatus(c.getString(c.getColumnIndex(COL_NAME_STATUS)));
			msg.setType(c.getString(c.getColumnIndex(COL_NAME_TYPE)));
			msgList.add(msg);
		}while(c.moveToNext());
		
		return msgList;
	}
	public void getConversationPreviewItmes()
	{
		ArrayList<MessageContainer>inMsgList = getSmsMessages(null, null, COL_NAME_DATE+ " DESC", INCOMING_TBL);
		ArrayList<MessageContainer>outMsgList = getSmsMessages(null, null, COL_NAME_DATE+ " DESC", OUTGOING_TBL);
		
		HashMap<String, ConversationPreview> convPrevList = new HashMap<String, ConversationPreview>();
		
		for(MessageContainer msg:inMsgList)
		{
			//Since they are in order, no need to check if next is more recent
			if(!convPrevList.containsKey(msg.getPhoneNumber()))
			{
				//TODO - look up contact info
				ConversationPreview preview = new ConversationPreview(null, msg.getPhoneNumber(), msg.getBody(), msg.isRead(), msg.getDate() );
				convPrevList.put(msg.getPhoneNumber(), preview);
			}
		}
		for(MessageContainer msg:outMsgList)
		{
			//Since they are in order, no need to check if next is more recent
			if(!convPrevList.containsKey(msg.getPhoneNumber()))
			{
				//TODO - look up contact info
				ConversationPreview preview = new ConversationPreview(null, msg.getPhoneNumber(), msg.getBody(), msg.isRead(), msg.getDate() );
				convPrevList.put(msg.getPhoneNumber(), preview);
			}
			else
			{
				if(convPrevList.get(msg.getPhoneNumber()).getDate()<msg.getDate())
				{
					ConversationPreview preview = convPrevList.get(msg.getPhoneNumber());
					preview.setDate(msg.getDate());
					preview.setPreviewText(msg.getBody());
				}
			}
		}
	}
}
