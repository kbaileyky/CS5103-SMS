package edu.utsa.cs.smsmessenger.util;

import edu.utsa.cs.smsmessenger.R;
import android.app.Activity;
import android.widget.ArrayAdapter;

public class AutoContactFillAdapter extends ArrayAdapter<String> {

	public AutoContactFillAdapter(Activity activity) {
		super(activity.getApplicationContext(), R.layout.single_contact,
				R.id.tv_ContactName, ContactsUtil.getAllContactNames(activity.getContentResolver()));
	}
}
