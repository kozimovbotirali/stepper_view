package com.sablab.stepperview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt

class StepperView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var listenerComplete: OnCompleteListener? = null

    var currentStep = 1
        set(value) {
            if (value == stepSize)
                listenerComplete?.onComplete()

            if (value <= stepSize) {
                field = value
                invalidate()
            }
        }
    var stepSize: Int = 15
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    var stepColor: Int = context.getColor(R.color.initStepColor)

    var isComplete: Boolean = false
        private set
        get() = currentStep == stepSize

    @ColorInt
    var defColor: Int = context.getColor(R.color.defStepColor)

    @ColorInt
    var currentColor: Int = context.getColor(R.color.white)

    @ColorInt
    var textColor: Int = context.getColor(R.color.black)

    private val currentPaint = Paint()
    private val steppedPaint = Paint()
    private val noneSteppedPaint = Paint()

    private val textPaint = TextPaint()
    private val currentTextPaint = TextPaint()

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepperView)
        stepSize = typedArray.getInt(R.styleable.StepperView_stepSize, stepSize)
        currentStep = typedArray.getInt(R.styleable.StepperView_currentStep, currentStep)
        stepColor = typedArray.getColor(R.styleable.StepperView_stepColor, stepColor)
        defColor = typedArray.getColor(R.styleable.StepperView_defColor, defColor)
        typedArray.recycle()

        currentPaint.style = Paint.Style.FILL
        currentPaint.color = currentColor
        currentPaint.strokeWidth = defCircleRadius / 2

        steppedPaint.style = Paint.Style.FILL
        steppedPaint.color = stepColor
        steppedPaint.strokeWidth = defCircleRadius / 2

        noneSteppedPaint.style = Paint.Style.FILL
        noneSteppedPaint.color = defColor
        noneSteppedPaint.strokeWidth = defCircleRadius / 2

        textPaint.color = textColor
        textPaint.textSize = defCircleRadius
        textPaint.textAlign = Paint.Align.CENTER

        currentTextPaint.color = currentColor
        currentTextPaint.textSize = defCircleRadius
        currentTextPaint.textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width: Int
        var height: Int

        //Measure Width
        width = if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            //Must be this size
            widthSize
        } else {
            -1
        }

        //Measure Height
        height = if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            //Must be this size
            heightSize
        } else {
            -1
        }
        if (height >= 0 && width >= 0) {
            width = height.coerceAtMost(width)
            height = width / 2
        } else if (width >= 0) {
            height = width / 2
        } else if (height >= 0) {
            width = height * 2
        } else {
            width = 0
            height = 0
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val t = (width / (defCircleRadius * 3)).toInt()
        Log.d("T12T", t.toString())
        stepSize = if (t > stepSize) {
            stepSize
        } else {
            t
        }
        stepSize = if ((stepSize < 1)) 1 else stepSize

        currentStep = if (currentStep > stepSize) stepSize else currentStep
        currentStep = if ((currentStep < 1)) 1 else currentStep
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        val startY = height / 2f
        var startX = defCircleRadius + defCircleRadius / 2

        val t = (width / (defCircleRadius * 3)).toInt()

        val multiply = stepSize.toMultiplyCount(t)

        startX += multiply * defCircleRadius

        val steppedXEnd = startX + defCircleRadius * (3 * (currentStep - 1))
        val noneSteppedXEnd = startX + defCircleRadius * (3 * (stepSize - 1))

        canvas.drawLine(startX, startY, steppedXEnd, startY, steppedPaint)
        canvas.drawLine(steppedXEnd, startY, noneSteppedXEnd, startY, noneSteppedPaint)

        for (i in 0 until stepSize) {
            canvas.drawCircle(startX, startY, defCircleRadius, if ((i + 1) <= currentStep) steppedPaint else noneSteppedPaint)
            if ((i + 1) == currentStep) {
                canvas.drawCircle(
                    startX, startY, defCircleRadius * 0.7f,
                    currentPaint
                )
                canvas.drawText("${i + 1}", startX, startY + defCircleRadius / 3, textPaint)
            } else {
                canvas.drawText("${i + 1}", startX, startY + defCircleRadius / 3, currentTextPaint)
            }
            startX += defCircleRadius * 3
        }
    }

    private fun Int.toMultiplyCount(maxCircleCount: Int): Int {
        var multiply = 3 * ((maxCircleCount - this) / 2)
        if (this % 2 == 0)
            multiply++
        return multiply
    }

    fun setOnCompleteListener(block: OnCompleteListener) {
        listenerComplete = block
    }

    fun restart() {
        currentStep = 1
    }

    fun interface OnCompleteListener {
        fun onComplete()
    }

    companion object {
        const val defCircleRadius = 40f
    }
}