package space.ahaha.project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONException;
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
import java.util.Date;

import space.ahaha.project.Utils.Constant;
import space.ahaha.project.Utils.SharedPreferencesUtils;



public class SetOrderActivity extends AppCompatActivity {
    private Order order;
    private MaterialSpinner spinner;
    private SuperTextView order_start_time;
    TimeSelector set_order_start_time;
    private SimpleDateFormat sf;
    private SuperTextView order_address;
    private SuperTextView check_order;
    private EditText order_remarks;
    String name;

    private String network_error="Bad network";
    private boolean cancel=true;       //当cancel置为false时，在耗时注册结束之前，不允许退出当前activity
    private ProgressBar progressBar;
    private SendOrderTask mAuthTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_set_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar= (ProgressBar) findViewById(R.id.loading_process_dialog_progressBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView)findViewById(R.id.itemtitle);
        title.setText("确认订单");
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值

        name = bundle.getString("type");



        initData();
        initView();
    }
    void initData(){
        order = new Order();
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        order_start_time = (SuperTextView) findViewById(R.id.order_start_time);
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        order_address = (SuperTextView) findViewById(R.id.order_address);
        check_order = (SuperTextView) findViewById(R.id.setOrder);
        order_remarks = (EditText) findViewById(R.id.edit_text);
        String phonenumber = (String) SharedPreferencesUtils.getParam(SetOrderActivity.this, Constant.USER_PHONE, "");
        order.setorderUserId(phonenumber);
    }
    void initView(){

        spinner.setItems("日常保洁", "大扫除", "沙发保养", "地板打蜡");
        switch(name)
        {
            case "rcbj":
                spinner.setSelectedIndex(0);
                order.setordeType(0);
                break;
            case "dbdl":
                spinner.setSelectedIndex(3);
                order.setordeType(3);
                break;
            case "sfby":
                spinner.setSelectedIndex(2);
                order.setordeType(2);
                break;
            case "dsc":
                spinner.setSelectedIndex(1);
                order.setordeType(1);
                break;
        }
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                order.setordeType(position);
            }
        });

        order_start_time.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onSuperTextViewClick() {
                long times = System.currentTimeMillis();
                System.out.println(times);
                Date date = new Date(times);
                String tim = sf.format(date);
                super.onSuperTextViewClick();
                set_order_start_time = new TimeSelector(SetOrderActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
                        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Timestamp date = null;
                        try {
                            date = Timestamp.valueOf(time + ":00");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        order.setorderWorkTime(date);
                        order_start_time.setRightString(time);
                    }
                }, tim, "2018-12-31 00:00");
                set_order_start_time.show();
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

        order_address.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onSuperTextViewClick() {
                /*
                EditTextDialog dialog = new EditTextDialog();
                dialog.show(getSupportFragmentManager());
                */
                Intent intent= new Intent(SetOrderActivity.this, AddressManagement.class);

                Bundle bundle=new Bundle();
                bundle.putInt("choice", 1);
                intent.putExtras(bundle);

                startActivityForResult(intent, 1);
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


        check_order.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onSuperTextViewClick() {
                order.setOrderRemarks(order_remarks.getText().toString());
                if(checkOrder()){
                    if(sendOrderData()){
                        setResult(1);
                        Toast.makeText(getApplicationContext(),"创建订单1", Toast.LENGTH_LONG).show();
                    }
                    else{
                        //setResult(0);
                        Toast.makeText(getApplicationContext(),"创建订单失败，请重试/(ㄒoㄒ)/~~", Toast.LENGTH_LONG).show();
                    }
                }
                else{

                    Toast.makeText(getApplicationContext(),"还有信息没有确定呢！！！", Toast.LENGTH_LONG).show();
                }
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
    boolean checkOrder(){
        if(order.getorderWorkTime() == null || order.getorderAddress() == null){
            return false;
        }
        return true;
    }
    boolean sendOrderData(){
        //发送订单数据
        if (mAuthTask != null) {
            return false;
        }
        cancel=false;
        showProgress(true);
        mAuthTask = new SendOrderTask(order);
        mAuthTask.execute((Void) null);

        return true;
    }
    public class SendOrderTask extends AsyncTask<Void, Void, String> {

        Order preOrder;
        private final String url = "http://115.159.24.100:8080/GoodAunt/addOrder?";

        SendOrderTask(Order order) {
            this.preOrder = order;
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
                connection.setReadTimeout(5000);//
                OutputStream out = null;
                out = connection.getOutputStream();

                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String worktime = sdf.format(order.getorderWorkTime());
                String content = "orderType=" + order.getordeType() + "&orderUserId=" + order.getorderUserId() + "&orderAddress=" + order.getorderAddress() + "&orderWorkTime=" + worktime +"&orderRemarks=" + order.getOrderRemarks();//设置内容
                out.write(content.getBytes());
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }

                JSONObject jsonObj = new JSONObject(stringBuffer.toString());

                if(jsonObj.getString("orderId") != null){
                    return "succeed";
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
            if (code.equals("succeed")) {
                finish();
            }
            else if(code.equals(network_error)){
                Toast.makeText(SetOrderActivity.this, "请检查网络状况！", Toast.LENGTH_SHORT).show();
                showProgress(false);
                cancel=true;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(cancel==false)return false;
            else {
                Intent intent = new Intent(SetOrderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    * 用于在点击下单之后显示进度并disable其他控件
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }


    void set_Order_address(String address){
        order.setorderAddress(address);
        Toast.makeText(getApplicationContext(), "工作地址是："+address, Toast.LENGTH_LONG).show();
        order_address.setRightString("√");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == 1){
                Bundle bundle = data.getExtras();
                String address = bundle.getString("address");
                address = address.replaceAll("\\s*", "");
                String address_sub = bundle.getString("address_sub");
                address_sub = address_sub.replaceAll("\\s*", "");
                set_Order_address(address + address_sub);
            }
        }
    }
}
