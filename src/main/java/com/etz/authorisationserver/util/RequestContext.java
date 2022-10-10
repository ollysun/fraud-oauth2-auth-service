package com.etz.authorisationserver.util;


import lombok.extern.java.Log;

@Log
public class RequestContext {


    /**
     * ThreadLocal storage of username Strings.
     */
    private static final ThreadLocal<String> usernames = new ThreadLocal<>();

    private RequestContext() {

    }

    /**
     * Get the username for the current thread.
     *
     * @return A String username.
     */
    public static String getUsername() {
        return usernames.get();
    }

    /**
     * Set the username for the current thread.
     *
     * @param username A String username.
     */
    public static void setUsername(String username) {
        usernames.set(username);
        log.info("RequestContext added username {} to current thread " +
                username);
    }

    /**
     * Initialize the ThreadLocal attributes for the current thread.
     */
    public static void init() {
        usernames.set(null);
    }
}