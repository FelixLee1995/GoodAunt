package space.ahaha.project;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static space.ahaha.project.Utils.PhoneFormatCheckUtils.isPhoneLegal;

public class CommitFeedback extends AppCompatActivity {
    private EditText phone;
    private EditText editText;
    private TextView commit,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setTitle("");
        setContentView(R.layout.activity_commit_feedback);


        phone= (EditText) findViewById(R.id.phone);
        editText= (EditText) findViewById(R.id.edit_text);
        commit= (TextView) findViewById(R.id.commit);
        back= (TextView) findViewById(R.id.back);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText().toString().isEmpty()||editText.getText().toString().isEmpty()){
                    Toast.makeText(CommitFeedback.this,"请完善您的反馈信息",Toast.LENGTH_SHORT).show();
                }

                else if(!isPhoneLegal(phone.getText().toString())){
                    // TODO: 2016/12/27 提交反馈
                    Toast.makeText(CommitFeedback.this,"请输入正确的手机号码！",Toast.LENGTH_SHORT).show();
                }
                else {
                    // TODO: 2016/12/27 提交反馈
                    Toast.makeText(CommitFeedback.this,"真诚感谢您的反馈！",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getText().toString().isEmpty()||!editText.getText().toString().isEmpty()){
                    dialog();
                }
                else finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getText().toString().isEmpty()||!editText.getText().toString().isEmpty()){
                    dialog();
                }
                else finish();
            }
        });
    }


    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommitFeedback.this);
        builder.setMessage("建议尚未保存，是否退出？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CommitFeedback.this.finish();
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
