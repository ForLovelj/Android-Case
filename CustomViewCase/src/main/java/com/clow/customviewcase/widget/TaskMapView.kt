package com.clow.customviewcase.widget
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.clow.customviewcase.R
import com.clow.customviewcase.data.TaskMapNode

/**
 * Created by clow
 * Des:
 * Date: 2026/1/9.
 */
class TaskMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 画笔
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val completedNodePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pendingNodePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val currentStarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 虚线效果
    private val dashPathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)

    // 节点尺寸
    private var nodeRadius = 0f
    private var nodePadding = 0f
    private var verticalSpacing = 0f // 节点垂直间距

    // 当前进度头像
    private var currentAvatarDrawable: Drawable? = null
    private var currentAvatarWidth = 0f
    private var currentAvatarHeight = 0f
    private var currentAvatarOffset = 0f // 头像相对于节点中心的Y轴偏移
    private var avatarAnimValue = 0f // 头像动画Y轴偏移量

    // 数据
    private var taskMapNodes: List<TaskMapNode> = emptyList()

    // 监听器
    private var onNodeClickListener: ((TaskMapNode) -> Unit)? = null

    // 动画
    private var avatarAnimator: ObjectAnimator? = null

    init {
        initPaints()
        initAttributes(attrs)
        initAvatarAnimation()
    }

    private fun initPaints() {
        linePaint.apply {
            color = Color.parseColor("#CCCCCC") // 默认灰色虚线
            strokeWidth = dpToPx(3f)
            style = Paint.Style.STROKE
        }

        completedNodePaint.apply {
            color = Color.parseColor("#FFC107") // 黄色，已完成
            style = Paint.Style.FILL
        }

        pendingNodePaint.apply {
            color = Color.parseColor("#673AB7") // 紫色，未完成
            style = Paint.Style.FILL
        }

        currentStarPaint.apply {
            color = Color.parseColor("#FF9800") // 橙色星形背景
            style = Paint.Style.FILL
        }

        textPaint.apply {
            color = Color.WHITE
            textSize = dpToPx(12f)
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }
    }

    private fun initAttributes(attrs: AttributeSet?) {
        // 可以从 XML 获取自定义属性，这里简化为硬编码
        nodeRadius = dpToPx(20f)
        nodePadding = dpToPx(40f) // 节点距离左右边缘的距离
        verticalSpacing = dpToPx(80f) // 节点垂直方向的间距

        currentAvatarDrawable = ContextCompat.getDrawable(context, R.drawable.ic_avatar_placeholder) // 替换为你的头像Drawable
        currentAvatarWidth = dpToPx(40f)
        currentAvatarHeight = dpToPx(40f)
        currentAvatarOffset = dpToPx(-25f) // 头像在节点上方偏移量
    }

    private fun initAvatarAnimation() {
        avatarAnimator = ObjectAnimator.ofFloat(this, "avatarAnimValue", 0f, 1f, 0f).apply {
            duration = 1500 // 1.5秒
            repeatCount = ObjectAnimator.INFINITE // 无限循环
            repeatMode = ObjectAnimator.REVERSE // 来回动画
            interpolator = LinearInterpolator()
            addUpdateListener {
                // 当动画值更新时，重新绘制视图
                invalidate()
            }
        }
    }

    fun setNodes(nodes: List<TaskMapNode>) {
        this.taskMapNodes = nodes
        // 重新计算节点位置
        requestLayout()
        // 确保动画开始
        startAvatarAnimation()
    }

    fun setOnNodeClickListener(listener: (TaskMapNode) -> Unit) {
        this.onNodeClickListener = listener
    }

    // 设置动画值，供 ObjectAnimator 调用
    fun setAvatarAnimValue(value: Float) {
        this.avatarAnimValue = value
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        // 根据节点数量计算高度
        val desiredHeight = if (taskMapNodes.isNotEmpty()) {
            (taskMapNodes.size * verticalSpacing + nodeRadius * 2 + paddingTop + paddingBottom).toInt()
        } else {
            0
        }

        setMeasuredDimension(desiredWidth, desiredHeight)

        // 在测量阶段计算节点位置
        calculateNodePositions(desiredWidth)
    }

    private fun calculateNodePositions(width: Int) {
        if (taskMapNodes.isEmpty()) return

        val centerX = width / 2f
        val leftX = nodePadding + nodeRadius
        val rightX = width - nodePadding - nodeRadius

        taskMapNodes.forEachIndexed { index, node ->
            node.y = (index * verticalSpacing) + nodeRadius + paddingTop
            // 奇数节点在左边，偶数节点在右边，或者Z字型
            if (index % 2 == 0) { // 假设0是第一个，在左边
                node.x = leftX
            } else { // 第二个在右边
                node.x = rightX
            }

            // 如果节点是第一个，或者课程数只有一个
            if (taskMapNodes.size == 1 || index == 0) {
                node.x = centerX // 居中显示
            }
            // 如果节点是最后一个，且是偶数位置，也居中
            if (index == taskMapNodes.size - 1 && taskMapNodes.size > 1 && index % 2 == 0) {
                node.x = centerX
            }

            // 对齐优化：如果只有一个节点，或第一个节点，或最后一个节点，可以考虑居中
            if (taskMapNodes.size == 1 || index == 0) {
                node.x = centerX
            } else if (index == taskMapNodes.size -1) {
                // 最后一个节点如果前面是偶数个节点，则它也是奇数行，应该在右侧，或者根据需求居中
                // 暂时保持Z字型，如果需要居中，可以设置 node.x = centerX
                node.x = centerX
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (taskMapNodes.isEmpty()) return

        // 1. 绘制连线
        for (i in 0 until taskMapNodes.size - 1) {
            val startNode = taskMapNodes[i]
            val endNode = taskMapNodes[i + 1]

            // 检查下一个节点是否已完成，或者当前节点已完成且下一个未完成（虚线）
            if (!endNode.isCompleted && !startNode.isCompleted) {
                linePaint.color = Color.parseColor("#CCCCCC") // 虚线颜色
                linePaint.pathEffect = dashPathEffect
            } else {
                linePaint.color = Color.parseColor("#4CAF50") // 实线颜色 (绿色)
                linePaint.pathEffect = null // 移除虚线效果
            }

            // 绘制Z字型连线
            val path = Path()
            path.moveTo(startNode.x, startNode.y)
            path.lineTo(endNode.x, endNode.y)
            canvas.drawPath(path, linePaint)
        }

        // 2. 绘制节点和星星
        taskMapNodes.forEachIndexed { index, node ->
            // 绘制节点背景 (圆圈)
            val nodeBgPaint = if (node.isCompleted) completedNodePaint else pendingNodePaint
            canvas.drawCircle(node.x, node.y, nodeRadius, nodeBgPaint)

            // 绘制节点上的星星 (如果需要)
            if (node.isCurrent) { // 当前节点绘制橙色星形
                drawStar(canvas, node.x, node.y, nodeRadius * 0.7f, currentStarPaint) // 绘制星星
                // 绘制数字1图标 (表示当前关卡)
                val oneDrawable = ContextCompat.getDrawable(context, R.drawable.ic_number_one) // 替换为你的数字1Drawable
                oneDrawable?.let {
                    val iconSize = nodeRadius * 0.8f // 图标大小
                    val left = (node.x - iconSize / 2).toInt()
                    val top = (node.y - iconSize / 2).toInt()
                    val right = (node.x + iconSize / 2).toInt()
                    val bottom = (node.y + iconSize / 2).toInt()
                    it.setBounds(left, top, right, bottom)
                    it.draw(canvas)
                }
            } else {
                // 其他节点绘制蓝色/黄色星星图标 (如果需要)
                val starDrawableRes = if (node.isCompleted) R.drawable.ic_star_completed else R.drawable.ic_star_pending
                val starDrawable = ContextCompat.getDrawable(context, starDrawableRes)
                starDrawable?.let {
                    val starSize = nodeRadius * 0.6f
                    val left = (node.x - starSize / 2).toInt()
                    val top = (node.y - starSize / 2).toInt()
                    val right = (node.x + starSize / 2).toInt()
                    val bottom = (node.y + starSize / 2).toInt()
                    it.setBounds(left, top, right, bottom)
                    it.draw(canvas)
                }
            }

            // 绘制标题 (如果需要) - 暂时不绘制在节点上，因为空间有限
            // 如果需要绘制在节点下方，可以在这里添加 textPaint 绘制
        }

        // 3. 绘制当前进度头像
        taskMapNodes.firstOrNull { it.isCurrent }?.let { currentNode ->
            currentAvatarDrawable?.let {
                // 计算动画偏移量
                val animatedYOffset = currentAvatarOffset + (avatarAnimValue * dpToPx(5f)) // 动画上下浮动5dp

                val left = (currentNode.x - currentAvatarWidth / 2).toInt()
                val top = (currentNode.y - nodeRadius - currentAvatarHeight + animatedYOffset).toInt()
                val right = (currentNode.x + currentAvatarWidth / 2).toInt()
                val bottom = (currentNode.y - nodeRadius + animatedYOffset).toInt()

                it.setBounds(left, top, right, bottom)
                it.draw(canvas)
            }
        }
    }

    // 绘制五角星的辅助方法
    private fun drawStar(canvas: Canvas, centerX: Float, centerY: Float, outerRadius: Float, paint: Paint) {
        val path = Path()
        val innerRadius = outerRadius / 2.5f // 内圆半径

        for (i in 0 until 5) {
            val outerAngle = Math.toRadians((18 + i * 72).toDouble())
            val innerAngle = Math.toRadians((54 + i * 72).toDouble())

            val outerX = (centerX + outerRadius * Math.cos(outerAngle)).toFloat()
            val outerY = (centerY + outerRadius * Math.sin(outerAngle)).toFloat()

            val innerX = (centerX + innerRadius * Math.cos(innerAngle)).toFloat()
            val innerY = (centerY + innerRadius * Math.sin(innerAngle)).toFloat()

            if (i == 0) {
                path.moveTo(outerX, outerY)
            } else {
                path.lineTo(outerX, outerY)
            }
            path.lineTo(innerX, innerY)
        }
        path.close()
        canvas.drawPath(path, paint)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // 检查是否点击了某个节点
                val clickedNode = taskMapNodes.firstOrNull { node ->
                    val distanceX = event.x - node.x
                    val distanceY = event.y - node.y
                    // 判断点击点是否在节点圆圈内
                    distanceX * distanceX + distanceY * distanceY <= nodeRadius * nodeRadius
                }
                clickedNode?.let {
                    onNodeClickListener?.invoke(it)
                    return true // 消费事件
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 获取当前正在进行的课程节点的中心 Y 坐标
     */
    fun getCurrentNodeCenterY(): Float {
        val currentNode = taskMapNodes.find { it.isCurrent }
        // 如果找到了当前节点，返回它的 Y 坐标；否则返回 0
        return currentNode?.y ?: 0f
    }

    private fun dpToPx(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }

    private fun startAvatarAnimation() {
        if (avatarAnimator?.isStarted != true) {
            avatarAnimator?.start()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAvatarAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        avatarAnimator?.cancel()
    }
}