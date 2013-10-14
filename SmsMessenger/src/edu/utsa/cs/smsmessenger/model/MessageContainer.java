package edu.utsa.cs.smsmessenger.model;

/**
 * This class is a data model representing a message.
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class MessageContainer implements Comparable<MessageContainer> {

	private long id;
	private String phoneNumber;
	private int contactId;
	private long date;
	private String subject;
	private String body;
	private boolean read;
	private String status;
	private String type;
	private boolean saved;

	/**
	 * Constructs a MessageContainer with id set to -1, contactId initialized to
	 * -1, saved set to false, read set to false. Warning: the type field must
	 * be set to SmsMessengerHandler.MSG_TYPE_OUT or
	 * SmsMessengerHandler.MSG_TYPE_IN prior to saving MessageContainer to the
	 * database.
	 */
	public MessageContainer() {
		this.id = -1;
		this.contactId = -1;
		this.saved = false;
		this.read = false;
	}

	/**
	 * Constructs a MessageContainer with id set to -1, contactId initialized to
	 * -1, saved set to false, read set to false, and type set to the specified
	 * value. Warning: the type field should only be set to
	 * SmsMessengerHandler.MSG_TYPE_OUT or SmsMessengerHandler.MSG_TYPE_IN
	 * prior.
	 */
	public MessageContainer(String type) {
		this.type = type;
		this.id = -1;
		this.contactId = -1;
		this.saved = false;
		this.read = false;
	}

	@Override
	public int compareTo(MessageContainer another) {
		if (this.getDate() > another.getDate())
			return 1;
		if (this.getDate() < another.getDate())
			return -1;
		return 0;
	}

	@Override
	public String toString() {
		return "MessageContainer [id=" + id + ", phoneNumber=" + phoneNumber
				+ ", contactId=" + contactId + ", date=" + date + ", subject="
				+ subject + ", body=" + body + ", read=" + read + ", status="
				+ status + ", type=" + type + ", saved=" + saved + "]";
	}

	/**
	 * @return returns true if the message has been saved to the database,
	 *         returns false if it has not been saved to the database.
	 */
	public boolean isSaved() {
		return saved;
	}

	/**
	 * @param saved
	 *            represents if the message has been saved to the database.
	 */
	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	/**
	 * @return returns the message's respective database table id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            represent the respective database table id.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return returns the status of the message. Message status can be
	 *         SmsMessageHandler.SMS_DRAFT, SmsMessageHandler.SMS_SENT,
	 *         SmsMessageHandler.SMS_DELIVERED, SmsMessageHandler.SMS_FAILED,
	 *         SmsMessageHandler.SMS_RECEIVED
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            represent the message status. Message status can be
	 *            SmsMessageHandler.SMS_DRAFT, SmsMessageHandler.SMS_SENT,
	 *            SmsMessageHandler.SMS_DELIVERED, SmsMessageHandler.SMS_FAILED,
	 *            SmsMessageHandler.SMS_RECEIVED
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return returns for both incoming and outgoing messages, returns the
	 *         phone number of the contact and not the user.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            for both incoming and outgoing messages, represents the phone
	 *            number of the contact and not the user.
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return for both incoming and outgoing messages, returns the contact id
	 *         for the contact and not the user.
	 */
	public int getContactId() {
		return contactId;
	}

	/**
	 * @param contactId
	 *            for both incoming and outgoing messages, represents the
	 *            contact id for the contact and not the user.
	 */
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	/**
	 * @return returns an long representing the date the message was sent or
	 *         received.
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date
	 *            a long that represents the date the message is sent or
	 *            received.
	 */
	public void setDate(long date) {
		this.date = date;
	}

	/**
	 * @return returns the subject of the message.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            represents the subject of the message.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return returns the body of the message.
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            represents the body of the message.
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return returns true if the message has been read by the user, returns
	 *         false if has not.
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * @param read
	 *            represents if the message has been read by the user.
	 */
	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * @return returns the message type. Message type can be
	 *         SmsMessengerHandler.MSG_TYPE_OUT or
	 *         SmsMessengerHandler.MSG_TYPE_IN which also corresponds to the
	 *         database table name to which the message is saved.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            represents the message type. Message type can be
	 *            SmsMessengerHandler.MSG_TYPE_OUT or
	 *            SmsMessengerHandler.MSG_TYPE_IN which also corresponds to the
	 *            database table name to which the message is saved.
	 */
	public void setType(String type) {
		this.type = type;
	}

}
