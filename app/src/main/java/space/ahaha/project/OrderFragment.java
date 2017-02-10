package space.ahaha.project;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.allen.library.CommonTextView;
import com.allen.library.SuperTextView;
import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import space.ahaha.project.Utils.Constant;
import space.ahaha.project.Utils.SharedPreferencesUtils;

import static android.R.attr.data;
import static android.R.attr.order;


/**
 * Created by ROhan on 2016/11/29 0029.
 */

public class OrderFragment extends Fragment {

    PullRefreshLayout layout;
    private List<Order> mOrderList;

    private String network_error = "Bad network";
    private boolean cancel = true;       //当cancel置为false时，在耗时注册结束之前，不允许退出当前activity
    private GetOrderTask mAuthTask = null;
    private Handler mHandler;
    RecyclerView recyclerView;
    MyAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mOrderList = new ArrayList<>();
        getOrderData();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter(getContext(), mOrderList);
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                OrderDetailDialog dialog = new OrderDetailDialog();
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String str = gson.toJson(order);
                bundle.putString("orderjson", str);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager());
            }
        });
        adapter.setOnItemLongClickListener(new MyAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final String orderId, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("是否删除订单信息？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOrder(orderId);
                        mOrderList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, mOrderList.size());
                        dialog.dismiss();
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
        });
        recyclerView.setAdapter(adapter);

        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        adapter.listorders = mOrderList;
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        recyclerView.invalidate();//
                        break;
                    default:
                        break;
                }
            }
        };
        layout = (PullRefreshLayout) getActivity().findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                    }

                }, 2000);
                getOrderData();
                Message msg = Message.obtain();
                //将Message对象的arg1参数的值设置为i
                msg.arg1 = 1;
                mHandler.sendMessage(msg);
            }
        });
    }

    void deleteOrder(String orderId) {
        //删除Order数据
        Toast.makeText(getContext(), "删除订单功能正在加工中！！", Toast.LENGTH_LONG).show();
    }

    void getOrderData() {
        //获取Order数据
        cancel=false;
        mAuthTask = new GetOrderTask();
        mAuthTask.execute((Void) null);

    }

    public class GetOrderTask extends AsyncTask<Void, Void, String> {

        private final String url = "http://115.159.24.100:8080/GoodAunt/getOrderList?";

        GetOrderTask() {
        }

        @Override
        protected String doInBackground(Void... params) {

            // Simulate network access.
            StringBuffer stringBuffer = new StringBuffer();
                String str;
                try {
                URL httpURL = null;
                httpURL = new URL(url);//创建URL
                HttpURLConnection connection = null;
                connection = (HttpURLConnection) httpURL.openConnection();//创建http连接
                connection.setRequestMethod("POST");//设置模式
                connection.setReadTimeout(5000);
                OutputStream out = null;
                out = connection.getOutputStream();

                String phonenumber = (String) SharedPreferencesUtils.getParam(getActivity(), Constant.USER_PHONE, "");

                String content ="userId=" + phonenumber;
                out.write(content.getBytes());
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                //JSONObject jsonObj = new JSONObject(stringBuffer.toString());

                    com.alibaba.fastjson.JSONArray  js = JSON.parseArray(stringBuffer.toString());
                    /*
                    for(int i=0;i<js.size();i++){
                        com.alibaba.fastjson.JSONObject jobj =  (com.alibaba.fastjson.JSONObject) js.get(i);
                        Order order = new Order();
                        order.setorderUserId(jobj.getString("orderUserId"));
                        order.setOrderRemarks(jobj.getString("orderRemarks"));
                        order.setorderAddress(jobj.getString("orderAddress"));
                        order.setorderCloseDate(Timestamp.valueOf(jobj.getString("orderCloseDate")));
                        order.setordeType(jobj.getInteger("orderType"));
                        order.setordeStar(jobj.getInteger("orderStar"));
                        order.setorderWorkTime(Timestamp.valueOf(jobj.getString("orderWorkTime")));
                        order.setorderStatus(jobj.getInteger("orderStatus"));
                        order.setorderStartWorkTime(Timestamp.valueOf(jobj.getString("orderStartWorkTime")));
                        order.setorderMoney(jobj.getInteger("orderMoney"));
                        order.setorderId(jobj.getString("orderId"));
                        order.setorderFinishedWorkTime(Timestamp.valueOf(jobj.getString("orderFinishedWorkTime")));
                        order.setorderAuntId(jobj.getString("orderAuntId"));
                        order.setorderDate(Timestamp.valueOf(jobj.getString("orderDate")));
                        order.setorderFinishDate(Timestamp.valueOf(jobj.getString("orderFinishDate")));
                        mOrderList.add(order);
                    }
**/
                Gson gson = new Gson();
                mOrderList = gson.fromJson(stringBuffer.toString(), new TypeToken<List<Order>>(){}.getType());
                    for(int i=0;i<js.size();i++) {
                        com.alibaba.fastjson.JSONObject jobj = (com.alibaba.fastjson.JSONObject) js.get(i);
                        mOrderList.get(i).setordeType(jobj.getInteger("orderType"));
                        mOrderList.get(i).setordeStar(jobj.getInteger("orderStar"));
                    }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "succeed";
        }

        @Override
        protected void onPostExecute(final String code) {
            mAuthTask = null;
            if (code.equals("succeed")) {
                Message msg = Message.obtain();
                //将Message对象的arg1参数的值设置为i
                msg.arg1 = 1;
                mHandler.sendMessage(msg);
                return;
             }
        }
    }


    public static OrderFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }
     static class MyAdapter extends RecyclerView.Adapter<ViewHolder>{
        List<Order> listorders;
        private Context mContext;
        private OnItemClickListener mOnItemClickListener;
         private OnItemLongClickListener mOnItemLongClickListener;
        public interface OnItemClickListener{
            void onItemClick(Order order);
        }
         public interface OnItemLongClickListener{
             void onItemLongClick(View view,String orderId, int positon);
         }
        public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
            this.mOnItemClickListener = mOnItemClickListener;
        }
         public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
             this.mOnItemLongClickListener = mOnItemLongClickListener;
         }
        public MyAdapter(Context context, List<Order> orders) {
            mContext = context;
            listorders = orders;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(View.inflate(viewGroup.getContext(), R.layout.order_item, null));
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            viewHolder.order_title.setLeftTextString(listorders.get(i).getorderId());
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            viewHolder.order_title.setCenterTextString(sdf.format(listorders.get(i).getorderDate()));
            String type="";
            switch (listorders.get(i).getorderStatus()){
                case 1:
                    type = "未接单";
                    break;
                case 2:
                    type = "取消订单";
                    break;
                case 3:
                    type = "已完成";
                    break;
                case 4:
                    type = "交易中";
                    break;
                case 5:
                    type = "已接单";
                    break;
                case 6:
                    type = "退货";
                    break;
                case 7:
                    type = "已评价";
                    break;

            }
            viewHolder.order_title.setRightTextString(type);
            switch(listorders.get(i).getordeType()){
                case 0://rcbj
                    viewHolder.order_detail.setLeftIcon(mContext.getResources().getDrawable(R.drawable.ic_action_database));
                    viewHolder.order_detail.setLeftString(mContext.getString(R.string.rcbj));
                    break;
                case 3://"dbdl":
                    viewHolder.order_detail.setLeftIcon(mContext.getResources().getDrawable(R.drawable.ic_action_database));
                    viewHolder.order_detail.setLeftString(mContext.getString(R.string.dbdl));
                    break;
                case 2://"sfby":
                    viewHolder.order_detail.setLeftIcon(mContext.getResources().getDrawable(R.drawable.ic_action_database));
                    viewHolder.order_detail.setLeftString(mContext.getString(R.string.sfby));
                    break;
                case 1://"dsc":
                    viewHolder.order_detail.setLeftIcon(mContext.getResources().getDrawable(R.drawable.ic_action_database));
                    viewHolder.order_detail.setLeftString(mContext.getString(R.string.dsc));
                    break;
            }

            if(mOnItemClickListener != null){
                //为ItemView设置监听器
                viewHolder.order_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = viewHolder.getLayoutPosition();

                        mOnItemClickListener.onItemClick(listorders.get(position)); // 2
                    }
                });
            }
            if(mOnItemLongClickListener != null){
                viewHolder.order_detail.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = viewHolder.getLayoutPosition();
                        String id =viewHolder.order_title.getLeftTextString().toString();
                        mOnItemLongClickListener.onItemLongClick(viewHolder.itemView,id, position);
                        //返回true 表示消耗了事件 事件不会继续传递
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return listorders.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public SuperTextView order_detail;
        public CommonTextView order_title;

        public ViewHolder(View itemView) {
            super(itemView);
            order_title = (CommonTextView) itemView.findViewById(R.id.order_title);
            order_detail = (SuperTextView) itemView.findViewById(R.id.order_detail);
        }
    }

}