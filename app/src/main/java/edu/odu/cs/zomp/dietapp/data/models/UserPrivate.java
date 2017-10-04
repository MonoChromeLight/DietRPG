package edu.odu.cs.zomp.dietapp.data.models;


import java.util.HashMap;
import java.util.Map;

public class UserPrivate {

    public String id;
    public String activeDietId;
    public int gold;

    public UserPrivate() {
        this.id = null;
        this.activeDietId = null;
        this.gold = 0;
    }

    public UserPrivate(String id, String activeDietId, int gold) {
        this.id = id;
        this.activeDietId = activeDietId;
        this.gold = gold;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("activeDietId", activeDietId);
        map.put("gold", gold);
        return map;
    }
}
