package com.udacity

import android.animation.*
import android.content.Context
import android.graphics.*
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.math.roundToInt
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    companion object {
        const val ARC_FULL_ROTATION_DEGREE = 360
        const val PERCENTAGE_DIVIDER = 100.0
        const val PERCENTAGE_VALUE_HOLDER = "PERCENTAGE"
    }

    private var currentRectPercentage = 0
    private var currentArcPercentage = 0

    enum class ButtonState(val text: Int) {
        NORMAL(text = R.string.button_normal_text),
        DOWNLOADING(text = R.string.button_downloading_text)
    }

    private var isDownloading = false

    // Button State observable
    var buttonState: ButtonState by Delegates.observable(ButtonState.NORMAL) { _, _, _ ->

        if (buttonState == ButtonState.DOWNLOADING){
            animateProgressBar()
            animateProgressCircle()
        }

        invalidate() //force redraw of view
    }

    // storage for retrieved styling attribute colors
    private var normalStateColor: Int = 0
    private var downloadingStateColor: Int = 0

    // canvas (view) dimensions
    private var viewWidth = 0
    private var viewHeight = 0

    // graphic objects
    private val startDownloadRect = RectF()
    private var downloadingProgressRect = RectF()
    private val downloadingProgressArc = RectF()

    private val animationDuration = 1000L

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            normalStateColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            downloadingStateColor = getColor(R.styleable.LoadingButton_secondaryBackgroundColor, 0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawNormalStateView(canvas)
        if (buttonState == ButtonState.DOWNLOADING) {
            drawDownloadingStateView(canvas)
            drawProgressCircle(canvas)
        }

        drawViewText(canvas)
    }

    // set canvas view space
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )

        viewWidth = w
        viewHeight = h
        setMeasuredDimension(w, h)

        //set viewContainer space
        startDownloadRect.set(
            x,
            y,
            x + w,
            y + h
        )

        Log.i("LoadingButton.init", "Canvas Dimens: w: $viewWidth, h: $viewHeight")
    }

    private fun drawNormalStateView(canvas: Canvas?) {
        val viewContainerPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = context.getColor(R.color.primary_green)
        }

        canvas?.drawRect(startDownloadRect, viewContainerPaint)
    }

    private fun drawDownloadingStateView(canvas: Canvas?) {

        val paint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = context.getColor(R.color.primary_dark_green)
        }

        val percentageToFill =  (viewWidth * (currentRectPercentage / PERCENTAGE_DIVIDER)).toFloat()

        downloadingProgressRect.set(
            0f,
            0f,
            percentageToFill,
            viewHeight.toFloat()
        )

        canvas?.drawRect(downloadingProgressRect, paint)
    }

    private fun animateProgressBar() {
        val valuesHolder = PropertyValuesHolder.ofFloat("PERCENTAGE", 0f, 100f)
        val animator = ValueAnimator().apply {
            setValues(valuesHolder)
            duration = animationDuration
            addUpdateListener {
                val percentage = it.getAnimatedValue(PERCENTAGE_VALUE_HOLDER) as Float
                currentRectPercentage = percentage.toInt()
                invalidate()
            }

            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationStart(animation: Animator?) {
                    isDownloading = true
                    isClickable = false
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isDownloading = false
                    isClickable = true
                }
            })
        }

       animator.start()
    }

    private fun drawViewText(canvas: Canvas?) {

        // paint object for drawing button text
        val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            color = Color.WHITE
            textSize = 50.0f
            typeface = Typeface.create( "", Typeface.BOLD)
        }

        // set x, y text drawing position
        val viewCenterX = (width / 2).toFloat()  // canvas (view) center x
        val textPositionY = (height * 0.6f).roundToInt().toFloat() // canvas (view) y position

        // draw text
        val buttonText = if (buttonState == ButtonState.NORMAL) {
            resources.getString(ButtonState.NORMAL.text)

        } else {
            resources.getString(ButtonState.DOWNLOADING.text)
        }

        canvas?.drawText(buttonText, viewCenterX, textPositionY, paintText)
    }

    private fun drawProgressCircle(canvas: Canvas?) {

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = context.getColor(R.color.orange)
        }

        val centerX = (viewWidth / 1.2).toFloat()
        val centerY = (viewHeight / 2).toFloat()
        val ovalSize: Float = (viewHeight * 0.25).toFloat()

        downloadingProgressArc.set(
            centerX - ovalSize, // top
            centerY - ovalSize, // left
            centerX + ovalSize, // right
            centerY + ovalSize // bottom
        )

        val percentageToFill = (ARC_FULL_ROTATION_DEGREE * (currentArcPercentage / PERCENTAGE_DIVIDER)).toFloat()

        canvas?.drawArc(downloadingProgressArc, 270f, percentageToFill, false, paint)
    }

    private fun animateProgressCircle() {
        val valuesHolder = PropertyValuesHolder.ofFloat("PERCENTAGE", 0f, 100f)
        val animator = ValueAnimator().apply {
            setValues(valuesHolder)
            duration = animationDuration
            addUpdateListener {
                val percentage = it.getAnimatedValue(PERCENTAGE_VALUE_HOLDER) as Float
                currentArcPercentage = percentage.toInt()
                invalidate()
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {}
            })
        }

        animator.start()
    }
}