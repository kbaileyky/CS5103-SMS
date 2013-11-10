package edu.utsa.cs.smsmessenger.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactsUtil {
	/**
	 * Get All the Contact Names
	 * 
	 * @return
	 */
	public static List<String> getAllContactNames(Activity activity) {
		List<String> lContactNamesList = new ArrayList<String>();
		try {
			// Get all Contacts
			Cursor lPeople = activity.getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI, null, null, null,
					null);
			if (lPeople != null) {
				while (lPeople.moveToNext()) {
					// Add Contact's Name into the List
					lContactNamesList
							.add(lPeople.getString(lPeople
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
				}
			}
		} catch (NullPointerException e) {
			Log.e("getAllContactNames()", e.getMessage());
		}
		return lContactNamesList;
	}
}
