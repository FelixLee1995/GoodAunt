package space.ahaha.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import static android.R.attr.name;
import static space.ahaha.project.R.id.cardView;


/**
 * Created by ROhan on 2016/11/29 0029.
 */

public class HomeFragment extends Fragment{
    @Nullable
    View lrcbj, ldsc, ldbdl, lsfby;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    public static HomeFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    void initView(){
        //lrcbj, ldsc, ldbdl, lsfby
        lrcbj = getView().findViewById(R.id.lrcbj);
        lrcbj.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),Item.class);

                Bundle bundle=new Bundle();

                bundle.putString("name", "rcbj");

                intent.putExtras(bundle);

                startActivityForResult(intent,1);
            }
        });

        ldsc = getView().findViewById(R.id.ldsc);
        ldsc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),Item.class);

                Bundle bundle=new Bundle();

                bundle.putString("name", "dsc");

                intent.putExtras(bundle);

                startActivityForResult(intent,1);
            }
        });

        ldbdl = getView().findViewById(R.id.ldbdl);
        ldbdl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),Item.class);

                Bundle bundle=new Bundle();

                bundle.putString("name", "dbdl");

                intent.putExtras(bundle);

                startActivityForResult(intent,1);
            }
        });

        lsfby = getView().findViewById(R.id.lsfby);
                lsfby.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent= new Intent(getContext(),Item.class);

                        Bundle bundle=new Bundle();

                        bundle.putString("name", "sfby");

                        intent.putExtras(bundle);

                startActivityForResult(intent,1);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
            if (1 == resultCode) {
                ((MainActivity)getActivity()).setDefaultFragment(1);
            }
        }
    }
}