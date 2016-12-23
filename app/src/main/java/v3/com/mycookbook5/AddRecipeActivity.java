package v3.com.mycookbook5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener{
    Button buttonAddRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        buttonAddRecipe = (Button) findViewById(R.id.buttonAddRecipe);
        buttonAddRecipe.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {

    }
}

