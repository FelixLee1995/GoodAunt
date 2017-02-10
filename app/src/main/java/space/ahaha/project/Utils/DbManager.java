package space.ahaha.project.Utils;

import android.content.Context;

/**
 * Created by lf729 on 2016/12/25.
 */

public class DbManager {
    private  static MySqliteHelper helper;
    public  static MySqliteHelper getInstance(Context context){
        if(helper==null){
            helper=new MySqliteHelper(context);
        }
        return  helper;
    }

}
