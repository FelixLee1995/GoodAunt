package space.ahaha.project.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lf729 on 2016/12/25.
 */

public class MySqliteHelper extends SQLiteOpenHelper {

    public MySqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySqliteHelper(Context context) {
        super(context, Constant.DATABASE_NAME, null , Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table "+Constant.ADDRESS_TABLE_NAME+"("+Constant.ID+" Integer primary key,"+Constant.USER_PHONE+" varchar(20),"+Constant.NAME+" varchar(50),"+Constant.PHONE+" varchar(20),"+Constant.ADDRESS+" varchar(100),"+Constant.ADDRESS_SUB+" varchar(100),"+Constant.ISDEFAULT+" Integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
