package edu.utsa.cs.smsmessenger.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.fragment.DatePickerFragment;
import edu.utsa.cs.smsmessenger.fragment.TimePickerFragment;
import edu.utsa.cs.smsmessenger.util.SmsMessageHandler;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class ScheduleMessageActivity extends Activity {

	public static final String SCHEDULE_RESQUEST_DATE_KEY = "edu.utsa.cs.smsmessenger.SCHEDULE_RESQUEST";
	public static final String PASSED_SCHEDULE_DATE_KEY = "edu.utsa.cs.smsmessenger.PASSED_SCHEDULE_DATE";
	private Button cancelButton;
	private Button setButton;
	private EditText timeEditText;
	private EditText dateEditText;
	private SimpleDateFormat dateSdf;
	private SimpleDateFormat timeSdf;
	private Calendar scheduleDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_message);
		
		Bundle extras = getIntent().getExtras();
		long dateMilliSeconds = extras.getLong(PASSED_SCHEDULE_DATE_KEY);
		
		cancelButton = (Button)findViewById(R.id.scheduleMsgCancelButton);
		setButton = (Button)findViewById(R.id.scheduleMsgSetButton);
		
		timeEditText = (EditText)findViewById(R.id.scheduleMsgTimeEditText);
		dateEditText = (EditText)findViewById(R.id.scheduleMsgDateEditText);
		
		this.dateSdf = new SimpleDateFormat(getResources().getString(
				R.string.date_format), getResources()
				.getConfiguration().locale);
		
		this.timeSdf = new SimpleDateFormat(getResources().getString(
				R.string.time_format), getResources()
				.getConfiguration().locale);
		
		scheduleDate = Calendar.getInstance();
		if(dateMilliSeconds==-1)
			scheduleDate.add(Calendar.MINUTE, 5);
		else
			scheduleDate.setTimeInMillis(dateMilliSeconds);
		scheduleDate.set(Calendar.SECOND, 0);

		timeEditText.setText(timeSdf.format(scheduleDate.getTime()));
		dateEditText.setText(dateSdf.format(scheduleDate.getTime()));
		
		cancelButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				cancelSchedule();
			}
			
		});
		setButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				setSchedule();
			}
			
		});
		timeEditText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
	             DialogFragment newFragment = new TimePickerFragment() {

					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
					    // Use the current time as the default values for the picker
					    final Calendar c = scheduleDate;
					    int hour = c.get(Calendar.HOUR_OF_DAY);
					    int minute = c.get(Calendar.MINUTE);
					
					    // Create a new instance of TimePickerDialog and return it
					    return new TimePickerDialog(getActivity(), this, hour, minute,
					            DateFormat.is24HourFormat(getActivity()));
					}
	                 @Override
	                 public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	                   
	                	Calendar tempDate = (Calendar)scheduleDate.clone();
						tempDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
						tempDate.set(Calendar.MINUTE, minute);
						 
						if(tempDate.after(Calendar.getInstance()))
						{
							scheduleDate = tempDate;
							timeEditText.setText(timeSdf.format(scheduleDate.getTime()));
						}
						else
							ShowToastMessage("Error: Date in the past");
	                 }
	              };
	              newFragment.show(getActivity().getFragmentManager(), "datePicker");
			}
			
		});
		dateEditText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				DialogFragment newFragment = new DatePickerFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						// Use the current date as the default date in the
						// picker
						final Calendar c = scheduleDate;
						int year = c.get(Calendar.YEAR);
						int month = c.get(Calendar.MONTH);
						int day = c.get(Calendar.DAY_OF_MONTH);

						// Create a new instance of DatePickerDialog and return
						// it
						return new DatePickerDialog(this.getActivity(), this,
								year, month, day);
					}
	                 @Override
	                 public void onDateSet(DatePicker view, int year, int month, int day) {
	                   
						Calendar tempDate = (Calendar)scheduleDate.clone();
						tempDate.set(Calendar.YEAR, year);
						tempDate.set(Calendar.MONTH, month);
						tempDate.set(Calendar.DATE, day);
						 
						if(tempDate.after(Calendar.getInstance()))
						{
							scheduleDate = tempDate;
							dateEditText.setText(dateSdf.format(scheduleDate.getTime()));
						}
						else
							ShowToastMessage("Error: Date in the past");
	                		 
	                 }
	              };
	              newFragment.show(getActivity().getFragmentManager(), "datePicker");
			}
			
		});
	}

	private ScheduleMessageActivity getActivity() {
		return this;
	}
	private void ShowToastMessage(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	public void setSchedule()
	{
		Intent returnIntent = new Intent();
		returnIntent.putExtra(SCHEDULE_RESQUEST_DATE_KEY, scheduleDate);
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public void cancelSchedule()
	{
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}
}
