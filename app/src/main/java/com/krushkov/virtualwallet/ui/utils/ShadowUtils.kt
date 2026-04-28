package com.krushkov.virtualwallet.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.outerShadow(
    shape: Shape,
    color: Color = Color.Black.copy(alpha = 0.15f),
    offsetX: Dp = 5.dp,
    offsetY: Dp = 5.dp,
    blur: Dp = 5.dp
) = this.drawBehind {
    val shadowOutline = shape.createOutline(size, layoutDirection, this)
    
    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        if (blur.toPx() > 0) {
            frameworkPaint.maskFilter = android.graphics.BlurMaskFilter(blur.toPx(), android.graphics.BlurMaskFilter.Blur.NORMAL)
        }
        frameworkPaint.color = color.toArgb()
        
        canvas.save()
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}

fun Modifier.innerShadow(
    shape: Shape,
    color: Color = Color.Black.copy(alpha = 0.15f),
    offsetX: Dp = 5.dp,
    offsetY: Dp = 5.dp,
    blur: Dp = 5.dp
) = this.drawWithContent {
    drawContent()
    
    val outline = shape.createOutline(size, layoutDirection, this)
    
    drawIntoCanvas { canvas ->
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            this.isAntiAlias = true
            if (blur.toPx() > 0) {
                this.maskFilter = android.graphics.BlurMaskFilter(blur.toPx(), android.graphics.BlurMaskFilter.Blur.NORMAL)
            }
            this.style = android.graphics.Paint.Style.STROKE
            this.strokeWidth = blur.toPx() * 2
        }
        
        val nativeCanvas = canvas.nativeCanvas
        val androidPath = android.graphics.Path().apply {
            if (outline is androidx.compose.ui.graphics.Outline.Rectangle) {
                addRect(outline.rect.left, outline.rect.top, outline.rect.right, outline.rect.bottom, android.graphics.Path.Direction.CW)
            } else if (outline is androidx.compose.ui.graphics.Outline.Rounded) {
                val rr = outline.roundRect
                addRoundRect(
                    rr.left, rr.top, rr.right, rr.bottom,
                    floatArrayOf(
                        rr.topLeftCornerRadius.x, rr.topLeftCornerRadius.y, 
                        rr.topRightCornerRadius.x, rr.topRightCornerRadius.y, 
                        rr.bottomRightCornerRadius.x, rr.bottomRightCornerRadius.y, 
                        rr.bottomLeftCornerRadius.x, rr.bottomLeftCornerRadius.y
                    ),
                    android.graphics.Path.Direction.CW
                )
            }
        }
        
        canvas.save()
        // Clip to the shape so shadow is only inside
        val clipPath = androidx.compose.ui.graphics.Path().apply {
            if (outline is androidx.compose.ui.graphics.Outline.Rectangle) {
                addRect(outline.rect)
            } else if (outline is androidx.compose.ui.graphics.Outline.Rounded) {
                addRoundRect(outline.roundRect)
            }
        }
        canvas.clipPath(clipPath)
        
        nativeCanvas.translate(offsetX.toPx(), offsetY.toPx())
        nativeCanvas.drawPath(androidPath, paint)
        
        canvas.restore()
    }
}
