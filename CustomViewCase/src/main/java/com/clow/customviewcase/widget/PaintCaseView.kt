package com.clow.customviewcase.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.clow.customviewcase.R
import kotlin.math.sin

/**
 * Created by clow
 * Des: Paint 测试
 * Date: 2023/2/2.
 */
class PaintCaseView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val mPaint = Paint().apply {
        strokeWidth = 10f
        isAntiAlias = true
    }
    private var mType = -1


    fun drawForType(type: Int) {
        mType = type
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when(mType){
            0 -> drawSetStyle(canvas)
            1 -> drawSetStrokeWidth(canvas)
            2 -> drawSetStrokeCap(canvas)
            3 -> drawSetStrokeJoin(canvas)
            4 -> drawSetStrokeMiter(canvas)
            5 -> drawSetXfermode(canvas)
            6 -> drawSetColor(canvas)
            7 -> drawSetARGB(canvas)
            8 -> drawSetShader(canvas)
            9 -> drawSetColorFilter(canvas)
            10 -> drawSetMaskFilter(canvas)
            11 -> drawSetShadowLayer(canvas)
            12 -> drawSetPathEffect(canvas)
        }
    }

    private fun drawSetPathEffect(canvas: Canvas) {

//        drawCornerPathEffect(canvas)
//        drawDiscretePathEffect(canvas)
        drawDashPathEffect(canvas)

    }

    private fun drawDashPathEffect(canvas: Canvas) {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 4f
        val path = Path().apply {
            moveTo(100f, 100f)
            lineTo(200f, 300f)
            rLineTo(100f, -200f)
            rLineTo(100f, 200f)
            rLineTo(150f, -250f)
            rLineTo(200f, 200f)
        }
        //绘制原path
        canvas.drawPath(path,mPaint)

        canvas.translate(0f,400f)
        val dashPathEffect = DashPathEffect(floatArrayOf(20f,10f,5f,10f),5f)
        mPaint.setPathEffect(dashPathEffect)
        //绘制DashPathEffect效果
        canvas.drawPath(path,mPaint)
    }

    private fun drawDiscretePathEffect(canvas: Canvas) {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 4f
        val path = Path().apply {
            moveTo(100f, 100f)
            lineTo(200f, 300f)
            rLineTo(100f, -200f)
            rLineTo(100f, 200f)
            rLineTo(150f, -250f)
            rLineTo(200f, 200f)
        }
        //绘制原path
        canvas.drawPath(path,mPaint)

        canvas.translate(0f,400f)
        val discretePathEffect = DiscretePathEffect(10f,5f)
        mPaint.setPathEffect(discretePathEffect)
        //绘制DiscretePathEffect效果
        canvas.drawPath(path,mPaint)

    }

    private fun drawCornerPathEffect(canvas: Canvas) {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 4f
        val path = Path().apply {
            moveTo(100f, 100f)
            lineTo(200f, 300f)
            rLineTo(100f, -200f)
            rLineTo(100f, 200f)
            rLineTo(150f, -250f)
            rLineTo(200f, 200f)
        }
        //绘制原path
        canvas.drawPath(path,mPaint)

        canvas.translate(0f,400f)
        val cornerPathEffect = CornerPathEffect(50f)
        mPaint.setPathEffect(cornerPathEffect)
        //绘制CornerPathEffect效果
        canvas.drawPath(path,mPaint)

    }

    /**
     *  设置阴影层
     */
    private fun drawSetShadowLayer(canvas: Canvas) {

        val text = "人间忽晚，山河已秋"
        mPaint.textSize = 80f
        mPaint.setShadowLayer(4f,4f,4f,Color.RED)
        canvas.drawText(text, 100f, 300f, mPaint)
    }

    /**
     * 基于整个画面过滤
     */
    private fun drawSetMaskFilter(canvas: Canvas) {
        //模糊遮罩效果
        drawBlurMaskFilter(canvas)
        //浮雕遮罩效果
//        drawEmbossMaskFilter(canvas)

    }

    private fun drawEmbossMaskFilter(canvas: Canvas) {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.beauty1)
        //绘制原图
        canvas.drawBitmap(bitmap,100f,100f,mPaint)
        //EmbossMaskFilter已被废弃  如果要看到效果需要关闭硬件加速
        val embossMaskFilter = EmbossMaskFilter(floatArrayOf(2f, 2f, 2f), 0.1f, 10f, 10f)
        mPaint.setMaskFilter(embossMaskFilter)
        //绘制过滤后的图
        canvas.drawBitmap(bitmap,100f,150f+bitmap.height,mPaint)
    }

    private fun drawBlurMaskFilter(canvas: Canvas) {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.beauty1)
        val rectF = RectF(100f, 100f, 100f + bitmap.width / 2, 100f + bitmap.height / 2)
        canvas.drawBitmap(bitmap,null, rectF,mPaint)    //原图

        //NORMAL效果
        canvas.translate(0f,300f+bitmap.height/2)
        val normalMaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.NORMAL)
        mPaint.setMaskFilter(normalMaskFilter)
        //绘制过滤后的图
        canvas.drawBitmap(bitmap,null, rectF,mPaint)

        //SOLID效果
        rectF.offset(200f+bitmap.width/2f,0f)
        val solidMaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.SOLID)
        mPaint.setMaskFilter(solidMaskFilter)
        canvas.drawBitmap(bitmap,null, rectF,mPaint)

        //INNER效果
        canvas.translate(0f,300f+bitmap.height/2)
        rectF.offset(-(200f+bitmap.width/2f),0f)
        val innerMaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.INNER)
        mPaint.setMaskFilter(innerMaskFilter)
        canvas.drawBitmap(bitmap,null, rectF,mPaint)

        //OUTER效果
        rectF.offset(200f+bitmap.width/2f,0f)
        val outerMaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.OUTER)
        mPaint.setMaskFilter(outerMaskFilter)
        canvas.drawBitmap(bitmap,null, rectF,mPaint)
    }
    /**
     * 基于每个像素进行颜色过滤
     */
    private fun drawSetColorFilter(canvas: Canvas) {
        //LightingColorFilter
//        drawLightingColorFilter(canvas)
        //PorterDuffColorFilter
//        drawPorterDuffColorFilter(canvas)
        //ColorMatrixColorFilter
        drawColorMatrixColorFilter(canvas)
    }

    private fun drawColorMatrixColorFilter(canvas: Canvas) {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.beauty1)
        canvas.drawBitmap(bitmap,100f,100f,mPaint)

        // 反相效果 -- 底片(曝光)效果（就是将每个像素都变成它的相反的值）
//        val colorMatrix = ColorMatrix(
//            floatArrayOf(
//                -1f, 0f,0f,0f,255f,
//                0f,-1f,0f,0f,255f,
//                0f,0f,-1f,0f,255f,
//                0f,0f,0f,1f,0f
//            )
//        )
        //美白效果
//        val colorMatrix = ColorMatrix(
//            floatArrayOf(
//                1.2f, 0f,0f,0f,0f,
//                0f,1.2f,0f,0f,0f,
//                0f,0f,1.2f,0f,0f,
//                0f,0f,0f,1.2f,0f
//            )
//        )
        //复古效果
        val colorMatrix = ColorMatrix(
            floatArrayOf(
                1/2f,1/2f,1/2f,0f,0f,
                1/3f, 1/3f,1/3f,0f,0f,
                1/4f,1/4f,1/4f,0f,0f,
                0f,0f,0f,1f,0f
            )
        )
//        colorMatrix.setSaturation(2f)   //设置饱和度
//        colorMatrix.setScale(1.2f,1.2f,1.2f,1)   //设置缩放
        val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
        mPaint.setColorFilter(colorMatrixColorFilter)
        //绘制颜色过滤后的图
        canvas.drawBitmap(bitmap,100f,150f+bitmap.height,mPaint)
    }

    private fun drawPorterDuffColorFilter(canvas: Canvas) {
        //这里需要注意 这里我们的bitmap属于目标图片（DST），Color.parseColor("#300000ff")属于源图片（SRC）
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.beauty1)
        canvas.drawBitmap(bitmap,100f,100f,mPaint)

        val porterDuffColorFilter = PorterDuffColorFilter(Color.parseColor("#300000ff"), PorterDuff.Mode.SRC_OVER)
        mPaint.setColorFilter(porterDuffColorFilter)
        //绘制过滤颜色后的图
        canvas.drawBitmap(bitmap,100f,150f+bitmap.height,mPaint)
    }

    private fun drawLightingColorFilter(canvas: Canvas) {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.beauty1)
        //绘制原图
        canvas.drawBitmap(bitmap,100f,100f,mPaint)
//        val lightingColorFilter = LightingColorFilter(0x00ffff, 0x000000)   //移除红色
        val lightingColorFilter = LightingColorFilter(0xffffff, 0x404040)   //原图所有颜色效果增强
        mPaint.setColorFilter(lightingColorFilter)
        //绘制过滤颜色后的图
        canvas.drawBitmap(bitmap,100f,150f+bitmap.height,mPaint)

    }

    /**
     * 设置着色器
     */
    private fun drawSetShader(canvas: Canvas) {

        //线性渐变测试
//        testLinearGradient(canvas)
        //辐射渐变测试
//        testRadialGradient(canvas)
        //扫描渐变测试
//        testSweepGradient(canvas)
        //bitmap着色
//        testBitmapShader(canvas)
        //混合着色器
        testComposeShader(canvas)
    }

    private fun testComposeShader(canvas: Canvas) {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.beauty1)
        //第一个bitmapShader
        val bitmapShader = BitmapShader(bitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP)
        //第二个辐射渐变shader
        val radialShader = RadialGradient(400f,300f,100f,Color.parseColor("#FF7400"),
            Color.parseColor("#BB86FC"),Shader.TileMode.CLAMP)
        // ComposeShader：结合两个 Shader
        val shader = ComposeShader(bitmapShader,radialShader,PorterDuff.Mode.MULTIPLY)

        mPaint.shader = shader
        canvas.drawCircle(400f,400f,300f,mPaint)
    }

    private fun testBitmapShader(canvas: Canvas) {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.beauty1)
        //CLAMP 当所画图形的尺寸大于bitmap的尺寸，会用bitmap和剩余空间相邻位置的颜色填充剩余空间
        //当所画图形的尺寸小于bitmap的尺寸，会对bitmap进行裁剪，利用这个原理，我们可以去制造圆形头像
        val shader = BitmapShader(bitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP)
        mPaint.shader = shader
        canvas.drawCircle(400f,400f,300f,mPaint)

        //另外几种填充方式和之前渐变一样，这儿不再赘述
//        val shader = BitmapShader(bitmap,Shader.TileMode.MIRROR,Shader.TileMode.MIRROR)
//        canvas.drawRect(0f,0f,canvas.width.toFloat(),canvas.height.toFloat(),mPaint)
    }

    private fun testSweepGradient(canvas: Canvas) {
        val shader = SweepGradient(300f,300f,Color.parseColor("#FF7400"),
            Color.parseColor("#BB86FC"))
        mPaint.shader = shader
        canvas.drawCircle(300f,300f,200f,mPaint)
    }

    private fun testRadialGradient(canvas: Canvas) {
        //CLAMP
        val shader = RadialGradient(300f,300f,150f,Color.parseColor("#FF7400"),
            Color.parseColor("#BB86FC"),Shader.TileMode.CLAMP)
        mPaint.shader = shader
        canvas.drawCircle(300f,300f,150f,mPaint)

        //MIRROR
        val shader1 = RadialGradient(300f,600f,50f,Color.parseColor("#FF7400"),
            Color.parseColor("#BB86FC"),Shader.TileMode.MIRROR)
        mPaint.shader = shader1
        canvas.drawCircle(300f,600f,150f,mPaint)

        //REPEAT
        val shader2 = RadialGradient(300f,900f,50f,Color.parseColor("#FF7400"),
            Color.parseColor("#BB86FC"),Shader.TileMode.REPEAT)
        mPaint.shader = shader2
        canvas.drawCircle(300f,900f,150f,mPaint)

        //DECAL
        val shader3 = RadialGradient(300f,1200f,50f,Color.parseColor("#FF7400"),
            Color.parseColor("#BB86FC"),Shader.TileMode.DECAL)
        mPaint.shader = shader3
        canvas.drawCircle(300f,1200f,150f,mPaint)
    }

    private fun testLinearGradient(canvas: Canvas) {
        //CLAMP 渐变开始和结束点范围之外 用相邻位置的颜色填充
        val shader = LinearGradient(100f,100f,500f,200f,Color.parseColor("#FF7400"),
            Color.parseColor("#BB86FC"),Shader.TileMode.CLAMP)
        mPaint.shader = shader
        canvas.drawRect(100f,100f,900f,200f,mPaint)

        //MIRROR    镜像填充
        val shader1 = LinearGradient(100f,300f,500f,400f,Color.parseColor("#FF7400"),
            Color.parseColor("#BB86FC"),Shader.TileMode.MIRROR)
        mPaint.shader = shader1
        canvas.drawRect(100f,300f,900f,400f,mPaint)

        //REPEAT    重复填充
        val shader2 = LinearGradient(100f,500f,500f,600f,Color.parseColor("#FF7400"),
            Color.parseColor("#BB86FC"),Shader.TileMode.REPEAT)
        mPaint.shader = shader2
        canvas.drawRect(100f,500f,900f,600f,mPaint)

        //DECAL api >= 31 仅填充渐变开始和结束点范围。如果超出其原始边界，则绘制透明黑色。
        val shader3 = LinearGradient(100f,700f,500f,800f,Color.parseColor("#FF7400"),
            Color.parseColor("#BB86FC"),Shader.TileMode.DECAL)
        mPaint.shader = shader3
        canvas.drawRect(100f,700f,900f,800f,mPaint)
    }


    /**
     * 设置基础颜色
     */
    private fun drawSetARGB(canvas: Canvas) {
        mPaint.strokeWidth = 40f
        mPaint.setARGB(100,255,0,0)
        canvas.drawLine(100f,100f,800f,100f,mPaint)

        mPaint.setARGB(100,0,255,0)
        canvas.drawLine(100f,200f,800f,200f,mPaint)

        mPaint.setARGB(255,0,0,255)
        canvas.drawLine(100f,300f,800f,300f,mPaint)

    }

    /**
     * 设置基础颜色
     */
    private fun drawSetColor(canvas: Canvas) {
        mPaint.strokeWidth = 40f
        mPaint.color = Color.RED
        canvas.drawLine(100f,100f,800f,100f,mPaint)

        mPaint.color = Color.parseColor("#FF9300")
        canvas.drawLine(100f,200f,800f,200f,mPaint)

        mPaint.color = Color.BLUE
        canvas.drawLine(100f,300f,800f,300f,mPaint)
    }

    /**
     * setXfermode
     */
    private fun drawSetXfermode(canvas: Canvas) {
        val source = BitmapFactory.decodeResource(resources, R.mipmap.source)   //源图
        val dst = BitmapFactory.decodeResource(resources, R.mipmap.destination) //目标图
        val mode = PorterDuff.Mode.ADD  //PorterDuff 混合模式
        val xfermode = PorterDuffXfermode(mode)

        val rect = RectF(0f,0f,canvas.width.toFloat(),canvas.height.toFloat())
        val saveCount = canvas.saveLayer(rect, mPaint)  //将绘制操作保存到新的图层

        val bitmapRect = RectF(0f,0f,source.width.toFloat(),source.height.toFloat())
        canvas.drawBitmap(dst,null,bitmapRect,mPaint) //绘制目标图
        mPaint.setXfermode(xfermode)    //设置混合模式
        canvas.drawBitmap(source,null,bitmapRect,mPaint) //绘制源图

        mPaint.setXfermode(null)    //清除混合模式
        canvas.restoreToCount(saveCount)    //还原画布

    }


    /**
     * 设置 `MITER` 型拐角的延长线的最大值
     */
    private fun drawSetStrokeMiter(canvas: Canvas) {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 40f

        //画一个等边直角三角形
        val path = Path().apply {
            moveTo(100f,100f)
            lineTo(300f,100f)
            lineTo(100f,300f)
            close()
        }
        //1度=π/180≈0.01745弧度，1弧度=180/π≈57.3度
        mPaint.strokeMiter = (1 / sin(20 * Math.PI/180)).toFloat()  //大于这个40°的尖角会被保留，小于的就被削成平头
        canvas.drawPath(path,mPaint)

        canvas.translate(0f,300f)
        mPaint.strokeMiter = (1/sin(30 * Math.PI/180)).toFloat()    //大于这个60°的尖角会被保留，小于的就被削成平头
        canvas.drawPath(path,mPaint)

        canvas.translate(0f,300f)
        mPaint.strokeMiter = (1/sin(60*Math.PI/180)).toFloat()  //大于这个120°的尖角会被保留，小于的就被削成平头
        canvas.drawPath(path,mPaint)
    }

    /**
     * 设置拐角的形状
     */
    private fun drawSetStrokeJoin(canvas: Canvas) {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 40f
        //画一个三角形
        val path = Path().apply {
            moveTo(100f,100f)
            lineTo(400f,100f)
            lineTo(400f,300f)
            close()
        }

        mPaint.strokeJoin = Paint.Join.MITER    //尖角
        canvas.drawPath(path,mPaint)

        canvas.translate(0f,300f)
        mPaint.strokeJoin = Paint.Join.BEVEL    //平角
        canvas.drawPath(path,mPaint)

        canvas.translate(0f,300f)
        mPaint.strokeJoin = Paint.Join.ROUND    //圆角
        canvas.drawPath(path,mPaint)
    }

    /**
     * 设置线头的形状
     */
    private fun drawSetStrokeCap(canvas: Canvas) {

        mPaint.strokeWidth = 40f
        mPaint.strokeCap = Paint.Cap.BUTT
        canvas.drawLine(100f,100f,800f,100f,mPaint)

        mPaint.strokeCap = Paint.Cap.ROUND
        canvas.drawLine(100f,200f,800f,200f,mPaint)

        mPaint.strokeCap = Paint.Cap.SQUARE
        canvas.drawLine(100f,300f,800f,300f,mPaint)
    }

    /**
     * 设置画笔粗细
     */
    private fun drawSetStrokeWidth(canvas: Canvas) {
        mPaint.style = Paint.Style.STROKE   //设置描边模式

        mPaint.strokeWidth = 1f
        canvas.drawCircle(200f,300f,100f,mPaint)

        mPaint.strokeWidth = 10f
        canvas.drawCircle(500f,300f,100f,mPaint)

        mPaint.strokeWidth = 40f
        canvas.drawCircle(800f,300f,100f,mPaint)
    }

    /**
     * 设置画笔样式
     */
    private fun drawSetStyle(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL //设置填充模式
        canvas.drawCircle(200f,300f,100f,mPaint)

        mPaint.style = Paint.Style.STROKE   //设置描边模式
        canvas.drawCircle(500f,300f,100f,mPaint)

        mPaint.style = Paint.Style.FILL_AND_STROKE  //设置描边加填充模式
        canvas.drawCircle(800f,300f,100f,mPaint)
    }

}