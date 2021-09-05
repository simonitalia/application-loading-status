package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.math.roundToInt


//sealed class ButtonStates {
//    object Clicked : ButtonStates()
//    object Loading : ButtonStates()
//    object Completed : ButtonStates()
//}


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private enum class ButtonState(val text: Int) {
        NORMAL(text = R.string.button_normal_text),
        DOWNLOADING(text = R.string.button_downloading_text)
    }

    //Paint text object
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        color = Color.WHITE
        textSize = 50.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private var normalStateColor: Int = 0
    private var downloadingStateColor: Int = 0

    // canvas (view) dimensions
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

       // get text position
        val viewCenterX = (width / 2).toFloat()  // canvas (view) position
        val textPositionY = (height * 0.6f).roundToInt().toFloat() // canvas (view) text drawing position

        var buttonText = ""

        // set button color & text based on button state
         when (buttonState) {
            ButtonState.NORMAL -> {
                setBackgroundColor(normalStateColor)
                buttonText = resources.getString(ButtonState.NORMAL.text)
            }

            ButtonState.DOWNLOADING -> {
                setBackgroundColor(downloadingStateColor)
                buttonText = resources.getString(ButtonState.DOWNLOADING.text)
            }
        }

        canvas?.drawText(buttonText, viewCenterX, textPositionY, paintText)
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
        Log.i("LoadingButton.init", "Canvas Dimens: w: $width, h: $height")
    }
}