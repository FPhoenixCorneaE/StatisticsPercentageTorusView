package com.fphoenixcorneae.widget.torus

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import java.util.*
import kotlin.math.roundToLong

/**
 * @desc：统计百分比圆环图
 * @date：2022/11/08 11:43
 */
class StatisticsPercentageTorusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    /** 画笔 */
    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    /** 圆环矩形 */
    private val mRingRectF by lazy { RectF() }

    /** 动画队列 */
    private val mAnimatorList by lazy { LinkedList<Animator>() }

    /** 动画索引 */
    private var mAnimatorIndex = 0

    /** 是否显示动画 */
    private var isShowAnim = false

    /** 动画执行中当前的角度 */
    private var mAnimatedAngle: Float = 0f

    /** 统计项 */
    private val mItems = mutableListOf<Pair<Int, Int>>()

    /** 圆环宽度，默认为宽度的1/4 */
    private var mRingWidth = 0f

    /** 总数数字的颜色 */
    private val mSumColor = Color.WHITE

    /** ”总数“的颜色 */
    private val mSumTextColor = Color.parseColor("#8d9ab7")

    /** ”总数“字符 */
    private val mSumText = "总数"

    override fun onResume(owner: LifecycleOwner) {
        if (isShowAnim) {
            mAnimatorIndex = 0
            mAnimatorList.poll()?.start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 宽高一样
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec))
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        // 中心坐标
        val centerX = measuredWidth / 2f
        val centerY = measuredHeight / 2f
        // 画布逆时针旋转90°
        canvas?.rotate(-90f, centerX, centerY)
        // 设置画笔属性
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = if (mRingWidth <= 0f) {
            (measuredWidth / 4f).also { mRingWidth = it }
        } else {
            mRingWidth
        }
        // 设置圆环矩形
        mRingRectF.set(
            mRingWidth / 2,
            mRingWidth / 2,
            measuredWidth - mRingWidth / 2,
            measuredHeight - mRingWidth / 2
        )
        var startAngle = 1f
        if (isShowAnim) {
            // 显示动画
            for (index in 0 until mAnimatorIndex) {
                mPaint.color = mItems[index].first
                val sweepAngle = 360 * (mItems[index].second / getMax())
                canvas?.drawArc(mRingRectF,
                    startAngle,
                    sweepAngle - 1,
                    false,
                    mPaint)
                startAngle += sweepAngle
            }
            mPaint.color = mItems[mAnimatorIndex].first
            // 绘制圆环弧线
            canvas?.drawArc(mRingRectF,
                startAngle,
                mAnimatedAngle,
                false,
                mPaint)
        } else {
            // 不显示动画
            mItems.forEach {
                mRingRectF.set(
                    mRingWidth / 2,
                    mRingWidth / 2,
                    measuredWidth - mRingWidth / 2,
                    measuredHeight - mRingWidth / 2
                )
                mPaint.color = it.first
                val sweepAngle = 360 * (it.second / getMax())
                // 绘制圆环弧线
                canvas?.drawArc(mRingRectF,
                    startAngle,
                    sweepAngle - 1,
                    false,
                    mPaint)
                startAngle += sweepAngle
            }
        }
        // 画布顺时针旋转90°
        canvas?.rotate(90f, centerX, centerY)
        // 绘制总数数字
        getMax().toInt().toString().run {
            mPaint.apply {
                style = Paint.Style.FILL
                strokeWidth = 1f
                color = mSumColor
                textSize = measuredWidth / 6f
                isFakeBoldText = true
            }.getTextBounds(this).let {
                canvas?.drawText(this, centerX - it.width() / 2f, centerY, mPaint)
            }
        }
        // 绘制“总数”文字
        mSumText.run {
            mPaint.apply {
                style = Paint.Style.FILL
                strokeWidth = 1f
                color = mSumTextColor
                textSize = measuredWidth / 10f
                isFakeBoldText = false
            }.getTextBounds(this).let {
                canvas?.drawText(this, centerX - it.width() / 2f, centerY + measuredWidth / 8f, mPaint)
            }
        }
    }

    private fun Paint.getTextBounds(text: String) = run {
        val rect = Rect()
        getTextBounds(text, 0, text.length, rect)
        rect
    }

    private fun Float.dp2Px() = run {
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
    }

    fun getMax() = run {
        mItems.flatMap { listOf(it.second) }.sum().toFloat()
    }

    /**
     * 添加统计项
     */
    fun add(color: Int, count: Int) = apply {
        mItems.add(color to count)
    }

    /**
     * 圆环宽度，默认是View宽度的1/4
     * @param width dp value
     */
    fun ringWidth(width: Float) = apply {
        mRingWidth = width.dp2Px()
    }

    fun setLifecycle(lifecycle: Lifecycle) = apply {
        lifecycle.addObserver(this)
    }

    fun showAnim() = apply {
        isShowAnim = true
        mItems.forEach {
            val sweepAngle = 360 * (it.second / getMax())
            mAnimatorList.offer(ValueAnimator().apply {
                setFloatValues(0f, sweepAngle - 1)
                duration = (1000 * (it.second / getMax())).roundToLong()
                interpolator = LinearInterpolator()
                addUpdateListener {
                    mAnimatedAngle = it.animatedValue as Float
                    invalidate()
                }
                doOnEnd {
                    removeAllListeners()
                    removeAllUpdateListeners()
                    mAnimatorList.poll()?.let {
                        mAnimatorIndex += 1
                        it.start()
                    } ?: run { isShowAnim = false }
                }
            })
        }
    }

    init {
        if (isInEditMode) {
            add(Color.RED, 10)
            add(Color.YELLOW, 4)
            add(Color.GREEN, 3)
            add(Color.BLUE, 1)
        }
    }
}