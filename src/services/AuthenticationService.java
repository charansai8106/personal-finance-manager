package services;

import models.User;
import storage.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles user registration, login and logout for the Personal Finance
 * Manager application.
 * <p>
 * This service keeps an in-memory list of all registered {@link User}
 * objects (loaded from disk via {@link FileManager}) and tracks which
 * user, if any, is currently logged in.
 * </p>
 *
 * @author Personal Finance Manager Team
 * @version 1.0
 */
public class AuthenticationService {

    /** Handles reading/writing user data to disk. */
    private final FileManager fileManager;

    /** In-memory list of all registered users. */
    private final List<User> users;

    /** The user currently logged in, or {@code null} if nobody is logged in. */
    private User currentUser;

    /**
     * Constructs a new AuthenticationService, loading any previously
     * registered users from disk.
     *
     * @param fileManager the file manager used for persistence
     */
    public AuthenticationService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.users = new ArrayList<>(fileManager.loadUsers());
        this.currentUser = null;
    }

    /**
     * Registers a new user after collecting details from the console.
     * Rejects registration if the chosen username is already taken.
     *
     * @param scanner the {@link Scanner} used to read console input
     */
    public void register(Scanner scanner) {
        System.out.println("\n--- Register New Account ---");
        System.out.print("Enter desired username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty. Registration cancelled.");
            return;
        }
        if (findUserByUsername(username) != null) {
            System.out.println("This username is already taken. Please try again with a different username.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        User newUser = new User(username, password, fullName, email);
        users.add(newUser);
        fileManager.saveUsers(users);

        System.out.println("Registration successful! You can now log in, " + fullName + ".");
    }

    /**
     * Logs a user in by verifying their username and password against
     * stored records. On success, the user becomes the "current user"
     * for the rest of the session.
     *
     * @param scanner the {@link Scanner} used to read console input
     * @return {@code true} if login succeeded, {@code false} otherwise
     */
    public boolean login(Scanner scanner) {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User user = findUserByUsername(username);
        if (user == null || !user.verifyPassword(password)) {
            System.out.println("Invalid username or password. Please try again.");
            return false;
        }

        currentUser = user;
        System.out.println("Login successful! Welcome back, " + user.getFullName() + ".");
        return true;
    }

    /**
     * Logs the currently logged-in user out of the application.
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("Goodbye, " + currentUser.getFullName() + ". You have been logged out.");
            currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    /**
     * Checks whether a user is currently logged in.
     *
     * @return {@code true} if a user is logged in, {@code false} otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Returns the currently logged-in user.
     *
     * @return the current {@link User}, or {@code null} if nobody is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Searches the in-memory user list for a user with the given username.
     *
     * @param username the username to search for
     * @return the matching {@link User}, or {@code null} if not found
     */
    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }
}
