package com.example.zad1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class DragableCircle(context: Context, attrs: AttributeSet) : View(context, attrs) {

    //flagging to know when we create the view
    private var smallerCircleX = -1F
    private var smallerCircleY = -1F
    private var smallerCircleRadius = 50F

    private var biggerCircleX = 0F
    private var biggerCircleY = 0F
    private var biggerCircleRadius = 0F
    private var draggingState = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isEventInsideSmallCircle(event)) {
                    draggingState = true
                    return true
                }
                return false
            }
            MotionEvent.ACTION_CANCEL -> {
                draggingState = false
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isEventInsideSmallCircle(event) && isEventInsideBigCircle(event) && draggingState
                ) {
                    smallerCircleX = event.x
                    smallerCircleY = event.y
                    invalidate()
                }
            }

        }
        return true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        biggerCircleX = (width / 2F)
        biggerCircleY = (height / 2F)
        biggerCircleRadius = getBiggerCircleRadius(canvas) - 50F

        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.STROKE

        }
        canvas.drawCircle(
            biggerCircleX,
            biggerCircleY,
            biggerCircleRadius,
            paint
        )

        drawSmallCircle(canvas)
    }

    private fun getBiggerCircleRadius(canvas: Canvas): Float {
        return if (canvas.width < canvas.height) {
            canvas.width / 2F - 30F
        } else {
            canvas.height / 2F - 30F
        }
    }

    private fun drawSmallCircle(canvas: Canvas) {

        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.STROKE

        }
        if (smallerCircleX == -1F && smallerCircleY == -1F) {
            canvas.drawCircle(
                biggerCircleX,
                biggerCircleY,
                smallerCircleRadius,
                paint
            )
            smallerCircleX = biggerCircleX
            smallerCircleY = biggerCircleY
            return
        }
        canvas.drawCircle(
            smallerCircleX,
            smallerCircleY,
            smallerCircleRadius,
            paint
        )
    }

    //I've thought about making one universal function
    // but it's much simpler if we break it down into two instead of one
    private fun isEventInsideSmallCircle(event: MotionEvent): Boolean {
        val dX = abs(smallerCircleX - event.x)
        val dY = abs(smallerCircleY - event.y)
        val r: Float = smallerCircleRadius
        return dX * dX + dY * dY <= r * r
    }

    private fun isEventInsideBigCircle(event: MotionEvent): Boolean {
        val dX = abs(biggerCircleX - event.x)
        val dY = abs(biggerCircleY - event.y)
        val r: Float = abs(biggerCircleRadius - smallerCircleRadius)
        return dX * dX + dY * dY <= r * r
    }
}