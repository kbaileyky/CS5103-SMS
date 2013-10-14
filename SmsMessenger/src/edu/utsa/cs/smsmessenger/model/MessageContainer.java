package edu.utsa.cs.smsmessenger.model;

/**
 * This class is a data model representing a message
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

	public MessageContainer() {
		this.contactId = -1;
		this.saved = false;
		this.read = false;
	}

	public MessageContainer(String type) {
		this.type = type;
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

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
