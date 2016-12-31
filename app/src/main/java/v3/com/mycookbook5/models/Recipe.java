package v3.com.mycookbook5.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Recipe {

    public String uid, imageUrl, username, title, description, dishType, recipeUid;
    public int cookingTime, servings /*, rating*/, like;
    public Boolean isLiked;
    public Map<String, Boolean> likeCount = new HashMap<>();
    public ArrayList<String> ingredients, directions;

    public Recipe() {
        // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    }

    public Recipe(/*String recipeUid,*/ String uid, String username, String imageUrl, String title, int cookingTime, int servings, String description /*, int rating */, String dishType, ArrayList<String> ingredients, ArrayList<String> directions) {
        //this.recipeUid = recipeUid;
        this.uid = uid;
        //this.isUser = isUser;
        //this.username = username;
        this.imageUrl = imageUrl;
        //this.imageUrl = getLargeImageUrl(imageUrl);
        this.title = title;
        this.cookingTime = cookingTime;
        this.servings = servings;
        this.description = description;
        //this.rating = rating;
        this.dishType = dishType;
        this.ingredients = ingredients;
        this.directions = directions;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        //result.put("recipeUid", recipeUid);
        result.put("uid", uid);
        result.put("username", username);
        result.put("imageUrl", imageUrl);
        result.put("title", title);
        result.put("cookingTime", cookingTime);
        result.put("servings", servings);
        result.put("like", like);
        result.put("isLiked", isLiked);
        result.put("description", description);
        //result.put("rating", rating);
        result.put("dishType", dishType);
        result.put("ingredients", ingredients);
        result.put("directions", directions);
        result.put(uid, true);
        return result;
    }
//    public String getLargeImageUrl(String imageUrl) {
//        String largeImageUrl = imageUrl.substring(0, imageUrl.length() - 6).concat("o.jpg");
//        return largeImageUrl;
//    }

//    public boolean isUserRecipe(){
//        return false;
//    }
}
