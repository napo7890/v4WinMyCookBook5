package v3.com.mycookbook5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import v3.com.mycookbook5.models.DishType;
import v3.com.mycookbook5.models.Recipe;
import v3.com.mycookbook5.models.User;
import v3.com.mycookbook5.viewholder.HintAdapter;

import static android.R.attr.description;
import static android.R.attr.theme;
import static android.R.attr.track;

public class CreateRecipeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "CreateRecipeActivity";
    private static final String REQUIRED = "Required";
    private static final int RC_TAKE_PICTURE = 101;
    private static final int RC_STORAGE_PERMS = 102;
    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_DOWNLOAD_URL = "key_download_url";
    private Uri mDownloadUrl, mFileUri = null;
    private StorageReference mStorageRef;
    private TextView recipeCookingTimeTextView, recipeServingsTextView, recipeDescriptionTextView;
    private EditText recipeTitleEditText, recipeIngredientEditText, recipeDirectionsEditText;
    //private RatingBar recipeRating;
    private Button saveRecipeBtn;
    private ImageView recipeIngredientsImageView, recipeImageView, recipeDirectionsImageView;
    private LinearLayout descriptionLayout, ingredientsLayout, ingredientsRowLayout, directionsLayout, directionsRowLayout;
    private Spinner spinner;
    private String ingredientItem, directionItem, selectedDishType, recipeKey;
    private ArrayList<String> ingredientsArrayList, directionsArrayList;
    private ArrayList<DishType> dishType;
    private LinearLayout.LayoutParams layoutParams, params, Params2;
    private HintAdapter adapter;
    private Recipe recipe;
    private boolean descriptionFirstClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        recipeCookingTimeTextView = (TextView) findViewById(R.id.create_activity_recipe_cooking_time_text_view);
        recipeServingsTextView = (TextView) findViewById(R.id.create_activity_recipe_servings_text_view);
        recipeDescriptionTextView = (TextView) findViewById(R.id.create_activity_recipe_description_text_view);
        recipeTitleEditText = (EditText) findViewById(R.id.create_recipe_activity_title_text_view);
        recipeImageView = (ImageView) findViewById(R.id.create_recipe_activity_image_view);
        //recipeRating = (RatingBar) findViewById(R.id.create_recipe_activity_rating_bar);
        descriptionLayout = (LinearLayout) findViewById(R.id.create_recipe_activity_description_layout);
        ingredientsLayout = (LinearLayout) findViewById(R.id.activity_create_recipe_ingredients_layout);
        directionsLayout = (LinearLayout) findViewById(R.id.activity_create_recipe_directions_layout);
        spinner = (Spinner) findViewById(R.id.spinner);

        initDishTypeList();

        adapter = new HintAdapter(this, dishType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
        spinner.setOnItemSelectedListener(this);

        recipeImageView.setOnClickListener(this);
        //recipeRating.setOnRatingBarChangeListener(this);
        saveRecipeBtn = (Button) findViewById(R.id.saveRecipeBtn);
        saveRecipeBtn.setOnClickListener(this);
        descriptionLayout.setOnClickListener(this);

        ingredientsArrayList = new ArrayList<>();
        directionsArrayList = new ArrayList<>();

        /** Initialize database references **/
        databaseReference = FirebaseDatabase.getInstance().getReference();

        /** Initialize Firebase Storage Ref, start get_storage_ref **/
        mStorageRef = FirebaseStorage.getInstance().getReference();

        /** Restore instance state **/
        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            mDownloadUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
        }
        setParams();
        createNewIngredientsLayout();
        createNewDirectionsLayout();
        getDataFromRecipeDetailActivity();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: resize image to get it load faster. .resize(RecipeHolder.MAX_WIDTH, RecipeHolder.MAX_HEIGHT)
        if (requestCode == RC_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                if (mFileUri != null) {
                    uploadFromUri(mFileUri);
                    Picasso.with(this)
                            .load(mFileUri)
                            .fit()
                            .centerCrop()
                            .rotate(90)
                            .into(recipeImageView);
                } else {
                }
            } else {
                Toast.makeText(this, "Taking picture failed.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == DESCRIPTION_REQUEST_CODE && resultCode == RESULT_OK) {
            cookingTime = data.getIntExtra(COOKING_TIME, 0);
            servings = data.getIntExtra(SERVINGS, 0);
            description = data.getStringExtra(DESCRIPTION).trim();
            if (cookingTime != 0) {
                recipeCookingTimeTextView.setText(cookingTime + " mins");
            } else {
                recipeCookingTimeTextView.setText("Time");
            }
            if (servings != 0) {
                recipeServingsTextView.setText(servings + "");
            } else {
                recipeServingsTextView.setText("Servings");
            }
            if (!description.isEmpty()) {
                recipeDescriptionTextView.setText(description);
            } else {
                recipeDescriptionTextView.setText("About this recipe summary...");
            }
        }
    }

    private void uploadFromUri(Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        /** Get child reference, get a reference to store file at photos/<FILENAME>.jpg **/
        final StorageReference photoRef = mStorageRef.child("photos")
                .child(fileUri.getLastPathSegment());

        /** Upload file to Firebase Storage **/
        showProgressDialog();
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        photoRef.putFile(fileUri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        /** Upload succeeded **/
                        Log.d(TAG, "uploadFromUri:onSuccess");

                        /** Get the public download URL **/
                        mDownloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                        hideProgressDialog();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        /** Upload failed **/
                        Log.w(TAG, "uploadFromUri:onFailure", e);

                        mDownloadUrl = null;

                        hideProgressDialog();
                        Toast.makeText(CreateRecipeActivity.this, "Error: upload failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @AfterPermissionGranted(RC_STORAGE_PERMS)
    private void launchCamera() {
        Log.d(TAG, "launchCamera");
        /** Check that we have permission to read images from external storage. **/
        String perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_STORAGE_PERMS, perm);
            return;
        }

        /** Choose file storage location, must be listed in res/xml/file_paths **/
        File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
        File file = new File(dir, UUID.randomUUID().toString() + ".jpg");
        try {
            /** Create directory if it does not exist **/
            if (!dir.exists()) {
                dir.mkdir();
            }
            boolean created = file.createNewFile();
            Log.d(TAG, "file.createNewFile:" + file.getAbsolutePath() + ":" + created);
        } catch (IOException e) {
            Log.e(TAG, "file.createNewFile:" + file.getAbsolutePath() + ":FAILED", e);
        }
        /** Create content:// URI for file, required since Android N **/
        mFileUri = FileProvider.getUriForFile(this,
                "v3.com.mycookbook5.fileprovider", file);


        /** Create and launch intent **/
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
        startActivityForResult(takePictureIntent, RC_TAKE_PICTURE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_recipe_activity_image_view:
                launchCamera();
                break;
            case R.id.saveRecipeBtn:
                submitRecipe();
                break;
            case R.id.create_recipe_activity_description_layout:
                writeDescription();
                break;
        }
    }

    private void writeDescription() {
        Intent intent = new Intent(this, RecipeDescriptionActivity.class);
        if (!descriptionFirstClick) {
            intent.putExtra(RecipeDescriptionActivity.COOKING_TIME, cookingTime);
            intent.putExtra(RecipeDescriptionActivity.SERVINGS, servings);
            intent.putExtra(RecipeDescriptionActivity.DESCRIPTION, description);
        }
        startActivityForResult(intent, DESCRIPTION_REQUEST_CODE);
        descriptionFirstClick = false;


    }

    private void setParams() {
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 9;

        Params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        Params2.weight = 1;
    }

    private void createNewIngredientsLayout() {
        addNewIngredientsLayout();
        recipeIngredientEditText.setHint("Ingredient(s)");
        recipeIngredientsImageView.setImageResource(R.drawable.add_item);

        for (int i = 0; i < ingredientsLayout.getChildCount(); i++) {
            final int finalI = i;
            recipeIngredientsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ingredientItem = recipeIngredientEditText.getText().toString().trim();
                    ingredientsArrayList.add(ingredientItem);
                    Log.d(TAG, "ingredient was added" + ingredientsArrayList);
                    recipeIngredientsImageView.setImageResource(R.drawable.ic_close_grey600_18dp);
                    recipeIngredientsImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeIngredientItem(finalI);
                        }
                    });
                    createNewIngredientsLayout();
                }

            });
        }
    }

    private void removeIngredientItem(int finalI) {
        ingredientsArrayList.remove(ingredientItem);
        if (ingredientsLayout.getChildCount() > 1) {
            ingredientsLayout.removeViewAt(finalI);
            Log.d(TAG, "ingredient was removed" + ingredientsArrayList);
        } else {
            recipeIngredientsImageView.setImageResource(R.drawable.add_item);
        }
    }

    private void createNewDirectionsLayout() {
        addNewDirectionsLayout();
        recipeDirectionsEditText.setHint("Step(s)");
        recipeDirectionsImageView.setImageResource(R.drawable.add_item);

        for (int i = 0; i < directionsLayout.getChildCount(); i++) {
            final int finalI = i;
            recipeDirectionsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    directionItem = recipeDirectionsEditText.getText().toString().trim();
                    directionsArrayList.add(directionItem);
                    //Log.d(TAG, "ingredient was added" + ingredientsArrayList);
                    recipeDirectionsImageView.setImageResource(R.drawable.ic_close_grey600_18dp);
                    recipeDirectionsImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeDirectionItem(finalI);
                        }
                    });
                    createNewDirectionsLayout();
                }
            });
        }
    }

    private void removeDirectionItem(int finalI) {
        directionsArrayList.remove(directionItem);
        if (directionsLayout.getChildCount() > 1) {
            directionsLayout.removeViewAt(finalI);
            //Log.d(TAG, "ingredient was removed" + directionsArrayList);
        } else {
            recipeDirectionsImageView.setImageResource(R.drawable.add_item);
        }
    }

    private void submitRecipe() {
        if (mDownloadUrl == null) {
            mDownloadUrl = Uri.parse("");
        }
        if (selectedDishType == getString(R.string.dish_type_select)) {
            selectedDishType = null;
        }
        final String imageUrl = mDownloadUrl.toString();
        final String title = recipeTitleEditText.getText().toString().trim();
        final String recipeDescription = description;
        final String recipeDishType = selectedDishType;
        final int recipeCookingTime = cookingTime;
        final int recipeServings = servings;
        //final int rating = (int) recipeRating.getRating();
        final ArrayList<String> recipeIngredients = ingredientsArrayList;
        final ArrayList<String> recipeDirections = directionsArrayList;

        for (int i = 0; i < ingredientsRowLayout.getChildCount() - 1; i++) {
            if (!ingredientsArrayList.isEmpty()) {
                ingredientItem = ingredientsArrayList.get(i);
            }
        }
        for (int i = 0; i < directionsRowLayout.getChildCount() - 1; i++) {
            if (!directionsArrayList.isEmpty()) {
                directionItem = directionsArrayList.get(i);
            }
        }
        /** Title is required */
        if (TextUtils.isEmpty(title)) {
            recipeTitleEditText.setError(REQUIRED);
            return;
        }
        /** single_value_read */
        final String userId = getUid();
        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        /** Get user value */
                        User user = dataSnapshot.getValue(User.class);
                        /** Start exclude */
                        if (user == null) {
                            /** User is null, error out */
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(CreateRecipeActivity.this, "Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                        } else if (recipeKey == null) {
                            /** Write new recipe */
                            writeNewRecipe(userId, user.username, imageUrl, title, recipeCookingTime, recipeServings, recipeDescription, recipeDishType, recipeIngredients, recipeDirections);
                        } else {
                            updateRecipe(userId, user.username, imageUrl, title, recipeCookingTime, recipeServings, recipeDescription, recipeDishType, recipeIngredients, recipeDirections);
                        }

                        /** Finish this Activity, back to the stream */
                        //finish();
                        Intent intent = new Intent(CreateRecipeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                }
        );
    }

    private void writeNewRecipe(String userId, String username, String imageUrl, String title, int cookingTime, int servings, String description, String dishType, ArrayList<String> ingredients, ArrayList<String> directions) {
        String key = databaseReference.child("recipes").push().getKey();
        recipe = new Recipe(userId, username, imageUrl, title, cookingTime, servings, description, dishType, ingredients, directions);
        Map<String, Object> recipeValues = recipe.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/recipes/" + key, recipeValues);
        databaseReference.updateChildren(childUpdates);


    }

    private void updateRecipe(String userId, String username, String imageUrl, String title, int cookingTime, int servings, String description, String dishType, ArrayList<String> ingredients, ArrayList<String> directions) {
        String key = databaseReference.child("recipes").child(recipeKey).getKey();
        recipe = new Recipe(userId, username, imageUrl, title, cookingTime, servings, description, dishType, ingredients, directions);
        Map<String, Object> recipeValues = recipe.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/recipes/" + key, recipeValues);
        databaseReference.updateChildren(childUpdates);
    }
//    @Override
//    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//    }

    private void initDishTypeList() {
        dishType = new ArrayList<>();
        dishType.add(new DishType(getString(R.string.dish_type_starter)));
        dishType.add(new DishType(getString(R.string.dish_type_main_course)));
        dishType.add(new DishType(getString(R.string.dish_type_dessert)));
        dishType.add(new DishType(getString(R.string.dish_type_other)));
        dishType.add(new DishType(getString(R.string.dish_type_select)));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (dishType.get(position).getDishType() != dishType.get(4).toString()) {
            selectedDishType = dishType.get(position).getDishType();

        } else {
            selectedDishType = "";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void getDataFromRecipeDetailActivity() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            recipeKey = bundle.getString(RECIPE_KEY);
            String image = bundle.getString(IMAGE);
            String set_title = bundle.getString(TITLE);
            int set_cooking_time = bundle.getInt(COOKING_TIME);
            int set_serving = bundle.getInt(SERVINGS);
            String set_description = bundle.getString(DESCRIPTION);
            String set_dish_type = bundle.getString(DISH_TYPE);

            recipeTitleEditText.setText(set_title);

            if (set_cooking_time != 0) {
                recipeCookingTimeTextView.setText(set_cooking_time + " mins");
            } else {
                recipeCookingTimeTextView.setText(null);
            }

            if (set_serving != 0) {
                recipeServingsTextView.setText(set_serving + "");
            } else {
                recipeServingsTextView.setText(null);
            }

            recipeDescriptionTextView.setText(set_description);

            setRecipeImage(image);
            setDishType(set_dish_type);
            setIngredients();
            setDirections();

            cookingTime = set_cooking_time;
            servings = set_serving;
            description = set_description;
            descriptionFirstClick = false;
        }
    }

    private void setRecipeImage(String image) {
        Uri uri = Uri.parse(image);
        if (image.isEmpty()) {
            recipeImageView.setImageResource(R.drawable.recipe_default_item);
        } else {
            Picasso.with(this).load(uri).resize(MAX_WIDTH, MAX_HEIGHT).rotate(90).into(recipeImageView);
            mDownloadUrl = Uri.parse(image);
        }
    }

    private void setDishType(String dish_type) {
        if (!dish_type.isEmpty()) {
            spinner.setSelection(adapter.getIndex(spinner, dish_type));
            selectedDishType = dish_type;

        } else {
            spinner.setSelection(adapter.getCount());
        }

    }

    private void setIngredients() {
        ArrayList<String> ingredientsList = (ArrayList<String>) getIntent().getSerializableExtra(INGREDIENTS);
        if (!ingredientsList.isEmpty()) {
            ingredientsLayout.removeAllViews();
            ingredientsRowLayout.removeAllViews();
            for (int i = 0; i < ingredientsList.size(); i++) {
                final int finalI = i;
                addNewIngredientsLayout();
                recipeIngredientEditText.setText(ingredientsList.get(i));
                ingredientItem = recipeIngredientEditText.getText().toString().trim();
                ingredientsArrayList.add(ingredientItem);
                recipeIngredientsImageView.setImageResource(R.drawable.ic_close_grey600_18dp);
                recipeIngredientsImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeIngredientItem(finalI);
                    }
                });
            }
        }
    }

    private void setDirections() {
        ArrayList<String> directionsList = (ArrayList<String>) getIntent().getSerializableExtra(DIRECTIONS);
        if (!directionsList.isEmpty()) {
            directionsLayout.removeAllViews();
            directionsRowLayout.removeAllViews();
            for (int i = 0; i < directionsList.size(); i++) {
                final int finalI = i;
                addNewDirectionsLayout();
                recipeDirectionsEditText.setText(directionsList.get(i));
                directionItem = recipeDirectionsEditText.getText().toString().trim();
                directionsArrayList.add(directionItem);
                recipeDirectionsImageView.setImageResource(R.drawable.ic_close_grey600_18dp);
                recipeDirectionsImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeDirectionItem(finalI);
                    }
                });
            }
        }
    }

    private void addNewIngredientsLayout() {
        recipeIngredientsImageView = new ImageView(this);
        recipeIngredientsImageView.setLayoutParams(Params2);

        recipeIngredientEditText = new EditText(this);
        recipeIngredientEditText.setLayoutParams(layoutParams);

        ingredientsRowLayout = new LinearLayout(this);
        ingredientsRowLayout.setLayoutParams(params);

        ingredientsRowLayout.addView(recipeIngredientEditText);
        ingredientsLayout.addView(ingredientsRowLayout);
        ingredientsRowLayout.addView(recipeIngredientsImageView);
    }

    private void addNewDirectionsLayout() {
        directionsRowLayout = new LinearLayout(this);
        directionsRowLayout.setLayoutParams(params);

        recipeDirectionsEditText = new EditText(this);
        recipeDirectionsEditText.setLayoutParams(layoutParams);

        recipeDirectionsImageView = new ImageView(this);
        recipeDirectionsImageView.setLayoutParams(Params2);

        directionsRowLayout.addView(recipeDirectionsEditText);
        directionsRowLayout.addView(recipeDirectionsImageView);
        directionsLayout.addView(directionsRowLayout);
    }
}




