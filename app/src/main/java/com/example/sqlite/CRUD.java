package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CRUD
{
    DBHelper dbHandler;
    SQLiteDatabase db;
    Context mContext;

    private static final String[] columns =
    {
        DBHelper.ID,
        DBHelper.NAME,
        DBHelper.GENDER,
        DBHelper.ADDRESS,
        DBHelper.BIRTH,
        DBHelper.WEIGHT,
        DBHelper.TIME
    };
    public CRUD(Context context)
    {
        mContext = context;
    }
    public void open() //開啟資料庫
    {
        dbHandler = new DBHelper(mContext);
        db = dbHandler.getWritableDatabase();
    }
    public void close() //關閉資料庫
    {
        dbHandler.close();
    }
    public long add()
    {
        int r =(int)(Math.random()*1000);

        ContentValues contentValues = new ContentValues();
        //ID 為 AUTOINCREMENT
        contentValues.put( DBHelper.NAME, "小每"+r);
        contentValues.put( DBHelper.GENDER, "woman");
        contentValues.put( DBHelper.ADDRESS, "太平洋3街");
        contentValues.put( DBHelper.BIRTH, "2050-01-01");
        contentValues.put( DBHelper.WEIGHT, 45.45687);
        contentValues.put( DBHelper.TIME, "21:59:59");

        //return ID(AUTOINCREMENT)的值，失敗為-1
        return db.insert(DBHelper.TableName, null, contentValues);
    }
    public int delete(String ID)
    {

        /**whereClause:
         欄位1 = ? and 欄位2 = ?

         whereArgs:不用管資料型態，就算該欄位資料為數字，也是放字串資料
         new String[]{"欄位1值","欄位2值"}

         EX: db.delete(DBHelper.TableName, DBHelper.ID + "=? and "+DBHelper.GENDER+" =?", new String[] {ID,"woman"});

         刪除資料表
         db.delete(DBHelper.TableName, null, null);
         設定主鍵(自動遞增)為0
         db.execSQL("update sqlite_sequence set seq=0 where name='"+ DBHelper.TableName +"'");
         **/

        //return 成功:1，失敗為0
        return db.delete(DBHelper.TableName, DBHelper.ID + "=?", new String[] {ID});
        // 也可這樣寫 db.delete(DBHelper.TableName, DBHelper.ID + "=\"" + ID+"\"", null);

    }


    public int update(String ID)
    {
        /**whereClause:
         欄位1 = ? and 欄位2 = ?

         whereArgs:不用管資料型態，就算該欄位資料為數字，也是放字串資料
         new String[]{"欄位1值","欄位2值"}
        **/
         int r =(int)(Math.random()*1000);

        ContentValues contentValues = new ContentValues();
        contentValues.put( DBHelper.NAME, "小華"+r);
        contentValues.put( DBHelper.GENDER, "man");
        contentValues.put( DBHelper.ADDRESS, "太平洋5街");
        contentValues.put( DBHelper.BIRTH, "2050-11-01");
        contentValues.put( DBHelper.WEIGHT, 22.478687);

        //return 成功:1，失敗為0
        return db.update(DBHelper.TableName, contentValues,
                DBHelper.ID + "=?",new String[] {ID});

    }
    public void check()
    {
        /**selection:
         欄位1 = ? and 欄位2 = ?

         selectionArgs:不用管資料型態，就算該欄位資料為數字，也是放字串資料
         new String[]{"欄位1值","欄位2值"}

         EX: db.query(DBHelper.TableName,columns,DBHelper.ID+" =? and "+DBHelper.GENDER + " =?",new String[]{"2","woman"},null, null, null);
        **/

        Cursor cursor = db.query(DBHelper.TableName,columns,DBHelper.GENDER+"=?",new String[]{"man"},null, null, null);

        if(cursor.getCount() > 0) //多筆數量
        {
            while(cursor.moveToNext())
            {
                String NAME = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
                Float WEIGHT = cursor.getFloat(cursor.getColumnIndex(DBHelper.WEIGHT));

                Log.v("Tag","查到的資料為: "+NAME+" "+ WEIGHT);
            }
        }
    }
}
