package v3.com.mycookbook5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import v3.com.mycookbook5.models.Recipe;
import v3.com.mycookbook5.ui.DividerItemDecoration;
import v3.com.mycookbook5.viewholder.SearchRecipeAdapter;

import static android.R.attr.key;
import static android.content.Intent.getIntent;

public class SearchRecipeActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    public SearchView search;
    private SearchRecipeAdapter mAdapter;
    private List<String> recipeTitleList, recipeKeyList;
    private String recipeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
        search = (SearchView) findViewById(R.id.search);
        search.setIconified(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        recipeTitleList = new ArrayList<>();
        recipeKeyList = new ArrayList<>();

        /** call the adapter with argument list of items and context **/
        mAdapter = new SearchRecipeAdapter(recipeTitleList, recipeKeyList, this);
        mRecyclerView.setAdapter(mAdapter);
        search.setOnQueryTextListener(listener);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("recipes");

        final Query recipesQuery = databaseReference.orderByChild("title");
        recipesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                String recipeTitle = String.valueOf(value.get("title"));
                recipeTitleList.add(recipeTitle);
                recipeKey = String.valueOf(value.get("recipeUid"));
                recipeKeyList.add(recipeKey);

                //Toast.makeText(SearchRecipeActivity.this, recipeKeyList.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    /**
     * Filter the data with a matching string
     **/

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {

            query = query.toLowerCase();

            final List<String> filteredTitleList = new ArrayList<>();
            //final List<String> filteredKeyList = new ArrayList<>();

            for (int i = 0; i < recipeTitleList.size(); i++) {
                final String title = recipeTitleList.get(i).toLowerCase();
                if (title.contains(query)) {
                    filteredTitleList.add(recipeTitleList.get(i));
//                    //for (int a = 0; a < i; a++) {
//                        for (int a = 0; a < recipeKeyList.size(); a++) {
//                        filteredKeyList.add(recipeKeyList.get(a));
//
//                        //System.out.println(recipeTitleList.get(i));
//                    }
                }
            }

            mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchRecipeActivity.this));
            mAdapter = new SearchRecipeAdapter(filteredTitleList, recipeKeyList, SearchRecipeActivity.this);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();  // data set changed
            return true;
        }


        public boolean onQueryTextSubmit(String query) {
            //launchDetailActivity(recipeKey);

            return true;
        }
    };
//
//   private void launchDetailActivity(String recipeKey) {
//        Intent intent = new Intent(this, RecipeDetailActivity.class);
//        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_KEY, recipeKey);
//        startActivity(intent);
//    }
}
