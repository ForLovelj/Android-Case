package com.clow.animation_case.weidgt;

import android.util.Log;
import android.view.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
/**
 * 这个视图有几个目的。主要是作为时间曲线的可视化工具，它显示在1x1网格上。
 * 此外，当动画运行时，一个红色的球沿着曲线动画，以帮助展示可视化曲线如何影响对象的运动。
 * 最后，当曲线由PathInterpolator定义时，你可以定制出任何你想要的速度模型,路径控制点就会显示出来，并且可以通过移动来改变路径。
 */
public class CurveVisualizer extends View {

    Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mCurvePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mControlPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // 这个可视化工具保存着应用程序使用的核心插值器集
    Interpolator mInterpolator = new LinearInterpolator();

    public float getCx1() {
        return mCx1;
    }

    public float getCy1() {
        return mCy1;
    }

    public float getCx2() {
        return mCx2;
    }

    public float getCy2() {
        return mCy2;
    }

    // 当使用PathInterpolator时，这些变量保存着控制点
    float mCx1, mCy1, mCx2, mCy2;

    // 变量，以指示用户是否正在移动任意一个控制点
    boolean mMovingC1 = false, mMovingC2 = false;

    // 指示设置PathInterpolator时使用的路径类型 2次贝塞尔 3次贝塞尔
    boolean mCubicPath = false;
    boolean mQuadraticPath = false;

    // 当它们被用户移动时 这个回调用于用控制点的新值更新UI
    ControlPointCallback mPathCallback;

    // 动画的时间完成度
    float mFraction;

    // 控制点和动画对象的大小
    private static int RADIUS = 30;

    // 缩放网格/曲线 显示，以允许对象/曲线 在1x1网格之外绘制
    static float SCALE_FACTOR = 0.75f;

    public CurveVisualizer(Context context) {
        super(context);
        mFraction = 0;
        mGridPaint.setStrokeWidth(2f);
        mGridPaint.setColor(Color.GRAY);
        mCurvePaint.setStrokeWidth(5f);
        mCurvePaint.setColor(Color.BLUE);
        mBallPaint.setStyle(Paint.Style.FILL);
        mBallPaint.setColor(Color.RED);
        mControlPointPaint.setStyle(Paint.Style.FILL);
        mControlPointPaint.setColor(Color.GREEN);
    }

    /**
     * Sets the current interpolator. Note that a PathInterpolator must be set via
     * {@link #setQuadraticInterpolator(float, float, ControlPointCallback)} or
     * {@link #setCubicInterpolator(float, float, float, float, ControlPointCallback)}.
     */
    public void setInterpolator(Interpolator mInterpolator) {
        mQuadraticPath = mCubicPath = false;
        mPathCallback = null;
        this.mInterpolator = mInterpolator;
        invalidate();
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    private void setupPathInterpolator() {
        if (mQuadraticPath) {//2次贝塞尔
            mInterpolator = new PathInterpolator(mCx1, mCy1);
            if (mPathCallback != null) {
                mPathCallback.onControlPoint1Moved(mCx1, mCy1);
            }
        } else if (mCubicPath) {//3次贝塞尔
            mInterpolator = new PathInterpolator(mCx1, mCy1, mCx2, mCy2);
            if (mPathCallback != null) {
                mPathCallback.onControlPoint1Moved(mCx1, mCy1);
                mPathCallback.onControlPoint2Moved(mCx2, mCy2);
            }
        }
        invalidate();
    }

    /**
     * Sets a cubic PathInterpolator, along with a callback object to update the
     * UI when these control points are moved by the user.
     */
    public void setCubicInterpolator(float cx1, float cy1, float cx2, float cy2,
                                     ControlPointCallback callback) {
        mCx1 = cx1;
        mCy1 = cy1;
        mCx2 = cx2;
        mCy2 = cy2;
        mCubicPath = true;
        mQuadraticPath = false;
        mPathCallback = callback;
        setupPathInterpolator();
    }

    /**
     * Sets a quadratic PathInterpolator, along with a callback object to update the
     * UI when the control point is moved by the user.
     */
    public void setQuadraticInterpolator(float cx1, float cy1, ControlPointCallback callback) {
        mCx1 = cx1;
        mCy1 = cy1;
        mQuadraticPath = true;
        mCubicPath = false;
        mPathCallback = callback;
        setupPathInterpolator();
    }

    /**
     * This method is called by the animation to update the current position of the
     * animated object.
     */
    public void setFraction(float fraction) {
        mFraction = fraction;
        invalidate();
    }

    /**
     * This override allows the control points for PathInterpolators to be moved by the
     * user, by dragging them into new positions.
     *
     * {@inheritDoc}
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mCubicPath && !mQuadraticPath) {
            return false;
        }
        int action = event.getAction();
        float scaledX = (event.getX() - (getWidth() * (1 - SCALE_FACTOR) / 2)) / SCALE_FACTOR;
        float scaledY = (event.getY() - (getHeight() * (1 - SCALE_FACTOR) / 2)) / SCALE_FACTOR;
        float x = Math.max(Math.min(scaledX / getWidth(), 1), 0);
        float y = Math.max(Math.min((1 - scaledY / getHeight()), 1), 0);
        boolean handled = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (Math.abs(x - mCx1) < .05f && Math.abs(y - mCy1) < .05f) {
                    mMovingC1 = true;
                    handled = true;
                } else if (mCubicPath && Math.abs(x - mCx2) < .05f && Math.abs(y - mCy2) < .05f) {
                    mMovingC2 = true;
                    handled = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMovingC1) {
                    mCx1 = x;
                    mCy1 = y;
                    if (mPathCallback != null) {
                        mPathCallback.onControlPoint1Moved(x, y);
                    }
                } else if (mMovingC2) {
                    mCx2 = x;
                    mCy2 = y;
                    if (mPathCallback != null) {
                        mPathCallback.onControlPoint2Moved(x, y);
                    }
                }
                setupPathInterpolator();
                handled = true;
                break;
            case MotionEvent.ACTION_UP:
                handled = true;
                mMovingC1 = mMovingC2 = false;
                break;
        }
        if (handled) {
            invalidate();
        }
        return handled;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getWidth();
        final int height = getHeight();

        // 绘制较小的所有内容以在 1x1 网格之外绘制曲线和动画对象
        canvas.translate(width * (1 - SCALE_FACTOR) / 2, height * (1 - SCALE_FACTOR) / 2);
        canvas.scale(SCALE_FACTOR, SCALE_FACTOR);

        // 绘制 水平/垂直 线
        for (int i = 0; i <= 100; i += 10) {
            float percentage = (float) i / 100;
            float x = percentage * width;
            float y = percentage * height;
            canvas.drawLine(0, y, width, y, mGridPaint);
            canvas.drawLine(x, 0, x, height, mGridPaint);
        }

        // 绘制插值器的时序曲线（x轴：时间完成度  y轴：动画完成度）动画值随时间变化曲线
        float lastX = 0;
        float lastY = mInterpolator.getInterpolation(0);
        for (float x = 0; x <= 1f; x += .01f) {
            //获取插值分数
            float y = mInterpolator.getInterpolation(x);
            canvas.drawLine(lastX * width, height - (height * lastY),
                    width * x, height - (height * y), mCurvePaint);
            lastX = x;
            lastY = y;
        }

        // 当设置了PathInterpolator时，为路径绘制控制点
        if (mCubicPath || mQuadraticPath) {
            float cx1Pixels = getWidth() * mCx1;
            float cy1Pixels = getHeight() * (1 - mCy1);
            canvas.drawCircle(cx1Pixels, cy1Pixels, RADIUS * 1.4f, mControlPointPaint);
            if (mCubicPath) {
                float cx2Pixels = getWidth() * mCx2;
                float cy2Pixels = getHeight() * (1 - mCy2);
                canvas.drawCircle(cx2Pixels, cy2Pixels, RADIUS * 1.4f, mControlPointPaint);
            }
        }

        // 绘制球的位置 横坐标动画分数（时间线0-1）  纵坐标动画的插值分数（动画实际完成度）
        float fractionY = mInterpolator.getInterpolation(mFraction);
        float centerX = getWidth() * mFraction;
        float centerY = getHeight() * (1 - fractionY);
        canvas.drawCircle(centerX, centerY, RADIUS, mBallPaint);
    }
}
