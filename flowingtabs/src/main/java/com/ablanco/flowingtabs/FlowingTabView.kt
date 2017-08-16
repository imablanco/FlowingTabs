package com.ablanco.flowingtabs

import android.content.Context
import android.support.annotation.FloatRange
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

/**
 * Created by Álvaro Blanco Cabrero on 16/08/2017.
 * FlowingTabsSample.
 */
internal class FlowingTabView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    val tvTitle: TextView
    val viewIndicator: View

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(MODE_NORMAL.toLong(), MODE_COLLAPSED.toLong(), MODE_EXPANDED.toLong())
    annotation class Mode

    @Mode
    var mode: Int = MODE_NORMAL

    init {
        View.inflate(context, R.layout.view_flowing_tab, this)
        tvTitle = findViewById(R.id.tvTitle) as TextView
        viewIndicator = findViewById(R.id.viewIndicator)
    }

    fun fade(@FloatRange(from = 0.0, to = 1.0) factor: Float) {
        when (mode) {
            MODE_NORMAL -> {
                tvTitle.alpha = Math.max(0.4f, factor)
                viewIndicator.alpha = factor
            }
            MODE_COLLAPSED -> {
                tvTitle.alpha = 0f
                viewIndicator.alpha = Math.max(0.4f, factor)
            }
        }

    }


    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        when (mode) {
            MODE_NORMAL -> {
                tvTitle.alpha = if (selected) 1f else 0.4f
                viewIndicator.alpha = if (selected) 1f else 0f
            }
            MODE_COLLAPSED -> {
                tvTitle.alpha = 0f
                viewIndicator.alpha = if (selected) 1f else 0.4f
            }
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = Math.max(MeasureSpec.getSize(widthMeasureSpec), context.dip(MIN_WIDTH))
        val height = Math.max(MeasureSpec.getSize(widthMeasureSpec), context.dip(MIN_HEIGHT))
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, widthMode),
                MeasureSpec.makeMeasureSpec(height, heightMode))
    }

    companion object {

        const val MIN_HEIGHT = 56// dp
        const val MIN_WIDTH = 46// dp

        const val MODE_NORMAL = 0
        const val MODE_COLLAPSED = 1
        const val MODE_EXPANDED = 2
    }

}