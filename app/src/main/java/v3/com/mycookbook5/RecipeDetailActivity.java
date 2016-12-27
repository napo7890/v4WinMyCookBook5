package v3.com.mycookbook5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import v3.com.mycookbook5.fragment.FavoritesFragment;
import v3.com.mycookbook5.fragment.RecipeFragment;
import v3.com.mycookbook5.models.Recipe;

public class RecipeDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RecipeDetailActivity";
    private ValueEventListener valueEventListener;
    private ListView ingredientsListView, directionsListView;
    private ArrayList<String> ingredientsArrayList, directionsArrayList;
    private ArrayAdapter<String> ingredientsAdapter, directionsAdapter;
    private TextView titleTextView, cookingTimeTextView, servingsTextView, descriptionTextView, dishTypeTextView, regionTextView, likeTextView;
    private ImageView recipeImageView;
    private FloatingActionButton likeButton;
    private Recipe r;
    private String imageUri, title, description, dishType;
    //private RatingBar recipeRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        setupToolbar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** Get recipe key from intent */
        mRecipeKey = getIntent().getStringExtra(EXTRA_RECIPE_KEY);
        if (mRecipeKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_RECIPE_KEY");
        }

        /** Initialize Database */
        databaseReference = FirebaseDatabase.getInstance().getReference().child("recipes").child(mRecipeKey);

        /** Initialize Views */
        titleTextView = (TextView) findViewById(R.id.activity_recipe_detail_title_text_view);
        cookingTimeTextView = (TextView) findViewById(R.id.activity_recipe_detail_cooking_time_text_view);
        servingsTextView = (TextView) findViewById(R.id.activity_recipe_detail_servings_text_view);
        likeTextView = (TextView) findViewById(R.id.activity_recipe_detail_likes_text_view);
        descriptionTextView = (TextView) findViewById(R.id.activity_recipe_detail_description_text_view);
        dishTypeTextView = (TextView) findViewById(R.id.activity_recipe_detail_dish_type_text_view);
        //regionTextView = (TextView) findViewById(R.id.activity_recipe_detail_region_text_view);
        ingredientsListView = (ListView) findViewById(R.id.ingredients_list_view);
        directionsListView = (ListView) findViewById(R.id.directions_list_view);
        recipeImageView = (ImageView) findViewById(R.id.activity_recipe_detail_image_view);
        likeButton = (FloatingActionButton) findViewById(R.id.activity_recipe_detail_like_fab);
        //recipeRatingBar = (RatingBar) findViewById(R.id.activity_recipe_detail_rating_bar);
        likeButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


        /** Add value event listener to the recipe */
        ValueEventListener recipeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /** Get Recipe object and use the values to update the UI */
                if (dataSnapshot.exists()) {
                    final Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    if (recipe.imageUrl.isEmpty()) {
                        recipeImageView.setImageResource(R.drawable.recipe_default_item);
                    } else {
                        Picasso.with(RecipeDetailActivity.this).load(recipe.imageUrl).resize(MAX_WIDTH, MAX_HEIGHT).rotate(90).into(recipeImageView);
                    }
                    titleTextView.setText(recipe.title);
                    if (recipe.cookingTime != 0) {
                        cookingTimeTextView.setText(recipe.cookingTime + " mins");
                    } else {
                        cookingTimeTextView.setText("Time");
                    }
                    if (recipe.servings != 0) {
                        servingsTextView.setText(recipe.servings + "");
                    } else {
                        servingsTextView.setText("Servings");
                    }
                    likeTextView.setText(recipe.like + "");
                    if (recipe.description != null) {
                        descriptionTextView.setText(recipe.description);
                    } else {
                        descriptionTextView.setText("About this recipe summary...");
                    }
                    if (recipe.dishType != null) {
                        dishTypeTextView.setText(recipe.dishType);
                    } else {
                        dishTypeTextView.setText(null);
                    }
                    if (recipe.like == 0){
                        likeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textBackgroundLightGray)));
                    }
//                    if (recipe.region != null) {
//                        regionTextView.setText(recipe.region);
//                    } else {
//                        regionTextView.setText(null);
//                    }
                    //recipeRatingBar.setRating(recipe.rating);
                    setIngredients(recipe);
                    setDirections(recipe);

                    imageUri = recipe.imageUrl;
                    cookingTime = recipe.cookingTime;
                    servings = recipe.servings;
                    title = recipe.title;
                    description = recipe.description;
                    dishType = recipe.dishType;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /** Getting Recipe failed, log a message */
                Log.w(TAG, "loadRecipe:onCancelled", databaseError.toException());
                Toast.makeText(RecipeDetailActivity.this, "Failed to load recipe.", Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addValueEventListener(recipeListener);

        /** Keep copy of recipe listener so we can remove it when app stops */
        valueEventListener = recipeListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        /** Remove recipe value event listener */
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    @Override
    public void onClick(View v) {
        onLikeClicked(databaseReference);
    }

    private void onLikeClicked(DatabaseReference mRecipeReference) {
        mRecipeReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                r = mutableData.getValue(Recipe.class);
                if (r == null) {
                    return Transaction.success(mutableData);
                }
                if (r.likeCount.containsKey(getUid())) {
                    /** Unlike the recipe and remove self from likeCount **/
                    r.like = r.like - 1;
                    r.likeCount.remove(getUid());
                    r.isLiked = false;
                } else {
                    /** Like the recipe and add self to likeCount **/
                    r.like = r.like + 1;
                    r.likeCount.put(getUid(), true);
                    r.isLiked = true;
                }
                /** Set value and report transaction success **/
                mutableData.setValue(r);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                /** Transaction completed **/
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                if (r.likeCount.containsKey(getUid())) {
                    Toast.makeText(RecipeDetailActivity.this, "Recipe was liked", Toast.LENGTH_SHORT).show();
                    likeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorHomeBottomLayout)));
                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Recipe was un-liked", Toast.LENGTH_SHORT).show();
                    likeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textBackgroundLightGray)));
                }
            }
        });
    }

    private void setIngredients(Recipe recipe) {
        ingredientsArrayList = new ArrayList<>();
        if (recipe.ingredients != null) {
            for (int i = 0; i < recipe.ingredients.size(); i++) {
                String ingredient = recipe.ingredients.get(i);
                ingredientsArrayList.add(ingredient);
            }
            ingredientsAdapter = new ArrayAdapter<String>(RecipeDetailActivity.this, android.R.layout.simple_list_item_1, ingredientsArrayList) {
            };
            ingredientsListView.setAdapter(ingredientsAdapter);
        }
    }

    private void setDirections(Recipe recipe) {
        directionsArrayList = new ArrayList<>();
        if (recipe.directions != null) {
            for (int i = 0; i < recipe.directions.size(); i++) {
                String direction = recipe.directions.get(i);
                directionsArrayList.add(direction);
            }
            directionsAdapter = new ArrayAdapter<String>(RecipeDetailActivity.this, android.R.layout.simple_list_item_1, directionsArrayList) {
            };
            directionsListView.setAdapter(directionsAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** Inflate the options menu from XML */
        getMenuInflater().inflate(R.menu.recipe_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /** Respond to the action bar's Up/Home button **/
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.delete_recipe:
                createDeleteAlertDialog();
            case R.id.update_recipe:
                sendDataToCreateRecipeActivity();
        }
        //return super.onOptionsItemSelected(item);
        return true;
    }

    public boolean createDeleteAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_recipe);
        //builder.setIcon(R.drawable.ic_action_delete);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /** remove recipe from list **/
                deleteRecipe();
            }
        });
        builder.setNegativeButton("NO", null);
        builder.show();
        return true;
    }

    private void deleteRecipe() {
        String key = mDatabase.child(mRecipeKey).getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/recipes/" + key, null);
        childUpdates.put("/users/" + getUid() + "/recipes/" + key, null);
        mDatabase.updateChildren(childUpdates);
        finish();
    }

    private void sendDataToCreateRecipeActivity() {
        String recipeKey = databaseReference.getKey();

        Intent intent = new Intent(this, CreateRecipeActivity.class);
        intent.putExtra(CreateRecipeActivity.RECIPE_KEY, recipeKey);
        intent.putExtra(CreateRecipeActivity.IMAGE, imageUri);
        intent.putExtra(CreateRecipeActivity.TITLE, title);
        intent.putExtra(CreateRecipeActivity.COOKING_TIME, cookingTime);
        intent.putExtra(CreateRecipeActivity.SERVINGS, servings);
        intent.putExtra(CreateRecipeActivity.DESCRIPTION, description);
        intent.putExtra(CreateRecipeActivity.DISH_TYPE, dishType);
        intent.putExtra(CreateRecipeActivity.INGREDIENTS, ingredientsArrayList);
        intent.putExtra(CreateRecipeActivity.DIRECTIONS, directionsArrayList);
        //setResult(RESULT_OK, intent);
        startActivityForResult(intent, UPDATE_REQUEST_CODE);
        //startActivityForResult(intent, DESCRIPTION_REQUEST_CODE);

    }

}
