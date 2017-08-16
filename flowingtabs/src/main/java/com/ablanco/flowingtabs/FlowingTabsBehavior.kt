package com.ablanco.flowingtabs

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout

/**
 * Created by Álvaro Blanco Cabrero on 16/08/2017.
 * FlowingTabsSample.
 */

internal class FlowingTabsBehavior @JvmOverloads constructor(context: Context? = null, attrs: AttributeSet? = null) :
        CoordinatorLayout.Behavior<FlowingTabsLayout>(context, attrs) {

    private var maxMeasuredTabHeight = 0
    private var maxMeasuredTabWidth = 0

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: FlowingTabsLayout?, dependency: View?) =
            dependency is AppBarLayout

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: FlowingTabsLayout, dependency: View): Boolean {
        val appBarLayout = dependency as AppBarLayout
        child.y = dependency.y

        val scrolledFactor = getDependencyScrolledFactor(appBarLayout)
        child.tabsContainer.children.forEachInstance<FlowingTabView> { tab ->
            maxMeasuredTabHeight = Math.max(maxMeasuredTabHeight, tab.height)
            maxMeasuredTabWidth = Math.max(maxMeasuredTabWidth, tab.width)

            val width = (1 - scrolledFactor) * maxMeasuredTabWidth
            val height = (1 - scrolledFactor) * maxMeasuredTabHeight
            Log.d("FlowingTabsBehavior", "width : $width height: $height")
            tab.layoutParams = LinearLayout.LayoutParams(width.toInt(), height.toInt())

            tab.viewIndicator.alpha = if (tab.isSelected) 1f else Math.min(0.4f, scrolledFactor)
            tab.tvTitle.alpha = Math.min(0.4f, (1 - scrolledFactor * TAB_TITLE_ALPHA_COLLAPSING_FACTOR).toFloat())

            //offset every tab on Y axis to avoid get translated by parent moving
            val finalYPosition = child.bottom - child.context.dip(FlowingTabView.MIN_HEIGHT) - tab.top
            tab.y = tab.top + (finalYPosition * scrolledFactor)

            if (scrolledFactor == 0f) tab.mode = FlowingTabView.MODE_NORMAL
            if (scrolledFactor == 1f) {
                tab.mode = FlowingTabView.MODE_COLLAPSED
                child.scrollContainer.smoothScrollTo(child.width / 2, 0)
            }

        }

        //keep scrolled on selected tab
        child.tabsContainer.firstSelected()?.let { tab ->
            /*val scrollPos = tab.left - (child.width - tab.width) / 2
            child.scrollContainer.scrollTo(scrollPos, 0)*/
            val x = child.tabsContainer.width / 2
            val scrollTo = (x * (scrolledFactor) + (1 - scrolledFactor) * tab.left).toInt()
            child.scrollContainer.smoothScrollTo(scrollTo, 0)
        }


        return true
    }


    /**
     * @param dependency The [AppBarLayout] that this view depends on
     * @return normalized value between [0-1] indicating the scrolled percentage of the dependent view
     */
    private fun getDependencyScrolledFactor(dependency: AppBarLayout): Float {
        //dependency will move all its height minus pinned dummy Toolbar inside it
        val totalScroll = (dependency.height - dependency.context.dimen(R.dimen.toolbar_height)).toFloat()
        return Math.abs(dependency.y / totalScroll)
    }

    companion object {
        const val TAB_TITLE_ALPHA_COLLAPSING_FACTOR = 2.0
    }
}