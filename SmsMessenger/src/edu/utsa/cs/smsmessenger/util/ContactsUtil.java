package edu.utsa.cs.smsmessenger.util;

import java.util.ArrayList;
import java.util.List;

import edu.utsa.cs.smsmessenger.model.ContactContainer;

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
				lPeople.close();
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
							pCur.close();
							cur.close();
							return phoneNo;
						}
						pCur.close();
					}
				}

			}
			cur.close();
		}
		return null;
	}

	public static ContactContainer getContactByPhoneNumber(
			ContentResolver contentResolver, String lookUpPhoneNumber) {

		//Log.e("getContactByPhoneNumber", "**********start");
		ContactContainer contact = new ContactContainer();
		contact.setPhoneNumber(lookUpPhoneNumber);

		String phoneNumber = lookUpPhoneNumber.replaceAll("[^\\d]", "");// ("[^0-9]",
																		// "");
		if (!isAPhoneNumber(phoneNumber)) {
			//Log.e("getContactByPhoneNumber", "**********Not phone number");
			return contact;
		}

		// String phoneNumber = lookUpPhoneNumber.replaceAll("[^\\d]", ""
		// );//("[^0-9]", "");

		contact.setDisplayName(phoneNumber);
		contact.setPhoneNumber(phoneNumber);
		if (phoneNumber.length() == 10) {
			phoneNumber = "1" + phoneNumber;
		}

		// ContentResolver cr = activity.getContentResolver();
		Cursor cur = contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cur != null) // null pointer exception on phone
		{
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					String id = cur.getString(cur
							.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cur
							.getString(cur
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					String photoUri = cur
							.getString(cur
									.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO_URI));

					if (Integer
							.parseInt(cur.getString(cur
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

						Cursor pCur = contentResolver
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = ?", new String[] { id },
										null);
						if (pCur != null) // Null pointer exception on phone
						{
							while (pCur.moveToNext()) {
								String phoneNo = pCur
										.getString(
												pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
										.replaceAll("[^0-9]", "");
								if (phoneNo.length() == 10) {
									phoneNo = "1" + phoneNo;
								}

								if (phoneNumber.equals(phoneNo)) {
									contact.setId(id);
									contact.setDisplayName(name);
									contact.setPhotoUri(photoUri);
									//Log.e("getContactByPhoneNumber",
									//		"**********Found match contact, end");
									pCur.close();
									cur.close();
									return contact;
								}
							}
							pCur.close();
						} //else
							//Log.e("getContactByPhoneNumber",
							//		"**********pCur is null");
					}

				}
			}
			cur.close();
		} //else
			//Log.e("getContactByPhoneNumber", "**********cur is null");
		//Log.e("getContactByPhoneNumber", "**********end");
		return contact;
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

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}
}