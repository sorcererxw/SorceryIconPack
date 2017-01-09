package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import timber.log.Timber;

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    /**
     * Called when a descendant of the CoordinatorLayout attempts to initiate a nested scroll.
     * <p/>
     * <p>Any Behavior associated with any direct child of the CoordinatorLayout may respond
     * to this event and return true to indicate that the CoordinatorLayout should act as
     * a nested scrolling parent for this scroll. Only Behaviors that return true from
     * this method will receive subsequent nested scroll events.</p>
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     *                          associated with
     * @param child             the child view of the CoordinatorLayout this Behavior is associated with
     * @param directTargetChild the child view of the CoordinatorLayout that either is or
     *                          contains the target of the nested scroll operation
     * @param target            the descendant view of the CoordinatorLayout initiating the nested scroll
     * @param nestedScrollAxes  the axes that this nested scroll applies to. See
     *                          {@link ViewCompat#SCROLL_AXIS_HORIZONTAL},
     *                          {@link ViewCompat#SCROLL_AXIS_VERTICAL}
     * @return true if the Behavior wishes to accept this nested scroll
     * @see NestedScrollingParent#onStartNestedScroll(View, View, int)
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout,
                child,
                directTargetChild,
                target,
                nestedScrollAxes);
    }

    /**
     * Called when a nested scroll in progress has updated and the target has scrolled or
     * attempted to scroll.
     * <p/>
     * <p>Any Behavior associated with the direct child of the CoordinatorLayout may elect
     * to accept the nested scroll as part of {@link #onStartNestedScroll}. Each Behavior
     * that returned true will receive subsequent nested scroll events for that nested scroll.
     * </p>
     * <p/>
     * <p><code>onNestedScroll</code> is called each time the nested scroll is updated by the
     * nested scrolling child, with both consumed and unconsumed components of the scroll
     * supplied in pixels. <em>Each Behavior responding to the nested scroll will receive the
     * same values.</em>
     * </p>
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     *                          associated with
     * @param child             the child view of the CoordinatorLayout this Behavior is associated with
     * @param target            the descendant view of the CoordinatorLayout performing the nested scroll
     * @param dxConsumed        horizontal pixels consumed by the target's own scrolling operation
     * @param dyConsumed        vertical pixels consumed by the target's own scrolling operation
     * @param dxUnconsumed      horizontal pixels not consumed by the target's own scrolling
     *                          operation, but requested by the user
     * @param dyUnconsumed      vertical pixels not consumed by the target's own scrolling operation,
     *                          but requested by the user
     * @see NestedScrollingParent#onNestedScroll(View, int, int, int, int)
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout,
                child,
                target,
                dxConsumed,
                dyConsumed,
                dxUnconsumed,
                dyUnconsumed);
        MyFloatingActionButton fab = (MyFloatingActionButton) child;
        if (dyConsumed > 0) {
            fab.hide();
        } else if (dyConsumed < 0) {
            fab.show();
        }
    }
}