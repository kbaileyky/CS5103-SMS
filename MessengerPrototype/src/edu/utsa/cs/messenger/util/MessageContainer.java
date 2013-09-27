package edu.utsa.cs.messenger.util;

import java.util.Date;


public class MessageContainer {
	
	public static final String MSG_DB_COL_THREAD_ID = "thread_id";
	public static final String MSG_DB_COL_ADDRESS = "address";
	public static final String MSG_DB_COL_PERSON = "person";
	public static final String MSG_DB_COL_DATE = "date";
	public static final String MSG_DB_COL_DATE_SENT = "date_sent";
	public static final String MSG_DB_COL_SUBJECT = "subject";
	public static final String MSG_DB_COL_BODY = "body";
	public static final String MSG_DB_COL_READ = "read";
	
	//These are all the fields we care about... I think
	private int threadId;
	private String address;
	private int person;  
	private Date date;
	private Date dateSent;
	private String subject;
	private String body;
	private boolean read;
	
	public MessageContainer()
	{
	}
	
	public MessageContainer(int threadId, String address, int person, Date date,
			Date dateSent, String subject, String body, boolean read) {
		super();
		this.threadId = threadId;
		this.address = address;
		this.person = person;
		this.date = date;
		this.dateSent = dateSent;
		this.subject = subject;
		this.body = body;
		this.read = read;
	}


	public int getThreadId() {
		return threadId;
	}
	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPerson() {
		return person;
	}
	public void setPerson(int person) {
		this.person = person;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDateSent() {
		return dateSent;
	}
	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
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

	@Override
	public String toString() {
		return "Message [threadId=" + threadId + ", address=" + address
				+ ", person=" + person + ", date=" + date + ", dateSent="
				+ dateSent + ", subject=" + subject + ", body=" + body
				+ ", read=" + read + "]";
	}
	
}
