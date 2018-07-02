package com.example.andre.kawaiicards2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by andre on 01/07/2018.
 */

public class AnimeOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = AnimeOpenHelper.class.getSimpleName();

    // Versions has to be 1 first time or app will crash.
    private static final int DATABASE_VERSION = 1;
    private static final String TAREA_TABLE = "tareas_entries";
    private static final String DATABASE_NAME = "tareas";

    // Column names...
    public static final String KEY_ID = "_id";
    public static final String KEY_TAREA = "tarea";
    public static final String KEY_DESCRIPCION = "descripcion";
    public static final String KEY_FECHA = "fecha";
    public static final String KEY_HORA = "hora";
    public static final String KEY_TERMINADO = "terminado";

    // ... and a string array of columns.
    private static final String[] COLUMNS =
            {KEY_ID, KEY_TAREA, KEY_DESCRIPCION, KEY_FECHA, KEY_HORA, KEY_TERMINADO};

    // Build the SQL query that creates the table.
    private static final String TAREA_TABLE_CREATE =
            "CREATE TABLE " + TAREA_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " + // will auto-increment if no value passed
                    KEY_TAREA + " TEXT, " +
                    KEY_DESCRIPCION + " TEXT, " +
                    KEY_FECHA + " TEXT, " +
                    KEY_HORA + " TEXT, " +
                    KEY_TERMINADO + " TEXT );";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public AnimeOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "Construct WordListOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TAREA_TABLE_CREATE);
        fillDatabaseWithData(db);
        // We cannot initialize mWritableDB and mReadableDB here, because this creates an infinite
        // loop of on Create being repeatedly called.
    }

    public void fillDatabaseWithData(SQLiteDatabase db) {
        // Create a container for the data.
        ContentValues values = new ContentValues();
        values.put(KEY_TAREA, "Tarea nueva");
        values.put(KEY_DESCRIPCION, "Descripción");
        values.put(KEY_FECHA, "1/1/2010");
        values.put(KEY_HORA, "8:00");
        values.put(KEY_TERMINADO, "no");
        db.insert(TAREA_TABLE, null, values);
    }

    public Anime query(int position) {
        String query = "SELECT * FROM " + TAREA_TABLE +
                " ORDER BY " + KEY_FECHA + " ASC " +
                "LIMIT " + position + ",1";

        Cursor cursor = null;
        Anime entry = new Anime();

        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            entry.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            entry.setTarea(cursor.getString(cursor.getColumnIndex(KEY_TAREA)));
            entry.setDescripcion(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPCION)));
            entry.setFecha(cursor.getString(cursor.getColumnIndex(KEY_FECHA)));
            entry.setHora(cursor.getString(cursor.getColumnIndex(KEY_HORA)));
            entry.setTerminado(cursor.getString(cursor.getColumnIndex(KEY_TERMINADO)));
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            // Must close cursor and db now that we are done with it.
            cursor.close();
            return entry;
        }
    }

    public long count() {
        if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
        return DatabaseUtils.queryNumEntries(mReadableDB, TAREA_TABLE);
    }

    public long insert(String tarea, String descripcion, String fecha, String hora, String terminado) {
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_TAREA, tarea);
        values.put(KEY_DESCRIPCION, descripcion);
        values.put(KEY_FECHA, fecha);
        values.put(KEY_HORA, hora);
        values.put(KEY_TERMINADO, terminado);
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
            newId = mWritableDB.insert(TAREA_TABLE, null, values);
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return newId;
    }

    public int update(int id, String tarea, String descripcion, String fecha, String hora, String terminado) {
        int mNumberOfRowsUpdated = -1;
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
            ContentValues values = new ContentValues();
            values.put(KEY_TAREA, tarea);
            values.put(KEY_DESCRIPCION, descripcion);
            values.put(KEY_FECHA, fecha);
            values.put(KEY_HORA, hora);
            values.put(KEY_TERMINADO, terminado);

            mNumberOfRowsUpdated = mWritableDB.update(TAREA_TABLE, //table to change
                    values, // new values to insert
                    KEY_ID + " = ?", // selection criteria for row (in this case, the _id column)
                    new String[]{String.valueOf(id)}); //selection args; the actual value of the id

        } catch (Exception e) {
            Log.d (TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }
        return mNumberOfRowsUpdated;
    }

    public int delete(int id) {
        int deleted = 0;
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
            deleted = mWritableDB.delete(TAREA_TABLE, //table name
                    KEY_ID + " = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION! " + e.getMessage());        }
        return deleted;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(AnimeOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TAREA_TABLE);
        onCreate(db);
    }

}
