package com.view.pao11.library;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;


/**
 * Created by pao11 on 8/30/17.
 */
public class CardScaleHelper {

    public static final int PAGER_SNAP_HELPER = 0;
    public static final int LINEAR_SNAP_HELPER = 1;

    private RecyclerView mRecyclerView;
    private Context mContext;

    private float mScale = 0.9f; // 两边视图scale
    private int mPagePadding = 15; // 卡片的padding, 卡片间的距离等于2倍的mPagePadding
    private int mShowLeftCardWidth = 15;   // 左边卡片显示大小

    private int mCardWidth; // 卡片宽度
    private int mOnePageWidth; // 滑动一页的距离
    private int mCardGalleryWidth;

    private int mCurrentItemPos;
    private int mCurrentItemOffset;

    private int mSnapHelperType = PAGER_SNAP_HELPER;
    private boolean isSmoothScroll;

    //    private CardLinearSnapHelper mLinearSnapHelper = new CardLinearSnapHelper();
//    private PagerSnapHelper mPagerSnapHelper = new PagerSnapHelper();
    private SnapHelper mSnapHelper;

    public void attachToRecyclerView(final RecyclerView mRecyclerView) {
        if (this.mRecyclerView == mRecyclerView) {
            return; // nothing to do
        }
        this.mRecyclerView = mRecyclerView;
        mContext = mRecyclerView.getContext();
        if (mSnapHelperType == PAGER_SNAP_HELPER) {
            mSnapHelper = new CardPagerSnapHelper();
        } else {
            mSnapHelper = new CardLinearSnapHelper();
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mCurrentItemPos = mRecyclerView.getChildAdapterPosition(mSnapHelper.findSnapView(mRecyclerView.getLayoutManager()));
                    onScrolledChangedCallback();
                }
                if (mSnapHelperType == LINEAR_SNAP_HELPER) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                        ((CardLinearSnapHelper) mSnapHelper).mNoNeedToScroll = mCurrentItemOffset == 0 || mCurrentItemOffset == getDestItemOffset(mRecyclerView.getAdapter().getItemCount() - 1);
                        ((CardLinearSnapHelper) mSnapHelper).mNoNeedToScroll = mCurrentItemPos == 0 || mCurrentItemPos == mRecyclerView.getAdapter().getItemCount() - 1;
                    } else {
                        ((CardLinearSnapHelper) mSnapHelper).mNoNeedToScroll = false;
                    }
                } else {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                        ((CardPagerSnapHelper) mSnapHelper).mNoNeedToScroll = mCurrentItemOffset == 0 || mCurrentItemOffset == getDestItemOffset(mRecyclerView.getAdapter().getItemCount() - 1);
                        ((CardPagerSnapHelper) mSnapHelper).mNoNeedToScroll = mCurrentItemPos == 0 || mCurrentItemPos == mRecyclerView.getAdapter().getItemCount() - 1;
                    } else {
                        ((CardPagerSnapHelper) mSnapHelper).mNoNeedToScroll = false;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // dx>0则表示右滑, dx<0表示左滑, dy<0表示上滑, dy>0表示下滑
                mCurrentItemOffset += dx;
                computeCurrentItemPos();
                onScrolledChangedCallback();
            }

        });

        initWidth();
        mSnapHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 初始化卡片宽度
     */
    private void initWidth() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mCardGalleryWidth = mRecyclerView.getWidth();
                mCardWidth = mCardGalleryWidth - ScreenUtil.dip2px(mContext, 2 * (mPagePadding + mShowLeftCardWidth));
                mOnePageWidth = mCardWidth;
                int itemCount = mRecyclerView.getAdapter().getItemCount();
                if (itemCount < mCurrentItemPos) {
                    mCurrentItemPos = itemCount > 0 ? itemCount - 1 : 0;
                }
                if (isSmoothScroll) {
                    mRecyclerView.smoothScrollToPosition(mCurrentItemPos);
                } else if (mCurrentItemPos > 0) {
                    mRecyclerView.scrollBy(mOnePageWidth * mCurrentItemPos, 0);
                }
                onScrolledChangedCallback();
            }
        });
    }

    /**
     * @param currentItemPos 该值从1开始
     */
    public void setCurrentItemPos(int currentItemPos) {
        this.mCurrentItemPos = currentItemPos > 0 ? currentItemPos - 1 : 0;
    }

    /**
     * 设置默认显示图片并初始化数据
     * 此方法内部会调用RecyclerView.getAdapter().notifyDataSetChanged();
     *
     * @param currentItemPos 该值从1开始
     */
    public void setCurrentItemPosWithNotify(int currentItemPos) {
        final int lastPos = mCurrentItemPos;
        this.mCurrentItemPos = currentItemPos > 0 ? currentItemPos - 1 : 0;
        if (mRecyclerView != null) {
            int itemCount = mRecyclerView.getAdapter().getItemCount();
            if (itemCount < mCurrentItemPos) {
                mCurrentItemPos = itemCount > 0 ? itemCount - 1 : 0;
            }
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.getAdapter().notifyDataSetChanged();

                    if (isSmoothScroll) {
                        mRecyclerView.smoothScrollToPosition(mCurrentItemPos);
                    } else {
                        mRecyclerView.scrollBy(mOnePageWidth * (mCurrentItemPos - lastPos), 0);
                    }

                    onScrolledChangedCallback();
                }
            });
        }
    }

    /**
     * @return 返回值从0开始
     */
    public int getCurrentItemPos() {
        return mCurrentItemPos;
    }

    private int getDestItemOffset(int destPos) {
        return mOnePageWidth * destPos;
    }

    /**
     * 计算mCurrentItemOffset
     */
    private void computeCurrentItemPos() {
        if (mOnePageWidth <= 0) return;
        boolean pageChanged = false;
        // 滑动超过一页说明已翻页
        // 乐视手机回翻图片时mCurrentItemOffset-mCurrentItemPos * mOnePageWidth比mOnePageWidth少1.
        if (Math.abs(mCurrentItemOffset - mCurrentItemPos * mOnePageWidth) >= mOnePageWidth - 5) {
            pageChanged = true;
        }
        if (pageChanged) {
//            int tempPos = mCurrentItemPos;
            mCurrentItemPos = (mCurrentItemOffset + 5) / mOnePageWidth;
        }
    }

    /**
     * RecyclerView位移事件监听, view大小随位移事件变化
     */
    private void onScrolledChangedCallback() {
        int offset = mCurrentItemOffset - mCurrentItemPos * mOnePageWidth;
        float percent = (float) Math.max(Math.abs(offset) * 1.0 / mOnePageWidth, 0.0001);

        View leftView = null;
        View currentView;
        View rightView = null;
        if (mCurrentItemPos > 0) {
            leftView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos - 1);
        }
        currentView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos);
        if (mCurrentItemPos < mRecyclerView.getAdapter().getItemCount() - 1) {
            rightView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos + 1);
        }

        if (leftView != null) {
            // y = (1 - mScale)x + mScale
            leftView.setScaleY((1 - mScale) * percent + mScale);
        }
        if (currentView != null) {
            // y = (mScale - 1)x + 1
            currentView.setScaleY((mScale - 1) * percent + 1);
        }
        if (rightView != null) {
            // y = (1 - mScale)x + mScale
            rightView.setScaleY((1 - mScale) * percent + mScale);
        }
    }

    public void setScale(float scale) {
        mScale = scale;
    }

    public void setPagePadding(int pagePadding) {
        mPagePadding = pagePadding;
    }

    public void setShowLeftCardWidth(int showLeftCardWidth) {
        mShowLeftCardWidth = showLeftCardWidth;
    }

    /**
     * @param mSnapHelperType
     */
    public void setSnapHelperType(int mSnapHelperType) {
        this.mSnapHelperType = mSnapHelperType;
    }

    /**
     * @param smoothScroll
     */
    public void setSmoothScroll(boolean smoothScroll) {
        isSmoothScroll = smoothScroll;
    }
}
