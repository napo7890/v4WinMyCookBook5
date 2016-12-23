package v3.com.mycookbook5;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class BaseActivity extends AppCompatActivity {

    /** Declare_database_ref **/
    public DatabaseReference databaseReference, mDatabase;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navDrawer;
    private ActionBarDrawerToggle drawerToggle;
    public int cookingTime, servings;
    public String mRecipeKey, description;
    private ProgressDialog mProgressDialog;

    public static final String EXTRA_RECIPE_KEY = "recipe_key";
    public static final int MAX_WIDTH = 200;
    public static final int MAX_HEIGHT = 150;
    public static final int DESCRIPTION_REQUEST_CODE = 200;
    public static final int UPDATE_REQUEST_CODE = 201;
    public static final String RECIPE_KEY = "key";
    public static final String IMAGE = "image";
    public static final String TITLE = "title";
    public static final String COOKING_TIME = "cookingTime";
    public static final String SERVINGS = "servings";
    public static final String DESCRIPTION = "description";
    public static final String DISH_TYPE = "dish-type";
    public static final String INGREDIENTS = "ingredients";
    public static final String DIRECTIONS = "directions";

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setupActionbar(){
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void setupDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        /** Tie DrawerLayout events to the ActionBarToggle **/
        drawerLayout.addDrawerListener(drawerToggle);
        navDrawer = (NavigationView) findViewById(R.id.nav_view);

        /** Setup drawer view **/
        setupDrawerContent(navDrawer);
    }

    public ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /** Pass any configuration change to the drawer toggles **/
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** Inflate the options menu from XML **/
        getMenuInflater().inflate(R.menu.app_bar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    public void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_my_recipes:
                Intent intentMyRecipes = new Intent(this, HomeScreenActivity.class);
                startActivity(intentMyRecipes);
                break;

//            case R.id.action_create_recipe:
//                Intent intentCreateRecipe = new Intent(this, CreateRecipeActivity.class);
//                startActivity(intentCreateRecipe);
//                break;
//            case R.id.action_add_recipe:
//                Intent intentAddRecipe = new Intent(this, AddRecipeActivity.class);
//                startActivity(intentAddRecipe);
//                break;
            case R.id.action_search_recipe:
                Intent intentSearchRecipe = new Intent(this, SearchRecipeActivity.class);
                startActivity(intentSearchRecipe);
                break;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
//        /** Highlight the selected item has been done by NavigationView **/
//        //item.setChecked(true);
//        // Set action bar title
//        setTitle(item.getTitle());
//    }
    }

}
