package com.wheretogo.data.remote;

import com.vk.api.sdk.requests.VKRequest;
import com.wheretogo.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VkClientRequest extends VKRequest<User> {
    public VkClientRequest() {
        super("users.get");
    }

    public User parse(JSONObject r) throws JSONException {
        JSONArray users = r.getJSONArray("response");
        JSONObject jsonUser = users.getJSONObject(0);
        return User.parse(jsonUser);
    }
}
