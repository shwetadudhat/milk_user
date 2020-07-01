package com.vaapglkns;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.view.SimpleListDividerDecorator;
import com.vaapglkns.adapter.DummyAdapter;
import com.vaapglkns.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DummyActivity extends BaseActivity {
    //    @BindView(R.id.btnSendRequest)
//    Button btnSendRequest;
//
    @BindView(R.id.recyclerViewDummy)
    RecyclerView recyclerViewDummy;
    DummyAdapter dummyAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        ButterKnife.bind(this);

        initDrawer();
        init();
    }

    private void init() {
        setTitleText("Adapter");

        recyclerViewDummy.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDummy.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));
        recyclerViewDummy.addItemDecoration(new SimpleListDividerDecorator(getResources().getDrawable(R.drawable.list_divider), true));

        dummyAdapter = new DummyAdapter(getActivity());
        recyclerViewDummy.setAdapter(dummyAdapter);
    }
}
