package space.ahaha.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.allen.library.SuperTextView;

public class Feedback extends AppCompatActivity {
    private SuperTextView opinion;
    private SuperTextView problem_1;
    private SuperTextView problem_2;
    private SuperTextView problem_3;
    private SuperTextView problem_4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setTitle("");
        setContentView(R.layout.activity_feedback);
        opinion= (SuperTextView) findViewById(R.id.opinion);
        problem_1= (SuperTextView) findViewById(R.id.problem_1);
        problem_2= (SuperTextView) findViewById(R.id.problem_2);
        problem_3= (SuperTextView) findViewById(R.id.problem_3);
        problem_4= (SuperTextView) findViewById(R.id.problem_4);

        problem_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Feedback.this)
                        .setMessage(getString(R.string.problem_1))
                        .setPositiveButton("确认",null).setTitle(getString(R.string.problem_title_1))
                        .show();


            }
        });
        problem_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Feedback.this)
                        .setMessage(getString(R.string.problem_2))
                        .setPositiveButton("确认",null).setTitle(getString(R.string.problem_title_2))
                        .show();


            }
        });
        problem_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Feedback.this)
                        .setMessage(getString(R.string.problem_3))
                        .setPositiveButton("确认",null).setTitle(getString(R.string.problem_title_3))
                        .show();


            }
        });
        problem_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Feedback.this)
                        .setMessage(getString(R.string.problem_4))
                        .setPositiveButton("确认",null).setTitle(getString(R.string.problem_title_4))
                        .show();


            }
        });


        opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Feedback.this,CommitFeedback.class);
                startActivity(intent);
            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
