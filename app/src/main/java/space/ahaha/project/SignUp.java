package space.ahaha.project;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import static space.ahaha.project.Utils.PhoneFormatCheckUtils.isPhoneLegal;

/**
 * Created by lf729 on 2016/11/27.
 */
public class SignUp extends AppCompatActivity{
    private Button sign_up;
    private EditText phone,pwd1,pwd2;
    private ProgressBar progressBar;
    private String network_error="Bad network";
    private TextView textView;
    private boolean cancel=true;       //当cancel置为false时，在耗时注册结束之前，不允许退出当前activity

    private UserSignupTask mAuthTask=null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setTitle("");
        setContentView(R.layout.sign_up);

        phone= (EditText) findViewById(R.id.phone_sign_up);
        pwd1= (EditText) findViewById(R.id.password1);
        pwd2= (EditText) findViewById(R.id.password2);
        sign_up= (Button) findViewById(R.id.sign_up_btn);
        progressBar= (ProgressBar) findViewById(R.id.loading_process_dialog_progressBar);
        textView= (TextView) findViewById(R.id.agreement);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog=new MyDialog(v.getContext(),getResources().getString(R.string.agreement_content));
                myDialog.show();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getText().toString().isEmpty()||!pwd1.getText().toString().isEmpty()||!pwd2.getText().toString().isEmpty()){
                    dialog();
                }
                else finish();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkValid(phone.getText().toString(),pwd1.getText().toString(),pwd2.getText().toString())==true){
                    attempt_sign_up(phone.getText().toString(),pwd1.getText().toString(),pwd2.getText().toString());
                }
            }
        });


    }

    public boolean checkValid(String phone,String pwd1, String pwd2){
        if(phone.length()==0){
            Toast.makeText(this,"请输入手机号码！",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd1.length()==0){
            Toast.makeText(this,"请输入密码！",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd2.length()==0){
            Toast.makeText(this, "请确认密码！",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!isPhoneLegal(phone)){
            Toast.makeText(this, "请输入合法手机号码！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!isPwdLegal(pwd1)||!isPwdLegal(pwd2)){
            Toast.makeText(this, "请输入合法密码！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!pwd1.equals(pwd2)){
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void attempt_sign_up(String phone,String pwd, String auth_code){
        if (mAuthTask != null) {
            return;
        }
        cancel=false;
        showProgress(true);
        mAuthTask = new UserSignupTask(phone, pwd,auth_code);
        mAuthTask.execute((Void) null);
    }





    public class UserSignupTask extends AsyncTask<Void, Void, String> {

        private String mPhone;
        private String mPassword;
        private String mAuth_code;
        private final String url = "http://115.159.24.100:8080/GoodAunt/register?";

        UserSignupTask(String phone, String password,String auth_code) {
            mPhone = phone;
            mPassword = password;
            mAuth_code=auth_code;
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
                String content = "phoneNumber=" + mPhone + "&password=" + mPassword;
                out.write(content.getBytes());
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }

                JSONObject jsonObj = new JSONObject(stringBuffer.toString());
                String code=jsonObj.getString("code");

                if(code.equals("666")) {
                    JSONObject jsonUser = jsonObj.getJSONObject("information");
                    String phonenumber = jsonUser.getString("phoneNumber");
                    String avatar = jsonUser.getString("avatar");
                    SharedPreferencesUtils.setParam(SignUp.this, Constant.USER_STATUS, true);
                    SharedPreferencesUtils.setParam(SignUp.this, Constant.USER_PHONE, phonenumber);
                    return code;
                }
                else if(code.equals("505")){
                    return code;
                }


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
                SharedPreferencesUtils.setParam(SignUp.this, Constant.USER_STATUS,true);
                SharedPreferencesUtils.setParam(SignUp.this,Constant.USER_PHONE,mPhone);
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else if(code.equals("505")){
                Toast.makeText(SignUp.this, "注册失败，用户已存在", Toast.LENGTH_SHORT).show();
                showProgress(false);
                cancel=true;
            }
           else if(code.equals(network_error)){
                Toast.makeText(SignUp.this, "请检查网络状况", Toast.LENGTH_SHORT).show();
                showProgress(false);
                cancel=true;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(cancel==false) {

                return false;
            }
            else {
                if(!phone.getText().toString().isEmpty()||!pwd1.getText().toString().isEmpty()||!pwd2.getText().toString().isEmpty()){
                    dialog();
                }
                else finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    * 用于在点击登录之后显示进度并disable其他控件
    *
    */
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        builder.setMessage("是否退出注册？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SignUp.this.finish();
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
