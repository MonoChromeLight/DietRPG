package edu.odu.cs.zomp.dietapp.net.yummly;

import edu.odu.cs.zomp.dietapp.net.yummly.models.RecipeResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YummlyAPI {

    @GET("/recipes")
    Observable<RecipeResult> getRecipes(@Query("allowedDiet[]") String diet);
}
