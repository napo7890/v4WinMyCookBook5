package v3.com.mycookbook5;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

public class SearchActivity extends BaseActivity {

    //    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Get the intent, verify the action and get the query
     **/
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchRecipe(query);
            //use the query to search the data somehow
        }
    }

    private void searchRecipe(String query) {

    }



}
