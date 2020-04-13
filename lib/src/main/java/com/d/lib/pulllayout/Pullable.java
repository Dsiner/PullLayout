package com.d.lib.pulllayout;

/**
 * Pullable
 * Created by D on 2017/4/26.
 */
public interface Pullable {

    /**
     * The Pullable is not currently scrolling.
     *
     * @see #getPullState()
     */
    int PULL_STATE_IDLE = 0;

    /**
     * The Pullable is currently being dragged by outside input such as user touch input.
     *
     * @see #getPullState()
     */
    int PULL_STATE_DRAGGING = 1;

    /**
     * The Pullable is currently animating to a final position while not under
     * outside control.
     *
     * @see #getPullState()
     */
    int PULL_STATE_SETTLING = 2;

    /**
     * Return the current scrolling state of the Pullable.
     *
     * @return {@link #PULL_STATE_IDLE}, {@link #PULL_STATE_DRAGGING} or
     * {@link #PULL_STATE_SETTLING}
     */
    int getPullState();

    void setPullState(int state);

    boolean canPullDown();

    boolean canPullUp();

    void setCanPullDown(boolean enable);

    void setCanPullUp(boolean enable);

    /**
     * Add a listener that will be notified of any changes in pull state or position.
     *
     * <p>Components that add a listener should take care to remove it when finished.
     * Other components that take ownership of a view may call {@link #clearOnPullScrollListeners()}
     * to remove all attached listeners.</p>
     *
     * @param listener listener to set or null to clear
     */
    void addOnPullScrollListener(OnPullListener listener);

    /**
     * Remove a listener that was notified of any changes in pull state or position.
     *
     * @param listener listener to set or null to clear
     */
    void removeOnPullScrollListener(OnPullListener listener);

    /**
     * Remove all secondary listener that were notified of any changes in pull state or position.
     */
    void clearOnPullScrollListeners();

    abstract class OnPullListener {

        /**
         * Callback method to be invoked when Pullable's scroll state changes.
         *
         * @param pullable The Pullable whose scroll state has changed.
         * @param newState The updated scroll state. One of {@link #PULL_STATE_IDLE},
         *                 {@link #PULL_STATE_DRAGGING} or {@link #PULL_STATE_SETTLING}.
         */
        public void onPullStateChanged(Pullable pullable, int newState) {
        }

        /**
         * Callback method to be invoked when the Pullable has been scrolled. This will be
         * called after the scroll has completed.
         * <p>
         * This callback will also be called if visible item range changes after a layout
         * calculation. In that case, dx and dy will be 0.
         *
         * @param pullable The PullableView which scrolled.
         * @param dx       The amount of horizontal scroll.
         * @param dy       The amount of vertical scroll.
         */
        public void onPulled(Pullable pullable, int dx, int dy) {
        }
    }
}
