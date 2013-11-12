package edu.utsa.cs.smsmessenger.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
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

	public static String getPhoneNumberByContactName(Activity activity,
			String contactName) {
		ContentResolver cr = activity.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				if (name.equalsIgnoreCase(contactName)) {
					if (Integer
							.parseInt(cur.getString(cur
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

						Cursor pCur = cr
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = ?", new String[] { id },
										null);

						while (pCur.moveToNext()) {
							String phoneNo = pCur
									.getString(pCur
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							return phoneNo;
						}

					}
				}

			}
		}
		return null;
	}

	public static boolean isAPhoneNumber(String contact) {
		if (contact
				.matches("^\\d{10}|^(\\(\\d{3}\\)\\s?)?\\d{3}-\\d{4}$|^\\d{3}([.-])\\d{3}\\2\\d{4}$")) {
			return true;
		}
		return false;
	}
}
