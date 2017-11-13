package edu.odu.cs.zomp.dietapp.data.models;


import java.util.HashMap;
import java.util.Map;

public class UserPrivate {

    public String id;
    public Diet activeDiet;
    public int gold;

    public UserPrivate() {
        this.id = null;
        this.activeDiet = null;
        this.gold = 0;
    }

    public UserPrivate(String id, Diet activeDiet, int gold) {
        this.id = id;
        this.activeDiet = activeDiet;
        this.gold = gold;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("activeDiet", activeDiet);
        map.put("gold", gold);
        return map;
    }
}
