package space.ahaha.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;

import static org.xmlpull.v1.XmlPullParser.START_TAG;

public class ChooseProvince extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    ArrayList<String>mIDs=new ArrayList<String>();
    ArrayList<String>mDatas = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setTitle("");
        setContentView(R.layout.activity_choose_province);
        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HomeAdapter();
        mRecyclerView.setAdapter(mAdapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    protected void initData()
    {
        String str=null;
        try {
            InputStream is = getAssets().open("provinces.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case START_TAG:
                        if(parser.getName().equals("Province")){
                            mIDs.add(parser.getAttributeValue(0));
                            eventType=parser.next();
                            mDatas.add(parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        break;
                }
                eventType = parser.next();
            }



        is.close();
        } catch (Exception e) {
        }

    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.district_item, parent,
                    false));


            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position)
        {
            holder.tv.setText(mDatas.get(position));
            holder.tv.setTag(position);
            holder.province.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(ChooseProvince.this,ChooseCity.class);
                    Bundle bundle=new Bundle();
                    String data=mDatas.get(position);
                    String pos=mIDs.get(position);
                    bundle.putString("province",data);
                    bundle.putString("position",pos);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,12);
//                    Toast.makeText(ChooseProvince.this,"2333",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder
        {
            public TextView tv;
            public RelativeLayout province;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.district);
                province= (RelativeLayout) view.findViewById(R.id.district_item);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: 2016/12/24

        if(resultCode==12){
            Bundle bundle=data.getExtras();
            Intent intent=new Intent();
            intent.putExtras(bundle);
            setResult(12,intent);
            finish();
        }
    }


}

