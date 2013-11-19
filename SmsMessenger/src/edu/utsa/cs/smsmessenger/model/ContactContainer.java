package edu.utsa.cs.smsmessenger.model;

/**
 * This class is a container to hold contact information.
 * 
 * @author Michael Madrigal
 * @version 1.0
 * @since 1.0
 * 
 */
public class ContactContainer {

	private String displayName;
	private String phoneNumber;
	private String photoUri;
	private long id;

	/**
	 * Construct initializes id to -1
	 * 
	 */
	public ContactContainer() {
		id = -1;
	}

	/**
	 * This method sets the display name for the contact
	 * 
	 * @param displayName
	 *            represent the display name to set for the contact
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * This method gets the display name for the contact.
	 * 
	 * @return returns the contact's display name.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * This method sets the phone number for the contact.
	 * 
	 * @param phoneNumber
	 *            represent the phone number to set for the contact
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * This method gets the phone number for the contact.
	 * 
	 * @return returns the contact's phone number.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * This method gets the contact's photo uri.
	 * 
	 * @return returns the contact's photo uri.
	 */
	public String getPhotoUri() {
		return this.photoUri;
	}

	/**
	 * This method sets the contact's uri.
	 * 
	 * @param photoUri
	 *            represent the photo uri to set for the contact's photo
	 */
	public void setPhotoUri(String photoUri) {
		this.photoUri = photoUri;
	}

	/**
	 * This method gets the contact's id.
	 * 
	 * @return returns the contact's id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * This method sets the contact's id.
	 * 
	 * @param id
	 *            represent the id to set for the contact
	 */
	public void setId(long id) {
		this.id = id;
	}

}
