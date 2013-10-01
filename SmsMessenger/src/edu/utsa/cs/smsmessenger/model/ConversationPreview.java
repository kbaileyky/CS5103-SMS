package edu.utsa.cs.smsmessenger.model;

/** ConversationPreview is a data model representing a conversation preview displayed in the ConversationsListActivity
 * 
 * @author mmadrigal
 *
 */
public class ConversationPreview {
	
	private String contactImgSrc;
	private String contactName;
	private String previewText;
	private boolean read;
	private long date;
	
	public ConversationPreview()
	{
		
	}

	public ConversationPreview(String contactImgSrc, String contactName,
			String previewText, boolean read, long date) {
		super();
		this.contactImgSrc = contactImgSrc;
		this.contactName = contactName;
		this.previewText = previewText;
		this.read = read;
		this.date = date;
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
				+ previewText + ", read=" + read + ", date=" + date + "]";
	}
	

}
