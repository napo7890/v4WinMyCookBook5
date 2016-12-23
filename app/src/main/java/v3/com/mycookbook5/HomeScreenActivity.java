package v3.com.mycookbook5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class HomeScreenActivity extends BaseActivity {


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
    }


}

