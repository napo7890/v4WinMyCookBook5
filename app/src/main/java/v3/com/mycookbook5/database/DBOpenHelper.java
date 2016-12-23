//package v3.com.mycookbook5.database;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DBOpenHelper extends SQLiteOpenHelper {
//
//    /** Constants for recipe db name and version */
//    private static final String DATABASE_NAME = "recipes.db";
//    private static final int DATABASE_VERSION = 3;
//
//    /** Table names */
//    public static final String TABLE_RECIPES = "recipes";
////    public static final String TABLE_SHOPPING_LIST = "shopping_list";
////    public static final String TABLE_INGREDIENTS = "ingredients";
////    public static final String TABLE_DIRECTIONS = "directions";
////    public static final String TABLE_IMAGES = "images";
//
//    /** Recipe columns */
//    public static final String RECIPE_ID = "_id";
//    public static final String RECIPE_TITLE = "recipeTitle";
//    public static final String RECIPE_IMAGE_PATH = "recipeImagePath";
////    public static final String RECIPE_COOKING_TIME = "recipeCookingTime";
////    public static final String RECIPE_SERVINGS = "recipeServings";
////    public static final String RECIPE_REGION = "recipeRegion";
////    public static final String RECIPE_DISH_TYPE = "recipeDishType";
////    public static final String RECIPE_DESCRIPTION = "recipeDescription";
////    public static final String RECIPE_RATING = "recipeRating";
////    public static final String RECIPE_INGREDIENTS = "recipeIngredients";
////    public static final String RECIPE_DIRECTION = "recipeDirections";
////    public static final String RECIPE_SOURCE = "recipeSource";
////    public static final String RECIPE_URL = "recipeUrl";
////    public static final String RECIPE_VIDEO_URL = "recipeVideoUrl";
//    public static final String RECIPE_CREATED = "recipeCreated";
//
////    /** Shopping List columns */
////    public static final String SHOPPING_LIST_ID = "_id";
////    public static final String SHOPPING_LIST_TITLE = "shoppingListTitle";
////    public static final String SHOPPING_INGREDIENTS = "shoppingListIngredients";
//
////    /** Ingredients columns */
////    public static final String INGREDIENTS_ID = "_id";
////    public static final String INGREDIENTS_TEXT = "ingredient";
//
////    /** Directions columns */
////    public static final String DIRECTIONS_ID = "_id";
////    public static final String DIRECTIONS_TEXT = "direction";
//
////    /** Recipe Images columns */
////    public static final String IMAGE_ID = "_id";
////    public static final String RECIPE_IMAGE_ID = "recipeId";
////    public static final String IMAGE_URI = "imageUri";
//
////    /** Calling all columns from recipe table */
////    public static final String[] ALL_COLUMN_RECIPE =
////            {RECIPE_ID, RECIPE_TITLE, RECIPE_IMAGE_PATH, RECIPE_COOKING_TIME,
////                    RECIPE_SERVINGS, RECIPE_REGION, RECIPE_DISH_TYPE,
////                    RECIPE_DESCRIPTION, RECIPE_RATING, RECIPE_INGREDIENTS,
////                    RECIPE_DIRECTION, RECIPE_SOURCE, RECIPE_URL, RECIPE_VIDEO_URL, RECIPE_CREATED};
//
//    /** Calling all columns from recipe table */
//    public static final String[] ALL_COLUMN_RECIPE =
//            {RECIPE_ID, RECIPE_TITLE, RECIPE_IMAGE_PATH, RECIPE_CREATED};
//
////    /** Calling all columns from shopping list table */
////    public static final String[] ALL_COLUMN_SHOPPING_LIST =
////            {SHOPPING_LIST_ID, SHOPPING_LIST_TITLE, SHOPPING_INGREDIENTS};
//
////    /** Calling all columns from ingredients table */
////    public static final String[] ALL_COLUMN_INGREDIENTS =
////            {INGREDIENTS_ID, INGREDIENTS_TEXT};
//
////    /** Calling all columns from directions table */
////    public static final String[] ALL_COLUMN_DIRECTIONS =
////            {DIRECTIONS_ID, DIRECTIONS_TEXT};
//
//    /** SQL to create recipe table */
//    private static final String CREATE_RECIPE_TABLE =
//            "CREATE TABLE " + TABLE_RECIPES + " (" +
//                    RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    RECIPE_TITLE + " TEXT RECIPE_TITLE, " +
//                    RECIPE_IMAGE_PATH + " TEXT IMAGE_PATH, " +
//                    RECIPE_CREATED + " TEXT default CURRENT_TIMESTAMP" +
//                    ")";
//
////    /** SQL to create recipe table */
////    private static final String CREATE_RECIPE_TABLE =
////            "CREATE TABLE " + TABLE_RECIPES + " (" +
////                    RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
////                    RECIPE_TITLE + " TEXT RECIPE_TITLE, " +
////                    RECIPE_IMAGE_PATH + " TEXT IMAGE_PATH, " +
////                    RECIPE_COOKING_TIME + " TEXT COOKING_TIME, " +
////                    RECIPE_SERVINGS + " INT, " +
////                    RECIPE_REGION + " TEXT, " +
////                    RECIPE_DISH_TYPE + " ENUM, " +
////                    RECIPE_DESCRIPTION + " TEXT DESCRIPTION, " +
////                    RECIPE_RATING + " RATING, " +
////                    RECIPE_INGREDIENTS + " LIST INGREDIENTS, " +
////                    RECIPE_DIRECTION + " LIST DIRECTION, " +
////                    RECIPE_SOURCE + " TEXT SOURCE, " +
////                    RECIPE_URL + " TEXT URL, " +
////                    RECIPE_VIDEO_URL + " TEXT VIDEO URL, " +
////                    RECIPE_CREATED + " TEXT default CURRENT_TIMESTAMP" +
////                    ")";
//
////    /** SQL to create shopping list table */
////    private static final String CREATE_SHOPPING_LIST_TABLE =
////            "CREATE TABLE " + TABLE_SHOPPING_LIST + " (" +
////                    SHOPPING_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
////                    SHOPPING_LIST_TITLE + " TEXT, " +
////                    SHOPPING_INGREDIENTS + " LIST INGREDIENTS, " +
////                    ")";
//
////    /** SQL to create ingredients table */
////    private static final String CREATE_INGREDIENTS_TABLE =
////            "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
////                    INGREDIENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
////                    INGREDIENTS_TEXT + " TEXT, " +
////                    ")";
//
////    /** SQL to create direction table */
////    private static final String CREATE_DIRECTIONS_TABLE =
////            "CREATE TABLE " + TABLE_DIRECTIONS + " (" +
////                    DIRECTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
////                    DIRECTIONS_TEXT + " TEXT, " +
////                    ")";
//
////    /** SQL to create recipe images table */
////    private static final String CREATE_IMAGE_TABLE =
////            "CREATE TABLE " + TABLE_IMAGES + " (" +
////                    IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
////                    RECIPE_IMAGE_ID + " INTEGER " +
////                    IMAGE_URI + " URI " +
////                    ")";
//
//    public DBOpenHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        db.execSQL(CREATE_RECIPE_TABLE);
////        db.execSQL(CREATE_SHOPPING_LIST_TABLE);
////        db.execSQL(CREATE_INGREDIENTS_TABLE);
////        db.execSQL(CREATE_DIRECTIONS_TABLE);
////        db.execSQL(CREATE_IMAGE_TABLE);
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
////        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);
////        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
////        db.execSQL("DROP TABLE IF EXISTS " + CREATE_DIRECTIONS_TABLE);
////        db.execSQL("DROP TABLE IF EXISTS " + CREATE_IMAGE_TABLE);
//
//        onCreate(db);
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
