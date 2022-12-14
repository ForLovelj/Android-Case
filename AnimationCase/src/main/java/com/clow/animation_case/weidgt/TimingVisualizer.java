package com.clow.animation_case.weidgt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 这个视图显示两个对象在动画运行时向右和向下滑动。
 * setFraction()方法由动画调用，它导致视图无效，并将对象绘制到新的位置。
 */
public class TimingVisualizer extends View {

   float mFraction = 0;
   Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
   private static float RADIUS = 30;
   private static float mBallTop, mBallBottom, mBallLeft, mBallRight, mBallWidth, mBallHeight;

   public TimingVisualizer(Context context, AttributeSet attrs) {
      super(context, attrs);
      init();
   }

   private void init() {
      mPaint.setColor(Color.RED);
      mPaint.setStyle(Paint.Style.FILL);
   }

   public void setFraction(float fraction) {
      mFraction = fraction;
      invalidate();
   }

   @Override
   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
      mBallTop = RADIUS;
      mBallLeft = RADIUS;
      mBallRight = getWidth() - RADIUS;
      mBallBottom = getHeight() - RADIUS;
      mBallWidth = mBallRight - mBallLeft;
      mBallHeight = mBallBottom - mBallTop;
   }

   @Override
   protected void onDraw(Canvas canvas) {
      final int width = getWidth();
      final int height = getHeight();

      // Draw everything smaller to account for drawing the curve and animated object
      // outside the 1x1 grid
      canvas.translate(width * (1 - CurveVisualizer.SCALE_FACTOR) / 2,
              height * (1 - CurveVisualizer.SCALE_FACTOR) / 2);
      canvas.scale(CurveVisualizer.SCALE_FACTOR, CurveVisualizer.SCALE_FACTOR);

      canvas.drawCircle(mBallLeft, mBallTop + mFraction * mBallHeight, RADIUS, mPaint);
      canvas.drawCircle(mBallLeft + mFraction * mBallWidth, mBallTop, RADIUS, mPaint);
   }
}
