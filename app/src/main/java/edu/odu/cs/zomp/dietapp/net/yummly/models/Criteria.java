package edu.odu.cs.zomp.dietapp.net.yummly.models;

import java.util.List;

public class Criteria {

    private List<String> allowedDiet = null;
    private Object q;
    private Object allowedIngredient;
    private Object excludedIngredient;

    public List<String> getAllowedDiet() {
        return allowedDiet;
    }

    public void setAllowedDiet(List<String> allowedDiet) {
        this.allowedDiet = allowedDiet;
    }

    public Object getQ() {
        return q;
    }

    public void setQ(Object q) {
        this.q = q;
    }

    public Object getAllowedIngredient() {
        return allowedIngredient;
    }

    public void setAllowedIngredient(Object allowedIngredient) {
        this.allowedIngredient = allowedIngredient;
    }

    public Object getExcludedIngredient() {
        return excludedIngredient;
    }

    public void setExcludedIngredient(Object excludedIngredient) {
        this.excludedIngredient = excludedIngredient;
    }
}