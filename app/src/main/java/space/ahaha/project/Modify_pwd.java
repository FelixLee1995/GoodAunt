package space.ahaha.project;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import space.ahaha.project.Utils.Constant;
import space.ahaha.project.Utils.SharedPreferencesUtils;

import static space.ahaha.project.Utils.PasswordFormatCheckUtils.isPwdLegal;



public class Modify_pwd extends AppCompatActivity {
    private UserUpdateTask mAuthTask = null;
    private EditText pre_pwd,new_pwd_1,new_pwd_2;
    private Button button;
    private int pwd_valid=0;
    private int pwd_error=1;
    private int pwd_unmatch=2;
    private int pwd_invalid=3;
    private String network_error="Bad network";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setTitle("");
        setContentView(R.layout.activity_modify_pwd);
        TextView textView = (TextView)findViewById(R.id.not_use);    //一个不使用的TextView来获取焦点，使得创建activity时 editText不自动弹出软键盘
        textView.requestFocus();
        pre_pwd= (EditText) findViewById(R.id.pre_pwd);
        new_pwd_1= (EditText) findViewById(R.id.new_pwd_1);
        new_pwd_2= (EditText) findViewById(R.id.new_pwd_2);
        button= (Button) findViewById(R.id.modify_btn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pre_pwd.getText().toString().isEmpty()||!new_pwd_1.getText().toString().isEmpty()||new_pwd_2.getText().toString().isEmpty()){
                    dialog();
                }
                else finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPwdValid()==pwd_invalid){
                    Toast.makeText(Modify_pwd.this,"请输入合法新密码！",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(isPwdValid()==pwd_unmatch){
                    Toast.makeText(Modify_pwd.this,"请确认密码是否相同！",Toast.LENGTH_SHORT).show();
                }
                else attemptModify();
            }
        });




    }


    private int isPwdValid(){
        if(isPwdLegal(new_pwd_1.getText().toString())&&isPwdLegal(new_pwd_2.getText().toString())){
            if(new_pwd_1.getText().toString().equals(new_pwd_2.getText().toString())){
                return pwd_valid;
            }
            else return pwd_unmatch;
        }
        else return pwd_invalid;
    }

    private void attemptModify(){
        if (mAuthTask != null) {
            return;
        }
        String user_phone= (String) SharedPreferencesUtils.getParam(Modify_pwd.this, Constant.USER_PHONE,"");
        mAuthTask = new UserUpdateTask(user_phone, pre_pwd.getText().toString(),new_pwd_1.getText().toString());
        mAuthTask.execute((Void) null);

    }




    public class UserUpdateTask extends AsyncTask<Void, Void, String> {

        private String mPhone;
        private String pre_pwd;
        private String new_pwd;
        private final String url = "http://115.159.24.100:8080/GoodAunt/updatePassword?";

        UserUpdateTask(String phone, String pre_pwd,String new_pwd) {
            this.mPhone = phone;
            this.pre_pwd = pre_pwd;
            this.new_pwd=new_pwd;
        }


        @Override
        protected String doInBackground(Void... params) {

            // Simulate network access.
            StringBuffer stringBuffer = new StringBuffer();
            String str;
            try {
                URL httpURL = null;
                httpURL = new URL(url);
                HttpURLConnection connection = null;
                connection = (HttpURLConnection) httpURL.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(5000);
                OutputStream out = null;
                out = connection.getOutputStream();
                String content = "phoneNumber=" + mPhone + "&password1="+pre_pwd +"&password2="+new_pwd;
                out.write(content.getBytes());
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }

                JSONObject jsonObj = new JSONObject(stringBuffer.toString());
                String code=jsonObj.getString("code");
                return code;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return network_error;
        }

        @Override
        protected void onPostExecute(final String code) {
            mAuthTask = null;
            if (code.equals("666")) {
                Toast.makeText(Modify_pwd.this, "修改成功，请重新登录", Toast.LENGTH_SHORT).show();
                SharedPreferencesUtils.setParam(Modify_pwd.this,Constant.USER_STATUS,false);
                Intent logoutIntent = new Intent(Modify_pwd.this, LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
            }
            else if(code.equals("405")) {
                Toast.makeText(Modify_pwd.this, "修改失败，密码错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Modify_pwd.this);
        builder.setMessage("建议尚未保存，是否退出？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Modify_pwd.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
