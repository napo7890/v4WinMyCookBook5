package v3.com.mycookbook5.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import v3.com.mycookbook5.R;
import v3.com.mycookbook5.RecipeDetailActivity;
import v3.com.mycookbook5.SearchRecipeClickListener;

public class SearchRecipeAdapter extends RecyclerView.Adapter<SearchRecipeAdapter.ViewHolder> {

    private List<String> recipeTitleList;
    private List<String> recipeKeyList;
    private Context context;
    private ViewHolder viewHolder;
    private String recipeKey;

    public SearchRecipeAdapter(List<String> recipeTitleList, List<String> recipeKeyList, Context context) {
        this.recipeTitleList = recipeTitleList;
        this.recipeKeyList = recipeKeyList;
        this.context = context;
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item
     **/
    @Override
    public SearchRecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        /** create a layout **/
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_search_recipe_item_view, null);

        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position
     **/
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.name.setText(recipeTitleList.get(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final int pos = viewHolder.getLayoutPosition();
//                recipeKey = recipeKeyList.get(pos);

                recipeKey = recipeKeyList.get(position);
                //Toast.makeText(context, recipeKey, Toast.LENGTH_SHORT).show();
                launchDetailActivity(recipeKey);
            }
        });
    }

    /**
     * Returns the total number of items in the data set hold by the adapter
     **/
    @Override
    public int getItemCount() {
        return recipeTitleList.size();
    }

    /**
     * initializes some private fields to be used by RecyclerView
     **/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            name = (TextView) itemLayoutView.findViewById(R.id.search_recipe_title);
        }
    }

    private void launchDetailActivity(String recipeKey) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_KEY, recipeKey);
        context.startActivity(intent);
    }

}