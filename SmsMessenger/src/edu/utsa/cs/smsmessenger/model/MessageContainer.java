package edu.utsa.cs.smsmessenger.model;

import java.util.Date;

/** MessageContainer is a data model representing a message
 * 
 * @author mmadrigal
 *
 */
public class MessageContainer {

	private int id;
	private String phoneNumber;
	private int contactId;  
	private Date date;
	private String subject;
	private String body;
	private boolean read;
	private String status;
	
	public MessageContainer()
	{
		
	}

	public MessageContainer(int id, String phoneNumber, int contactId, Date date,
			String subject, String body, boolean read, String status) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.contactId = contactId;
		this.date = date;
		this.subject = subject;
		this.body = body;
		this.read = read;
		this.status = status;
	}

	@Override
	public String toString() {
		return "MessageContainer [id=" + id + ", phoneNumber=" + phoneNumber
				+ ", contactId=" + contactId + ", date=" + date + ", subject="
				+ subject + ", body=" + body + ", read=" + read + ", status="
				+ status + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
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
	
}
