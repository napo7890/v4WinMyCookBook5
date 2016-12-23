package v3.com.mycookbook5.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import v3.com.mycookbook5.BaseFragment;
import v3.com.mycookbook5.R;
import v3.com.mycookbook5.models.Recipe;
import v3.com.mycookbook5.viewholder.RecipeHolder;

public abstract class RecipeListFragment extends BaseFragment {

    public RecipeListFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setLayoutManager();

        /** Set up FirebaseRecyclerAdapter with the Query */
        Query recipesQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Recipe, RecipeHolder>(Recipe.class, R.layout.item_recipe,
                RecipeHolder.class, recipesQuery) {
            @Override
            protected void populateViewHolder(final RecipeHolder viewHolder, final Recipe recipe, final int position) {
                final DatabaseReference recipeRef = getRef(position);

                /** Set click listener for the whole recipe view */
                final String recipeKey = recipeRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchDetailActivity(recipeKey);
                    }
                });
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        createDeleteAlertDialog(recipeKey);
                        return true;
                    }
                });
                /** Bind Recipe to ViewHolder */
                viewHolder.bindToRecipe(recipe);
            }
        };
        mRecycler.setAdapter(mAdapter);

    }

    public abstract Query getQuery(DatabaseReference databaseReference);

}