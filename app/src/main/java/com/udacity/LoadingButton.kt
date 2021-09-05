package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates


sealed class ButtonStates {
    object Clicked : ButtonStates()
    object Loading : ButtonStates()
    object Completed : ButtonStates()
}


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private enum class ButtonState {
        NORMAL,
        DOWNLOADING
    }

    //Paint object
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 100.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private var normalStateColor = 0
    private var downloadingStateColor = 0

    private var viewWidth = 0
    private var viewHeight = 0

    private var buttonState = ButtonState.NORMAL

    private val valueAnimator = ValueAnimator()

//    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
//
//    }

    init {
        isClickable = true // enable user interaction with button

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            normalStateColor = getColor(R.styleable.LoadingButton_buttonColorPrimary, 0)
            downloadingStateColor = getColor(R.styleable.LoadingButton_buttonColorPrimaryDark, 0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = when (buttonState) {
            ButtonState.NORMAL -> normalStateColor
            ButtonState.DOWNLOADING -> downloadingStateColor

        }

        val rect = Rect()
        canvas?.drawRect(rect, paint)

    }

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
    }

    private fun measureWidth(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else {
            viewWidth
        }
    }

    private fun measureHeight(measureSpecHeight: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpecHeight)
        val specSize = MeasureSpec.getSize(measureSpecHeight)
        return if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else {
            viewHeight
        }
    }
}