package v3.com.mycookbook5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RecipeDescriptionActivity extends BaseActivity implements View.OnClickListener {

    private EditText cookingTimeEditText;
    private EditText servingsEditText;
    private EditText descriptionEditText;
    private Button saveDescriptionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        cookingTimeEditText = (EditText) findViewById(R.id.recipe_description_activity_cooking_time_edit_text);
        servingsEditText = (EditText) findViewById(R.id.recipe_description_activity_servings_edit_text);
        descriptionEditText = (EditText) findViewById(R.id.recipe_description_activity_description_edit_text);
        saveDescriptionButton = (Button) findViewById(R.id.saveDescriptionBtn);
        saveDescriptionButton.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String cooking = String.valueOf(bundle.getInt(COOKING_TIME));
            String servings = String.valueOf(bundle.getInt(SERVINGS));
            String description = bundle.getString(DESCRIPTION);

            if (bundle.getInt(COOKING_TIME) != 0){
                cookingTimeEditText.setText(cooking);

            }else{
                cookingTimeEditText.setText("");
            }
            if (bundle.getInt(SERVINGS) != 0){
                servingsEditText.setText(servings);
            }else{
                servingsEditText.setText("");
            }
            descriptionEditText.setText(description);
        }
    }


    @Override
    public void onClick(View v) {
        sendBackToCreateRecipeActivity();
    }

    private void sendBackToCreateRecipeActivity() {
        int cookingTime;
        int servings;
        String description = descriptionEditText.getText().toString();

        if (!cookingTimeEditText.getText().toString().isEmpty()) {
            cookingTime = Integer.parseInt(cookingTimeEditText.getText().toString());
        }
        else{
            cookingTime = 0;
        }
        if (!servingsEditText.getText().toString().isEmpty()){
            servings = Integer.parseInt(servingsEditText.getText().toString());
        }else{
            servings = 0;

        }

        Intent intent = new Intent();
        intent.putExtra(COOKING_TIME, cookingTime);
        intent.putExtra(SERVINGS, servings);
        intent.putExtra(DESCRIPTION, description);
        setResult(RESULT_OK, intent);
        finish();
    }
}
