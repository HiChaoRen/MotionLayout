package com.example.motionlayout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 分隔线
 */
public class StickDecoration extends RecyclerView.ItemDecoration {

    /**
     * 悬浮类型为文字
     */
    private final static int STICK_TYPE_TEXT = 1;
    /**
     * 悬浮类型为View
     */
    private final static int STICK_TYPE_VIEW = 2;

    /**
     * 分隔线 颜色、高度、左Margin、右Margin
     */
    private Paint mDividerPaint;
    private int mDividerColor = Color.parseColor("#dbdbdb");
    private int mDividerHeight = 1;
    private int mDividerLeftMargin = 0;
    private int mDividerRightMargin = 0;
    /**
     * 悬浮文案 颜色、字体颜色、字体大小、左Margin
     */
    private Paint mStickPaint;
    private int mStickColor = Color.parseColor("#dbdbdb");
    private int mStickTextColor = Color.parseColor("#333333");
    private float mStickTextSize = 40f;
    private int mStickTextLeftMargin = 100;
    private Paint mStickTextPaint;
    /**
     * 悬浮文案 文案区域框
     */
    private Rect mStickTextBounds;
    /**
     * 悬浮文案 高度
     */
    private int mStickHeight = 100;
    /**
     * RecyclerView 首部偏移量，前mOffsetStart个item不展示分隔线
     */
    private int mOffsetStart = 1;
    /**
     * RecyclerView 尾部偏移量，后mOffsetEnd个item不展示分隔线
     */
    private int mOffsetEnd = 1;
    /**
     * 悬浮文案缓存
     */
    private SparseArray<String> mStickText = new SparseArray<>();
    /**
     * 悬浮View缓存
     */
    private SparseArray<WeakReference<View>> mStickView = new SparseArray<>();

    public StickDecoration() {
        this.mDividerPaint = new Paint();
        //分隔线画笔
        mDividerPaint.setColor(mDividerColor);
        //抗锯齿
        mDividerPaint.setAntiAlias(true);
        //抗抖动
        mDividerPaint.setDither(true);

        //悬浮分隔
        mStickPaint = new Paint();
        mStickPaint.setAntiAlias(true);
        mStickPaint.setDither(true);
        mStickPaint.setColor(mStickColor);

        //悬浮分隔文字
        mStickTextPaint = new TextPaint();
        mStickTextPaint.setAntiAlias(true);
        mStickTextPaint.setDither(true);
        //文字加粗
        mStickTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mStickTextPaint.setColor(mStickTextColor);
        mStickTextPaint.setTextSize(mStickTextSize);
        //悬浮分隔区域
        mStickTextBounds = new Rect();
    }

    /**
     * 用以设置ItemView的内嵌偏移长度（inset）
     *
     * @param outRect Rect
     * @param view    View
     * @param parent  RecyclerView
     * @param state   RecyclerView.State
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //获取子View在RecyclerView中位置
        int positionInRecycler = parent.getChildAdapterPosition(view);
        //判断当前位置是否需要 分隔View 或 分隔线 或 悬浮分隔
        View pinnedView = getStickView(positionInRecycler, parent);
        if (pinnedView != null) {
            pinnedView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            outRect.top = pinnedView.getMeasuredHeight();
        } else if (isHasStickDivider(parent, positionInRecycler)) {
            outRect.top = mStickHeight;
        } else if (isHasDivider(parent, positionInRecycler)) {
            outRect.top = mDividerHeight;
        }
    }

    /**
     * 在子视图上设置绘制范围，并绘制内容
     * 先于ItemView绘制, 所以绘制图层在ItemView以下，所以如果绘制区域与ItemView区域相重叠，会被遮挡
     *
     * @param c      Canvas
     * @param parent RecyclerView
     * @param state  RecyclerView.State
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //不需绘制分隔线
        if (mDividerHeight < 0) {
            return;
        }
        //获取RecyclerView的Child view的个数
        int childCount = parent.getChildCount();
        //如果子ItemView数量小于偏移数量,则返回
        if (parent.getChildCount() <= mOffsetStart + mOffsetEnd) {
            return;
        }
        //遍历每个Item，分别获取它们的位置信息，然后再绘制对应的分割线
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            if (isHasDivider(parent, childView)) {
                //绘制Divider宽度与ItemView宽度一致
                drawDivider(c, childView.getLeft() + mDividerLeftMargin,
                        childView.getTop() - mDividerHeight,
                        childView.getRight() - mDividerRightMargin,
                        childView.getTop());
            }
        }
    }

    /**
     * 同样是绘制内容，但与onDraw() 的区别是：绘制在图层的最上层，会覆盖底层的视图
     *
     * @param c      Canvas
     * @param parent RecyclerView
     * @param state  RecyclerView.State
     */
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //如果子ItemView数量小于偏移数量,则返回
        if (parent.getChildCount() <= mOffsetStart + mOffsetEnd) {
            return;
        }
        //当前页面中的悬浮分隔的计数器
        int stickIndex = 0;
        //parent.getChildCount为当前页面中可见的Child view的个数
        for (int i = 0; i < parent.getChildCount(); i++) {
            //获取页面中View在RecyclerView中的位置
            final View childView = parent.getChildAt(i);
            int positionInRecycler = parent.getChildAdapterPosition(childView);

            //悬浮分隔视图：若为空，则不展示
            View stickView = getStickView(positionInRecycler, parent);
            if (stickView != null) {
                if (childView.getTop() <= stickView.getMeasuredHeight()) {
                    //若有悬浮分隔的childView顶部 <= 悬浮分隔的高度, 则悬浮顶部
                    drawStickView(c, 0, 0, parent.getRight(), stickView.getMeasuredHeight(), stickView);
                } else {
                    //若有悬浮分隔的childView顶部 > 悬浮分隔的高度, 则绘制该View的悬浮分隔线
                    drawStickView(c, 0, childView.getTop() - stickView.getMeasuredHeight(), parent.getRight(), childView.getTop(), stickView);
                    //如果是该页面的第一个悬浮分割线，则寻找是否有上一个悬浮分隔
                    drawOverLastStick(c, parent, childView, positionInRecycler, stickIndex, stickView.getMeasuredHeight());
                }
                stickIndex++;
                continue;
            }
            //悬浮分隔文案：若为空，则不展示
            String stickText = getStickText(positionInRecycler, parent);
            if (!TextUtils.isEmpty(stickText)) {
                if (childView.getTop() <= mStickHeight) {
                    //若有悬浮分隔的childView顶部 <= 悬浮分隔的高度, 则悬浮顶部
                    drawStickText(c, 0, 0, parent.getRight(), mStickHeight, stickText);
                } else {
                    //若有悬浮分隔的childView顶部 > 悬浮分隔的高度, 则绘制该View的悬浮分隔线
                    drawStickText(c, 0, childView.getTop() - mStickHeight, parent.getRight(), childView.getTop(), stickText);
                    //如果是该页面的第一个悬浮分割线，则寻找是否有上一个悬浮分隔
                    drawOverLastStick(c, parent, childView, positionInRecycler, stickIndex, mStickHeight);
                }
                stickIndex++;
            }
        }
        //若当前页面中没有ChildView拥有悬浮分隔线，则寻找上一个悬浮分隔线，并悬浮顶部
        if (stickIndex == 0) {
            int positionInRecycler = parent.getChildAdapterPosition(parent.getChildAt(0));
            //上一个悬浮分隔的类型是视图还是文案
            int floatingStickType = getLastStickType(positionInRecycler, parent);
            if (floatingStickType == STICK_TYPE_VIEW) {
                //上一个悬浮视图
                View lastPinnedView = getLastStickView(positionInRecycler, parent);
                if (lastPinnedView != null) {
                    drawStickView(c, 0, 0, parent.getRight(), lastPinnedView.getMeasuredHeight(), lastPinnedView);
                    return;
                }
            } else if (floatingStickType == STICK_TYPE_TEXT) {
                //上一个悬浮文案
                String lastStickText = getLastStickText(positionInRecycler, parent);
                if (!TextUtils.isEmpty(lastStickText)) {
                    drawStickText(c, 0, 0, parent.getRight(), mStickHeight, lastStickText);
                }
            }
        }
    }

    private void drawOverLastStick(Canvas c, RecyclerView parent, View childView, int positionInRecycler, int stickIndex, int currentStickHeight) {
        //如果是该页面的第一个悬浮分割线，则寻找是否有上一个悬浮分隔，若有，则上一个悬浮分隔正在顶部悬浮
        if (stickIndex == 0) {
            //上一个悬浮分隔的类型是视图还是文案
            int floatingStickType = getLastStickType(positionInRecycler, parent);
            if (floatingStickType == STICK_TYPE_VIEW) {
                //若有上一个悬浮分隔，则悬浮顶部
                View lastPinnedView = getLastStickView(positionInRecycler, parent);
                if (lastPinnedView != null) {
                    //若该页面的第一个悬浮分割线的底部 < 悬浮分隔高度的2倍（悬浮顶部 + 该childView的悬浮分隔，则变换悬浮顶部的位置，实现向上推动的效果
                    if (childView.getTop() < lastPinnedView.getMeasuredHeight() + currentStickHeight) {
                        drawStickView(c, 0,
                                childView.getTop() - currentStickHeight - lastPinnedView.getMeasuredHeight(),
                                parent.getRight(),
                                childView.getTop() - currentStickHeight, lastPinnedView);
                    } else {
                        drawStickView(c, 0, 0, parent.getRight(), lastPinnedView.getMeasuredHeight(), lastPinnedView);
                    }
                }
            } else if (floatingStickType == STICK_TYPE_TEXT) {
                //若有上一个悬浮分隔，则悬浮顶部
                String lastStickText = getLastStickText(positionInRecycler, parent);
                if (!TextUtils.isEmpty(lastStickText)) {
                    //若该页面的第一个悬浮分割线的底部 < 悬浮分隔高度的2倍（悬浮顶部 + 该childView的悬浮分隔，则变换悬浮顶部的位置，实现向上推动的效果
                    if (childView.getTop() < mStickHeight + currentStickHeight) {
                        drawStickText(c, 0,
                                childView.getTop() - mStickHeight - currentStickHeight,
                                parent.getRight(),
                                childView.getTop() - currentStickHeight, lastStickText);
                    } else {
                        drawStickText(c, 0, 0, parent.getRight(), mStickHeight, lastStickText);
                    }
                }
            }
        }
    }

    //绘制分隔线
    private void drawDivider(Canvas c, float left, float top, float right, float bottom) {
        // 通过Canvas绘制矩形（分割线）
        c.drawRect(left, top, right, bottom, mDividerPaint);
    }

    //某个ItemView是否有分隔线
    private boolean isHasDivider(RecyclerView parent, View childView) {
        return isHasDivider(parent, parent.getChildAdapterPosition(childView));
    }

    //某个ItemView是否有悬浮分隔线
    private boolean isHasStickDivider(RecyclerView parent, View childView) {
        int position = parent.getChildAdapterPosition(childView);
        return !TextUtils.isEmpty(getStickText(position, parent));
    }

    /**
     * 某位置是否有分隔线
     * 子类可覆盖
     *
     * @param parent             RecyclerView
     * @param positionInRecycler RecyclerView中位置
     * @return boolean
     */
    protected boolean isHasDivider(RecyclerView parent, int positionInRecycler) {
        if (positionInRecycler <= mOffsetStart) {
            return false;
        }
        if (parent.getAdapter() != null) {
            if (positionInRecycler > parent.getAdapter().getItemCount() - mOffsetEnd - mOffsetStart) {
                return false;
            }
        }
        return true;
    }

    /**
     * 某位置是否有悬浮分隔
     * 子类可覆盖
     *
     * @param parent             RecyclerView
     * @param positionInRecycler RecyclerView中位置
     * @return boolean
     */
    protected boolean isHasStickDivider(RecyclerView parent, int positionInRecycler) {
        return !TextUtils.isEmpty(getStickText(positionInRecycler, parent));
    }

    //悬浮分隔文案
    protected String getStickText(int positionInRecycler, @NonNull RecyclerView parent) {
        String stickText = mStickText.get(positionInRecycler);
        if (TextUtils.isEmpty(stickText)) {
            stickText = onCreateStickText(positionInRecycler, parent);
        }
        return stickText;
    }

    //绘制悬浮分隔以及文案
    private void drawStickText(Canvas c, float left, float top, float right, float bottom, String text) {
        c.drawRect(left, top, right, bottom, mStickPaint);
        mStickTextPaint.getTextBounds(text, 0, text.length(), mStickTextBounds);
        c.drawText(text, left + mStickTextLeftMargin, bottom - mStickHeight / 2f + mStickTextBounds.height() / 2f, mStickTextPaint);
    }


    //获取某位置的上一个悬浮分隔文案
    private String getLastStickText(int positionInRecycler, RecyclerView parent) {
        String lastStickText = "";
        for (int i = positionInRecycler - 1; i >= 0; i--) {
            lastStickText = getStickText(i, parent);
            if (!TextUtils.isEmpty(lastStickText)) {
                break;
            }
        }
        return lastStickText;
    }

    /**
     * 悬浮分隔文案
     * 子类可覆盖
     *
     * @param positionInRecycler RecyclerView中位置
     * @param parent             RecyclerView
     * @return String
     */
    protected String onCreateStickText(int positionInRecycler, @NonNull RecyclerView parent) {
        return null;
    }

    //绘制悬浮分隔以及View
    private void drawStickView(Canvas c, float left, float top, float right, float bottom, View stickView) {
        if (stickView.getDrawingCache() != null) {
            c.drawBitmap(stickView.getDrawingCache(), left, top, null);
        }
    }

    private int getLastStickType(int positionInRecycler, @NonNull RecyclerView parent) {
        int type = 0;
        for (int i = positionInRecycler - 1; i >= 0; i--) {
            if (getStickView(i, parent) != null) {
                type = STICK_TYPE_VIEW;
                break;
            }
            if (!TextUtils.isEmpty(getStickText(i, parent))) {
                type = STICK_TYPE_TEXT;
                break;
            }
        }
        return type;
    }

    //获取某位置的上一个悬浮分隔视图
    private View getLastStickView(int positionInRecycler, @NonNull RecyclerView parent) {
        View lastPinnedView = null;
        for (int i = positionInRecycler - 1; i >= 0; i--) {
            lastPinnedView = getStickView(i, parent);
            if (lastPinnedView != null) {
                break;
            }
        }
        return lastPinnedView;
    }

    //悬浮分隔视图
    private View getStickView(int positionInRecycler, @NonNull RecyclerView parent) {
        View stickView = null;
        if (mStickView.get(positionInRecycler) != null) {
            stickView = mStickView.get(positionInRecycler).get();
        }
        if (stickView == null) {
            stickView = onCreateStickView(positionInRecycler, parent);
            if (stickView != null) {
                stickView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        stickView.getMeasuredHeight());
                stickView.setLayoutParams(layoutParams);
                stickView.setDrawingCacheEnabled(true);
                //指定高度、宽度的groupView
                stickView.layout(0, 0, parent.getRight(), stickView.getMeasuredHeight());
                stickView.buildDrawingCache();
                mStickView.get(positionInRecycler, new WeakReference<>(stickView));
            }
        }
        return stickView;
    }

    /**
     * 悬浮分隔视图
     * 子类可覆盖
     *
     * @param positionInRecycler RecyclerView中位置
     * @param parent             RecyclerView
     * @return View
     */
    protected View onCreateStickView(int positionInRecycler, @NonNull RecyclerView parent) {
        return null;
    }

}
