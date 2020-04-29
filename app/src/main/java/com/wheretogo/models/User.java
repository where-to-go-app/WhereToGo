package com.wheretogo.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String firstName;
    private String lastName;
    private int clientId;
    private String token;

    public User(String firstName, String lastName, int clientId, String token) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientId = clientId;
        this.token = token;
    }

    public User() {

    }

    public static User parse(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.setClientId(jsonObject.getInt("id"));
        user.setFirstName(jsonObject.getString("first_name"));
        user.setLastName(jsonObject.getString("last_name"));
        return user;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getClientId() {
        return clientId;
    }

    public String getToken() {
        return token;
    }
}
