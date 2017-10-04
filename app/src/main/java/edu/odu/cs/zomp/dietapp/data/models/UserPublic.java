package edu.odu.cs.zomp.dietapp.data.models;


import java.util.HashMap;
import java.util.Map;

public class UserPublic {

    public String id;
    public String name;

    public UserPublic() {
        this.id = null;
        this.name = null;
    }

    public UserPublic(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        return map;
    }
}
