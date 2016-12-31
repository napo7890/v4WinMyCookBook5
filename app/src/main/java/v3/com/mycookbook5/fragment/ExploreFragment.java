package v3.com.mycookbook5.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class ExploreFragment extends ExploreListFragment {

    public ExploreFragment(){}

    public Query getExploreQuery(DatabaseReference databaseReference) {
        /** All recipes exclude user recipes*/
        return databaseReference.child("recipes").orderByChild(getUid()).equalTo(null);
    }
}
