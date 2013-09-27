package edu.utsa.cs.messenger.util;

import java.util.Date;

public class ConversationListItem {
	
	private String contactName;
	private String conversationPreview;
	private String contactImageUrl;
	private boolean read;
	private Date date;
	
	public ConversationListItem(String contactName, String conversationPreview, String contactImageUrl, Date date, boolean read)
	{
		this.contactName = contactName;
		this.conversationPreview = conversationPreview;
		this.contactImageUrl = contactImageUrl;
		this.date = date;
		this.read = read;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getConversationPreview() {
		return conversationPreview;
	}

	public void setConversationPreview(String conversationPreview) {
		this.conversationPreview = conversationPreview;
	}

	public String getContactImageUrl() {
		return contactImageUrl;
	}

	public void setContactImageUrl(String contactImageUrl) {
		this.contactImageUrl = contactImageUrl;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ConversationListItem [contactName=" + contactName
				+ ", conversationPreview=" + conversationPreview
				+ ", contactImageUrl=" + contactImageUrl + ", read=" + read
				+ ", date=" + date + "]";
	}

}
