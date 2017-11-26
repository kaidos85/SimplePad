package com.kaidos85.simplepad.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.kaidos85.simplepad.Item;
import com.kaidos85.simplepad.NoteItem;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by AiDoS on 17.01.2016.
 */
public class RepositoryNote  extends Model{

    public static final String orderByDate = "NoteDate DESC";
    public static final String orderByTitle = "Title";

    public RepositoryNote(Context context) {
        super(context);
        super.createSql = "CREATE TABLE NoteItem (" +
                "    id          INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "    Title TEXT," +
                "    Description TEXT," +
                "    NoteDate    DATETIME, " +
                "    Location TEXT)";
        super.dropSql = "DROP TABLE IF EXISTS NoteItem";
    }

    public ArrayList<Item> getNoteItems(String orderBy)
    {
//        SQLiteDatabase db2 = getWritableDatabase();
//        db2.execSQL("INSERT INTO NoteItem (Title, Description, NoteDate) VALUES('Hello', 'some text', '2016-01-01')");
//        db2.execSQL("INSERT INTO NoteItem (Title, Description, NoteDate) VALUES('My row', 'some text 2', '2016-02-01')");
//        db2.execSQL("INSERT INTO NoteItem (Title, Description, NoteDate) VALUES('This row', 'some text 3', '2016-03-01')");
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Item> items = new ArrayList<>();
        Cursor cur = db.query("NoteItem", new String[]{"id", "Title", "Description", "NoteDate", "Location"}, null, null, null, null, orderBy);
        while (cur.moveToNext())
        {
            items.add(new NoteItem(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4)));
        }
        db.close();
        return items;
    }

    public NoteItem getNoteItem(String id){
        SQLiteDatabase db = getReadableDatabase();
        NoteItem item;
        String strSQL = "SELECT * FROM NoteItem WHERE id = ?";
        Cursor cur = db.rawQuery(strSQL, new String[]{ id });
        cur.moveToFirst();
        item = new NoteItem(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4));
        db.close();
        return item;
    }


    public NoteItem getNoteItemByTitle(String title){
        SQLiteDatabase db = getReadableDatabase();
        NoteItem item;
        String strSQL = "SELECT * FROM NoteItem WHERE Title = ?";
        Cursor cur = db.rawQuery(strSQL, new String[]{ title });
        cur.moveToFirst();
        item = new NoteItem(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4));
        db.close();
        return item;
    }

    public void insertOrUpdate(NoteItem item)
    {
        String strSQL="";
        if(item.Id == 0)
            strSQL = "INSERT INTO NoteItem (Title, Description, NoteDate, Location) VALUES(?, ?, ?, ?)";
        else
            strSQL = "UPDATE NoteItem SET Title=?, Description=?, NoteDate=?, Location=? WHERE id=?";
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(strSQL);
        stmt.bindString(1, item.Title);
        stmt.bindString(2, item.Description);
        stmt.bindString(3, item.NoteDate);
        stmt.bindString(4, item.Location);
        if(item.Id != 0)
           stmt.bindLong(5, item.Id);
        stmt.execute();
        db.close();
    }

    public void Delete(NoteItem item){
        String strSQL = "DELETE FROM NoteItem WHERE id=?";
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(strSQL);
        stmt.bindLong(1, item.Id);
        stmt.execute();
        db.close();
    }

}


