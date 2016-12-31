package v3.com.mycookbook5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeScreenActivity extends BaseActivity implements View.OnClickListener {
    //    private TextView cookToday;
//    private Button newRecipeButton;
    private LinearLayout cookTodayLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        setupToolbar();
        setupActionbar();
        setupDrawer();

        /** Button launches CreateRecipeActivity */
        findViewById(R.id.fab_create_recipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, CreateRecipeActivity.class));
            }
        });

        cookTodayLinearLayout = (LinearLayout) findViewById(R.id.home_screen_activity_search_linear_layout);
        cookTodayLinearLayout.setOnClickListener(this);

        //cookToday = (TextView) findViewById(R.id.home_screen_activity_search_text_view);
        //newRecipeButton = (Button) findViewById(R.id.activity_home_screen_add_recipe_button);

        //cookToday.setOnClickListener(this);
        //newRecipeButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_screen_activity_search_linear_layout:
                Intent cookTodayIntent = new Intent(HomeScreenActivity.this, SearchRecipeActivity.class);
                startActivity(cookTodayIntent);
//            case R.id.home_screen_activity_search_text_view:
//                Intent cookTodayIntent = new Intent(HomeScreenActivity.this, SearchRecipeActivity.class);
//                startActivity(cookTodayIntent);
//            case R.id.activity_home_screen_add_recipe_button:
//                Intent addRecipeIntent = new Intent(HomeScreenActivity.this, CreateRecipeActivity.class);
//                startActivity(addRecipeIntent);
        }
    }
}
