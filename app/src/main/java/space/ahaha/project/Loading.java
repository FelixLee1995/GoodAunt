package space.ahaha.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import space.ahaha.project.Utils.Constant;
import space.ahaha.project.Utils.SharedPreferencesUtils;

/**
 * Created by lf729 on 2016/12/12.
 */
public class Loading extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        this.setContentView(R.layout.loading);
        new Handler().postDelayed(r, 2000);
    }


    Runnable r = new Runnable() {
        @Override
        public void run() {
            boolean status= (boolean) SharedPreferencesUtils.getParam(Loading.this, Constant.USER_STATUS,false);
            String phone= (String) SharedPreferencesUtils.getParam(Loading.this,Constant.USER_PHONE,"");
            if(status==true){
                Intent intent = new Intent();
                intent.setClass(Loading.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent();
                intent.setClass(Loading.this, space.ahaha.project.LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
}
