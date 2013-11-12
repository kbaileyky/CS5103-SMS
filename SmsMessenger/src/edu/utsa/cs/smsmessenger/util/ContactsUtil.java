package edu.utsa.cs.smsmessenger.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * This class is a utility class to allow the activities to query contact
 * information. Contact Names on a phone, get a phone number based on contact,
 * etc.
 * 
 * @author Emilio Mercado
 * @version 1.0
 * @since 1.0
 * 
 */

public class ContactsUtil {
	/**
	 * This method retrieves all literal string contact names from the phone
	 * 
	 * @param activity
	 *            The activity which to use for obtaining contentResolver() for
	 *            querying.
	 * @return returns an ArrayList of String Contact names for the current
	 *         Phone Activity.
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

	public static String getContactNameByPhoneNumber(Activity activity,
			String contactName) {
		if (!isAPhoneNumber(contactName))
			return contactName;
		String phoneNumber = contactName.replaceAll("[^0-9]", "");
		if(phoneNumber.length() == 10)
		{
			phoneNumber = "1" + phoneNumber;
		}

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

				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);

					while (pCur.moveToNext()) {
						String phoneNo = pCur
								.getString(
										pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
								.replaceAll("[^0-9]", "");
						if(phoneNo.length() == 10)
						{
							phoneNo = "1"+phoneNo;
						}
						
						if (phoneNumber.equals(phoneNo))
							return name;
					}

				}

			}
		}
		return null;
	}

	public static boolean isAPhoneNumber(String contact) {
		if (contact
				.matches("^\\d{10}|^\\d{11}|^[1]?(\\(\\d{3}\\)\\s?)?\\d{3}-\\d{4}$|^\\d{3}([.-])\\d{3}\\2\\d{4}$")) {
			return true;
		}
		return false;
	}

	public static boolean isAValidPhoneNumber(Activity activity, String contact) {
		if (isAPhoneNumber(contact)) {
			return true;
		} else if (getPhoneNumberByContactName(activity, contact) != null) {
			return true;
		} else {
			return false;
		}
	}
}