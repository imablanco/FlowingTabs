package com.ablanco.flowingtabs

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView

/**
 * Created by Álvaro Blanco Cabrero on 15/08/2017.
 * FlowingTabsSample.
 */

@CoordinatorLayout.DefaultBehavior(FlowingTabsBehavior::class)
class FlowingTabsLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr), PageIndicator, ViewPager.OnPageChangeListener {

    private val backgroundImage: ImageView
    internal val tabsContainer: ViewGroup
    internal val scrollContainer: HorizontalScrollView

    private var selectedIndex = 0
    private var onPageChangeListener: ViewPager.OnPageChangeListener? = null
    private var iconSelectorRunnable: Runnable? = null
    private var viewPager: ViewPager? = null

    init {
        View.inflate(context, R.layout.view_flowing_tabs_layout, this)
        backgroundImage = findViewById(R.id.ivBackground) as ImageView
        scrollContainer = findViewById(R.id.scrollContainer) as HorizontalScrollView
        tabsContainer = findViewById(R.id.viewTabsContainer) as ViewGroup
        scrollContainer.interceptTouches()

        post { tabsContainer.setPadding(width / 4, 0, width / 4, 0) }
    }

    override fun setViewPager(view: ViewPager) {
        if (viewPager === view) return

        viewPager?.removeOnPageChangeListener(this)

        view.adapter ?: throw IllegalStateException("ViewPager does not have adapter instance.")

        viewPager = view
        view.addOnPageChangeListener(this)
        notifyDataSetChanged()
    }

    override fun setViewPager(view: ViewPager, initialPosition: Int) {
        setViewPager(view)
        setCurrentItem(initialPosition)
    }

    override fun setCurrentItem(item: Int) {
        setCurrentItemInternal(item)
    }

    private fun setCurrentItemInternal(item: Int, move: Boolean = true) {
        if (viewPager == null) throw IllegalStateException("ViewPager has not been bound.")

        selectedIndex = item
        viewPager?.currentItem = item

        tabsContainer.children.forEachIndexed { index, view ->
            val isSelected = index == item
            view.isSelected = isSelected
            if (move && isSelected) animateToIcon(item)
        }
    }

    override fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
        onPageChangeListener = listener
    }


    override fun notifyDataSetChanged() {
        tabsContainer.removeAllViews()
        val pageAdapter = viewPager?.adapter as? FlowingTabsAdapter
                ?: throw IllegalStateException("PagerAdapter must be an instance of FlowingTabsAdapter")

        post {
            pageAdapter.forEach { index ->
                val tab = FlowingTabView(context).apply {
                    layoutParams = LayoutParams(this@FlowingTabsLayout.width / 2, ViewGroup.LayoutParams.MATCH_PARENT)
                    tvTitle.text = pageAdapter.getPageTitle(index)
                }
                tabsContainer.addView(tab)
            }

            val count = pageAdapter.count
            if (selectedIndex > count) selectedIndex = count - 1

            setCurrentItem(selectedIndex)
            requestLayout()
        }
    }

    private fun animateToIcon(position: Int) {
        val iconView = tabsContainer.children[position]
        iconSelectorRunnable?.let { removeCallbacks(it) }

        iconSelectorRunnable = Runnable {
            val scrollPos = iconView.left - (width - iconView.width) / 2
            scrollContainer.smoothScrollTo(scrollPos, 0)
            iconSelectorRunnable = null
        }

        post(iconSelectorRunnable)
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        iconSelectorRunnable?.let { post(it) }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        iconSelectorRunnable?.let { removeCallbacks(it) }
    }

    override fun onPageScrollStateChanged(state: Int) {
        onPageChangeListener?.onPageScrollStateChanged(state)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //here we have to mutate between backgrounds, (cache every new background)
        if (!tabsContainer.isEmpty()) {
            val currentTab = tabsContainer.children[position]
            val scrollPos = currentTab.left - (width - currentTab.width) / 2
            scrollContainer.smoothScrollTo(scrollPos + (currentTab.width * positionOffset).toInt(), 0)

            //if pos == selectIndex, user is moving right, else lef
            val targetTab = tabsContainer.children.safeGetInstance<FlowingTabView>(position + 1)
            targetTab?.fade(positionOffset)
            (currentTab as? FlowingTabView)?.fade(1 - positionOffset)

        }

        onPageChangeListener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(position: Int) {
        setCurrentItemInternal(position, false)
        onPageChangeListener?.onPageSelected(position)
    }
}
