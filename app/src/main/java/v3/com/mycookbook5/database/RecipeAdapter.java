//package v3.com.mycookbook5.database;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CursorAdapter;
//import android.widget.TextView;
//
//import v3.com.mycookbook5.R;
//
//public class RecipeAdapter extends CursorAdapter {
//
//    public RecipeAdapter(Context context, Cursor c, int flags) {
//        super(context, c, flags);
//    }
//
//    /** inflate a new view and return it */
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
//    }
//
//
//    /** bind all data to a given view */
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//
//        /** Find fields to populate in inflated template */
//        TextView tv = (TextView) view.findViewById(R.id.list_item_recipe_title);
//        //ImageView recipeImage = (ImageView) view.findViewById(R.id.itemRecipeImage);
//
//        /** Extract properties from cursor */
//        String title = cursor.getString(cursor.getColumnIndex(DBOpenHelper.RECIPE_TITLE));
//        //String image = cursor.getString(cursor.getColumnIndex(DBOpenHelper.RECIPE_IMAGE_PATH));
//        //Log.d("TAG", "image: " + image);
//        //Uri imageUri = Uri.fromFile(new File(image));
//
//        /** Populate fields with extracted properties */
//        tv.setText(title);
//        //recipeImage.setImageURI(imageUri);
//
//
//
//    }
//
//
//
//}
//
//
//
//
//
