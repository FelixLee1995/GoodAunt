package space.ahaha.project;

import android.support.v4.content.ContextCompat;
import android.view.View;


import com.allen.library.CommonTextView;
import com.allen.library.SuperTextView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;


import me.shaohui.bottomdialog.BaseBottomDialog;

/**
 * Created by ROhan on 2016/12/26 0026.
 */

public class OrderDetailDialog extends BaseBottomDialog {

    private SuperTextView orderType;

    private SuperTextView orderId;
    private SuperTextView orderStatus;
    private SuperTextView orderTime;
    private CommonTextView orderAddress;
    private SuperTextView orderWorkTime;
    private CommonTextView orderAunt;
    private SuperTextView orderStartTime;
    private SuperTextView orderFinishedTime;
    private SuperTextView orderMoney;
    private CommonTextView orderStar;
    private Order order;
    @Override
    public int getLayoutRes() {
        return R.layout.order_detail;
    }

    public void bindView(View v) {
        order = new Order();
        String str = getArguments().getString("orderjson");

        Gson gson = new Gson();
        order = gson.fromJson(str, Order.class);
        // getOrderData(strorderId);

        orderType =(SuperTextView) v.findViewById(R.id.order_detail);

        orderId =(SuperTextView) v.findViewById(R.id.order_id);
        orderStatus =(SuperTextView) v.findViewById(R.id.order_status);
        orderTime =(SuperTextView) v.findViewById(R.id.order_time);
        orderAddress =(CommonTextView) v.findViewById(R.id.order_address);
        orderWorkTime =(SuperTextView) v.findViewById(R.id.order_time);
        orderStartTime = (SuperTextView) v.findViewById(R.id.order_start_time);
        orderFinishedTime =(SuperTextView) v.findViewById(R.id.order_finished_time);
        orderAunt =(CommonTextView) v.findViewById(R.id.order_aunt);
        orderMoney =(SuperTextView) v.findViewById(R.id.order_money);
        orderStar =(CommonTextView) v.findViewById(R.id.order_star);

        switch (order.getordeType()){
            case 0:
                orderType.setCenterString(getString(R.string.rcbj));
                break;
            case 1:
                orderType.setCenterString(getString(R.string.dsc));
                break;
            case 2:
                orderType.setCenterString(getString(R.string.sfby));
                break;

            case 3:
                orderType.setCenterString(getString(R.string.dbdl));
                break;
        }
        orderId.setRightString(order.getorderId());
        orderId.setRightTVColor(getResources().getColor(R.color.colorAccent));
        orderType.setLeftIcon(getResources().getDrawable(R.drawable.ic_action_database));
        orderType.setRightIcon(getResources().getDrawable(R.drawable.ic_action_cancel));
        orderType.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onSuperTextViewClick() {
                dismiss();
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderTime.setRightString(sdf.format(order.getorderDate()));
        //orderStatus.setRightString(order.getorderStatus());
        orderAddress.setRightTextString(order.getorderAddress());
        if(order.getorderWorkTime() != null)
            orderWorkTime.setRightString(sdf.format(order.getorderWorkTime()));
        orderAunt.setRightTextString(order.getorderAuntId());
        if(order.getorderStartWorkTime() != null)
            orderStartTime.setRightString(sdf.format(order.getorderStartWorkTime()));
        if(order.getorderFinishedWorkTime() != null)
            orderFinishedTime.setRightString(sdf.format(order.getorderFinishedWorkTime()));
        orderMoney.setRightString(Integer.toString(order.getorderMoney()) + "元");
        orderStar.setRightTextString(Integer.toString(order.getordeStar()));
    }
    void getOrderData(String id){
        //获得网络数据

    }

    @Override
    public float getDimAmount() {
        return 0.35f;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

