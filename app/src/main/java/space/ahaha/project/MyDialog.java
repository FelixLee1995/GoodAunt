package space.ahaha.project;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;

/**
 * Created by lf729 on 2017/1/9.
 */

public class MyDialog extends Dialog implements android.view.View.OnClickListener{

    private SuperTextView confirm;
    Context context;
    View localView;
    TextView textView;
    private RelativeLayout clearallpan;
    String str;

    protected MyDialog(Context context, String str) {
        super(context);
        this.context = context;
        this.str=str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 这句代码换掉dialog默认背景，否则dialog的边缘发虚透明而且很宽
        // 总之达不到想要的效果
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        localView = View.inflate(context,R.layout.animclearpan,null);
        localView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_bottom_to_top));
        textView= (TextView) findViewById(R.id.agreement_content);
//        textView.setText(R.string.agreement_content);
        setContentView(localView);
        // 这句话起全屏的作用
        getWindow().setLayout(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);

        initView();
        initListener();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.dismiss();
        return super.onTouchEvent(event);
    }

    private void initListener() {
        confirm.setOnClickListener(this);
    }

    private void initView() {
        confirm = (SuperTextView) findViewById(R.id.confirm);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                this.dismiss();
                break;
        }
    }
}