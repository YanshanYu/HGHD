package com.yu.zehnit.tools;

import android.view.View;

/**
 * 监听RecycleView每一个item的点击事件
 */
public interface OnRecycleViewItemClickListener {

    default void onClick(int pos) {

    }

    default void onClick(View v, int pos){}

}
