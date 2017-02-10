package space.ahaha.project;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.allen.library.SuperTextView;

import java.util.ArrayList;

import space.ahaha.project.Utils.Constant;
import space.ahaha.project.Utils.DbManager;
import space.ahaha.project.Utils.MySqliteHelper;
import space.ahaha.project.Utils.SharedPreferencesUtils;

public class AddressManagement extends AppCompatActivity {
    private int choice;
    private RecyclerView mRecyclerView;
    private AddressManagement.HomeAdapter mAdapter;
    private Button add_address;
    private ArrayList<Address> mDatas=new ArrayList<Address>();
    private MySqliteHelper helper;
    private String phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setTitle("");
        setContentView(R.layout.activity_address_management);
        initData();
        Bundle bundle = this.getIntent().getExtras();
        choice = bundle.getInt("choice");
        add_address= (Button) findViewById(R.id.add_address);

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddressManagement.this,AddAddress.class);
                startActivityForResult(intent,11);
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

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AddressManagement.HomeAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void initData(){
        phone_number= (String) SharedPreferencesUtils.getParam(AddressManagement.this,Constant.USER_PHONE,"");
        int id_idex,phone_idex,name_idex,address_idex,isDefault_index,address_sub_idex,phone_number_index;
        helper= DbManager.getInstance(AddressManagement.this);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from address_management",null);
        while (cursor.moveToNext()) {
            id_idex=cursor.getColumnIndex(Constant.ID);
            phone_idex=cursor.getColumnIndex(Constant.PHONE);
            name_idex=cursor.getColumnIndex(Constant.NAME);
            address_idex=cursor.getColumnIndex(Constant.ADDRESS);
            address_sub_idex=cursor.getColumnIndex(Constant.ADDRESS_SUB);
            isDefault_index=cursor.getColumnIndex(Constant.ISDEFAULT);
            phone_number_index=cursor.getColumnIndex(Constant.USER_PHONE);
            int isDefault=cursor.getInt(isDefault_index);
            int id=cursor.getInt(id_idex);
            String phone=cursor.getString(phone_idex);
            String name=cursor.getString(name_idex);
            String address=cursor.getString(address_idex);
            String address_sub=cursor.getString(address_sub_idex);
            String user_phone=cursor.getString(phone_number_index);
            if(user_phone.equals(phone_number)) {
                Address temp = new Address(id, name, phone, address, address_sub, isDefault);
                mDatas.add(temp);
            }
        }
        cursor.close();
        db.close();

    }

    class HomeAdapter extends RecyclerView.Adapter<AddressManagement.HomeAdapter.MyViewHolder> {


        @Override
        public AddressManagement.HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            AddressManagement.HomeAdapter.MyViewHolder holder = new AddressManagement.HomeAdapter.MyViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.address_item, parent,
                    false));


            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position)
        {   holder.address_item.setLeftTopString(mDatas.get(position).getName());
            String t=mDatas.get(position).getAddress()+mDatas.get(position).getAddress_sub();
            holder.address_item.setLeftBottomString(t);
            holder.address_item.setRightString(mDatas.get(position).getPhone());
            holder.id=mDatas.get(position).getId();
            if(choice == 0) {
                holder.address_item.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
                    @Override
                    public void onSuperTextViewClick() {
                        Intent intent = new Intent(AddressManagement.this, ModifyAddress.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", mDatas.get(position).getId());
                        bundle.putString("name", mDatas.get(position).getName());
                        bundle.putString("phone", mDatas.get(position).getPhone());
                        bundle.putString("address", mDatas.get(position).getAddress());
                        bundle.putString("address_sub", mDatas.get(position).getAddress_sub());
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 15);
                    }
                });
                holder.address_item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddressManagement.this);
                        builder.setMessage("是否删除地址信息？");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                helper = DbManager.getInstance(AddressManagement.this);
                                SQLiteDatabase db = helper.getWritableDatabase();
                                String[] args = {String.valueOf(mDatas.get(position).getId())};
                                db.delete(Constant.ADDRESS_TABLE_NAME, "id=?", args);
                                db.close();
                                mDatas.clear();
                                initData();
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        return true;
                    }
                });
            }
            else{
                holder.address_item.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
                    @Override
                    public void onSuperTextViewClick() {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", mDatas.get(position).getId());
                        bundle.putString("name", mDatas.get(position).getName());
                        bundle.putString("phone", mDatas.get(position).getPhone());
                        bundle.putString("address", mDatas.get(position).getAddress());
                        bundle.putString("address_sub", mDatas.get(position).getAddress_sub());
                        intent.putExtras(bundle);
                        setResult(1,intent);
                        finish();
                    }
                });


            }

//            holder.modify.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(AddressManagement.this,ModifyAddress.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putInt("id",mDatas.get(position).getId());
//                    bundle.putString("name",mDatas.get(position).getName());
//                    bundle.putString("phone",mDatas.get(position).getPhone());
//                    bundle.putString("address",mDatas.get(position).getAddress());
//                    bundle.putString("address_sub",mDatas.get(position).getAddress_sub());
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent,15);
//                }
//            });
//
//            holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(AddressManagement.this);
//                    builder.setMessage("是否删除地址信息？");
//                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            helper= DbManager.getInstance(AddressManagement.this);
//                            SQLiteDatabase db=helper.getWritableDatabase();
//                            String[]args={String.valueOf(mDatas.get(position).getId())};
//                            db.delete(Constant.ADDRESS_TABLE_NAME,"id=?",args);
//                            db.close();
//                            mDatas.clear();
//                            initData();
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    });
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.create().show();
//
//                }
//            });

        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder
        {
            public SuperTextView address_item;
            public int id;

            public MyViewHolder(View view)
            {
                super(view);
                address_item= (SuperTextView) view.findViewById(R.id.address_item);
            }
        }
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: 2016/12/24   新增或者修改地址。
        switch (resultCode){
            case 11:{
                mDatas.clear();
                initData();
                mAdapter.notifyDataSetChanged();
                break;
            }
            case 15:{
                mDatas.clear();
                initData();
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }



}
