package v3.com.mycookbook5.fragment;

import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import v3.com.mycookbook5.models.Recipe;

public class FavoritesFragment extends FavoritesListFragment {

    public FavoritesFragment() {
    }

    @Override
    public Query getFavoritesQuery(DatabaseReference databaseReference) {

//        return databaseReference.child("recipes").orderByChild(getUid()).equalTo(null);


        String userId = databaseReference.child(getUid()).getKey();
        //Toast.makeText(getContext(), userId, Toast.LENGTH_LONG).show();

//        for (int i = 0; i < mAdapter.getItemCount(); i++){
//            Toast.makeText(getContext(), mAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
//        }

        if (userId == getUid()) {
            return databaseReference.child("recipes").orderByChild("isLiked").equalTo(true);

        } else {
            return null;
        }

    }

}
