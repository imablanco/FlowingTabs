package com.ablanco.flowingtabs;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by Álvaro Blanco Cabrero on 15/08/2017.
 * FlowingTabsSample.
 */

class ViewOffsetHelper {
    private final View mView;

    private int mLayoutTop;
    private int mLayoutLeft;
    private int mOffsetTop;
    private int mOffsetLeft;

    ViewOffsetHelper(View view) {
        mView = view;
    }

    void onViewLayout() {
        // Now grab the intended top
        mLayoutTop = mView.getTop();
        mLayoutLeft = mView.getLeft();

        // And offset it as needed
        updateOffsets();
    }

    private void updateOffsets() {
        ViewCompat.offsetTopAndBottom(mView, mOffsetTop - (mView.getTop() - mLayoutTop));
        ViewCompat.offsetLeftAndRight(mView, mOffsetLeft - (mView.getLeft() - mLayoutLeft));
    }

    /**
     * Set the top and bottom offset for this {@link ViewOffsetHelper}'s view.
     *
     * @param offset the offset in px.
     * @return true if the offset has changed
     */
    boolean setTopAndBottomOffset(int offset) {
        if (mOffsetTop != offset) {
            mOffsetTop = offset;
            updateOffsets();
            return true;
        }
        return false;
    }

    /**
     * Set the left and right offset for this {@link ViewOffsetHelper}'s view.
     *
     * @param offset the offset in px.
     * @return true if the offset has changed
     */
    boolean setLeftAndRightOffset(int offset) {
        if (mOffsetLeft != offset) {
            mOffsetLeft = offset;
            updateOffsets();
            return true;
        }
        return false;
    }

    int getTopAndBottomOffset() {
        return mOffsetTop;
    }

    int getLeftAndRightOffset() {
        return mOffsetLeft;
    }

    int getLayoutTop() {
        return mLayoutTop;
    }

    int getLayoutLeft() {
        return mLayoutLeft;
    }
}
