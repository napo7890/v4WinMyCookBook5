package v3.com.mycookbook5.fragment;

import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecipeFragment extends RecipeListFragment {

    public RecipeFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        /** All my recipes */
        //return databaseReference.child("user-recipes").child(getUid());
        return databaseReference.child("recipes").orderByChild("uid").equalTo(getUid());
        //return databaseReference.child("recipes").orderByChild("isBelongToUser").equalTo(true);
        //return databaseReference.child("recipes").child(getUid()).orderByChild("isBelongToUser").equalTo(true);
        //return databaseReference.child("recipes").orderByChild("isBelongToUser").equalTo(true);
        //return databaseReference.child("recipes").child(getUid()).orderByChild("title");
    }

//    public Query getAllRecipes(DatabaseReference databaseReference) {
//        /** All recipes exclude user recipes*/
//        return databaseReference.child("recipes").orderByChild(getUid()).equalTo(null);
//    }
}