package com.example.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

/**
 SQLite不像一般大型資料庫，需先建立好資料庫的實體(如:建立資料庫、建立資料表、計設資料表欄位等)，再經由程式進行存取。
 SQLite是經由 Android幫你建立的，建立SQLite需要繼承 SQLiteOpenHelper
 */
public class DBHelper extends SQLiteOpenHelper
{
    private final static int DBVersion = 1;  //版本
    private final static String DBName = "SampleList.db";  //資料庫名
    public final static String TableName = "MySample";  //資料表名


    public DBHelper(@Nullable Context context)
    {
        super(context, DBName, null, DBVersion);
    }

    public static final String ID = "_id"; //primary key 的名字要設定為 _id
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String ADDRESS = "address";
    public static final String BIRTH = "birth";
    public static final String WEIGHT = "weight";
    public static final String TIME = "time";
    public static final String PHONE = "phone";

    /**
    如果 Android 載入時找不到生成的資料庫檔案，就會觸發 onCreate()，所以產生資料表的 SQL 要寫在onCreate()裡。
    一個資料庫可新增、使用多個資料表。
    新增資料表:
    CREATE TABLE IF NOT EXISTS 表格名 (欄位1 資料種類,欄位 2 資料種類, ... );
    */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //版本一
        final String SQL =
            "CREATE TABLE "+ TableName
            + "("
            + ID        + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
            + NAME      + " TEXT NOT NULL"                     + ","
            + GENDER    + " VARCHAR(10) DEFAULT 'man'"         + ","
            + ADDRESS   + " VARCHAR(50)"                       + ","
            + BIRTH     + " DATE NOT NULL"                     + ","
            + WEIGHT    + " FLOAT(2)"                          + ","
            + TIME      + " TIME"
            +")";
        db.execSQL(SQL);
//        //版本二
//        final String SQL =
//                "CREATE TABLE "+ TableName
//                        + "("
//                        + ID        + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
//                        + NAME      + " TEXT NOT NULL"                     + ","
//                        + GENDER    + " VARCHAR(10) DEFAULT 'man'"         + ","
//                        + ADDRESS   + " VARCHAR(50)"                       + ","
//                        + BIRTH     + " DATE NOT NULL"                     + ","
//                        + WEIGHT    + " FLOAT(2)"                          + ","
//                        + TIME      + " TIME"                              + ","
//                        + PHONE     + " TEXT"
//                        +")";
//        db.execSQL(SQL);
    }
    /**
     常見資料型態

     數字
     INTEGER:介於 -2,147,483,648 與 2,147,483,647 間的整數
     FLOAT:FLOAT(n)  n是儲存 float 數字的小數位數，介於 1 與 53 間
     DOUBLE

     字符
     CHAR
     VARCHAR
     TEXT:可放2的16次方減1個字元，用來儲存大量的資料

     時間
     DATE:日期欄位,預設格式yyyy-mm-dd, 範圍 1000-01-01~ 19999-12-3。
     TIME:格式為 hh:mm:dd,範圍 -838:59:59~838:59:59。
     YEAR:記錄年份,範圍 1901~2155。
     DATETIME:日期加上時間欄位,預設格式yyyy-mm-dd hh:mm:ss,範圍 1000-01-01 00:00:00~9999-12-31 23:59:59。
     ---------
     PRIMARY KEY AUTOINCREMENT :該欄位的數值會自動編號,每增一筆資料時,此欄位自動把編號加一。(此欄位須為索引(主键)、數值資料型態，一個資料表只能有一個欄位使用)。
     */




    /**
     如果資料庫結構有改變了就會觸發 onUpgrade，需在onUpgrade()中新增兩版本間差異的部分
     oldVersion為上一次執行時所用的版本
     newVersion為我們透過建構子裡super給SQLiteOpenHelper的值
     **/
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion)//
    {
        //用switch來判斷舊版本與新版本間需要做甚麼修改
        switch (oldVersion) {
            case 1:
                //方法一:新增兩版本差異的部分
                String sql = "ALTER TABLE " + TableName + " ADD " + PHONE + " TEXT";
                db.execSQL(sql);

//                //方法二:使用TEMP搬移資料
//                //將舊資料表重新命名
//                db.execSQL("ALTER TABLE "+TableName +" RENAME TO "+"TEMP_TABLE");
//                //建立新資料表
//                db.execSQL("DROP TABLE IF EXISTS "+TableName);
//                final String SQL =
//                    "CREATE TABLE "+ TableName
//                            + "("
//                            + ID        + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
//                            + NAME      + " TEXT NOT NULL"                     + ","
//                            + GENDER    + " VARCHAR(10) DEFAULT 'man'"         + ","
//                            + ADDRESS   + " VARCHAR(50)"                       + ","
//                            + BIRTH     + " DATE NOT NULL"                     + ","
//                            + WEIGHT    + " FLOAT(2)"                          + ","
//                            + TIME      + " TIME"                              + ","
//                            + PHONE     + " TEXT"
//                            +")";
//                db.execSQL(SQL);
//                //將舊資料insert到新的資料表
//                db.execSQL("INSERT INTO "+TableName+" ("+NAME+", "+BIRTH+") " +"SELECT "+NAME+","+ BIRTH +" FROM "+"TEMP_TABLE");
//                //刪除舊資料表
//                db.execSQL("DROP TABLE IF EXISTS "+"TEMP_TABLE");

                break;

            case 2:
                break;
        }
    }
}
