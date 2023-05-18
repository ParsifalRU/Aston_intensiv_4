package com.example.android_intensiv_4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.icu.util.Calendar
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ClockView : View {
    private var height = 0
    private var width = 0
    private var padding = 0
    private var fontSize = 0
    private val numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private var isInit = false
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()
    private var minSize = 0
    private var strokeWidth = 0f
    private var hourHandColor = 0
    private var minuteHandColor = 0
    private var secondHandColor = 0
    private var hourHandHeight = 0
    private var minuteHandHeight = 0
    private var secondHandHeight = 0
    private var hourHandWidth = 0
    private var minuteHandWidth = 0
    private var secondHandWidth = 0
    private var colorHand = 0

    constructor(context: Context?) : super(context){
        getAttributes(null, null)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        getAttributes(attrs, null)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        getAttributes(attrs, defStyleAttr)
    }

    private fun getAttributes(attrs: AttributeSet?, defStyleAttr: Int?){
        val attributeView = context?.obtainStyledAttributes(
            attrs,
            R.styleable.ClockView)
        try{
            hourHandColor = attributeView
                ?.getColor(R.styleable.ClockView_hourHandColor, Color.BLACK) ?: Color.BLACK
            minuteHandColor = attributeView
                ?.getColor(R.styleable.ClockView_minuteHandColor, Color.BLACK) ?: Color.BLACK
            secondHandColor = attributeView
                ?.getColor(R.styleable.ClockView_secondHandColor, Color.BLACK) ?: Color.BLACK
            hourHandHeight = attributeView
                ?.getDimensionPixelSize(R.styleable.ClockView_hourHandSize, 0) ?: 0
            minuteHandHeight = attributeView
                ?.getDimensionPixelSize(R.styleable.ClockView_minuteHandSize, 0) ?: 0
            secondHandHeight = attributeView
                ?.getDimensionPixelSize(R.styleable.ClockView_secondHandSize, 0) ?: 0
            hourHandWidth = attributeView
                ?.getDimensionPixelSize(R.styleable.ClockView_hourHandWidth, 0) ?: 0
            minuteHandWidth = attributeView
                ?.getDimensionPixelSize(R.styleable.ClockView_minuteHandWidth, 0) ?: 0
            secondHandWidth = attributeView
                ?.getDimensionPixelSize(R.styleable.ClockView_secondHandWidth, 0) ?: 0
        }
        finally{
            attributeView?.recycle()
        }
    }

    private fun initClock() {

        height = getHeight()
        width = getWidth()
        minSize = min(height, width)
        padding = numeralSpacing + minSize / 8
        fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            (minSize / 30.0).toFloat(),
            resources.displayMetrics
        ).toInt()
        radius = minSize / 2 - padding
        handTruncation = minSize / 20
        hourHandTruncation = minSize / 7
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initClock()
        }
        canvas.drawColor(Color.WHITE)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawCircleDoter(canvas)
        drawHands(canvas)
        postInvalidateDelayed(500)
        drawCircle(canvas)
    }


    private fun drawHand(
        canvas: Canvas,
        loc: Double,
        isHour: Boolean,
        isMinute: Boolean,
        isSecond: Boolean
    ) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        var handRadius = 0
        when {
            isHour -> {
                handRadius = radius - handTruncation - hourHandTruncation + hourHandWidth
                strokeWidth = (minSize/ 40.0).toFloat() + hourHandHeight
                colorHand = hourHandColor
            }
            isMinute -> {
                handRadius = radius - handTruncation - hourHandTruncation / 2 + minuteHandWidth
                strokeWidth = (minSize/90.0).toFloat() + minuteHandHeight
                colorHand = minuteHandColor
            }
            isSecond -> {
                handRadius = radius - handTruncation  + secondHandWidth
                strokeWidth = (minSize/200.0).toFloat() + secondHandHeight
                colorHand = secondHandColor
            }
        }
        canvas.drawLine(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2 + cos(angle) * handRadius).toFloat(),
            (height / 2 + sin(angle) * handRadius).toFloat(),
            Paint().apply {
                color = colorHand
                strokeWidth = this@ClockView.strokeWidth
            }
        )
    }


    private fun drawHands(canvas: Canvas) {
        val calendar = Calendar.getInstance()
        var hour = calendar[Calendar.HOUR_OF_DAY].toFloat()
        hour = if (hour > 12) hour - 12 else hour
        drawHand(canvas, ((hour + calendar[Calendar.MINUTE] / 60) * 5f).toDouble(),
            isHour = true,
            isMinute = false,
            isSecond = false
        )
        drawHand(canvas, calendar[Calendar.MINUTE].toDouble(),
            isHour = false,
            isMinute = true,
            isSecond = false
        )
        drawHand(canvas, calendar[Calendar.SECOND].toDouble(),
            isHour = false,
            isMinute = false,
            isSecond = true
        )
    }

    private val paintNumeral by lazy {
        Paint().apply {
            textSize = fontSize.toFloat()
        }
    }

    private fun drawNumeral(canvas: Canvas) {
        for (number in numbers) {
            val tmp = number.toString()
            paintNumeral.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (width / 2 + cos(angle) * (radius - minSize/10)  - rect.width() / 2).toInt()
            val y = (height / 2 + sin(angle) * (radius - minSize/10)  + rect.height() / 2).toInt()
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paintNumeral)
        }
    }

    private val paintCenter by lazy {
        Paint().apply {
            style = Paint.Style.FILL
        }
    }

    private fun drawCenter(canvas: Canvas) {
        canvas.drawCircle(
            (width / 2.0).toFloat(),
            (height / 2.0).toFloat(),
            (minSize/ 90.0).toFloat(),
            paintCenter)
    }

    private val paintCircleDoter by lazy {
        Paint().apply {
            style = Paint.Style.FILL
        }
    }

    private fun drawCircleDoter(canvas: Canvas){
        var doterRadius: Float
        for (i in 1 .. 60) {
            val angle = Math.PI / 30 * (i - 1)
            val x = (width / 2 + cos(angle) * radius).toInt()
            val y = (height / 2 + sin(angle) * radius).toInt()
            doterRadius = if ((i-1) % 5 == 0){
                (minSize / 90.0).toFloat()
            }else{
                (minSize / 125.0).toFloat()
            }
            canvas.drawCircle(x.toFloat(), y.toFloat(), doterRadius,  paintCircleDoter)
        }
    }

    private val paintCircle by lazy {
        Paint().apply {
            color = ContextCompat.getColor(context, R.color.black)
            strokeWidth = (minSize/25.0).toFloat()
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    }

    private fun drawCircle(canvas: Canvas) {
        canvas.drawCircle(
            (width / 2.0).toFloat(),
            (height / 2.0).toFloat(),
            (radius + padding - (minSize/25.0).toFloat()),
            paintCircle
        )
    }
}
