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

	/**
	 * Constructs a ConversationPreview with contactId initialized to -1, and
	 * read set to false.
	 */
	public ConversationPreview() {
		this.contactId = -1;
		this.read = false;
	}

	/**
	 * Constructs a ConversationPreview with object fields set to the specified
	 * values.
	 */
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

	/**
	 * @return returns the phone number of the contact, and not the user, in the
	 *         conversation.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            represents the phone number of the contact, and not the user,
	 *            in the conversation.
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return returns the contact id for the contact, and not the user, in the
	 *         conversation.
	 */
	public int getContactId() {
		return contactId;
	}

	/**
	 * @param contactId
	 *            represents the contact id for the contact, and not the user,
	 *            in the conversation.
	 */
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	/**
	 * @return returns the image src of the contact, and not the user, in the
	 *         conversation.
	 */
	public String getContactImgSrc() {
		return contactImgSrc;
	}

	/**
	 * @param contactImgSrc
	 *            represents the image src of the contact, and not the user, in
	 *            the conversation.
	 */
	public void setContactImgSrc(String contactImgSrc) {
		this.contactImgSrc = contactImgSrc;
	}

	/**
	 * @return returns the contact name of the contact in the conversation.
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param contactName
	 *            represents the contact name of the contact in the
	 *            conversation.
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * @return returns preview text of the conversation, which is the latest
	 *         message body in the conversation.
	 */
	public String getPreviewText() {
		return previewText;
	}

	/**
	 * @param previewText
	 *            represents the preview text of the conversation, which is the
	 *            latest message body in the conversation.
	 */
	public void setPreviewText(String previewText) {
		this.previewText = previewText;
	}

	/**
	 * @return returns true if all messages in the conversation have been read,
	 *         returns false if one or more messages in the conversation have
	 *         not been read.
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * @param read
	 *            represents if all messages in a conversation have been read.
	 */
	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * @return returns a long that represents the date last message in the
	 *         conversation was sent or received.
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date
	 *            a long that represents the date the last message in the
	 *            conversation was sent or received.
	 */
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
