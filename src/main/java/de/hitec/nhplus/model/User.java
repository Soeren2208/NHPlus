package de.hitec.nhplus.model;

/**
 * Users are persons who are authorized to use the NHPlus system.
 * Each user has a username and a (hashed) password to authenticate.
 */
public class User {
    private int id;
    private String username;
    private String password;

    /**
     * Constructor to initiate an object of class <code>User</code> with the given parameters.
     * Use this constructor to initiate objects which are already persisted and have a user id.
     *
     * @param id       Unique identifier of the user.
     * @param username Username of the user.
     * @param password (Hashed) password of the user.
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the user id
     *
     * @return User id as int.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the username of the user.
     *
     * @return Username as string.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the (hashed) password of the user.
     *
     * @return Password as string.
     */
    public String getPassword() {
        return password;
    }
}
