//package v3.com.mycookbook5.database;
//
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
//
//public class RecipeProvider extends ContentProvider {
//
//    private static final String AUTHORITY = "v3.com.mycookbook5.recipeprovider";
//    private static final String BASE_PATH = "recipes";
//    private SQLiteDatabase database;
//
//    /** Address to the table from in/outside the app */
//    public static final Uri CONTENT_URI =
//            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
//
//    /** Constant to identify the requested operation */
//    private static final int RECIPES = 1;
//    private static final int RECIPES_ID = 2;
//
//
//    private static final UriMatcher uriMatcher =
//            new UriMatcher(UriMatcher.NO_MATCH);
//
//    public static final String CONTENT_ITEM_TYPE = "Recipe";
//
//    /** Add URI matcher in a static initializer */
//    static {
//        uriMatcher.addURI(AUTHORITY, BASE_PATH, RECIPES);
//        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", RECIPES_ID);
//    }
//
//    @Override
//    public boolean onCreate() {
//        DBOpenHelper helper = new DBOpenHelper(getContext());
//        database = helper.getWritableDatabase();
//        return true;
//    }
//
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//        return database.query(DBOpenHelper.TABLE_RECIPES, DBOpenHelper.ALL_COLUMN_RECIPE,
//                selection, null, null, null, DBOpenHelper.RECIPE_CREATED + " DESC");
//    }
//
//    @Override
//    public String getType(Uri uri) {
//        return null;
//    }
//
//    @Override
//    public Uri insert(Uri uri, ContentValues values) {
//        long id = database.insert(DBOpenHelper.TABLE_RECIPES, null, values);
//        return Uri.parse(BASE_PATH + "/" + id);
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        return database.delete(DBOpenHelper.TABLE_RECIPES,
//                selection, selectionArgs);
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
//        return database.update(DBOpenHelper.TABLE_RECIPES,
//                values, selection, selectionArgs);
//    }
//}
//
//
//
