package com.ablanco.flowingtabs

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.Nullable
import android.support.v4.view.PagerAdapter

/**
 * Created by Álvaro Blanco Cabrero on 16/08/2017.
 * FlowingTabsSample.
 */
abstract class FlowingTabsAdapter : PagerAdapter() {

    @Nullable
    abstract fun getPageTitleBackground(): Drawable

    @DrawableRes
    abstract fun getPageTitleBackgroundResId(): Int
}