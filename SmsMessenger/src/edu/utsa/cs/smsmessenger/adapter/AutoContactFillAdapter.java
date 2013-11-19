package edu.utsa.cs.smsmessenger.adapter;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.util.ContactsUtil;
import android.app.Activity;
import android.widget.ArrayAdapter;

/**
 * This class is used to fill and display a contact in an activity.
 * 
 * @author Emilio Mercado
 * @version 1.0
 * @since 1.0
 * 
 */
public class AutoContactFillAdapter extends ArrayAdapter<String> {

	public AutoContactFillAdapter(Activity activity) {
		super(activity.getApplicationContext(), R.layout.single_contact,
				R.id.tv_ContactName, ContactsUtil.getAllContactNames(activity.getContentResolver()));
	}
	
}
