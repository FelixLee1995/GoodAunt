package space.ahaha.project;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import space.ahaha.project.Utils.Constant;
import space.ahaha.project.Utils.DbManager;
import space.ahaha.project.Utils.MySqliteHelper;
import space.ahaha.project.Utils.SharedPreferencesUtils;

import static space.ahaha.project.Utils.PhoneFormatCheckUtils.isPhoneLegal;

public class AddAddress extends AppCompatActivity {
    private EditText name,phone,addr_sub;
    private TextView addr;
    private Button save_addr;
    private LinearLayout choose_addr;
    private final int valid=0;
    private final int empty=1;
    private final int invalid_phone=2;
    private MySqliteHelper helper;
    private int address_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setTitle("");
        setContentView(R.layout.activity_add_address);

        name= (EditText) findViewById(R.id.name);
        phone= (EditText) findViewById(R.id.phone);
        addr_sub= (EditText) findViewById(R.id.addr_sub);
        addr= (TextView) findViewById(R.id.addr);
        save_addr= (Button) findViewById(R.id.save_addr);
        choose_addr= (LinearLayout) findViewById(R.id.choose_addr);

        
        choose_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AddAddress.this,ChooseProvince.class);
                startActivityForResult(intent,12);
            }
        });
        
        save_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValid()==valid){

                    helper= DbManager.getInstance(AddAddress.this);
                    SQLiteDatabase db=helper.getWritableDatabase();
                    address_count= (int) SharedPreferencesUtils.getParam(AddAddress.this,"address_count",0);
                    String user_phone= (String) SharedPreferencesUtils.getParam(AddAddress.this,Constant.USER_PHONE,"");
                    ContentValues values=new ContentValues();
                    values.put(Constant.ID,address_count);
                    values.put(Constant.USER_PHONE,user_phone);
                    values.put(Constant.NAME,name.getText().toString());
                    values.put(Constant.PHONE,phone.getText().toString());
                    values.put(Constant.ADDRESS,addr.getText().toString());
                    values.put(Constant.ADDRESS_SUB,addr_sub.getText().toString());
                    values.put(Constant.ISDEFAULT,0);
                    db.insert(Constant.ADDRESS_TABLE_NAME,null,values);
                    SharedPreferencesUtils.setParam(AddAddress.this,"address_count",address_count+1);
                    db.close();
                    Intent intent=new Intent();
                    setResult(11,intent);
                    finish();
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getText().toString().isEmpty()||!name.getText().toString().isEmpty()||!addr.getText().toString().isEmpty()||!addr_sub.getText().toString().isEmpty()){
                    dialog();
                }
                else finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==12&&resultCode==12){
            Bundle bundle=data.getExtras();
            String province=bundle.getString("province");
            String city=bundle.getString("city");
            String district=bundle.getString("district");
            addr.setText(province+city+"   "+district);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!phone.getText().toString().isEmpty()||!name.getText().toString().isEmpty()||!addr.getText().toString().isEmpty()||!addr_sub.getText().toString().isEmpty()){
                dialog();

            }
            else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    protected void dialog() {
         AlertDialog.Builder builder = new AlertDialog.Builder(AddAddress.this);
        builder.setMessage("地址尚未保存，是否退出？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AddAddress.this.finish();
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

    private int checkValid(){
        if(phone.getText().toString().isEmpty()||name.getText().toString().isEmpty()||addr.getText().toString().isEmpty()||addr_sub.getText().toString().isEmpty()){
            Toast.makeText(AddAddress.this,"请完善地址信息",Toast.LENGTH_SHORT).show();
            return empty;
        }
        else if(!isPhoneLegal(phone.getText().toString())){
            Toast.makeText(AddAddress.this,"手机号码不正确",Toast.LENGTH_SHORT).show();
            return invalid_phone;
        }
        else return valid;
    }

}
