package space.ahaha.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;


import com.allen.library.SuperTextView;
import com.gjiazhe.scrollparallaximageview.ScrollParallaxImageView;
import com.gjiazhe.scrollparallaximageview.parallaxstyle.HorizontalScaleStyle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import space.ahaha.project.Utils.Constant;
import space.ahaha.project.Utils.SharedPreferencesUtils;

import static android.R.attr.name;
import static java.security.AccessController.getContext;
import static space.ahaha.project.R.id.spinner;

public class Item extends AppCompatActivity{

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private SuperTextView superTextView;
    private TextView title;
    private RecyclerView rvScale;

    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_item);

        title = (TextView)findViewById(R.id.itemtitle);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值

        name = bundle.getString("name");
        switch(name)
        {
            case "rcbj":
                title.setText(getResources().getString(R.string.rcbj));
                break;
            case "dbdl":
                title.setText(getResources().getString(R.string.dbdl));
                break;
            case "sfby":
                title.setText(getResources().getString(R.string.sfby));
                break;
            case "dsc":
                title.setText(getResources().getString(R.string.dsc));
                break;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        TextFragment Text = new TextFragment();
        transaction.add(R.id.center, Text, "Text");
        transaction.commit();


        rvScale = (RecyclerView) findViewById(R.id.rv_horizontal_scale);
        rvScale.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvScale.setAdapter(new MyAdapter(new HorizontalScaleStyle()));

        superTextView = (SuperTextView) findViewById(R.id.setOrder);
        superTextView.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onSuperTextViewClick() {
                super.onSuperTextViewClick();
                boolean isSignin= (boolean) SharedPreferencesUtils.getParam(Item.this, Constant.USER_STATUS, false);
                if(!isSignin){
                    Toast.makeText(getApplicationContext(),"您还没有登录呢！", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Item.this, LoginActivity.class);
                    Item.this.startActivity(intent);
                    return;
                }
                Intent intent= new Intent(Item.this, SetOrderActivity.class);

                Bundle bundle=new Bundle();

                bundle.putString("type", name);

                intent.putExtras(bundle);

                startActivityForResult(intent, 001);
            }

            @Override
            public void onLeftTopClick() {
                super.onLeftTopClick();
                //do something
            }

            @Override
            public void onLeftBottomClick() {
                super.onLeftBottomClick();
                //do something
            }

            @Override
            public void onLeftBottomClick2() {
                super.onLeftBottomClick2();
                //do something
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (001 == requestCode) {
            if (1 == resultCode) {
                setResult(1);
                finish();
            }

        }

    }
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        private ScrollParallaxImageView.ParallaxStyle parallaxStyle;

        MyAdapter(ScrollParallaxImageView.ParallaxStyle parallaxStyle) {
            this.parallaxStyle = parallaxStyle;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_img_horizontal, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.iv.setParallaxStyles(parallaxStyle);
            switch(name)
            {
                case "rcbj":
                    switch (position) {
                        case 0 : holder.iv.setImageResource(R.drawable.rcbj1); break;
                        case 1 : holder.iv.setImageResource(R.drawable.rcbj2); break;
                        case 2 : holder.iv.setImageResource(R.drawable.rcbj3); break;
                        case 3 : holder.iv.setImageResource(R.drawable.rcbj4); break;
                        //case 4 : holder.iv.setImageResource(R.drawable.rcbj1); break;
                    }
                    break;
                case "dbdl":
                    switch (position) {
                        case 0 : holder.iv.setImageResource(R.drawable.dbdl1); break;
                        case 1 : holder.iv.setImageResource(R.drawable.dbdl2); break;
                        case 2 : holder.iv.setImageResource(R.drawable.dbdl3); break;
                        case 3 : holder.iv.setImageResource(R.drawable.dbdl4); break;
                       // case 4 : holder.iv.setImageResource(R.drawable.pic5); break;
                    }
                    break;
                case "sfby":
                    switch (position) {
                        case 0 : holder.iv.setImageResource(R.drawable.sfby1); break;
                        case 1 : holder.iv.setImageResource(R.drawable.sfby2); break;
                        case 2 : holder.iv.setImageResource(R.drawable.sfby3); break;
                        case 3 : holder.iv.setImageResource(R.drawable.sfby4); break;
                     //   case 4 : holder.iv.setImageResource(R.drawable.sfby1); break;
                    }
                    break;
                case "dsc":
                    switch (position) {
                        case 0 : holder.iv.setImageResource(R.drawable.dsc1); break;
                        case 1 : holder.iv.setImageResource(R.drawable.dsc2); break;
                        case 2 : holder.iv.setImageResource(R.drawable.dsc3); break;
                        case 3 : holder.iv.setImageResource(R.drawable.dsc4); break;
                     //   case 4 : holder.iv.setImageResource(R.drawable.dsc1); break;
                    }
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ScrollParallaxImageView iv;
            ViewHolder(View itemView) {
                super(itemView);
                iv = (ScrollParallaxImageView) itemView.findViewById(R.id.img);
            }
        }
    }
    public static  class TextFragment extends ListFragment {

        String title;

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //新接收数据
            Bundle bundle = getActivity().getIntent().getExtras();
            //接收name值
            title = bundle.getString("name");
            SampleTextListAdapter adapter = new SampleTextListAdapter(getActivity(),title);
            setListAdapter(adapter);
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // 设置ListFragment默认的ListView，即@id/android:list
        }
    }
}
