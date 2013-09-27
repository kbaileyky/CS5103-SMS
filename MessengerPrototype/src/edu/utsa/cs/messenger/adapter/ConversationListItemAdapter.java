package edu.utsa.cs.messenger.adapter;

import java.util.ArrayList;

import edu.utsa.cs.messenger.util.ConversationListItem;

import android.R;
import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class ConversationListItemAdapter extends ArrayAdapter<ConversationListItem> {
	
	private Context context;
	ArrayList<ConversationListItem> items;
	private int rowLayoutResourceId;
	private int contactImageViewId;
	private int contactNameTextViewId;
	private int conversationPreviewTextViewId;

	public ConversationListItemAdapter(Context context, ArrayList<ConversationListItem> objects,
			int layoutResourceId, int contactImageViewId, int contactNameTextViewId, int conversationPreviewTextViewId) {
		super(context, layoutResourceId, objects);
		this.context = context;
		this.items = objects;
		this.rowLayoutResourceId = layoutResourceId;
		this.contactImageViewId = contactImageViewId;
		this.contactNameTextViewId = contactNameTextViewId;
		this.conversationPreviewTextViewId = conversationPreviewTextViewId;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		    	
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
			convertView = inflater.inflate(rowLayoutResourceId, parent, false);
		}
		ImageView contactImageView = (ImageView)convertView.findViewById(contactImageViewId);
		TextView contactNameTextView = (TextView)convertView.findViewById(contactNameTextViewId);
		TextView conversationPreviewTextView = (TextView)convertView.findViewById(conversationPreviewTextViewId);
		
		final ConversationListItem item = items.get(position);
		contactNameTextView.setText(item.getContactName());
		conversationPreviewTextView.setText(item.getConversationPreview());
		
		//Hasn't been read, then highlight or something
		if(!item.isRead())
		{
			convertView.setBackgroundColor(Color.YELLOW);
		}
		
		convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO launch conversation Activity
				Toast.makeText(context, "Click " + item.getContactName() , Toast.LENGTH_SHORT).show();
				
			}
			
		});
    	return convertView;
    }
	public void setItems(ArrayList<ConversationListItem> items)
	{
		this.items = items;
	}
	public ArrayList<ConversationListItem> getItems()
	{
		return this.items;
	}
	public int getCount() {
		return items.size();
	}
	public ConversationListItem getItem(int position) {
		if(position>0 && position<items.size())
			return items.get(position);
		return null;
	}
	public long getItemId(int position) {
		return position;
	}
}