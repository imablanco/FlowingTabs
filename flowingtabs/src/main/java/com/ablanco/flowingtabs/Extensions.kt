package com.ablanco.flowingtabs

import android.content.Context
import android.support.annotation.DimenRes
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout

/**
 * Created by Álvaro Blanco Cabrero on 15/08/2017.
 * FlowingTabsSample.
 */

fun Context.dimen(@DimenRes dimen: Int) = resources.getDimensionPixelSize(dimen)

fun Context.dip(dip: Int) = (resources.displayMetrics.density * dip).toInt()

inline fun PagerAdapter.forEach(block: (index: Int) -> Unit) {
    for (i in 0 until count) {
        block(i)
    }
}

fun ViewGroup.interceptTouches() {
    setOnTouchListener({ _, _ -> true })
}

val ViewGroup.children: List<View>
    get() = (0 until childCount).map { getChildAt(it) }


fun ViewGroup.firstSelected() = children.firstOrNull { it.isSelected }

fun <T> List<T>.safeGet(position: Int): T? {
    return if (position < 0 || position > size - 1) null
    else get(position)
}

inline fun <reified T> List<*>.forEachInstance(block: (T) -> Unit) = filterIsInstance<T>().forEach(block)

inline fun <reified T> List<*>.safeGetInstance(position: Int): T? {
    return if (position < 0 || position > size - 1) null
    else if (get(position) is T) get(position) as T else null
}


fun ViewGroup.isEmpty() = children.isEmpty()

fun matchParentParams() = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT)

inline fun ViewGroup.linearLayout(block: LinearLayout.() -> Unit) =
        LinearLayout(context).apply { block() }

inline fun ViewGroup.framelayout(block: FrameLayout.() -> Unit) =
        FrameLayout(context).apply { block() }