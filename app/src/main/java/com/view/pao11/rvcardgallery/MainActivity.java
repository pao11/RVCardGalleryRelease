package com.view.pao11.rvcardgallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.view.pao11.rvcardgallery.util.BlurBitmapUtils;
import com.view.pao11.rvcardgallery.util.ViewSwitchUtils;
import com.view.pao11.library.CardScaleHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private RecyclerView mRecyclerView;
    private ImageView mBlurView;
    private List<Integer> mList = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper = null;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        final CardAdapter adapter = new CardAdapter(mList);
        mRecyclerView.setAdapter(adapter);
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
//        mCardScaleHelper.setCurrentItemPos(2);
//        mCardScaleHelper.setSmoothScroll(true);
//        mCardScaleHelper.setSnapHelperType(CardScaleHelper.LINEAR_SNAP_HELPER);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);

        ((Button)findViewById(R.id.btn_go)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.size() == 0) {
                    for (int i = 0; i < 1; i++) {
                        mList.add(R.drawable.pic4);
                        mList.add(R.drawable.pic5);
                        mList.add(R.drawable.pic6);
                    }
                }
                mCardScaleHelper.setCurrentItemPosWithNotify(20);

                notifyBackgroundChange();
            }
        });

        initBlurBackground();
    }

    private void initBlurBackground() {
        mBlurView = (ImageView) findViewById(R.id.blurView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange();
                    System.out.println("=============" + mCardScaleHelper.getCurrentItemPos());
                }
            }
        });

        notifyBackgroundChange();
    }

    private void notifyBackgroundChange() {
        if (mList.size() > 0) {
            if (mLastPos == mCardScaleHelper.getCurrentItemPos()) return;
            mLastPos = mCardScaleHelper.getCurrentItemPos();
            final int resId = mList.get(mLastPos);
            mBlurView.removeCallbacks(mBlurRunnable);
            mBlurRunnable = new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                    ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), bitmap, 15));
                }
            };
            mBlurView.postDelayed(mBlurRunnable, 500);
        }
    }

}
