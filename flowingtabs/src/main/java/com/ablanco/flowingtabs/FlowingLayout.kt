package com.ablanco.flowingtabs

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View

/**
 * Created by Álvaro Blanco Cabrero on 15/08/2017.
 * FlowingTabsSample.
 */

class FlowingLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        CoordinatorLayout(context, attrs, defStyleAttr), PageIndicator {

    private val appBarLayout: View
    private val flowingTabsLayout: FlowingTabsLayout

    init {

        View.inflate(context, R.layout.view_flowing_layout, this)

        appBarLayout = findViewById(R.id.appBarLayout)
        flowingTabsLayout = findViewById(R.id.flowingTabsLayout) as FlowingTabsLayout

        appBarLayout.post {
            flowingTabsLayout.apply {
                layoutParams = CoordinatorLayout.LayoutParams(appBarLayout.width, appBarLayout.height)
                requestLayout()
            }
        }
    }

    override fun setViewPager(view: ViewPager) = flowingTabsLayout.setViewPager(view)

    override fun setViewPager(view: ViewPager, initialPosition: Int) =
            flowingTabsLayout.setViewPager(view, initialPosition)

    override fun setCurrentItem(item: Int) = flowingTabsLayout.setCurrentItem(item)

    override fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener) =
            flowingTabsLayout.setOnPageChangeListener(listener)

    override fun notifyDataSetChanged() = flowingTabsLayout.notifyDataSetChanged()
}
