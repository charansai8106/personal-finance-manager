package models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a registered user of the Personal Finance Manager application.
 * <p>
 * This class follows the principle of Encapsulation - all fields are private
 * and are accessed/modified only through public getter and setter methods.
 * It implements {@link Serializable} so that user objects can be written to
 * and read from files for persistent storage.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class User implements Serializable {

    // serialVersionUID is used to ensure compatibility during deserialization
    private static final long serialVersionUID = 1L;

    /** Unique username chosen by the user during registration. */
    private String username;

    /** Password used for authentication (stored as plain text for simplicity in this academic project). */
    private String password;

    /** Full name of the user. */
    private String fullName;

    /** Email address of the user. */
    private String email;

    /** Date and time when the user account was created. */
    private LocalDateTime registeredOn;

    /**
     * Constructs a new User with the given details.
     * The registration timestamp is automatically set to the current date and time.
     *
     * @param username the unique username for login
     * @param password the password for authentication
     * @param fullName the full name of the user
     * @param email    the email address of the user
     */
    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.registeredOn = LocalDateTime.now();
    }

    /**
     * Returns the username of this user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Updates the username of this user.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of this user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the password of this user.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the full name of this user.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Updates the full name of this user.
     *
     * @param fullName the new full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Returns the email address of this user.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the email address of this user.
     *
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the date and time when this user registered.
     *
     * @return the registration timestamp
     */
    public LocalDateTime getRegisteredOn() {
        return registeredOn;
    }

    /**
     * Verifies whether the given password matches this user's stored password.
     *
     * @param inputPassword the password to verify
     * @return {@code true} if the password matches, {@code false} otherwise
     */
    public boolean verifyPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }

    /**
     * Returns a string representation of the user (excluding the password for safety).
     *
     * @return a formatted string describing the user
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", registeredOn=" + registeredOn +
                '}';
    }
}
