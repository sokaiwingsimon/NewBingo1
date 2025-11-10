package com.example.newbingo;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bingo_game.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_GAME_INFO = "gameinfo";

    // Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_ROUND_NUMBER = "round_number";
    private static final String COLUMN_BINGO_NUMBERS = "bingo_numbers";
    private static final String COLUMN_SYSTEM_TIME = "system_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the game info table
        String CREATE_GAME_INFO_TABLE = "CREATE TABLE " + TABLE_GAME_INFO + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_ROUND_NUMBER + " INTEGER,"
                + COLUMN_BINGO_NUMBERS + " TEXT,"
                + COLUMN_SYSTEM_TIME + " INTEGER"
                + ")";
        db.execSQL(CREATE_GAME_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME_INFO);
        // Create tables again
        onCreate(db);
    }

    // Method to add a GameInfo record to the database
    public void addGameInfo(GameInfo gameInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, gameInfo.getUsername());
        values.put(COLUMN_ROUND_NUMBER, gameInfo.getRoundNumber());
        values.put(COLUMN_BINGO_NUMBERS, gameInfo.getBingoNumbers());
        values.put(COLUMN_SYSTEM_TIME, gameInfo.getSystemTime());

        // Insert the new row
        db.insert(TABLE_GAME_INFO, null, values);
        db.close(); // Close the database connection
    }
}
