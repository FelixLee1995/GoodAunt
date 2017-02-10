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

public class ChooseDistrict extends AppCompatActivity {
    private String city;
    private String province;
    private RecyclerView mRecyclerView;
    private ChooseDistrict.HomeAdapter mAdapter;

    ArrayList<String> mDatas = new ArrayList<String>();
    ArrayList<String>mIDs=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setTitle("");
        setContentView(R.layout.activity_choose_district);


        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ChooseDistrict.HomeAdapter();
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
        try {
            Intent intent=getIntent();
            Bundle bundle= intent.getExtras();
            String pre_pos= bundle.getString("position");
            city=bundle.getString("city");
            province=bundle.getString("province");
            InputStream is = getAssets().open("districts.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("District")&&parser.getAttributeValue(2).equals(pre_pos)){
                            mIDs.add(parser.getAttributeValue(0));
                            eventType = parser.next();
                            mDatas.add(parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        break;



                }
                eventType = parser.next();
            }




        } catch (Exception e) {

        }
    }

    class HomeAdapter extends RecyclerView.Adapter<ChooseDistrict.HomeAdapter.MyViewHolder> {

        @Override
        public ChooseDistrict.HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            ChooseDistrict.HomeAdapter.MyViewHolder holder = new ChooseDistrict.HomeAdapter.MyViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.district_item, parent,
                    false));


            return holder;
        }

        @Override
        public void onBindViewHolder(ChooseDistrict.HomeAdapter.MyViewHolder holder, final int position)
        {
            holder.tv.setText(mDatas.get(position));
            holder.province.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent();
                    Bundle bundle=new Bundle();
                    String data=mDatas.get(position);
                    bundle.putString("district",data);
                    bundle.putString("city",city);
                    bundle.putString("province",province);
                    bundle.putString("position",mIDs.get(position));
                    intent.putExtras(bundle);
                    setResult(12,intent);
                    finish();
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



}
