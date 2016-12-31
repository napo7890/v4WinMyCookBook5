package v3.com.mycookbook5.fragment;

import android.os.Bundle;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import v3.com.mycookbook5.BaseFragment;

public abstract class ExploreListFragment extends BaseFragment {

    public ExploreListFragment(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setLayoutManager();

        /** Set up FirebaseRecyclerAdapter with the Query */
        Query recipesQuery = getExploreQuery(mDatabase);
        setAdapter(recipesQuery);
    }

    public abstract Query getExploreQuery(DatabaseReference databaseReference);


}
