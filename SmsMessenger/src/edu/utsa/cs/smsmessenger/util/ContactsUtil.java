package edu.utsa.cs.smsmessenger.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle.Control;

import edu.utsa.cs.smsmessenger.model.ContactContainer;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
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

	/**
	 * This method takes an ID and queries for a ContactContainer object that
	 * contains display name, Photo URI and Phone Number.
	 * 
	 * @param contentResolver
	 *            The ContentResolver which to use for querying.
	 * @param contactId
	 *            The ContentID query parameter.
	 * @return returns a new ContactContainer instance specified by Contact ID.
	 */
	public static ContactContainer getContactById(
			ContentResolver contentResolver, long contactId) {
		
		Log.d("getContactById", "id = " + contactId);
		
		ContactContainer contact = new ContactContainer();
		if (contactId == -1)
			return contact;
		
		String strId = "" + contactId;
		contact.setId(contactId);
		
		Cursor cursor = contentResolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
				new String[] { strId }, null);

		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String photoUri = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO_URI));
			String phoneNumber = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			contact.setDisplayName(name);
			contact.setPhotoUri(photoUri);
			contact.setPhoneNumber(phoneNumber);
			cursor.close();
			return contact;
		}
		cursor.close();
		return contact;
	}

	/**
	 * This method takes a String PhoneNumber and queries for a ContactContainer
	 * object that contains display name, Photo URI and Phone Number.
	 * 
	 * @param contentResolver
	 *            The ContentResolver which to use for querying.
	 * @param phoneNumber
	 *            The Phone Number to use for querying.
	 * @return returns a new ContactContainer instance specified by PhoneNumber.
	 */
	public static ContactContainer getContactByPhoneNumber(
			ContentResolver contentResolver, String phoneNumber) {
		
		ContactContainer contact = new ContactContainer();
		contact.setPhoneNumber(phoneNumber);
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cur = contentResolver.query(uri, new String[] { PhoneLookup._ID,
				PhoneLookup.DISPLAY_NAME, PhoneLookup.PHOTO_URI }, null, null,
				null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				for (String names : cur.getColumnNames()) {
					Log.d("names: ", names);
				}
				String name = cur.getString(cur
						.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				long id = cur.getLong(cur
						.getColumnIndex(ContactsContract.Data._ID));
				String photoUri = cur.getString(cur
						.getColumnIndex(ContactsContract.Data.PHOTO_URI));
				
				//Set contact data
				contact.setId(id);
				contact.setDisplayName(name);
				contact.setPhotoUri(photoUri);
			}
			cur.close();
		}
		return contact;
	}

	/*
	 * 
	 * 
	 * TODO: This function will be optimized in final release to use one query
	 * for the contactName and then one simple query off tha row for the
	 * PhoneNumber.
	 */
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

	/**
	 * This static method takes a String Contact verifies if it is a phone
	 * number.
	 * 
	 * @param contact
	 *            the contact will be checked to see if passes phone number
	 *            validation.
	 * @return returns true if it is a phone number.
	 */
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