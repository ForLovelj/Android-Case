package com.clow.customviewcase.widget;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.Gravity;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.clow.customviewcase.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
/**
 * Created by clow
 * Des: TextView 通过移动画布绘制两次文本实现跑马灯效果，根据两帧绘制的时间差计算跑动距离，这儿扩展支持竖向跑马灯效果。
 * Date: 2023/3/21.
 */
public class MarqueeTextView extends AppCompatTextView {

    private static final int DEFAULT_BG_COLOR = Color.parseColor("#00000000");

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private Marquee mMarquee;
    private boolean mRestartMarquee;
    private boolean isMarquee;

    private int mOrientation;
    private Marquee.Builder builder = new Marquee.Builder();

    public MarqueeTextView(@NonNull Context context) {
        this(context, null);
    }

    public MarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MarqueeTextView, defStyleAttr, 0);

        mOrientation = ta.getInt(R.styleable.MarqueeTextView_orientation, HORIZONTAL);
        ta.recycle();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mOrientation == HORIZONTAL) {
            if (getWidth() > 0) {
                mRestartMarquee = true;
            }
        } else {
            if (getHeight() > 0) {
                mRestartMarquee = true;
            }
        }
    }

    private void restartMarqueeIfNeeded() {
        if (mRestartMarquee) {
            mRestartMarquee = false;
            startMarquee(builder);
        }
    }

    /**
     * 开始跑马灯
     */
    public void start() {
        post(() -> {
            boolean wasStart = isMarquee();
            isMarquee = true;
            if (!wasStart) {
                startMarquee(builder);
            }
        });
    }

    /**
     * 通过配置开始跑马灯
     * @param builder
     */
    public void start(Marquee.Builder builder) {
        post(() -> {
            boolean wasStart = isMarquee();
            isMarquee = true;
            if (!wasStart) {
                startMarquee(builder);
            }
        });
    }

    /**
     * 停止跑马灯
     */
    public void stop() {
        boolean wasStart = isMarquee();
        isMarquee = false;
        if (wasStart) {
            stopMarquee();
        }
    }

    private void setMarquee(boolean marquee) {
        boolean wasStart = isMarquee();

        isMarquee = marquee;

        if (wasStart != marquee) {
            if (marquee) {
                startMarquee();
            } else {
                stopMarquee();
            }
        }
    }

    public void setOrientation(@OrientationMode int orientation) {
        mOrientation = orientation;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public boolean isRunning() {
        return mMarquee != null && mMarquee.isRunning();
    }

    public boolean isStopped() {
        return mMarquee != null && mMarquee.isStopped();
    }

    /**
     * 是否为跑马灯状态
     * @return
     */
    public boolean isMarquee() {
        return isMarquee;
    }

    private void stopMarquee() {
        if (mOrientation == HORIZONTAL) {
            setHorizontalFadingEdgeEnabled(false);
        } else {
            setVerticalFadingEdgeEnabled(false);
        }

        requestLayout();
        invalidate();

        if (mMarquee != null && !mMarquee.isStopped()) {
            mMarquee.stop();
        }
    }

    private void startMarquee() {
        if (canMarquee()) {

            if (mOrientation == HORIZONTAL) {
                setHorizontalFadingEdgeEnabled(true);
            } else {
                setVerticalFadingEdgeEnabled(true);
            }

            if (mMarquee == null) mMarquee = builder.build();
            mMarquee.start(builder.repeatLimit);
        }
    }

    private void startMarquee(Marquee.Builder builder) {
        if (canMarquee()) {

            if (mOrientation == HORIZONTAL) {
                setHorizontalFadingEdgeEnabled(true);
            } else {
                setVerticalFadingEdgeEnabled(true);
            }
            this.builder = builder;
            if (mMarquee == null) mMarquee = builder.marqueeTextView(this).build();
            mMarquee.start(builder.repeatLimit);
        }
    }

    private boolean canMarquee() {
        if (mOrientation == HORIZONTAL) {
            int viewWidth = getWidth() - getCompoundPaddingLeft() -
                    getCompoundPaddingRight();
            float lineWidth = getLayout().getLineWidth(0);
            return (mMarquee == null || mMarquee.isStopped())
                    && (isFocused() || isSelected() || isMarquee())
                    && viewWidth > 0
                    && lineWidth > viewWidth;
        } else {
            int viewHeight = getHeight() - getCompoundPaddingTop() -
                    getCompoundPaddingBottom();
            float textHeight = getLayout().getHeight();
            return (mMarquee == null || mMarquee.isStopped())
                    && (isFocused() || isSelected() || isMarquee())
                    && viewHeight > 0
                    && textHeight > viewHeight;
        }
    }

    int _getVerticalOffset(boolean forceNormal) {
        try {
            Class<?> clz = getClass().getSuperclass().getSuperclass();
            Method getVerticalOffset = clz.getDeclaredMethod("getVerticalOffset",boolean.class);
            getVerticalOffset.setAccessible(true);
            return (int) getVerticalOffset.invoke(this,forceNormal);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 是否需要重启启动跑马灯
        restartMarqueeIfNeeded();

        //如果调用了`super.onDraw(canvas);` 背景不能有透明度，有透明度会导致遮盖不了TextView绘制的文本
//        super.onDraw(canvas);

        // 再次绘制背景色，覆盖下面由TextView绘制的文本，我们这儿自己简单的处理了文本绘制，可以不调用`super.onDraw(canvas);`
        // 如果没有背景色则使用默认颜色
        Drawable background = getBackground();
        if (background != null) {
            background.draw(canvas);
        } else {
            canvas.drawColor(DEFAULT_BG_COLOR);
        }
        canvas.save();

        //处理文字的padding  Gravity
        final int compoundPaddingLeft = getCompoundPaddingLeft();
        final int compoundPaddingTop = getCompoundPaddingTop();
        final int compoundPaddingRight = getCompoundPaddingRight();
        final int compoundPaddingBottom = getCompoundPaddingBottom();
        int extendedPaddingTop = getExtendedPaddingTop();
        int extendedPaddingBottom = getExtendedPaddingBottom();

        final int vspace = getBottom() - getTop() - compoundPaddingBottom - compoundPaddingTop;
        final int maxScrollY = getLayout().getHeight() - vspace;

        float clipLeft = compoundPaddingLeft + getScrollX();
        float clipTop = (getScrollY() == 0) ? 0 : extendedPaddingTop + getScrollY();
        float clipRight = getRight() - getLeft() - compoundPaddingRight+ getScrollX();
        float clipBottom = getBottom() - getTop() + getScrollY()
                - ((getScrollY() == maxScrollY) ? 0 : extendedPaddingBottom);

        float mShadowRadius = getShadowRadius();
        if (mShadowRadius != 0) {
            float mShadowDx = getShadowDx();
            float mShadowDy = getShadowDy();
            clipLeft += Math.min(0, mShadowDx - mShadowRadius);
            clipRight += Math.max(0, mShadowDx + mShadowRadius);

            clipTop += Math.min(0, mShadowDy - mShadowRadius);
            clipBottom += Math.max(0, mShadowDy + mShadowRadius);
        }
        canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);
        // translate in by our padding
        /* shortcircuit calling getVerticaOffset() */
        int mGravity = getGravity();
        int voffsetText = 0;
        if ((mGravity & Gravity.VERTICAL_GRAVITY_MASK) != Gravity.TOP) {
            //反射处理Gravity
            voffsetText = _getVerticalOffset(false);
        }
        canvas.translate(compoundPaddingLeft, extendedPaddingTop + voffsetText);

//        canvas.translate(compoundPaddingLeft, extendedPaddingTop);
        //处理文字颜色
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        //水平跑马灯
        if (mOrientation == HORIZONTAL) {
            // 判断跑马灯是否启动
            if (mMarquee != null && mMarquee.isRunning()) {
                final float dx = -mMarquee.getScroll();
                // 移动画布
                canvas.translate(dx, 0.0F);
            }
            //绘制文本
            getLayout().draw(canvas, null, null, 0);

            //判断是否可以绘制重影文本
            if (mMarquee != null && mMarquee.shouldDrawGhost()) {
                final float dx = mMarquee.getGhostOffset();
                // 移动画布
                canvas.translate(dx, 0.0F);
                //绘制文本
                getLayout().draw(canvas, null, null, 0);
            }
        } else {
            //垂直跑马灯
            if (mMarquee != null && mMarquee.isRunning()) {
                final float dy = -mMarquee.getScroll();
                canvas.translate(0.0F, dy);
            }

            getLayout().draw(canvas, null, null, 0);

            if (mMarquee != null && mMarquee.shouldDrawGhost()) {
                final float dy = mMarquee.getGhostOffset();
                canvas.translate(0.0F, dy);
                getLayout().draw(canvas, null, null, 0);
            }
        }

        canvas.restore();
    }

    @Override
    protected float getLeftFadingEdgeStrength() {
        if (mOrientation == HORIZONTAL && mMarquee != null && !mMarquee.isStopped()) {
            final Marquee marquee = mMarquee;
            if (marquee.shouldDrawLeftFade()) {
                final float scroll = marquee.getScroll();
                return scroll / getHorizontalFadingEdgeLength();
            } else {
                return 0.0F;
            }
        }
        return super.getLeftFadingEdgeStrength();
    }

    @Override
    protected float getRightFadingEdgeStrength() {
        if (mOrientation == HORIZONTAL && mMarquee != null && !mMarquee.isStopped()) {
            if (mMarquee.shouldDrawFade()) {
                final Marquee marquee = mMarquee;
                final float maxFadeScroll = marquee.getMaxFadeScroll();
                final float scroll = marquee.getScroll();
                return (maxFadeScroll - scroll) / getHorizontalFadingEdgeLength();
            } else {
                return 0.0F;
            }

        }
        return super.getRightFadingEdgeStrength();
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        if (mOrientation == VERTICAL && mMarquee != null && !mMarquee.isStopped()) {
            final Marquee marquee = mMarquee;
            if (marquee.shouldDrawTopFade()) {
                final float scroll = marquee.getScroll();
                return scroll / getVerticalFadingEdgeLength();
            } else {
                return 0.0F;
            }
        }
        return super.getTopFadingEdgeStrength();
    }

    @Override
    protected float getBottomFadingEdgeStrength() {
        if (mOrientation == VERTICAL && mMarquee != null && !mMarquee.isStopped()) {
            final Marquee marquee = mMarquee;
            if (mMarquee.shouldDrawFade()) {
                final float maxFadeScroll = marquee.getMaxFadeScroll();
                final float scroll = marquee.getScroll();
                return (maxFadeScroll - scroll) / getVerticalFadingEdgeLength();
            } else {
                return 0.0F;
            }

        }
        return super.getBottomFadingEdgeStrength();
    }

    public static final class Marquee {
        //重跑时间间隔
        private static final int MARQUEE_DELAY = 1200;
        //跑动速度
        private static final int MARQUEE_DP_PER_SECOND = 30;

        private static final byte MARQUEE_STOPPED = 0x0;
        private static final byte MARQUEE_STARTING = 0x1;
        private static final byte MARQUEE_RUNNING = 0x2;

        private static final String METHOD_GET_FRAME_TIME = "getFrameTime";

        private final WeakReference<MarqueeTextView> mView;
        // 帧率相关
        private final Choreographer mChoreographer;
        // 状态
        private byte mStatus = MARQUEE_STOPPED;
        // 绘制一次跑多长距离
        private final float mPixelsPerSecond;
        // 最大滚动距离
        private float mMaxScroll;
        // 是否可以绘制右阴影, 右侧淡入淡出效果
        private float mMaxFadeScroll;
        // 重影文本什么时候开始绘制
        private float mGhostStart;
        // 重影文本绘制位置偏移量
        private float mGhostOffset;
        // 是否可以绘制左阴影，左侧淡入淡出效果
        private float mFadeStop;
        // 重复限制
        private int mRepeatLimit;
        // 跑动距离
        private float mScroll;
        // 最后一次跑动时间，单位毫秒
        private long mLastAnimationMs;
        //重跑时间间隔
        private int marqueeDelay;
        //跑动速度
        private int marqueeDpPerSecond;
        //是否绘制淡入淡出
        private boolean shouldDrawFade;

        Marquee(Builder builder) {
            final float density = builder.marqueeTextView.getContext().getResources().getDisplayMetrics().density;
            this.marqueeDelay = builder.marqueeDelay;
            this.marqueeDpPerSecond = builder.marqueeDpPerSecond;
            this.shouldDrawFade = builder.shouldDrawFade;
            this.mRepeatLimit = builder.repeatLimit;

            // 计算每次跑多长距离
            mPixelsPerSecond = marqueeDpPerSecond * density;
            mView = new WeakReference<>(builder.marqueeTextView);
            mChoreographer = Choreographer.getInstance();
        }

        public static final class Builder {
            private int marqueeDelay = MARQUEE_DELAY;
            //跑动速度
            private int marqueeDpPerSecond = MARQUEE_DP_PER_SECOND;
            //是否绘制淡入淡出
            private boolean shouldDrawFade = false;
            //-1无限次重复
            private int repeatLimit = -1;
            private MarqueeTextView marqueeTextView;

            public Builder() {
            }

            public Builder marqueeDelay(int marqueeDelay) {
                this.marqueeDelay = marqueeDelay;
                return this;
            }

            public Builder marqueeDpPerSecond(int marqueeDpPerSecond) {
                this.marqueeDpPerSecond = marqueeDpPerSecond;
                return this;
            }

            public Builder shouldDrawFade(boolean shouldDrawFade) {
                this.shouldDrawFade = shouldDrawFade;
                return this;
            }

            public Builder repeatLimit(int repeatLimit) {
                this.repeatLimit = repeatLimit;
                return this;
            }

            Builder marqueeTextView(MarqueeTextView marqueeTextView) {
                this.marqueeTextView = marqueeTextView;
                return this;
            }

            public Marquee build() {
                return new Marquee(this);
            }
        }


        // 帧率回调，用于跑马灯跑动
        private final Choreographer.FrameCallback mTickCallback = frameTimeNanos -> tick();

        // 帧率回调，用于跑马灯开始跑动
        private final Choreographer.FrameCallback mStartCallback = new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                mStatus = MARQUEE_RUNNING;
                mLastAnimationMs = getFrameTime();
                tick();
            }
        };

        /**0
         * `getFrameTime`是隐藏api，此处使用反射调用，Android P 开始限制反射使用隐藏api。
         *  高系统版本可能失效，可使用某些方案绕过此限制
         *
         *  警告日志 Accessing hidden method Landroid/view/Choreographer;->getFrameTime()J (greylist, reflection, allowed)
         */
        @SuppressLint("PrivateApi")
        private long getFrameTime() {
            try {
                Class<? extends Choreographer> clz = mChoreographer.getClass();
                Method getFrameTime = clz.getDeclaredMethod(METHOD_GET_FRAME_TIME);
                getFrameTime.setAccessible(true);
                return (long) getFrameTime.invoke(mChoreographer);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }

        // 帧率回调，用于跑马灯重新跑动
        private final Choreographer.FrameCallback mRestartCallback = new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                if (mStatus == MARQUEE_RUNNING) {
                    if (mRepeatLimit >= 0) {
                        mRepeatLimit--;
                    }
                    start(mRepeatLimit);
                }
            }
        };

        // 跑马灯跑动实现
        void tick() {
            if (mStatus != MARQUEE_RUNNING) {
                return;
            }

            mChoreographer.removeFrameCallback(mTickCallback);

            final MarqueeTextView textView = mView.get();
            // 判断TextView是否处于获取焦点或选中状态
            if (textView != null && (textView.isFocused() || textView.isSelected() || textView.isMarquee())) {
                // 获取当前时间
                long currentMs = getFrameTime();
                // 计算当前时间与上次时间的差值
                long deltaMs = currentMs - mLastAnimationMs;
                mLastAnimationMs = currentMs;
                // 根据时间差计算本次跑动的距离，减轻视觉上跳动/卡顿
                float deltaPx = deltaMs / 1000F * mPixelsPerSecond;
                // 计算跑动距离
                mScroll += deltaPx;
                // 判断是否已经跑完
                if (mScroll > mMaxScroll) {
                    mScroll = mMaxScroll;
                    // 发送重新开始跑动事件
                    mChoreographer.postFrameCallbackDelayed(mRestartCallback, marqueeDelay);
                } else {
                    // 发送下一次跑动事件
                    mChoreographer.postFrameCallback(mTickCallback);
                }
                textView.invalidate();
            }
        }

        // 停止跑马灯
        void stop() {
            mStatus = MARQUEE_STOPPED;
            mChoreographer.removeFrameCallback(mStartCallback);
            mChoreographer.removeFrameCallback(mRestartCallback);
            mChoreographer.removeFrameCallback(mTickCallback);
            resetScroll();
        }

        private void resetScroll() {
            mScroll = 0.0F;
            final MarqueeTextView textView = mView.get();
            if (textView != null) textView.invalidate();
        }

        // 启动跑马灯
        void start(int repeatLimit) {
            if (repeatLimit == 0) {
                stop();
                return;
            }
            mRepeatLimit = repeatLimit;
            final MarqueeTextView textView = mView.get();
            if (textView != null && textView.getLayout() != null) {
                // 设置状态为在跑
                mStatus = MARQUEE_STARTING;
                // 重置跑动距离
                mScroll = 0.0F;

                if (textView.getOrientation() == HORIZONTAL) {
                    // 计算TextView宽度
                    int viewWidth = textView.getWidth() - textView.getCompoundPaddingLeft() -
                            textView.getCompoundPaddingRight();
                    // 获取文本第0行的宽度
                    float lineWidth = textView.getLayout().getLineWidth(0);
                    // 取TextView宽度的三分之一
                    float gap = viewWidth / 3.0F;
                    // 计算什么时候可以开始绘制重影文本：首部文本跑动到哪里可以绘制重影文本
                    mGhostStart = lineWidth - viewWidth + gap;
                    // 计算最大滚动距离：什么时候认为跑完一次
                    mMaxScroll = mGhostStart + viewWidth;
                    // 重影文本绘制偏移量
                    mGhostOffset = lineWidth + gap;
                    // 跑动到哪里时不绘制左侧阴影
                    mFadeStop = lineWidth + viewWidth / 6.0F;
                    // 跑动到哪里时不绘制右侧阴影
                    mMaxFadeScroll = mGhostStart + lineWidth + lineWidth;
                } else {
                    // 计算TextView高度
                    int viewHeight = textView.getHeight() - textView.getCompoundPaddingTop() -
                            textView.getCompoundPaddingBottom();
                    float textHeight = textView.getLayout().getHeight();
                    float gap = viewHeight / 3.0F;
                    mGhostStart = textHeight - viewHeight + gap;
                    mMaxScroll = mGhostStart + viewHeight;
                    mGhostOffset = textHeight + gap;
                    mFadeStop = textHeight + viewHeight / 6.0F;
                    mMaxFadeScroll = mGhostStart + textHeight + textHeight;
                }

                textView.invalidate();
                // 开始跑动
                mChoreographer.postFrameCallback(mStartCallback);
            }
        }

        // 获取尾部文本绘制位置偏移量
        float getGhostOffset() {
            return mGhostOffset;
        }

        // 获取当前滚动距离
        float getScroll() {
            return mScroll;
        }

        float getMaxFadeScroll() {
            return mMaxFadeScroll;
        }

        boolean shouldDrawLeftFade() {
            return shouldDrawFade && mScroll <= mFadeStop;
        }

        boolean shouldDrawTopFade() {
            return shouldDrawFade && mScroll <= mFadeStop;
        }

        boolean shouldDrawFade() {
            return shouldDrawFade;
        }

        // 判断是否可以绘制重影文本
        boolean shouldDrawGhost() {
            return mStatus == MARQUEE_RUNNING && mScroll > mGhostStart;
        }

        // 跑马灯是否在跑
        boolean isRunning() {
            return mStatus == MARQUEE_RUNNING;
        }

        // 跑马灯是否停止
        boolean isStopped() {
            return mStatus == MARQUEE_STOPPED;
        }
    }
}
