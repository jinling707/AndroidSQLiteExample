## AndroidSQLiteExample

### 簡介
Android中本地的數據資料庫

<!-- more -->

### 說明
SQLite不像一般大型資料庫，需先建立好資料庫的實體(如:建立資料庫、建立資料表、計設資料表欄位等)，再經由程式進行存取。
SQLite是經由 Android幫你建立的，建立SQLite需要繼承 SQLiteOpenHelper

可在Android Debug Database來查看手機資料庫的狀況
#### 使用Android Debug Database
(需使用手機而非模擬器，並開啟網路)
在 module 的 build.gradle 中添加
``` java
debugImplementation 'com.amitshekhar.android:debug-db:1.0.0'
```
在執行時，查找Logcat裡
``` 
D/DebugDB: Open http://XXX.XXX.X.XXX:8080 in your browser
```
並在瀏覽器中打開網址，便可察看手機資料庫


### 範例

#### 建立資料庫 DBHelper.java
``` java

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
    }

    /**
     如果資料庫結構有改變了就會觸發 onUpgrade，需在onUpgrade()中新增兩版本間差異的部分
     oldVersion為上一次執行時所用的版本
     newVersion為我們透過建構子裡super給SQLiteOpenHelper的值
     **/
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion)//
    {
 
    }
}

```
##### 常見資料型態
數字
INTEGER:介於 -2,147,483,648 與 2,147,483,647 間的整數
FLOAT:FLOAT(n)  n是儲存 float 數字的小數位數，介於 1 與 53 間
DOUBLE

字符
CHAR
VARCHAR
TEXT:可放2的16次方減1個字元，用來儲存大量的資料

時間
DATE:日期欄位,格式 yyyy-mm-dd, 範圍 1000-01-01 ~ 19999-12-3。
TIME:格式 hh:mm:dd,範圍 -838:59:59 ~ 838:59:59。
YEAR:記錄年份,範圍 1901 ~ 2155。
DATETIME:日期加上時間,格式yyyy-mm-dd hh:mm:ss,範圍 1000-01-01 00:00:00 ~ 9999-12-31 23:59:59。

##### PRIMARY KEY AUTOINCREMENT 
該欄位的數值會自動編號,每增一筆資料時,此欄位自動把編號加一。(此欄位須為索引(主键)、數值資料型態，一個資料表只能有一個欄位使用)。


#### 使用資料庫 CRUD.java
操作資料庫的增、刪、改、查
``` java
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
        //return 成功:1，失敗為0
        return db.delete(DBHelper.TableName, DBHelper.ID + "=?", new String[] {ID});
        // 也可這樣寫 db.delete(DBHelper.TableName, DBHelper.ID + "=\"" + ID+"\"", null);
    }

    public int update(String ID)
    {
         int r =(int)(Math.random()*1000);

        ContentValues contentValues = new ContentValues();
        contentValues.put( DBHelper.NAME, "小華"+r);
        contentValues.put( DBHelper.GENDER, "man");
        contentValues.put( DBHelper.ADDRESS, "太平洋5街");
        contentValues.put( DBHelper.BIRTH, "2050-11-01");
        contentValues.put( DBHelper.WEIGHT, 22.478687);

        //return 成功:1，失敗為0
        return db.update(DBHelper.TableName, contentValues,DBHelper.ID + "=?",new String[] {ID});

    }

    public void check()
    {
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
```
##### 比對、找尋的欄位資料為2個以上時
whereClause/selection:
欄位1 = ? and 欄位2 = ?

whereArgs/selectionArgs:不用管資料型態，就算該欄位資料為數字，也是放字串資料
new String[]{"欄位1值","欄位2值"}
    
EX:
刪除
``` java
db.delete(DBHelper.TableName, DBHelper.ID + "=? and "+DBHelper.GENDER+" =?", new String[] {ID,"woman"});
```
查看
``` java
db.query(DBHelper.TableName,columns,DBHelper.ID+" =? and "+DBHelper.GENDER + " =?",new String[]{"2","woman"},null, null, null);
```
##### 刪除資料表
db.delete(資料表名, null, null);
``` java
db.delete(DBHelper.TableName, null, null);
```
##### 設定主鍵(自動遞增)為0
update sqlite_sequence set seq=0 where name='資料表名'
``` java
db.execSQL("update sqlite_sequence set seq=0 where name='"+ DBHelper.TableName +"'");
```
     
#### MainActivity.java
``` java
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
    CRUD crud;
    EditText ET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ET = findViewById(R.id.ET);
    }
    
    public void add(View view)
    {
        crud = new CRUD(getApplicationContext());
        crud.open();
        long i = crud.add();
        Log.v("Tag","add"+i);
        crud.close();
    }

    public void delete(View view)
    {
        crud = new CRUD(getApplicationContext());
        crud.open();
        crud.delete(ET.getText().toString());
        crud.close();
    }

    public void update(View view)
    {
        crud = new CRUD(getApplicationContext());
        crud.open();
        crud.update(ET.getText().toString());
        crud.close();
    }

    public void check(View view) {
        crud = new CRUD(getApplicationContext());
        crud.open();
        crud.add();
        crud.check();
    }
}
```
#### activity_main.xml
``` xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:orientation="vertical">

    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="增"
        android:onClick="add"/>
    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="查"
        android:onClick="check"/>
    <View
        android:layout_width="match_parent"
        android:layout_height="5dp"
        android:background="#303F9F"/>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="刪/改 ID"
        android:textColor="#ff0000"/>

    <EditText
        android:id="@+id/ET"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="#ff0000"/>

    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="刪"
        android:onClick="delete"
        android:textColor="#ff0000"/>
    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="改"
        android:onClick="update"
        android:textColor="#ff0000"/>
    
</LinearLayout>
```

### 資料庫Update說明

APP開展的過程中，若需要修改、更動資料庫，可直接在設定中清除APP資料。然後重新建立資料庫。
若是產品已經發布，或是需要保留原先資料庫數據，就必須用Upgrade的方式。
Upgrade可讓SQLite新增欄位，但不能對舊欄位做刪改，如欄位名稱、型態都不能改，當然也不能刪除。若真需要做欄位的刪改，須將原先資料庫設為暫時資料庫，待建立新資料庫後，再將資料搬移到新資料庫中。
### 資料庫Update範例
1.將新版本的資料表寫在onCreate(如下onCreate的表已改為"版本二"的)
2.修改版本變數 DBVersion = 2(該值透過建構子裡super給SQLiteOpenHelper)
3.在onUpgrade()中新增兩版本間差異的部分，可用switch來判斷舊版本與新版本間需要做甚麼修改

DBHelper.java
``` java
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper
{
    private final static int DBVersion = 2;  //版本
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
       //版本二
       final String SQL =
               "CREATE TABLE "+ TableName
                       + "("
                       + ID        + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
                       + NAME      + " TEXT NOT NULL"                     + ","
                       + GENDER    + " VARCHAR(10) DEFAULT 'man'"         + ","
                       + ADDRESS   + " VARCHAR(50)"                       + ","
                       + BIRTH     + " DATE NOT NULL"                     + ","
                       + WEIGHT    + " FLOAT(2)"                          + ","
                       + TIME      + " TIME"                              + ","
                       + PHONE     + " TEXT"
                       +")";
       db.execSQL(SQL);
    }

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
```
若是單純的增加資料表欄位，在onUpgrade可用方法一來新增兩版本差異的部分
而若是資料表需要修、刪，可用方法二，將原先資料庫設為暫時資料庫，待建立新資料庫後，再將資料搬移到新資料庫中。


### 參考資料
Android Debug Database:
https://www.itread01.com/content/1544582526.html
SQLite:
https://sweeteason.pixnet.net/blog/post/37364146-android-%E4%BD%BF%E7%94%A8-sqlite-%E8%B3%87%E6%96%99%E5%BA%AB%E7%9A%84%E6%96%B9%E6%B3%95
onUpgrade:
https://mrraybox.blogspot.com/2017/01/android-sqlite-onupgrade.html
https://ithelp.ithome.com.tw/articles/10188563
SQL資料型態:
PHP+MySQL 快速入門(滄海)
