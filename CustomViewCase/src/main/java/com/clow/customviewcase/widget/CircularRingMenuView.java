package com.clow.customviewcase.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.clow.customviewcase.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clow
 * Des: 圆形弹出菜单选择
 * Date: 2025/11/18.
 */
public class CircularRingMenuView extends View {

    private  static final int ANIMATION_DURATION = 300;

    private  float ringInnerRadius = 0;
    private  float ringOuterRadius = 0;
    private  float centerRadius = 80;//中心圆半径
    private  int centerColor = 0xff2196F3;//中心圆颜色
    private  float ringSize = 200;//圆环大小
    private  float space = 20;//中心圆和圆环间距
    private  float spaceAngle = 1;//menuItems 之间的分割角度
    private  int selectedColor = 0xffFF9800; //选中圆环颜色
    private  int labelTextColor = 0xffffffff; //文字颜色
    private  float labelTextSize = 36f; //文字大小


    private Paint centerButtonPaint;
    private Paint ringPaint;
    private Paint selectedRingPaint;
    private Paint textPaint;

    private float centerX, centerY;
    private boolean isMenuExpanded = false;
    private int selectedMenuItem = -1;

    private List<MenuItem> menuItems = new ArrayList<>();
    private ValueAnimator expandAnimator;
    private float animationProgress = 0f;

    private OnMenuItemClickListener listener;

    public interface OnMenuItemClickListener {
        void onMenuItemClick(int position);
    }

    public static class MenuItem {
        String text;
        int color;
        float startAngle;
        float sweepAngle;
        RectF ringBounds;

        public MenuItem(String text, int color) {
            this.text = text;
            this.color = color;
            this.ringBounds = new RectF();
        }
    }

    public CircularRingMenuView(Context context) {
        this(context, null);
    }

    public CircularRingMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircularRingMenuView);
        centerRadius = ta.getDimension(R.styleable.CircularRingMenuView_centerRadius, centerRadius);
        centerColor = ta.getColor(R.styleable.CircularRingMenuView_centerColor,centerColor);
        ringSize = ta.getDimension(R.styleable.CircularRingMenuView_ringSize, ringSize);
        space = ta.getDimension(R.styleable.CircularRingMenuView_space,space);
        labelTextSize = ta.getDimension(R.styleable.CircularRingMenuView_labelTextSize,labelTextSize);
        labelTextColor = ta.getColor(R.styleable.CircularRingMenuView_labelTextSize,labelTextColor);
        selectedColor = ta.getColor(R.styleable.CircularRingMenuView_selectedColor,selectedColor);
        ta.recycle();

        // 中心按钮画笔
        centerButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerButtonPaint.setColor(centerColor);
        centerButtonPaint.setStyle(Paint.Style.FILL);

        // 环形菜单项画笔
        ringInnerRadius = centerRadius + space;
        ringOuterRadius = ringInnerRadius + ringSize;
        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(ringSize);

        // 选中状态环形画笔
        selectedRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedRingPaint.setStyle(Paint.Style.STROKE);
        selectedRingPaint.setStrokeWidth(ringSize);
        selectedRingPaint.setColor(selectedColor);

        // 文字画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(labelTextColor);
        textPaint.setTextSize(labelTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);


//        // 初始化菜单项
//        menuItems = new ArrayList<>();
//        menuItems.add(new MenuItem("A", Color.parseColor("#F44336")));
//        menuItems.add(new MenuItem("B", Color.parseColor("#4CAF50")));
//        menuItems.add(new MenuItem("C", Color.parseColor("#2196F3")));
//        menuItems.add(new MenuItem("D", Color.parseColor("#FFC107")));
//        menuItems.add(new MenuItem("E", Color.parseColor("#33C107")));

//        // 设置每个菜单项的起始角度和扫过的角度
//        float anglePerItem = 360f / menuItems.size();
//        for (int i = 0; i < menuItems.size(); i++) {
//            MenuItem item = menuItems.get(i);
//            item.startAngle = i * anglePerItem;
//            item.sweepAngle = anglePerItem - 1; // 留出1度的间隙
//        }

        setupAnimations();
    }

    private void setupAnimations() {
        expandAnimator = ValueAnimator.ofFloat(0f, 1f);
        expandAnimator.setDuration(ANIMATION_DURATION);
        expandAnimator.setInterpolator(new OvershootInterpolator());
        expandAnimator.addUpdateListener(animation -> {
            animationProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            //如果没有设置准确尺寸  指定尺寸
            setMeasuredDimension((int) ringOuterRadius*2, (int) ringOuterRadius*2);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f;
        centerY = h / 2f;
        updateRingBounds();
    }

    private void updateRingBounds() {
        float currentInnerRadius = ringInnerRadius * animationProgress;
        float currentOuterRadius = ringOuterRadius * animationProgress;
        float stoke = (ringOuterRadius - ringInnerRadius)/2;

        for (MenuItem item : menuItems) {
            item.ringBounds.set(
                    centerX - currentOuterRadius+stoke,
                    centerY - currentOuterRadius+stoke,
                    centerX + currentOuterRadius - stoke,
                    centerY + currentOuterRadius -stoke
            );
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制中心按钮
        canvas.drawCircle(centerX, centerY, centerRadius, centerButtonPaint);

        // 绘制环形菜单项
        if (isMenuExpanded || animationProgress > 0) {
            updateRingBounds();

            for (int i = 0; i < menuItems.size(); i++) {
                MenuItem item = menuItems.get(i);
                Paint paint;
                // 根据选中状态选择画笔
                if ((i == selectedMenuItem)) {
                    paint = selectedRingPaint;
                } else {
                    paint = ringPaint;
                    paint.setColor(item.color);
                }
                // 绘制环形扇形
                canvas.drawArc(
                        item.ringBounds,
                        item.startAngle,
                        item.sweepAngle,
                        false, // 不连接到中心点
                        paint
                );

                // 绘制文字（计算文字在环形中间位置）
                drawTextOnArc(canvas, item, i);
            }
        }
    }

    private void drawTextOnArc(Canvas canvas, MenuItem item, int position) {
        // 计算文字在环形中间位置的角度
        float middleAngle = item.startAngle + item.sweepAngle / 2;
        float textRadius = (ringInnerRadius + ringOuterRadius) / 2 * animationProgress;

        // 将角度转换为弧度
        double angleRad = Math.toRadians(middleAngle);

        // 计算文字位置
        float textX = centerX + textRadius * (float) Math.cos(angleRad);
        float textY = centerY + textRadius * (float) Math.sin(angleRad);

        // 绘制文字
        canvas.drawText(item.text, textX, textY + 12, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInCenterButton(x, y)) {
                    startLongPressCheck();
                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isMenuExpanded) {
                    checkMenuItemSelection(x, y);
                } else if (isLongPressTriggered && !isMenuExpanded) {
                    expandMenu();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                cancelLongPressCheck();
                if (isMenuExpanded) {
                    if (selectedMenuItem != -1 && listener != null) {
                        listener.onMenuItemClick(selectedMenuItem);
                    }
                    collapseMenu();
                }
                break;
        }

        return true;
    }

    private boolean isInCenterButton(float x, float y) {
        float distance = (float) Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        return distance <= centerRadius;
    }

    private void checkMenuItemSelection(float x, float y) {
        int previousSelection = selectedMenuItem;
        selectedMenuItem = -1;

        // 计算触摸点相对于中心的角度
        float touchAngle = (float) Math.toDegrees(Math.atan2(y - centerY, x - centerX));
        if (touchAngle < 0) {
            touchAngle += 360;
        }

        // 计算触摸点距离中心的距离
        float touchDistance = (float) Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

        // 检查触摸点是否在环形区域内
        float currentInnerRadius = ringInnerRadius * animationProgress;
        float currentOuterRadius = ringOuterRadius * animationProgress;

        if (touchDistance >= currentInnerRadius && touchDistance <= currentOuterRadius) {
            // 检查触摸点落在哪个菜单项的角度范围内
            for (int i = 0; i < menuItems.size(); i++) {
                MenuItem item = menuItems.get(i);
                float endAngle = item.startAngle + item.sweepAngle;

                if (isAngleInRange(touchAngle, item.startAngle, endAngle)) {
                    selectedMenuItem = i;
                    break;
                }
            }
        }

        if (previousSelection != selectedMenuItem) {
            invalidate();
        }
    }

    private boolean isAngleInRange(float angle, float start, float end) {
        if (end > 360) {
            // 处理跨360度的情况
            return angle >= start || angle <= end - 360;
        }
        return angle >= start && angle <= end;
    }

    private boolean isLongPressTriggered = false;
    private Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            isLongPressTriggered = true;
            if (!isMenuExpanded) {
                expandMenu();
            }
        }
    };

    private void startLongPressCheck() {
        isLongPressTriggered = false;
        postDelayed(longPressRunnable, 500);
    }

    private void cancelLongPressCheck() {
        removeCallbacks(longPressRunnable);
        isLongPressTriggered = false;
    }

    public void expandMenu() {
        if (!isMenuExpanded) {
            isMenuExpanded = true;
            expandAnimator.start();
        }
    }

    public void collapseMenu() {
        if (isMenuExpanded) {
            isMenuExpanded = false;
            selectedMenuItem = -1;
            expandAnimator.reverse();
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    public void setMenuItems(List<String> texts, List<Integer> colors) {
        menuItems.clear();
        for (int i = 0; i < Math.min(texts.size(), colors.size()); i++) {
            menuItems.add(new MenuItem(texts.get(i), colors.get(i)));
        }

        // 重新计算角度
        float anglePerItem = 360f / menuItems.size();
        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem item = menuItems.get(i);
            item.startAngle = i * anglePerItem;
            item.sweepAngle = anglePerItem - spaceAngle;
        }

        invalidate();
    }
}
