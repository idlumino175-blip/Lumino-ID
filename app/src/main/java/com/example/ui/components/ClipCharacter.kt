package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Friendly custom SVG clip mascot with warm peach body (#f7b98d),
 * deep navy outlines (#1d4c6b), and expressive animated eyes.
 * Reserved exclusively for empty states and onboarding flows.
 */
@Composable
fun ClipCharacter(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "clip_bounce")
    val bounceY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    val blinkScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink"
    )

    Box(
        modifier = modifier.size(110.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            val w = size.width
            val h = size.height
            val strokeWidth = 3.5f

            val peachBody = Color(0xFFF7B98D)
            val navyOutline = Color(0xFF1D4C6B)
            val blushPink = Color(0xFFE89382)
            val shadowColor = Color(0x1A1D4C6B)

            // Ground Shadow
            drawOval(
                color = shadowColor,
                topLeft = Offset(w * 0.2f, h * 0.88f),
                size = Size(w * 0.6f, h * 0.08f)
            )

            // Body offset from bounce
            val bodyTop = h * 0.18f + bounceY
            val bodyWidth = w * 0.54f
            val bodyHeight = h * 0.62f
            val bodyLeft = (w - bodyWidth) / 2f

            // Paper / Note Body
            drawRoundRect(
                color = peachBody,
                topLeft = Offset(bodyLeft, bodyTop),
                size = Size(bodyWidth, bodyHeight),
                cornerRadius = CornerRadius(20f, 20f)
            )

            // Body Outline
            drawRoundRect(
                color = navyOutline,
                topLeft = Offset(bodyLeft, bodyTop),
                size = Size(bodyWidth, bodyHeight),
                cornerRadius = CornerRadius(20f, 20f),
                style = Stroke(width = strokeWidth)
            )

            // Decorative folded corner at top-right
            val foldSize = 14f
            val foldPath = Path().apply {
                moveTo(bodyLeft + bodyWidth - foldSize, bodyTop)
                lineTo(bodyLeft + bodyWidth, bodyTop + foldSize)
                lineTo(bodyLeft + bodyWidth - foldSize, bodyTop + foldSize)
                close()
            }
            drawPath(foldPath, color = Color(0xFFE5A172))
            drawPath(foldPath, color = navyOutline, style = Stroke(width = strokeWidth))

            // Cheeks (Blush)
            drawCircle(
                color = blushPink,
                radius = 4f,
                center = Offset(bodyLeft + bodyWidth * 0.22f, bodyTop + bodyHeight * 0.52f)
            )
            drawCircle(
                color = blushPink,
                radius = 4f,
                center = Offset(bodyLeft + bodyWidth * 0.78f, bodyTop + bodyHeight * 0.52f)
            )

            // Eyes
            val eyeRadius = 3.5f
            val eyeY = bodyTop + bodyHeight * 0.44f
            drawOval(
                color = navyOutline,
                topLeft = Offset(bodyLeft + bodyWidth * 0.32f - eyeRadius, eyeY - (eyeRadius * blinkScale)),
                size = Size(eyeRadius * 2, eyeRadius * 2 * blinkScale)
            )
            drawOval(
                color = navyOutline,
                topLeft = Offset(bodyLeft + bodyWidth * 0.68f - eyeRadius, eyeY - (eyeRadius * blinkScale)),
                size = Size(eyeRadius * 2, eyeRadius * 2 * blinkScale)
            )

            // Smile
            val smilePath = Path().apply {
                moveTo(bodyLeft + bodyWidth * 0.42f, bodyTop + bodyHeight * 0.54f)
                quadraticBezierTo(
                    bodyLeft + bodyWidth * 0.5f,
                    bodyTop + bodyHeight * 0.64f,
                    bodyLeft + bodyWidth * 0.58f,
                    bodyTop + bodyHeight * 0.54f
                )
            }
            drawPath(
                path = smilePath,
                color = navyOutline,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Paperclip on top-left of the card
            val clipLeft = bodyLeft + 4f
            val clipTop = bodyTop - 10f
            val clipPath = Path().apply {
                moveTo(clipLeft + 12f, clipTop + 24f)
                lineTo(clipLeft + 12f, clipTop + 8f)
                quadraticBezierTo(clipLeft + 12f, clipTop, clipLeft + 6f, clipTop)
                quadraticBezierTo(clipLeft, clipTop, clipLeft, clipTop + 8f)
                lineTo(clipLeft, clipTop + 28f)
                quadraticBezierTo(clipLeft, clipTop + 36f, clipLeft + 8f, clipTop + 36f)
                quadraticBezierTo(clipLeft + 16f, clipTop + 36f, clipLeft + 16f, clipTop + 28f)
                lineTo(clipLeft + 16f, clipTop + 14f)
            }
            drawPath(
                path = clipPath,
                color = Color(0xFFC8DED5),
                style = Stroke(width = strokeWidth + 1.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            drawPath(
                path = clipPath,
                color = navyOutline,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}
