package com.example.sorena.wanandroidapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup
{

    public FlowLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        //
    }

    public FlowLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context)
    {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //获取当前控件的宽高和测量模式
        //如果是match_parent就是父布局的对应宽高
        //如果是wrap_parent就需要动态计算
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //LogUtil.d("日志:测量值","sizeWidth:" + sizeWidth + "  widthMode:" + modeWidth  + " sizeHeight:" + sizeHeight + "  heightMode:" + modeHeight);


        //定义两个数值用于存储wrap_content时的宽高
        int width = 0;
        int height = 0;

        //记录每一行的宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;

        //得到内部元素的个数
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++)
        {
            View child = getChildAt(i);

            //测量子view的宽高,传入父布局的widthMeasureSpec,和heightMeasureSpec,据说可以理解成设置
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            //获取子view的layoutParams,子控件的layoutParams是由父控件的layoutParams决定的
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            //计算子view占据的高度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;

            //计算子view占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;

            //如果这一行不能再放入控件
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight())
            {
                //将现在这一行的行宽和之前的行宽最大值作比较，去取最大值作为新的最大行宽
                width = Math.max(width, lineWidth);
                //重置lineWidth
                lineWidth = childWidth;
                //记录现在的总行高
                height += lineHeight;
                //重置行高
                lineHeight = childHeight;
            } else
            //如果可以继续放入控件
            {
                // 叠加行宽
                lineWidth += childWidth;
                //得到当前行最大的高度
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //如果当前控件是最后一个控件
            if (i == cCount - 1)
            {
                //取最大行宽
                width = Math.max(lineWidth, width);
                //把当前行的行高叠加上去
                height += lineHeight;
            }
        }

        //通过判断测量模式设置最终行高,这里放到最前面感觉可以做优化
        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop()+ getPaddingBottom()//
        );

    }

    //存储所有的view,按行存储
    private List<List<View>> mAllViews = new ArrayList<>();

    //储存每一行的高度
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        mAllViews.clear();
        mLineHeight.clear();

        //当前ViewGroup的宽度
        int width = getWidth();

        //当前行的行宽和行高
        int lineWidth = 0;
        int lineHeight = 0;

        //当前行的view集合
        List<View> lineViews = new ArrayList<>();

        //子控件的个数
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++)
        {
            View child = getChildAt(i);
            //获取margin的集合
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            //获取子控件的宽高
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //如果需要换行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight())
            {
                //记录LineHeight
                mLineHeight.add(lineHeight);
                //记录当前行的Views
                mAllViews.add(lineViews);

                //重置我们的行宽和行高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;

                //重置View集合
                lineViews = new ArrayList<View>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);

        }
        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        //设置子View的位置

        //基准left值和基准top值
        int left = getPaddingLeft();
        int top = getPaddingTop();

        //行数
        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i++)
        {
            //当前行的所有的View
            lineViews = mAllViews.get(i);
            //当前行的高度
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineViews.size(); j++)
            {
                View child = lineViews.get(j);
                // 判断child的状态
                if (child.getVisibility() == View.GONE)
                {
                    continue;
                }

                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();

                int leftPosition = left + layoutParams.leftMargin;
                int topPosition = top + layoutParams.topMargin;
                int rightPosition = leftPosition + child.getMeasuredWidth();
                int bottomPosition = topPosition + child.getMeasuredHeight();

                // 为子View进行布局
                child.layout(leftPosition, topPosition, rightPosition, bottomPosition);

                //更新左基准值
                left += child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            left = getPaddingLeft() ;
            top += lineHeight ;
        }

    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

}