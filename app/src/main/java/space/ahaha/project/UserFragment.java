package space.ahaha.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.allen.library.SuperTextView;

import de.hdodenhof.circleimageview.CircleImageView;
import space.ahaha.project.Utils.Constant;
import space.ahaha.project.Utils.SharedPreferencesUtils;

/**
 * Created by ROhan on 2016/11/29 0029.
 */

public class UserFragment extends Fragment {
    private CircleImageView circleImageView;
    private Button status;
    private com.allen.library.SuperTextView account_management;
    private com.allen.library.SuperTextView address_management;
    private com.allen.library.SuperTextView info;
    private com.allen.library.SuperTextView feedback;
//    private com.allen.library.SuperTextView setting;
    private boolean isSignin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        status= (Button) getActivity().findViewById(R.id.status);
        account_management= (SuperTextView) getActivity().findViewById(R.id.account_management);
        address_management= (SuperTextView) getActivity().findViewById(R.id.address_management);
        feedback= (SuperTextView) getActivity().findViewById(R.id.feedback);
        info= (SuperTextView) getActivity().findViewById(R.id.info);
        circleImageView= (CircleImageView) getActivity().findViewById(R.id.profile_image);
//        setting= (SuperTextView) getActivity().findViewById(R.id.setting);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSignin) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                }
            }
        });

        account_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSignin) {
                    Intent intent = new Intent(getActivity(), AccountManagement.class);
                    startActivityForResult(intent, 10);
                }
                else{
                    Toast.makeText(getActivity(), "请登录后再试", Toast.LENGTH_SHORT).show();
                }
            }
        });

        address_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSignin) {
                    Intent intent = new Intent(getActivity(), AddressManagement.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("choice", 0);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity(), "请登录后再试", Toast.LENGTH_SHORT).show();
                }
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Feedback.class);
                startActivity(intent);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Info.class);
                startActivity(intent);
            }
        });
//        setting.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener(){
//            @Override
//            public void onSuperTextViewClick() {
//                super.onSuperTextViewClick();
//                Toast.makeText(getActivity(), "正在紧张制作中！", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        getSharePreference();
    }

    public static UserFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void getSharePreference() {

        String phonenumber = (String) SharedPreferencesUtils.getParam(getActivity(), Constant.USER_PHONE, "");
        isSignin= (boolean) SharedPreferencesUtils.getParam(getActivity(), Constant.USER_STATUS, false);
        if(isSignin){
            status.setText(phonenumber);
            String path= Environment
                    .getExternalStorageDirectory().getAbsolutePath()+"/"+phonenumber+"_avatar.png";
            Bitmap bmp= BitmapFactory.decodeFile(path);
            if(bmp!=null) {
                circleImageView.setImageBitmap(bmp);
            }
        }
        else {
            circleImageView.setImageResource(R.drawable.head_default);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==10){
            Bundle bundle=data.getExtras();
            String path=bundle.getString("path");
            Bitmap bmp= BitmapFactory.decodeFile(path);
            circleImageView.setImageBitmap(bmp);
        }
    }



}
