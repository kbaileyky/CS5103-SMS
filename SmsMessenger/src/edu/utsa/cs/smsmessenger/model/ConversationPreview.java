package edu.utsa.cs.smsmessenger.model;

/**
 * This class is a data model representing a conversation preview displayed in
 * the ConversationsListActivity
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class ConversationPreview {

	private String contactImgSrc;
	private String contactName;
	private String previewText;
	private String phoneNumber;
	private int contactId;
	private boolean read;
	private long date;

	public ConversationPreview() {
		this.contactId = -1;
		this.read = false;
	}

	public ConversationPreview(String contactImgSrc, String contactName,
			String previewText, boolean read, long date, String phoneNumber,
			int contactId) {
		super();
		this.contactImgSrc = contactImgSrc;
		this.contactName = contactName;
		this.previewText = previewText;
		this.read = read;
		this.date = date;
		this.phoneNumber = phoneNumber;
		this.contactId = contactId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getContactImgSrc() {
		return contactImgSrc;
	}

	public void setContactImgSrc(String contactImgSrc) {
		this.contactImgSrc = contactImgSrc;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getPreviewText() {
		return previewText;
	}

	public void setPreviewText(String previewText) {
		this.previewText = previewText;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ConversationPreview [contactImgSrc=" + contactImgSrc
				+ ", contactName=" + contactName + ", previewText="
				+ previewText + ", phoneNumber=" + phoneNumber + ", contactId="
				+ contactId + ", read=" + read + ", date=" + date + "]";
	}

}
