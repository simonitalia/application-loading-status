package com.udacity

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
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
        const val PERCENTAGE_DIVIDER = 100.0
        const val PERCENTAGE_VALUE_HOLDER = "PERCENTAGE"
    }

    private var currentPercentage = 0

    enum class ButtonState(val text: Int) {
        NORMAL(text = R.string.button_normal_text),
        DOWNLOADING(text = R.string.button_downloading_text)
    }

    private val animator = ValueAnimator.ofFloat(0f, -100f)
    private var isDownloading = false

    // Button State observable
    var buttonState: ButtonState by Delegates.observable(ButtonState.NORMAL) { _, _, _ ->
        isClickable = buttonState != ButtonState.DOWNLOADING
        isDownloading = buttonState == ButtonState.DOWNLOADING

        if (buttonState == ButtonState.DOWNLOADING){
            animateProgress()
        }

        invalidate() //force redraw of view
    }

    // storage for retrieved styling attribute colors
    private var normalStateColor: Int = 0
    private var downloadingStateColor: Int = 0

    // canvas (view) dimensions
    private var viewWidth = 0
    private var viewHeight = 0

    // button container object
    private val viewContainer = RectF()

    private var downloadingRect = RectF()


    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            normalStateColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            downloadingStateColor = getColor(R.styleable.LoadingButton_secondaryBackgroundColor, 0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawNormalStateView(canvas)
        if (buttonState == ButtonState.DOWNLOADING) { drawDownloadingStateView(canvas) }

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
        viewContainer.set(
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

        canvas?.drawRect(viewContainer, viewContainerPaint)
    }

    private fun drawDownloadingStateView(canvas: Canvas?) {

        fun getCurrentPercentageToFill() =
            (viewWidth * (currentPercentage / PERCENTAGE_DIVIDER)).toFloat()

        val paint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = context.getColor(R.color.primary_dark_green)
        }

        val percentageToFill = getCurrentPercentageToFill()

        downloadingRect.set(
            0f,
            0f,
            0f + percentageToFill,
            0f + (viewHeight)
        )

        canvas?.drawRect(downloadingRect, paint)
    }

    private fun animateProgress() {
        val valuesHolder = PropertyValuesHolder.ofFloat("PERCENTAGE", 0f, 100f)
        val animator = ValueAnimator().apply {
            setValues(valuesHolder)
            duration = 1000
            addUpdateListener {
                val percentage = it.getAnimatedValue(PERCENTAGE_VALUE_HOLDER) as Float
                currentPercentage = percentage.toInt()
                invalidate()
            }
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
}