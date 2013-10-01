package edu.utsa.cs.smsmessenger.model;


/** MessageContainer is a data model representing a message
 * 
 * @author mmadrigal
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
	
	public MessageContainer()
	{
		this.contactId = -1;
	}

	public MessageContainer(long id, String phoneNumber, int contactId, long date,
			String subject, String body, boolean read, String status, String type) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.contactId = contactId;
		this.date = date;
		this.subject = subject;
		this.body = body;
		this.read = read;
		this.status = status;
		this.type = type;
	}

	@Override
	public int compareTo(MessageContainer another) {
		if(this.getDate()>another.getDate())
			return 1;
		if(this.getDate()<another.getDate())
			return -1;
		return 0;
	}
	
	@Override
	public String toString() {
		return "MessageContainer [id=" + id + ", phoneNumber=" + phoneNumber
				+ ", contactId=" + contactId + ", date=" + date + ", subject="
				+ subject + ", body=" + body + ", read=" + read + ", status="
				+ status + "]";
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
