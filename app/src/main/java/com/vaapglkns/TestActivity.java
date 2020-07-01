package com.vaapglkns;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.common.view.SimpleListDividerDecorator;
import com.vaapglkns.adapter.CommentsAdapter;
import com.vaapglkns.objects.Test;
import com.vaapglkns.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {


    //    @BindView(R.id.btnSendRequest)
//    Button btnSendRequest;
//
    @BindView(R.id.rvComments)
    RecyclerView rvComments;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    CommentsAdapter commentsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

//        initDrawer();
        init();
    }

    private void init() {
//        setTitleText("Home");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvComments.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));
        rvComments.addItemDecoration(new SimpleListDividerDecorator(getResources().getDrawable(R.drawable.list_divider), true));

        commentsAdapter = new CommentsAdapter(getActivity());
        rvComments.setAdapter(commentsAdapter);

        List<Test.Array> data = new ArrayList<>();

//        for (int i = 1; i < 7; i++) {

        Test.Array array1 = new Test.Array();

        array1.vid = "1";
        array1.commentId = "1";
        array1.userId = "1";
        array1.content = "hi 1";
        data.add(array1);

        Test.Array array2 = new Test.Array();

        array2.vid = "1";
        array2.commentId = "2";
        array2.userId = "2";
        array2.content = "hi 1";
        array2.parent = "1";
        data.add(array2);

        Test.Array array3 = new Test.Array();

        array3.vid = "1";
        array3.commentId = "3";
        array3.userId = "3";
        array3.content = "hi 2";
        array3.parent = "2";
        data.add(array3);

        Test.Array array4 = new Test.Array();

        array4.vid = "1";
        array4.commentId = "4";
        array4.userId = "1";
        array4.content = "hi 1";
        array4.parent = "1";
        array4.parent2 = "3";
        data.add(array4);

        Test.Array array5 = new Test.Array();

        array5.vid = "1";
        array5.commentId = "5";
        array5.userId = "2";
        array5.content = "hi 1";
        array5.parent = "1";
        data.add(array5);

        Test.Array array6 = new Test.Array();

        array6.vid = "1";
        array6.commentId = "7";
        array6.userId = "2";
        array6.content = "hi hii";
        data.add(array6);

        Test.Array array7 = new Test.Array();

        array7.vid = "1";
        array7.commentId = "6";
        array7.userId = "1";
        array7.content = "hi dd 1";
        array7.parent = "1";
        data.add(array7);


//        }

        commentsAdapter.addAll(data);


        commentsAdapter.setEventlistener(new CommentsAdapter.Eventlistener() {
            @Override
            public void onItemviewClick(int position) {

            }

            @Override
            public void onFirstCommentviewClick(LinearLayout linearLayout) {
                if (linearLayout.getVisibility() == View.VISIBLE) {
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSecondCommentviewClick(LinearLayout linearLayout) {
                if (linearLayout.getVisibility() == View.VISIBLE) {
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onThirdCommentviewClick(LinearLayout linearLayout) {
                if (linearLayout.getVisibility() == View.VISIBLE) {
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
