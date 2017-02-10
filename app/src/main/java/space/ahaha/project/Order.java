package space.ahaha.project;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by ROhan on 2016/12/15 0015.
 */

public class Order {

    public static final int NONTAKING = 1;//未接单
    public static final int SETOUT = 8;//未接单
    public static final int CLOSED = 2;//取消订单
    public static final int PAID = 3;//已完成
    public static final int DEALING = 4;//交易中
    public static final int TAKING = 5;//已接单
    public static final int RETURNED = 6;//退货
    public static final int RATED = 7;//已评价

    private String orderId;//订单id-
    private int orderStatus;//订单状态-
    private Timestamp  orderDate;//订单生成时间-
    private Timestamp  orderCloseDate;//订单关闭时间
    private Timestamp  orderFinishDate;//订单完成时间
    private String orderUserId;//下单账户id
    private String orderAuntId;//接单阿姨id
    private String orderAddress;//下单地址√
    private Timestamp orderWorkTime;//预期工作时间√
    private Timestamp  orderStartWorkTime;//阿姨开始工作时间
    private Timestamp  orderFinishedWorkTime;//阿姨结束工作时间
    private int orderMoney;//总价格
    private int ordeStar;//订单评分
    private int ordeType;//订单类型√-
    //0 日常保洁
    //1 大扫除
    //2 沙发保养
    //3 地板打蜡
    private String orderRemarks;//订单备注

    public String getOrderRemarks() {
        return orderRemarks;
    }

    public void setOrderRemarks(String orderRemarks) {
        this.orderRemarks = orderRemarks;
    }



    public Timestamp  getorderStartWorkTime() {
        return orderStartWorkTime;
    }

    public void setorderStartWorkTime(Timestamp  orderStartWorkTime) {
        this.orderStartWorkTime = orderStartWorkTime;
    }

    public Timestamp  getorderFinishedWorkTime() {
        return orderFinishedWorkTime;
    }

    public void setorderFinishedWorkTime(Timestamp  orderFinishedWorkTime) {
        this.orderFinishedWorkTime = orderFinishedWorkTime;
    }


    public int getordeType() {
        return ordeType;
    }

    public void setordeType(int ordeType) {
        this.ordeType = ordeType;
    }




    public Timestamp getorderCloseDate() {
        return orderCloseDate;
    }

    public void setorderCloseDate(Timestamp  orderCloseDate) {
        this.orderCloseDate = orderCloseDate;
    }

    public Timestamp  getorderFinishDate() {
        return orderFinishDate;
    }

    public void setorderFinishDate(Timestamp  orderFinishDate) {
        this.orderFinishDate = orderFinishDate;
    }



    public String getorderId() {
        return orderId;
    }

    public void setorderId(String orderId) {
        this.orderId = orderId;
    }

    public int getorderStatus() {
        return orderStatus;
    }

    public void setorderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp  getorderDate() {
        return orderDate;
    }

    public void setorderDate(Timestamp  orderDate) {
        this.orderDate = orderDate;
    }

    public String getorderUserId() {
        return orderUserId;
    }

    public void setorderUserId(String orderUserId) {
        this.orderUserId = orderUserId;
    }

    public String getorderAuntId() {
        return orderAuntId;
    }

    public void setorderAuntId(String orderAuntId) {
        this.orderAuntId = orderAuntId;
    }

    public String getorderAddress() {
        return orderAddress;
    }

    public void setorderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }


    public int getorderMoney() {
        return orderMoney;
    }

    public void setorderMoney(int orderMoney) {
        this.orderMoney = orderMoney;
    }

    public Timestamp  getorderWorkTime() {
        return orderWorkTime;
    }

    public void setorderWorkTime(Timestamp  orderWorkTime) {
        this.orderWorkTime = orderWorkTime;
    }

    public int getordeStar() {
        return ordeStar;
    }

    public void setordeStar(int ordeStar) {
        this.ordeStar = ordeStar;
    }


}
