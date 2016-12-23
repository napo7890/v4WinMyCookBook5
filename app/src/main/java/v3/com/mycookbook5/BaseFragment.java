package v3.com.mycookbook5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import v3.com.mycookbook5.models.Recipe;
import v3.com.mycookbook5.ui.DividerItemDecoration;
import v3.com.mycookbook5.viewholder.RecipeHolder;

public class BaseFragment extends Fragment {

    public DatabaseReference mDatabase;
    public FirebaseRecyclerAdapter<Recipe, RecipeHolder> mAdapter;
    public RecyclerView mRecycler;
    public LinearLayoutManager mManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_recipes, container, false);
        return createDatabaseReference(rootView);
    }

    @NonNull
    public View createDatabaseReference(View rootView) {
        /** Create database reference */
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecycler = (RecyclerView) rootView.findViewById(R.id.recipes_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    public void setLayoutManager() {
        /** Set up Layout Manager, reverse layout */
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mRecycler.addItemDecoration(new DividerItemDecoration(getActivity()));
    }

    public void setAdapter(final Query recipesQuery) {
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
                        /** Launch RecipeDetailActivity */
                        launchDetailActivity(recipeKey);
                    }
                });
                /** Bind Recipe to ViewHolder */
                viewHolder.bindToRecipe(recipe);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    public void launchDetailActivity(String recipeKey) {
        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_KEY, recipeKey);
        startActivity(intent);
    }

    public boolean createDeleteAlertDialog(final String recipeKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_recipe);
        //builder.setIcon(R.drawable.ic_action_delete);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /** remove recipe from list **/
                deleteRecipe(recipeKey);

            }
        });
        builder.setNegativeButton("NO", null);
        builder.show();
        return true;
    }

    public void deleteRecipe(final String recipeKey) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/recipes/" + recipeKey, null);
        childUpdates.put("/users/" + getUid() + "/recipes/" + recipeKey, null);
        mDatabase.updateChildren(childUpdates);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }
}
