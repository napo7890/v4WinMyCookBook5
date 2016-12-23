package v3.com.mycookbook5.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import v3.com.mycookbook5.R;
import v3.com.mycookbook5.models.Recipe;


public class RecipeHolder extends RecyclerView.ViewHolder {

    //    public static final int MAX_WIDTH = 200;
//    public static final int MAX_HEIGHT = 150;
    public Context context;

    public TextView titleTextView, cookingTimeTextView, servingsTextView, likeTextView, descriptionTextView;
    public ImageView recipeImageView;
    //public RatingBar ratingBar;


    public RecipeHolder(View itemView) {
        super(itemView);
        recipeImageView = (ImageView) itemView.findViewById(R.id.item_recipe_image);
        titleTextView = (TextView) itemView.findViewById(R.id.item_recipe_title);
        //ratingBar = (RatingBar) itemView.findViewById(R.id.item_recipe_rating);
        cookingTimeTextView = (TextView) itemView.findViewById(R.id.item_recipe_cooking_time);
        servingsTextView = (TextView) itemView.findViewById(R.id.item_recipe_servings);
        likeTextView = (TextView) itemView.findViewById(R.id.item_recipe_like_text_view);
        descriptionTextView = (TextView) itemView.findViewById(R.id.item_recipe_description);
    }

    public void bindToRecipe(Recipe recipe) {
        if (recipe.imageUrl.isEmpty()) {
            recipeImageView.setImageResource(R.drawable.recipe_default_item);
        } else if (!recipe.imageUrl.contains("http")) {
            try {
                Bitmap imageBitmap = decodeFromFirebaseBase64(recipe.imageUrl);
                recipeImageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Picasso.with(itemView.getContext())
                    .load(recipe.imageUrl)
                    .fit()
                    //.resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .rotate(90)
                    .into(recipeImageView);
        }
        titleTextView.setText(recipe.title);
        if (recipe.cookingTime != 0){
            cookingTimeTextView.setText(recipe.cookingTime + " mins");
        } else{
            cookingTimeTextView.setText("Time");
        }
        if (recipe.servings != 0){
            servingsTextView.setText(recipe.servings + "");
        }else{
            servingsTextView.setText("Servings");
        }
        likeTextView.setText(recipe.like + "");

        if (recipe.description != null){
            descriptionTextView.setText(recipe.description);
        } else{
            descriptionTextView.setText("About this recipe summary...");
        }
        //ratingBar.setRating(recipe.rating);
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodeByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodeByteArray, 0, decodeByteArray.length);
    }
}
