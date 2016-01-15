package tao.jerry.windpush.opengglforandroid.view;

/**
 * Created by WindPush on 16/1/15.
 */

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

public class MusListView extends ListView {

    private Context mContext;
    private boolean outBound = false;
    private int distance;
    private int firstOut;

    public MusListView(Context c) {
        super(c);
        this.mContext = c;
    }

    public MusListView(Context c, AttributeSet attrs) {
        super(c, attrs);
        this.mContext = c;
    }

    public MusListView(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        this.mContext = c;
    }

    GestureDetector gestureDetector = new GestureDetector(
            new OnGestureListener() {

                public boolean onDown(MotionEvent e) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public boolean onFling(MotionEvent e1, MotionEvent e2,
                                       float velocityX, float velocityY) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public void onLongPress(MotionEvent e) {
                    // TODO Auto-generated method stub

                }
                /**捕捉滑动事件  e1为此处为的ACTION_DOWN事件（无论什么动作，起始都是该动作），而e2是触发调用onScroll的事件。而在此期间，可能已经
                 * 触发了多次的onScroll，因为我们滑动过程可能比较长，一旦长于某个值，就会触发一次（即一个Move应该是由多个
                 * move事件组成的，开头当然是个ACTION_DOWN事件），也就会发出一个移动的MotionEvent。但是期间开始此次
                 * scroll的e1是唯一的。而distance是最近一次调用onScroll以来的距离（前一个e2和现在e2的距离:比如上次是-30，这次是-60(比如向下拉),
                 * 那么 distanceY=-60-(-30)=-30）。
                 */

                public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                        float distanceX, float distanceY) {
                    /**
                     * firstPos和lastPos是adapter中元素的Id
                     */
                    int firstPos = getFirstVisiblePosition();
                    int lastPos = getLastVisiblePosition();
                    int itemCount = getCount();

                    /**
                     * 滑出边界，而且是一个极点，即可视部分已经已经不存在了，那么直接回到原点
                     */
                    if (outBound && firstPos != 0 && lastPos != (itemCount - 1)) {
                        scrollTo(0, 0);
                        return false;
                    }
                    /**
                     * getChildAt是屏幕上可见的元素的id，比如现在屏幕上可见的是adapter中的
                     * 4号到10号，那么你调用getChildAt应该是0~6号
                     * listView.getChildAt(i) works where 0 is the very first visible row and
                     *  (n-1) is the last visible row (where n is the number of visible views you see).
                     *  进入该onScroll有4种可能，第一种是刚开始的时候，此时firstPos==0,而且可视的item在getChildAt的
                     *  返回也是第一个元素，即adapter元素的index和可视的view的编号一致，所以firstview不为空（lastview也一样）。
                     *  当你向上 滑动时，distanceY是大于0的。此时将不消费此次事件，那么将正常地在没有超出边际出滚动。
                     *  第二种是，若以上是向下拉，那么应该属于超出范围的情况，则要消费此时事件。
                     *  第三种和第一种类似，只是到了当刚好显示最后一个item时，显然firstView和lastView都将是null，因为
                     *  此时的adapter的index和getChildAt的index不是相等的，而是成对应关系，
                     *  即index_adp-firstPos=index_getChild,此时你若使用getChildAt(firstpos-firstpos)，那返回的
                     *  将是非null。同理在lastView。第四种是当在第三情况下，向上拉，那么属于超出边界。那么lastView是null这个特征
                     *  将可以判断是否进入了下临界区。
                     *  总结以上四种情况，每当触发临界区时（dispatchTouchEvent时getFirstVisiblePosition()==0
                     *  和getLastVisiblePosition()==getCount()-1），就可以通过distanceY的方向性判断是正常的滑动
                     *  还是将要滑出临界区。若是滑出临界区，说明此次将消费该事件，所以返回true，那么在dispatchTouchEvent
                     *  将设置outBand为true,那么第二次再进入时，将可以通过outBand来确定是否出了临界区。
                     *
                     *  带方向的函数：onScrollBy/To和onScroll
                     */
                    View firstView = getChildAt(firstPos);
                    View lastView = getChildAt(lastPos-1);
                    /**
                     * 记录下第一次的e2的y轴距离，此次过后outBound就变为了true。这样distance就是跟踪最近的一次e2
                     * 和最开始一次的e2的距离。
                     */
                    if (!outBound) {
                        firstOut = (int) e2.getRawY();
                    }
                    if (firstView != null
                            && (outBound || (firstPos == 0
                            && firstView.getTop() == 0 && distanceY < 0))) {
                        distance = (int) (firstOut - e2.getRawY());//此处应为负值，即view向下滑动
                        /**
                         * scrollBy中的值带有方向，x若为正，则应该以view中该x点显示在新的原点上，即拿新的点去
                         <span style="white-space: pre;">                </span>*重合y轴，就好像整个布局被往左拉动。
                         * y为正，则向上滑动|y|距离。负则相反。
                         */
                        scrollBy(0, distance / 2);
                        Log.v("onScroll", "e2.getRawY():"+e2.getRawY());
                        Log.v("onScroll", "distance:"+distance);
                        Log.v("onScroll", "distanceY:"+distanceY);
                        return true;
                    }
                    if (lastView == null&&(outBound || (lastPos == itemCount - 1 && distanceY > 0))) {
                        Log.d("bottom", "bottom");
                        distance = (int) (firstOut - e2.getRawY());//此处应为正直，因为view向上滑动
                        scrollBy(0, distance/2);
                        return true;
                    }
                    return false;
                }

                public void onShowPress(MotionEvent e) {
                    // TODO Auto-generated method stub

                }

                public boolean onSingleTapUp(MotionEvent e) {
                    // TODO Auto-generated method stub
                    return false;
                }
            });

    /**
     * 最早响应触屏事件，按下和释放响应两次
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getFirstVisiblePosition()==0){
            int act = ev.getAction();
            if ((act == MotionEvent.ACTION_UP || act == MotionEvent.ACTION_CANCEL)
                    && outBound) {
                outBound = false;
            }
            if (!gestureDetector.onTouchEvent(ev)) {
                outBound = false;
            } else {
                outBound = true;
            }
            Rect rect = new Rect();
            getLocalVisibleRect(rect);
            /**
             * rect.top是个正的距离值，而TanslateAnimation填的是坐标值（有方向的）；
             */
            TranslateAnimation am = new TranslateAnimation(0, 0, -rect.top, 0);
            /**
             * 若此处时间设为0，将导致一阵的抖动，因为完成回滚的速度不是分步，而是直接到终点
             * 因为每次触发onScroll时都会做一次回滚，而当传进又一次move时，上一次的move还没作完
             * 就将被新的一次覆盖，所以不用担心产生抖动。所以此处给它设时间就是抓住它需要时间来完成回滚的目标，相当
             * 于给它一个时间的缓冲来实现移动，因为当你在移动时，实际是不需要回滚的，只有你释放了手指还才需要回滚。
             * 注意，此时调用scrollTo已经将位置返回了0（可以把animation当成是模型，只有使用scrollTo才
             * 能真正触发该移动，结果是已经知道了的，即移动到原点，而过程是TranslateAnimation参谋的，即
             * scrollTo在移动时会调用onScrollChange来实际移动，而onScrollChange则根据传入的参数来移动
             * 而TranslateAnimation则可以控制该参数。可以把scrollTo先去掉，就可以发现new top 和
             * after scrollBy是一样的值）。也就是new Top=0。所以每次迭代相减都是现在的e2减去最初的e2在y轴上的值，
             * 这样通过scrollBy就可以将view移动到新的位置，而此时top也就又被写成了新的滑动的位置（是滑动距离的一半位置）。
             * 11-19 23:51:11.101: V/onScroll(18396): after scrollBy top:0
             11-19 23:51:11.101: V/onScroll(18396): new top:0
             11-19 23:51:11.249: V/onScroll(18396): after scrollBy top:0
             11-19 23:51:11.249: V/onScroll(18396): new top:0
             11-19 23:51:11.288: V/onScroll(18396): after scrollBy top:-6
             11-19 23:51:11.288: V/onScroll(18396): new top:0
             11-19 23:51:11.319: V/onScroll(18396): after scrollBy top:-16
             11-19 23:51:11.319: V/onScroll(18396): new top:0
             11-19 23:51:11.358: V/onScroll(18396): after scrollBy top:-20
             11-19 23:51:11.358: V/onScroll(18396): new top:0
             11-19 23:51:11.374: V/onScroll(18396): after scrollBy top:-27
             11-19 23:51:11.374: V/onScroll(18396): new top:0
             */
            am.setDuration(300);
            startAnimation(am);
            Log.v("onScroll", "after scrollBy top:" + rect.top);
            scrollTo(0, 0);
            getLocalVisibleRect(rect);
            Log.v("onScroll", "new top:"+rect.top);
        }
        Log.d("getCount()", getCount()+"");
        if(getLastVisiblePosition()==getCount()-1){
            int act = ev.getAction();
            if ((act == MotionEvent.ACTION_DOWN || act == MotionEvent.ACTION_CANCEL)
                    && outBound) {
                outBound = false;
            }
            if (!gestureDetector.onTouchEvent(ev)) {
                outBound = false;
            } else {
                outBound = true;
            }
            if(outBound){
                Rect rect1 = new Rect();
                getLocalVisibleRect(rect1);
                TranslateAnimation am1 = new TranslateAnimation(0, 0, rect1.top, 0);
                am1.setDuration(300);
                startAnimation(am1);
                scrollTo(0, 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    };
}