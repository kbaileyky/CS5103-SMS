package edu.utsa.cs.smsmessenger.test;

import java.util.ArrayList;
import java.util.List;

import edu.utsa.cs.smsmessenger.model.ContactContainer;
import edu.utsa.cs.smsmessenger.util.ContactsUtil;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.test.AndroidTestCase;

/**
 * This is the JUnit Test Class for ContactsUtil.
 * 
 * @author Emilio Mercado
 * @version 1.0
 * @since 1.0
 * 
 */

public class ContactsUtilTest extends AndroidTestCase {
	public static final String TEST_NAME_A = "Emilio Mercado";
	public static final String TEST_NAME_B = "Michael Madrigal";
	public static final String UNREGISTERED_TEST_NAME = "Kendall";
	public static final String TEST_PHONE_NUM_A = "15555215556";
	public static final String TEST_PHONE_NUM_A_W_HYPHENS = "555-521-5556";
	public static final String TEST_PHONE_NUM_A_W_PERIODS = "555.521.5556";
	public static final String TEST_PHONE_NUM_A_W_PARENTHESES_A_HYPHEN = "(555)521-5556";
	public static final String TEST_PHONE_NUM_A_W_PARENTHESES_W_SPACE_A_HYPHEN = "(555) 521-5556";
	public static final String TEST_PHONE_NUM_A_W_PARENTHESES_W_SPACE_A_HYPHEN2 = "1(555) 521-5556";
	public static final String INVALID_TEST_PHONE_NUM_A_W_HYPHENS_A_COLON = "555:5215556";
	public static final String INVALID_TEST_PHONE_NUM_A_W_HYPHENS_A_SLASH = "555/5215556";
	public static final String INVALID_TEST_PHONE_ALHPA = "THIS IS NOT A PHONE NUMBER";
	public static final String INVALID_TEST_PHONE_ALHPA_NUM = "555521JJJM";
	public static final String INVALID_TEST_PHONE_ALHPA_NUM_SLASH1 = "ABC/5215556";
	public static final String INVALID_TEST_PHONE_ALHPA_NUM_SLASH2 = "521/ABC5556";
	public static final String TEST_PHONE_NUM_B = "09876543210";
	
	// new phase 2. International Numbers
	public static final String TEST_NAME_INTL_A = "INTL PERSON A";
	public static final String TEST_NAME_INTL_B = "INTL PERSON B";
	
	public static final String TEST_INTL_PHONE_NUM_A = "123";
	public static final String TEST_INTL_PHONE_NUM_B = "1234567890123";
	public static final String TEST_INTL_PHONE_NUM_C = "12345678901";
	public static final String TEST_INTL_PHONE_NUM_D = "123456789012";
	public static final String TEST_INTL_PHONE_NUM_B_W_PERIODS = "123.4567.890.123";
	public static final String TEST_INTL_PHONE_NUM_B_W_PERIODS_A_HYPHEN = "12-3456.78901-23";
	public static final String TEST_INTL_PHONE_NUM_B_W_PARENTHESES_W_PERIODS_A_HYPHEN = "(12)-3456.78901-23";
	public static final String INVALID_TEST_INTL_PHONE_NUM_B_W_SLASH = "12/345678/90123";
	public static final String INVALID_TEST_INTL_PHONE_NUM_B_W_ALPHA = "12A345678X90123S";
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		clearContacts();
		addContact(TEST_NAME_A, TEST_PHONE_NUM_A);
		addContact(TEST_NAME_B, TEST_PHONE_NUM_B);
		addContact(TEST_NAME_INTL_A, TEST_INTL_PHONE_NUM_A);
		addContact(TEST_NAME_INTL_B, TEST_INTL_PHONE_NUM_B);
	}

	public void testGetAllContactNames() {
		List<String> contacts = ContactsUtil.getAllContactNames(getContext()
				.getContentResolver());
		assertTrue(!contacts.isEmpty());
	}

	public void testGetContactById() {
		Cursor contacts = getContext().getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (contacts.moveToNext()) {
			long contactId = contacts.getLong(contacts
					.getColumnIndex(ContactsContract.Contacts._ID));
			assertTrue(!ContactsUtil.getContactById(
					getContext().getContentResolver(), contactId).isEmpty());
		}
		contacts.close();
	}

	public void testGetContactByPhoneNumber() {
		ContentResolver contentResolver = getContext().getContentResolver();

		ContactContainer contactContainer = ContactsUtil
				.getContactByPhoneNumber(contentResolver, TEST_PHONE_NUM_A);
		assertEquals(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, TEST_PHONE_NUM_A_W_HYPHENS);
		assertEquals(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, TEST_PHONE_NUM_A_W_PERIODS);
		assertEquals(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, TEST_PHONE_NUM_A_W_PARENTHESES_A_HYPHEN);
		assertEquals(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver,
				TEST_PHONE_NUM_A_W_PARENTHESES_W_SPACE_A_HYPHEN);
		assertEquals(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver,
				TEST_PHONE_NUM_A_W_PARENTHESES_W_SPACE_A_HYPHEN2);
		assertNotSame(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, INVALID_TEST_PHONE_NUM_A_W_HYPHENS_A_COLON);
		assertNotSame(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, INVALID_TEST_PHONE_NUM_A_W_HYPHENS_A_SLASH);
		assertNotSame(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, INVALID_TEST_PHONE_ALHPA);
		assertNotSame(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, INVALID_TEST_PHONE_ALHPA_NUM);
		assertNotSame(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, INVALID_TEST_PHONE_ALHPA_NUM_SLASH1);
		assertNotSame(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, INVALID_TEST_PHONE_ALHPA_NUM_SLASH2);
		assertNotSame(TEST_NAME_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, TEST_PHONE_NUM_B);
		assertEquals(TEST_NAME_B, contactContainer.getDisplayName());
		
		// PHASE 2 TEST CASES
		contactContainer = ContactsUtil
				.getContactByPhoneNumber(contentResolver, TEST_INTL_PHONE_NUM_A);
		assertEquals(TEST_NAME_INTL_A, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, TEST_INTL_PHONE_NUM_B);
		assertEquals(TEST_NAME_INTL_B, contactContainer.getDisplayName());
		
		contactContainer = ContactsUtil
				.getContactByPhoneNumber(contentResolver, TEST_INTL_PHONE_NUM_B_W_PERIODS);
		assertEquals(TEST_NAME_INTL_B, contactContainer.getDisplayName());

		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, TEST_INTL_PHONE_NUM_B_W_PERIODS_A_HYPHEN);
		assertEquals(TEST_NAME_INTL_B, contactContainer.getDisplayName());
		
		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, TEST_INTL_PHONE_NUM_B_W_PARENTHESES_W_PERIODS_A_HYPHEN);
		assertEquals(TEST_NAME_INTL_B, contactContainer.getDisplayName());
		
		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, INVALID_TEST_INTL_PHONE_NUM_B_W_SLASH);
		assertNotSame(TEST_NAME_INTL_B, contactContainer.getDisplayName());
		
		contactContainer = ContactsUtil.getContactByPhoneNumber(
				contentResolver, INVALID_TEST_INTL_PHONE_NUM_B_W_ALPHA);
		assertNotSame(TEST_NAME_INTL_B, contactContainer.getDisplayName());

	}

	public void testGetPhoneNumberByContactName() {
		ContentResolver contentResolver = getContext().getContentResolver();

		assertEquals(TEST_PHONE_NUM_A,
				ContactsUtil.getPhoneNumberByContactName(contentResolver,
						TEST_NAME_A));
		assertEquals(TEST_PHONE_NUM_B,
				ContactsUtil.getPhoneNumberByContactName(contentResolver,
						TEST_NAME_B));

	}

	public void testIsAPhoneNumber() {
		assertTrue(ContactsUtil.isAPhoneNumber(TEST_PHONE_NUM_A));
		assertTrue(ContactsUtil.isAPhoneNumber(TEST_PHONE_NUM_A_W_HYPHENS));
		assertTrue(ContactsUtil.isAPhoneNumber(TEST_PHONE_NUM_A_W_PERIODS));
		assertTrue(ContactsUtil
				.isAPhoneNumber(TEST_PHONE_NUM_A_W_PARENTHESES_A_HYPHEN));
		assertTrue(ContactsUtil
				.isAPhoneNumber(TEST_PHONE_NUM_A_W_PARENTHESES_W_SPACE_A_HYPHEN));
		assertTrue(ContactsUtil
				.isAPhoneNumber(TEST_PHONE_NUM_A_W_PARENTHESES_W_SPACE_A_HYPHEN2));
		assertFalse(ContactsUtil
				.isAPhoneNumber(INVALID_TEST_PHONE_NUM_A_W_HYPHENS_A_COLON));
		assertFalse(ContactsUtil
				.isAPhoneNumber(INVALID_TEST_PHONE_NUM_A_W_HYPHENS_A_SLASH));
		assertFalse(ContactsUtil.isAPhoneNumber(INVALID_TEST_PHONE_ALHPA));
		assertFalse(ContactsUtil.isAPhoneNumber(INVALID_TEST_PHONE_ALHPA_NUM));
		assertFalse(ContactsUtil
				.isAPhoneNumber(INVALID_TEST_PHONE_ALHPA_NUM_SLASH1));
		assertFalse(ContactsUtil
				.isAPhoneNumber(INVALID_TEST_PHONE_ALHPA_NUM_SLASH2));
	}

	public void testIsAValidPhoneNumber() {
		ContentResolver contentResolver = getContext().getContentResolver();
		assertTrue(ContactsUtil.isAValidPhoneNumber(contentResolver,
				TEST_PHONE_NUM_A));
		assertTrue(ContactsUtil.isAValidPhoneNumber(contentResolver,
				TEST_PHONE_NUM_B));
		assertFalse(ContactsUtil.isAValidPhoneNumber(contentResolver,
				UNREGISTERED_TEST_NAME));
		assertTrue(ContactsUtil.isAValidPhoneNumber(contentResolver,
				TEST_PHONE_NUM_A_W_HYPHENS));
		assertTrue(ContactsUtil.isAValidPhoneNumber(contentResolver,
				TEST_PHONE_NUM_A_W_PERIODS));
		assertTrue(ContactsUtil.isAValidPhoneNumber(contentResolver,
				TEST_PHONE_NUM_A_W_PARENTHESES_A_HYPHEN));
		assertTrue(ContactsUtil.isAValidPhoneNumber(contentResolver,
				TEST_PHONE_NUM_A_W_PARENTHESES_W_SPACE_A_HYPHEN));
		assertTrue(ContactsUtil.isAValidPhoneNumber(contentResolver,
				TEST_PHONE_NUM_A_W_PARENTHESES_W_SPACE_A_HYPHEN2));
		assertFalse(ContactsUtil.isAValidPhoneNumber(contentResolver,
				INVALID_TEST_PHONE_NUM_A_W_HYPHENS_A_COLON));
		assertFalse(ContactsUtil.isAValidPhoneNumber(contentResolver,
				INVALID_TEST_PHONE_NUM_A_W_HYPHENS_A_SLASH));
		assertFalse(ContactsUtil.isAValidPhoneNumber(contentResolver,
				INVALID_TEST_PHONE_ALHPA));
		assertFalse(ContactsUtil.isAValidPhoneNumber(contentResolver,
				INVALID_TEST_PHONE_ALHPA_NUM));
		assertFalse(ContactsUtil.isAValidPhoneNumber(contentResolver,
				INVALID_TEST_PHONE_ALHPA_NUM_SLASH1));
		assertFalse(ContactsUtil.isAValidPhoneNumber(contentResolver,
				INVALID_TEST_PHONE_ALHPA_NUM_SLASH2));
	}

	/*
	 * This is a setup function for adding contacts to the simulation to clear
	 * contacts.
	 */
	private void clearContacts() {
		ContentResolver cr = getContext().getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		while (cur.moveToNext()) {
			try {
				String lookupKey = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
				Uri uri = Uri
						.withAppendedPath(
								ContactsContract.Contacts.CONTENT_LOOKUP_URI,
								lookupKey);
				System.out.println("The uri is " + uri.toString());
				cr.delete(uri, null, null);
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
			}
		}
	}

	/*
	 * This is a setup function for adding contacts to the simulation for these
	 * test cases to work.
	 */
	private void addContact(String name, String phoneNumber) {
		ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>();
		op_list.add(ContentProviderOperation
				.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				// .withValue(RawContacts.AGGREGATION_MODE,
				// RawContacts.AGGREGATION_MODE_DEFAULT)
				.build());

		//Insert the Display Name
		op_list.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, 0)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, name).build());

		//Insert the Phone Number
		op_list.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
						phoneNumber)
				.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
						Phone.TYPE_MOBILE).build());

		try {
			getContext().getContentResolver().applyBatch(
					ContactsContract.AUTHORITY, op_list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
