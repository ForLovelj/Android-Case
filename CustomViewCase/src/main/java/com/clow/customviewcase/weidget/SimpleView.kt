package com.clow.customviewcase.weidget

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.clow.customviewcase.R

/**
 * Created by clow
 * Des:
 * Date: 2022/12/21.
 */
class SimpleView : View {

    private val mPaint = Paint().apply {
        style = Paint.Style.STROKE  //设置绘制模式
        color = Color.BLACK //设置颜色
        strokeWidth = 4f    //设置线条宽度
        textSize = 18f      //设置文字大小
        isAntiAlias = true  //抗锯齿开关
    }

    private val mTextPaint = Paint().apply {
        style = Paint.Style.FILL  //设置绘制模式
        color = Color.BLACK //设置颜色
        textSize = 40f      //设置文字大小
        isAntiAlias = true  //抗锯齿开关
        textAlign = Paint.Align.LEFT //设置文字对齐方式
//        isUnderlineText = true //设置文本的下划线
//        isStrikeThruText = true //设置文本的删除线
//        isFakeBoldText = true   //设置文本粗体
//        textSkewX = -0.5F //设置斜体
//        setShadowLayer(5F,5F,5F,Color.BLUE) //设置文字阴影
    }

    private var mType = -1

    constructor(context: Context) :
            this(context, null)

    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

    }

    fun drawForType(type: Int) {
        mType = type
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.save()
        when (mType) {
            0 -> drawAxis(canvas!!)
            1 -> translateCanvas(canvas!!)
            2 -> scaleCanvas(canvas!!)
            3 -> rotateCanvas(canvas!!)
            4 -> skewCanvas(canvas!!)
            5 -> drawColor(canvas!!)
            6 -> drawPoint(canvas!!)
            7 -> drawLine(canvas!!)
            8 -> drawRect(canvas!!)
            9 -> drawRoundRect(canvas!!)
            10 -> drawOval(canvas!!)
            11 -> drawCircle(canvas!!)
            12 -> drawArc(canvas!!)
            13 -> drawBitmap(canvas!!)
            14 -> drawPicture(canvas!!)
            15 -> drawText(canvas!!)
            16 -> drawTextRun(canvas!!)
            17 -> drawTextOnPath(canvas!!)
            18 -> drawPath(canvas!!)
        }
        canvas?.restore()

    }

    /**
     * 绘制坐标系
     */
    private fun drawAxis(canvas: Canvas) {
        val canvasWidth = canvas.width.toFloat()
        val canvasHeight = canvas.height.toFloat()
        mPaint.setColor(Color.GREEN)
        mPaint.strokeWidth = 8f
        //第一次绘制坐标轴(默认情况下的绘图坐标系)
        canvas.drawLine(0f, 0f, canvasWidth, 0f, mPaint)//绘制x轴
        canvas.drawLine(0f, 0f, 0f, canvasHeight, mPaint)//绘制Y轴
        //绘制一个矩形
        mPaint.setColor(Color.BLUE)
        val rect = RectF(100F, 100F, 200F, 200F)
        canvas.drawRect(rect, mPaint)

        //对坐标系平移后，第二次绘制坐标轴
        canvas.translate(300f, 300f)
        mPaint.setColor(Color.RED)
        canvas.drawLine(0f, 0f, canvasWidth, 0f, mPaint)//绘制x轴
        canvas.drawLine(0f, 0f, 0f, canvasHeight, mPaint)//绘制Y轴
        //绘制一个矩形
        mPaint.setColor(Color.BLUE)
        canvas.drawRect(rect, mPaint)

        //对坐标系旋转，第三次绘制坐标轴
        canvas.rotate(30f)
        mPaint.setColor(Color.BLACK)
        canvas.drawLine(0f, 0f, canvasWidth, 0f, mPaint)//绘制x轴
        canvas.drawLine(0f, 0f, 0f, canvasHeight, mPaint)//绘制Y轴
        //绘制一个矩形
        mPaint.setColor(Color.BLUE)
        canvas.drawRect(rect, mPaint)
    }

    /**
     * 画布平移
     */
    private fun translateCanvas(canvas: Canvas) {
        // 在坐标原点绘制一个黑色圆形
        mPaint.style = Paint.Style.FILL
        mPaint.setColor(Color.BLACK)
        canvas.translate(200f, 200f)
        canvas.drawCircle(0f, 0f, 100f, mPaint)

        // 在坐标原点绘制一个蓝色圆形
        mPaint.setColor(Color.BLUE)
        canvas.translate(200f, 200f)
        canvas.drawCircle(0f, 0f, 100f, mPaint)
    }

    /**
     * 画布缩放
     */
    private fun scaleCanvas(canvas: Canvas) {
        val canvasWidth = canvas.width.toFloat()
        val canvasHeight = canvas.height.toFloat()
        //画布原点平移到中心
        canvas.translate(canvasWidth / 2, canvasHeight / 2)
        //绘制坐标系
        mPaint.style = Paint.Style.STROKE
        mPaint.setColor(Color.RED)
        mPaint.strokeWidth = 4f
        canvas.drawLine(-canvasWidth / 2, 0f, canvasWidth / 2, 0f, mPaint)//x轴
        canvas.drawLine(0f, -canvasHeight / 2, 0f, canvasHeight / 2, mPaint)//y轴

        val rect = RectF(0F, 0F, 400F, 400F)    // 矩形区域
        mPaint.setColor(Color.BLACK)
        canvas.drawRect(rect, mPaint)       //绘制黑色矩形

        canvas.scale(0.5f, 0.5f)      // 画布缩放
//        canvas.scale(0.5f,0.5f,200f,0f)   // 画布缩放  <-- 缩放中心向右偏移了200个单位
//        canvas.scale(-0.5f,-0.5f)         // 画布缩放
//        canvas.scale(-0.5f,-0.5f,200f,0f)  // 画布缩放  <-- 缩放中心向右偏移了200个单位

        mPaint.setColor(Color.BLUE)
        canvas.drawRect(rect, mPaint)      //再绘制蓝色矩形

    }

    /**
     * 画布缩放demo
     */
    private fun scaleDemo(canvas: Canvas) {
        val canvasWidth = canvas.width.toFloat()
        val canvasHeight = canvas.height.toFloat()
        //画布原点平移到中心
        canvas.translate(canvasWidth / 2, canvasHeight / 2)
        mPaint.style = Paint.Style.STROKE
        mPaint.setColor(Color.BLACK)
        mPaint.strokeWidth = 20f
        val rect = RectF(-400f, -400f, 400f, 400f) // 矩形区域

        for (i in 0..20) {
            canvas.scale(0.9f, 0.9f)
            canvas.drawRect(rect, mPaint)
        }
    }

    /**
     * 画布旋转
     */
    private fun rotateCanvas(canvas: Canvas) {
        val canvasWidth = canvas.width.toFloat()
        val canvasHeight = canvas.height.toFloat()
        //画布原点平移到中心
        canvas.translate(canvasWidth / 2, canvasHeight / 2)
        //绘制坐标系
        mPaint.style = Paint.Style.STROKE
        mPaint.setColor(Color.RED)
        mPaint.strokeWidth = 4f
        canvas.drawLine(-canvasWidth / 2, 0f, canvasWidth / 2, 0f, mPaint)//x轴
        canvas.drawLine(0f, -canvasHeight / 2, 0f, canvasHeight / 2, mPaint)//y轴

        val rect = RectF(0F, 0F, 200F, 200F)
        mPaint.setColor(Color.BLACK)
        canvas.drawRect(rect, mPaint)   //绘制黑色矩形
        //顺时针旋转45°
        canvas.rotate(45f)
//        canvas.rotate(45f,100f,0f)  // 旋转45度 <-- 旋转中心向右偏移100个单位

        //绘制旋转后的绿色坐标轴
        mPaint.setColor(Color.GREEN)
        canvas.drawLine(-canvasWidth / 2, 0f, canvasWidth / 2, 0f, mPaint)//x轴
        canvas.drawLine(0f, -canvasHeight / 2, 0f, canvasHeight / 2, mPaint)//y轴

        mPaint.setColor(Color.BLUE)
        canvas.drawRect(rect, mPaint)   //绘制蓝色矩形
    }

    /**
     * 画布旋转Demo
     */
    private fun rotateDemo(canvas: Canvas) {
        val canvasWidth = canvas.width.toFloat()
        val canvasHeight = canvas.height.toFloat()
        //画布原点平移到中心
        canvas.translate(canvasWidth / 2, canvasHeight / 2)
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 10f
        mPaint.setColor(Color.BLACK)
        canvas.drawCircle(0f, 0f, 400f, mPaint) //绘制表盘

        mPaint.strokeWidth = 4f
        val textPaint = TextPaint().apply {
            textSize = 18f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        for (i in 0..360 step 30) {
            canvas.drawLine(0f, -380f, 0f, -400f, mPaint)   //绘制刻度
            if (i != 0) {
                canvas.save()
                canvas.rotate(-i.toFloat(), 0f, -360f)    //逆时针旋转数字对应的角度 使数字全部正向
                canvas.drawText("${i / 30}", 0f, -360f, textPaint)   //绘制数字
                canvas.restore()
            }

            canvas.rotate(30f)
        }
    }

    /**
     * 画布错切
     */
    private fun skewCanvas(canvas: Canvas) {
        val canvasWidth = canvas.width.toFloat()
        val canvasHeight = canvas.height.toFloat()
        //画布原点平移到中心
        canvas.translate(canvasWidth / 2, canvasHeight / 2)
        //绘制坐标系
        mPaint.style = Paint.Style.STROKE
        mPaint.setColor(Color.RED)
        mPaint.strokeWidth = 4f
        canvas.drawLine(-canvasWidth / 2, 0f, canvasWidth / 2, 0f, mPaint)//x轴
        canvas.drawLine(0f, -canvasHeight / 2, 0f, canvasHeight / 2, mPaint)//y轴

        val rect = RectF(0F, 0F, 200F, 200F)
        mPaint.setColor(Color.BLACK)
        canvas.drawRect(rect, mPaint)   //绘制黑色矩形
        //画布往X方向倾斜45度
        canvas.skew(1f, 0f)  // 水平错切 <- 45度

        mPaint.setColor(Color.BLUE)
        canvas.drawRect(rect, mPaint)   //绘制蓝色矩形
    }

    /**
     * 绘制颜色
     */
    private fun drawColor(canvas: Canvas) {
        canvas.drawColor(Color.BLUE)    //绘制蓝色
    }

    /**
     * 绘制点
     */
    private fun drawPoint(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL //设置填充模式
        mPaint.color = Color.BLACK  //设置画笔颜色
        mPaint.strokeWidth = 20f    //设置画笔宽度为20px

        canvas.drawPoint(200f, 200f, mPaint)//在坐标(200,200)位置绘制一个点

        mPaint.strokeCap = Paint.Cap.ROUND  //绘制圆点

        canvas.drawPoints(
            //绘制一组点，坐标位置由float数组指定
            floatArrayOf(
                500f, 500f,
                500f, 600f,
                500f, 700f
            ), mPaint
        )

        canvas.drawPoints(
            //绘制一组点，坐标位置由float数组指定
            floatArrayOf(
                500f, 500f,
                500f, 600f,
                500f, 700f
            ), mPaint
        )

        mPaint.color = Color.RED
        canvas.drawPoints(
            //绘制一组点，坐标位置由float数组指定
            floatArrayOf(
                600f, 500f,
                600f, 600f,
                600f, 700f
            ),
            2,//跳过前两个数即600,500
            4,//一共绘制4个数（2个点）
            mPaint
        )
    }

    /**
     * 绘制线
     */
    private fun drawLine(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL //设置填充模式
        mPaint.color = Color.BLACK  //设置画笔颜色
        mPaint.strokeWidth = 20f    //设置画笔宽度为20px

        //以(400,400)为起点，(700,700)为终点绘制一条直线
        canvas.drawLine(400f, 400f, 700f, 700f, mPaint)

        //批量绘制
        canvas.drawLines(
            floatArrayOf(
                100f, 800f, 400f, 800f,
                100f, 900f, 400f, 900f
            ), mPaint
        )

        mPaint.color = Color.RED    //画笔设置为红色
        mPaint.strokeCap = Paint.Cap.ROUND  //端点设置为圆头
        canvas.drawLines(
            floatArrayOf(
                100f, 800f, 400f, 800f,
                100f, 900f, 400f, 900f,
                100f, 1000f, 400f, 1000f,
            ),
            8,//跳过8个数
            4,//绘制4个数（一条线）
            mPaint
        )
    }

    /**
     * 绘制矩形
     */
    private fun drawRect(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL //设置填充模式
        mPaint.color = Color.BLACK  //设置画笔颜色
        mPaint.strokeWidth = 20f    //设置画笔宽度为20px

        //第一种
        canvas.drawRect(100f, 100f, 600f, 400f, mPaint)

        //第二种
        canvas.drawRect(
            Rect(100, 500, 600, 800),
            mPaint
        )

        //第三种
        canvas.drawRect(
            RectF(100f, 900f, 600f, 1200f),
            mPaint
        )

        mPaint.style = Paint.Style.STROKE //设置描边模式
        canvas.drawRect(100f, 1300f, 600f, 1600f, mPaint)
    }

    /**
     * 绘制圆角矩形
     */
    private fun drawRoundRect(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL //设置填充模式
        mPaint.color = Color.BLACK  //设置画笔颜色
        mPaint.strokeWidth = 20f    //设置画笔宽度为20px

        //第一种 left, top, right, bottom 是四条边的坐标，rx 和 ry 是圆角的横向半径和纵向半径
        canvas.drawRoundRect(100f, 100f, 600f, 400f, 30f, 30f, mPaint)

        //第二种
        canvas.drawRoundRect(
            RectF(100f, 500f, 600f, 800f),
            30f, 30f, mPaint
        )

        mPaint.style = Paint.Style.STROKE //设置描边模式
        canvas.drawRoundRect(100f, 900f, 600f, 1200f, 30f, 30f, mPaint)
    }

    /**
     * 绘制椭圆
     */
    private fun drawOval(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL //设置填充模式
        mPaint.color = Color.BLACK  //设置画笔颜色
        mPaint.strokeWidth = 20f    //设置画笔宽度为20px

        //第一种 left, top, right, bottom 是这个椭圆的左、上、右、下四个边界点的坐标
        canvas.drawOval(100f, 100f, 600f, 400f, mPaint)

        //第二种
        canvas.drawOval(
            RectF(100f, 500f, 600f, 800f),
            mPaint
        )

        mPaint.style = Paint.Style.STROKE //设置描边模式
        canvas.drawOval(100f, 900f, 600f, 1200f, mPaint)
    }

    /**
     * 绘制圆
     */
    private fun drawCircle(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL //设置填充模式
        mPaint.color = Color.BLACK  //设置画笔颜色
        mPaint.strokeWidth = 20f    //设置画笔宽度为20px

        // 绘制一个圆心坐标在(500,500)，半径为300 的圆。
        canvas.drawCircle(500f, 500f, 300f, mPaint)

        mPaint.style = Paint.Style.STROKE //设置描边模式
        //画笔为描边模式绘制出来的是一个圆环
        canvas.drawCircle(500f, 1200f, 300f, mPaint)
    }

    /**
     * 绘制圆弧
     */
    private fun drawArc(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL //设置填充模式
        mPaint.color = Color.BLACK  //设置画笔颜色
        mPaint.strokeWidth = 20f    //设置画笔宽度为20px

        canvas.drawArc(200f, 100f, 800f, 500f, -90f, 90f, true, mPaint) // 绘制扇形
        canvas.drawArc(200f, 100f, 800f, 500f, 20f, 90f, false, mPaint) // 绘制弧形

        mPaint.style = Paint.Style.STROKE // 描边模式
        canvas.drawArc(200f, 100f, 800f, 500f, 180f, 60f, false, mPaint) // 绘制不封口的弧形
    }

    /**
     * 绘制图片
     */
    private fun drawBitmap(canvas: Canvas) {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.beauty)
        //第一种
        canvas.drawBitmap(bitmap, Matrix(), mPaint)

        //第二种
//        canvas.drawBitmap(bitmap,200f,200f,mPaint)

        //第三种
//        val src = Rect(0,0,bitmap.width,bitmap.height/2)    //指定图片绘制区域(图片上半部分)
//        val dst = Rect(200,200,200+bitmap.width,200+bitmap.height/2)    //指定图片在屏幕上显示的区域
//        canvas.drawBitmap(bitmap,src,dst,mPaint)    //绘制Bitmap
    }

    private val boomBitmap = BitmapFactory.decodeResource(resources, R.mipmap.boom)
    private var level = 0   //最开始处于爆炸的第0片段
    private var frame = 0

    /**
     * 通过drawBitmap绘制爆炸效果
     */
    private fun drawBitmapDemo(canvas: Canvas) {
        frame++
        if (level >= 14) return
        //每两帧绘制一次爆炸
        if (frame % 2 == 0) {
            //爆炸效果由14个片段组成 算出每个片段的宽度
            val segmentWidth = boomBitmap.width / 14
            val left = level * segmentWidth

            val src = Rect(left, 0, left + segmentWidth, boomBitmap.height)
            val dst = Rect(300, 300, 300 + segmentWidth, 300 + boomBitmap.height)
            canvas.drawBitmap(boomBitmap, src, dst, mPaint)
            level++
        }
        invalidate()
    }

    /**
     * 返回一个录制了内容的Picture
     */
    private fun recording(): Picture {
        // 创建Picture对象
        val picture = Picture()
        // 开始录制 (接收返回值Canvas)
        val canvas = picture.beginRecording(500, 500)

        // 创建一个画笔
        // 创建一个画笔
        val paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLUE
        }

        // 在Canvas中具体操作
        // 位移
//        canvas.translate(250f,250f)
        // 绘制一个圆
        canvas.drawCircle(0f, 0f, 100f, paint)
        //结束录制
        picture.endRecording()
        return picture
    }

    /**
     *  drawPicture
     */
    private fun drawPicture(canvas: Canvas) {
        val picture = recording()
        //1. 直接绘制
        canvas.translate(250f, 250f)
        canvas.drawPicture(picture)

        //2. 绘制到目标矩形  可以看到绘制内容根据矩形区域被放大了一倍
        canvas.translate(250f, 250f)
        canvas.drawPicture(
            picture,
            RectF(0f, 0f, picture.width.toFloat() * 2, picture.width.toFloat() * 2)
        )
    }

    /**
     * drawText
     */
    private fun drawText(canvas: Canvas) {
        // 文本(要绘制的内容)
        val str = "abcdefghijk"
        mTextPaint.textSize = 40f
        // 参数分别为 (文本 基线x 基线y 画笔)
        canvas.drawText(str, 200f, 200f, mTextPaint)

        mPaint.style = Paint.Style.FILL //设置填充模式
        mPaint.color = Color.RED  //设置画笔颜色
        canvas.drawLine(200f, 200f, 800f, 200f, mPaint)  //绘制基线

        mTextPaint.textSize = 60f
        // 参数分别为 (字符串 开始截取位置 结束截取位置 基线x 基线y 画笔)
        canvas.drawText(str, 1, 7, 200f, 400f, mTextPaint)
        canvas.drawLine(200f, 400f, 800f, 400f, mPaint)  //绘制基线

        // 字符数组(要绘制的内容)
        val chars = "abcdefghijk".toCharArray()
        mTextPaint.textSize = 80f
        // 参数为 (字符数组 起始坐标 截取长度 基线x 基线y 画笔)
        canvas.drawText(chars, 1, 7, 200f, 600f, mTextPaint)
        canvas.drawLine(200f, 600f, 800f, 600f, mPaint)  //绘制基线
    }

    /**
     * drawTextRun
     */
    private fun drawTextRun(canvas: Canvas) {
        val str = "ABCDEFG"
        // text：要绘制的文字
        // start：从那个字开始绘制
        // end：绘制到哪个字结束
        // contextStart：上下文的起始位置。
        // contextStart 需要小于等于 start
        // contextEnd：上下文的结束位置。contextEnd 需要大于等于 end
        // x：文字左边的坐标
        // y：文字的基线坐标
        // isRtl：是否是 RTL（Right-To-Left，从右向左）
        canvas.drawTextRun(str, 2, 4, 0, 6, 200f, 200f, false, mTextPaint)

        canvas.drawTextRun(str, 2, 4, 0, 6, 200f, 400f, true, mTextPaint)
    }

    /**
     * drawTextOnPath
     */
    private fun drawTextOnPath(canvas: Canvas) {
        //创建path对象
        val path = Path().apply {
            //移动到点(200,200)
            moveTo(200f, 200f)
            //设置path轨迹 3次贝塞尔曲线
            cubicTo(300f, 500f, 400f, 300f, 500f, 500f)
        }
        mPaint.color = Color.RED
        //绘制path
        canvas.drawPath(path, mPaint)
        //根据path绘制文本 其中hOffset和vOffset它们是文字相对于 Path 的水平偏移量和竖直偏移量
        canvas.drawTextOnPath("在path上绘制文本", path, 50f, 0f, mTextPaint)
    }

    /**
     * 绘制路径
     */
    private fun drawPath(canvas: Canvas) {
        val canvasWidth = canvas.width.toFloat()
        val canvasHeight = canvas.height.toFloat()
        //画布原点平移到中心
        canvas.translate(canvasWidth / 2, canvasHeight / 2)
        //绘制坐标系
        mPaint.style = Paint.Style.STROKE
        mPaint.setColor(Color.RED)
        mPaint.strokeWidth = 4f
//        canvas.drawLine(-canvasWidth / 2, 0f, canvasWidth / 2, 0f, mPaint)//x轴
//        canvas.drawLine(0f, -canvasHeight / 2, 0f, canvasHeight / 2, mPaint)//y轴

        mPaint.color = Color.BLACK
        //1类方法.直接描述路径

        //lineTo rLineTo
//        val path = Path().apply {
//            lineTo(200f,200f)   //从原点连线到点(200,200)
//            lineTo(200f,0f)     //从点(200,200)连线到点(200,0)
//
//            rLineTo(200f,200f)//以点(200,0)为原点 连线到点(200,200)
//        }

        //moveTo rMoveTo
//        val path = Path().apply {
//            lineTo(200f,200f)   //从原点连线到点(200,200)
//
//            moveTo(200f,-0f)  //移动到点(200,0)
//
//            lineTo(200f,200f)     //从点(200,200)连线到点(200,0)
//
//            rMoveTo(200f,0f)  //以点(200,200)为原点 移动到点(200,0)
//
//            rLineTo(0f,-200f)       //以点(200,0)为原点 连线到点(0,-200)
//        }

        //绘制曲线
//        val path = Path().apply {
//            //从原点绘制贝塞尔曲线，前后2个坐标分别是控制点，和终点
//            quadTo(100f,100f,200f,0f)
//            //以终点为相对原点，再绘制
//            rQuadTo(100f,-100f,200f,0f)
//        }

        //绘制圆弧
//        val path = Path().apply {
//            lineTo(100f, 100f)
//            arcTo(
//                RectF(100f, 100f, 300f, 300f),
//                -90f, 90f, true
//            ) // 强制移动到弧形起点（无痕迹）
//
////            addArc(RectF(100f, 100f, 300f, 300f), -90f, 90f)// <-- 和上面一句作用等价
//        }

        // 闭合路径 close
//        val path = Path().apply {
//            lineTo(200f,200f)   //从原点连线到点(200,200)
//            lineTo(200f,0f)     //从点(200,200)连线到点(200,0)
//
//            close() //闭合路径
//        }

//        val path = Path().apply {
//            addRect(-200f,-200f,200f,200f,Path.Direction.CCW) //添加一个矩形
//            setLastPoint(-300f,300f) //更改最后一个点位置
//        }
//        canvas.drawPath(path, mPaint)

        //2类方法.辅助的设置或计算

//        testSetFillType(canvas)
//        testPathOp(canvas)
        testComputeBounds(canvas)
    }

    /**
     * 测试Path填充类型
     */
    private fun testSetFillType(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL //设置画笔为填充模式
        val path = Path().apply {
//            fillType = Path.FillType.EVEN_ODD//设置填充类型为奇偶规则
            fillType = Path.FillType.WINDING//设置填充类型为非零环绕规则

            //画一个五角星
            moveTo(0f,-130f)

            lineTo(-70f,110f)
            lineTo(110f,-30f)
            lineTo(-110f,-30f)
            lineTo(70f,110f)
            lineTo(0f,-130f)
        }
        canvas.drawPath(path, mPaint)
    }

    /**
     * 测试Path布尔运算
     */
    private fun testPathOp(canvas: Canvas) {
        val x = 80f
        val r = 100f
        val pathOpResult  = Path()
        val path1 = Path()
        val path2 = Path()

        mPaint.style = Paint.Style.FILL
        canvas.translate(250f,0f)

        path1.addCircle(-x, 0f, r, Path.Direction.CW)
        path2.addCircle(x, 0f, r, Path.Direction.CW)

        pathOpResult.op(path1,path2, Path.Op.DIFFERENCE)
        canvas.translate(0f, 200f)
        canvas.drawText("DIFFERENCE", 240f,0f,mTextPaint)
        canvas.drawPath(pathOpResult,mPaint)

        pathOpResult.op(path1,path2, Path.Op.REVERSE_DIFFERENCE)
        canvas.translate(0f, 300f)
        canvas.drawText("REVERSE_DIFFERENCE", 240f,0f,mTextPaint)
        canvas.drawPath(pathOpResult,mPaint)

        pathOpResult.op(path1,path2, Path.Op.INTERSECT)
        canvas.translate(0f, 300f)
        canvas.drawText("INTERSECT", 240f,0f,mTextPaint)
        canvas.drawPath(pathOpResult,mPaint)

        pathOpResult.op(path1,path2, Path.Op.UNION)
        canvas.translate(0f, 300f)
        canvas.drawText("UNION", 240f,0f,mTextPaint)
        canvas.drawPath(pathOpResult,mPaint)

        pathOpResult.op(path1,path2, Path.Op.XOR)
        canvas.translate(0f, 300f)
        canvas.drawText("XOR", 240f,0f,mTextPaint)
        canvas.drawPath(pathOpResult,mPaint)
    }

    /**
     * 计算path边界
     */
    private fun testComputeBounds(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL
        val rect = RectF() // 存放测量结果的矩形
        val path = Path().apply {
            addCircle(0f,0f,100f,Path.Direction.CW)
            addCircle(90f,0f,100f,Path.Direction.CW)
            computeBounds(rect,true)    // 测量Path
        }
        canvas.drawPath(path,mPaint)   // 绘制Path

        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.RED
        canvas.drawRect(rect,mPaint)    // 绘制边界
        LogUtils.i("$rect")
    }
}