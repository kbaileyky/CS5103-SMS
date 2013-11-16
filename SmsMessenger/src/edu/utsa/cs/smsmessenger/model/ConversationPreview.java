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
public class ConversationPreview implements Comparable<ConversationPreview>{

	private String contactImgUri;
	private String contactName;
	private String previewText;
	private String phoneNumber;
	private int contactId;
	private long date;
	private int notReadCount;

	/**
	 * Constructs a ConversationPreview with contactId initialized to -1, and
	 * read set to false.
	 */
	public ConversationPreview() {
		this.contactId = -1;
		this.notReadCount = 0;
	}

	/**
	 * Constructs a ConversationPreview with object fields set to the specified
	 * values.
	 */
	public ConversationPreview(String contactImgUri, String contactName,
			String previewText, int notReadCount, long date,
			String phoneNumber, int contactId) {
		super();
		this.contactImgUri = contactImgUri;
		this.contactName = contactName;
		this.previewText = previewText;
		this.notReadCount = notReadCount;
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
	 * @return returns the image uri of the contact, and not the user, in the
	 *         conversation.
	 */
	public String getContactImgUri() {
		return contactImgUri;
	}

	/**
	 * @param contactImgSrc
	 *            represents the image uri of the contact, and not the user, in
	 *            the conversation.
	 */
	public void setContactImgUri(String contactImgUri) {
		this.contactImgUri = contactImgUri;
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

	/**
	 * @return returns an int that represents the number of unread messages from
	 *         the contact.
	 */
	public int getNotReadCount() {
		return notReadCount;
	}

	/**
	 * @param notReadCount
	 *            an int that represents that represents the number of unread
	 *            messages from the contact.
	 */
	public void setNotReadCount(int notReadCount) {
		this.notReadCount = notReadCount;
	}

	/**
	 * @param increment
	 *            a boolean that determines if notReadCount should be
	 *            incremented.
	 */
	public void incremtNotReadCount(boolean increment) {
		if(increment)
			this.notReadCount++;
	}

	@Override
	public String toString() {
		return "ConversationPreview [contactImgUri=" + contactImgUri
				+ ", contactName=" + contactName + ", previewText="
				+ previewText + ", phoneNumber=" + phoneNumber + ", contactId="
				+ contactId + ", date=" + date + ", notReadCount="
				+ notReadCount + "]";
	}

	@Override
	public int compareTo(ConversationPreview another) {
		if (this.getDate() > another.getDate())
			return 1;
		if (this.getDate() < another.getDate())
			return -1;
		return 0;
	}

}
