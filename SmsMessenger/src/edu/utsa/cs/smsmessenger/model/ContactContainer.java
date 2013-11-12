package edu.utsa.cs.smsmessenger.model;

import android.net.Uri;
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
    private Uri photoUri;

	/**
	 * @param displayName
	 *            represent the display name to set for the contact
	 */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

	/**
	 * @return returns the contact's display name.
	 */
    public String getDisplayName() {
        return displayName;
    }
	/**
	 * @param phoneNumber
	 *            represent the phone number to set for the contact
	 */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

	/**
	 * @return returns the contact's phone number.
	 */
    public String getPhoneNumber() {
        return phoneNumber;
    }

	/**
	 * @return returns the contact's photo uri.
	 */
    public Uri getPhotoUri() {
        return this.photoUri;
    }

	/**
	 * @param photoUri
	 *            represent the photo uri to set for the contact's photo
	 */
    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

}
