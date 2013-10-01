package edu.utsa.cs.smsmessenger.adapter;

import java.util.ArrayList;
import java.util.List;

import edu.utsa.cs.smsmessenger.R;
import edu.utsa.cs.smsmessenger.model.ConversationPreview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ConversationPreviewAdapter extends ArrayAdapter<ConversationPreview>{
	
	private Context context;
	private int layoutResourceId;
	private ArrayList<ConversationPreview> objects;

	public ConversationPreviewAdapter(Context context, int layoutResourceId, List<ConversationPreview> objects) {
		super(context, layoutResourceId, objects);
		
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.objects = new ArrayList<ConversationPreview>();
		this.objects.addAll(objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);
		}
		ImageView contactImageView = (ImageView)convertView.findViewById(R.id.contactImageView);
		TextView contactNameTextView = (TextView)convertView.findViewById(R.id.contactNameTextView);
		TextView previewTextView = (TextView)convertView.findViewById(R.id.conversationPreviewTextView);
		
		ConversationPreview preview = objects.get(position);
		contactNameTextView.setText(preview.getContactName());
		previewTextView.setText(preview.getPreviewText());
		
		//TODO - mark view to indicate if message has not been read
		
		final ConversationPreview finalPreview = preview;
		convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO launch conversation Activity
				Toast.makeText(context, "Click " + finalPreview.getContactName() , Toast.LENGTH_SHORT).show();
				
			}
			
		});
		return convertView;
	}

	public void setItems(ArrayList<ConversationPreview> items)
	{
		this.objects = items;
	}
	public ArrayList<ConversationPreview> getItems()
	{
		return this.objects;
	}
	public int getCount() {
		return objects.size();
	}
	public ConversationPreview getItem(int position) {
		if(position>0 && position<objects.size())
			return objects.get(position);
		return null;
	}
	public long getItemId(int position) {
		return position;
	}
}
