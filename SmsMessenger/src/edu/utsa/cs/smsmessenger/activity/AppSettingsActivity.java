package edu.utsa.cs.smsmessenger.activity;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.util.AppConstants;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

/**
 * This class is the Activity that shows Application Settings
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class AppSettingsActivity extends Activity {

	private CheckBox smsPropagationCheckBox;
	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_settings);

		preferences = getSharedPreferences(AppConstants.APP_PREF_KEY, MODE_PRIVATE);
		boolean allowSMSPropagation = preferences.getBoolean(AppConstants.APP_PREF_SMS_PROPAGATION_KEY, true);
		
		smsPropagationCheckBox = (CheckBox)findViewById(R.id.smsPropagationCheckBox);
		smsPropagationCheckBox.setChecked(allowSMSPropagation);
		
		smsPropagationCheckBox.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				SharedPreferences.Editor preferencesEditor = preferences.edit();
				preferencesEditor.putBoolean(AppConstants.APP_PREF_SMS_PROPAGATION_KEY, smsPropagationCheckBox.isChecked());
				preferencesEditor.apply(); // store the updated preferences
			}
			
		});
		
		
	}

}
