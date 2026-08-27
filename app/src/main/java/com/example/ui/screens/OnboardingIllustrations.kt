package com.example.ui.screens

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
 * Slide 1 Clean & Smooth Illustration: Smart Clipboard Capture Mascot
 * Lightweight, gentle single-float animation for 60fps buttery smooth performance.
 */
@Composable
fun OnboardingCaptureIllustration(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "capture_anim")

    // Single gentle float animation for optimal performance
    val floatOffset by transition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mascot_float"
    )

    Box(
        modifier = modifier.size(230.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(210.dp)) {
            val w = size.width
            val h = size.height
            val strokeWidth = 3f

            val navyDark = Color(0xFF1D4C6B)
            val peach = Color(0xFFF7B98D)
            val mint = Color(0xFFC8DED5)
            val lavender = Color(0xFFD9D5EA)
            val yellowOtp = Color(0xFFF5DF9B)
            val shadowColor = Color(0x141D4C6B)

            // Ground Shadow
            drawOval(
                color = shadowColor,
                topLeft = Offset(w * 0.22f, h * 0.86f),
                size = Size(w * 0.56f, h * 0.07f)
            )

            // Clipboard Backboard (Navy Tinted / Warm Tan)
            val boardLeft = w * 0.24f
            val boardTop = h * 0.16f
            val boardWidth = w * 0.52f
            val boardHeight = h * 0.66f

            drawRoundRect(
                color = Color(0xFFEFE7DC),
                topLeft = Offset(boardLeft, boardTop),
                size = Size(boardWidth, boardHeight),
                cornerRadius = CornerRadius(22f, 22f)
            )
            drawRoundRect(
                color = navyDark,
                topLeft = Offset(boardLeft, boardTop),
                size = Size(boardWidth, boardHeight),
                cornerRadius = CornerRadius(22f, 22f),
                style = Stroke(width = strokeWidth)
            )

            // Clipboard Top Clip
            val clipW = boardWidth * 0.46f
            val clipH = 20f
            val clipL = boardLeft + (boardWidth - clipW) / 2f
            val clipT = boardTop - 8f

            drawRoundRect(
                color = Color(0xFFDCD2C3),
                topLeft = Offset(clipL, clipT),
                size = Size(clipW, clipH),
                cornerRadius = CornerRadius(8f, 8f)
            )
            drawRoundRect(
                color = navyDark,
                topLeft = Offset(clipL, clipT),
                size = Size(clipW, clipH),
                cornerRadius = CornerRadius(8f, 8f),
                style = Stroke(width = strokeWidth)
            )
            drawCircle(
                color = navyDark,
                radius = 3.5f,
                center = Offset(clipL + clipW / 2f, clipT + clipH / 2f)
            )

            // Paper Mascot in Front (Peach with cute face & gentle floating offset)
            val noteLeft = boardLeft + 14f
            val noteTop = boardTop + 22f + floatOffset
            val noteWidth = boardWidth - 28f
            val noteHeight = boardHeight - 38f

            drawRoundRect(
                color = peach,
                topLeft = Offset(noteLeft, noteTop),
                size = Size(noteWidth, noteHeight),
                cornerRadius = CornerRadius(16f, 16f)
            )
            drawRoundRect(
                color = navyDark,
                topLeft = Offset(noteLeft, noteTop),
                size = Size(noteWidth, noteHeight),
                cornerRadius = CornerRadius(16f, 16f),
                style = Stroke(width = strokeWidth)
            )

            // Folded Flap on Note Top-Right
            val fold = 15f
            val foldPath = Path().apply {
                moveTo(noteLeft + noteWidth - fold, noteTop)
                lineTo(noteLeft + noteWidth, noteTop + fold)
                lineTo(noteLeft + noteWidth - fold, noteTop + fold)
                close()
            }
            drawPath(foldPath, color = Color(0xFFE29E6F))
            drawPath(foldPath, color = navyDark, style = Stroke(width = strokeWidth))

            // Cheerful Mascot Eyes
            val eyeY = noteTop + noteHeight * 0.40f
            drawCircle(
                color = navyDark,
                radius = 3.5f,
                center = Offset(noteLeft + noteWidth * 0.35f, eyeY)
            )
            drawCircle(
                color = navyDark,
                radius = 3.5f,
                center = Offset(noteLeft + noteWidth * 0.65f, eyeY)
            )

            // Cute Blush
            drawCircle(
                color = Color(0xFFE89382),
                radius = 3.5f,
                center = Offset(noteLeft + noteWidth * 0.23f, eyeY + 6f)
            )
            drawCircle(
                color = Color(0xFFE89382),
                radius = 3.5f,
                center = Offset(noteLeft + noteWidth * 0.77f, eyeY + 6f)
            )

            // Cheerful Smile
            val smilePath = Path().apply {
                moveTo(noteLeft + noteWidth * 0.43f, eyeY + 6f)
                quadraticBezierTo(
                    noteLeft + noteWidth * 0.5f,
                    eyeY + 14f,
                    noteLeft + noteWidth * 0.57f,
                    eyeY + 6f
                )
            }
            drawPath(smilePath, color = navyDark, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))

            // Body preview line on note
            drawLine(
                color = navyDark.copy(alpha = 0.25f),
                start = Offset(noteLeft + 16f, eyeY + 24f),
                end = Offset(noteLeft + noteWidth - 16f, eyeY + 24f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )

            // Satellite Badge 1: Mint Link (Left)
            val badge1X = w * 0.10f
            val badge1Y = h * 0.30f + (floatOffset * 0.7f)
            drawRoundRect(
                color = mint,
                topLeft = Offset(badge1X, badge1Y),
                size = Size(46f, 22f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            drawRoundRect(
                color = navyDark,
                topLeft = Offset(badge1X, badge1Y),
                size = Size(46f, 22f),
                cornerRadius = CornerRadius(10f, 10f),
                style = Stroke(width = 2.5f)
            )
            drawCircle(color = Color(0xFF163C3A), radius = 3f, center = Offset(badge1X + 11f, badge1Y + 11f))
            drawLine(
                color = Color(0xFF163C3A),
                start = Offset(badge1X + 18f, badge1Y + 11f),
                end = Offset(badge1X + 36f, badge1Y + 11f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )

            // Satellite Badge 2: Lavender Code (Right)
            val badge2X = w * 0.70f
            val badge2Y = h * 0.56f - (floatOffset * 0.7f)
            drawRoundRect(
                color = lavender,
                topLeft = Offset(badge2X, badge2Y),
                size = Size(48f, 22f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            drawRoundRect(
                color = navyDark,
                topLeft = Offset(badge2X, badge2Y),
                size = Size(48f, 22f),
                cornerRadius = CornerRadius(10f, 10f),
                style = Stroke(width = 2.5f)
            )
            drawCircle(color = Color(0xFF332857), radius = 3f, center = Offset(badge2X + 11f, badge2Y + 11f))
            drawLine(
                color = Color(0xFF332857),
                start = Offset(badge2X + 18f, badge2Y + 11f),
                end = Offset(badge2X + 38f, badge2Y + 11f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )

            // Subtle Sparkle Accent (Top Right)
            drawSparkle(Offset(w * 0.82f, h * 0.22f), 8f, yellowOtp, navyDark)
        }
    }
}

/**
 * Slide 2 Clean & Smooth Illustration: Visual Organization & Tags
 * Clean, lightweight 3-tier card stack.
 */
@Composable
fun OnboardingOrganizeIllustration(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "organize_anim")

    val floatCard by transition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_float"
    )

    Box(
        modifier = modifier.size(230.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(210.dp)) {
            val w = size.width
            val h = size.height
            val strokeWidth = 3f

            val navyDark = Color(0xFF1D4C6B)
            val peach = Color(0xFFF7B98D)
            val mint = Color(0xFFC8DED5)
            val lavender = Color(0xFFD9D5EA)
            val goldStar = Color(0xFFF5B700)
            val shadowColor = Color(0x141D4C6B)

            // Ground Shadow
            drawOval(
                color = shadowColor,
                topLeft = Offset(w * 0.16f, h * 0.85f),
                size = Size(w * 0.68f, h * 0.07f)
            )

            // Card 3: Back Card (Lavender)
            val c3W = w * 0.62f
            val c3H = h * 0.32f
            val c3X = w * 0.19f
            val c3Y = h * 0.20f - floatCard

            drawRoundRect(
                color = lavender,
                topLeft = Offset(c3X, c3Y),
                size = Size(c3W, c3H),
                cornerRadius = CornerRadius(18f, 18f)
            )
            drawRoundRect(
                color = navyDark,
                topLeft = Offset(c3X, c3Y),
                size = Size(c3W, c3H),
                cornerRadius = CornerRadius(18f, 18f),
                style = Stroke(width = strokeWidth)
            )
            drawCircle(color = Color(0xFF332857), radius = 3.5f, center = Offset(c3X + 16f, c3Y + 16f))
            drawLine(
                color = Color(0xFF332857).copy(alpha = 0.45f),
                start = Offset(c3X + 26f, c3Y + 16f),
                end = Offset(c3X + c3W - 20f, c3Y + 16f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )

            // Card 2: Middle Card (Mint)
            val c2W = w * 0.68f
            val c2H = h * 0.34f
            val c2X = w * 0.16f
            val c2Y = h * 0.32f + floatCard

            drawRoundRect(
                color = mint,
                topLeft = Offset(c2X, c2Y),
                size = Size(c2W, c2H),
                cornerRadius = CornerRadius(18f, 18f)
            )
            drawRoundRect(
                color = navyDark,
                topLeft = Offset(c2X, c2Y),
                size = Size(c2W, c2H),
                cornerRadius = CornerRadius(18f, 18f),
                style = Stroke(width = strokeWidth)
            )
            drawCircle(color = Color(0xFF163C3A), radius = 3.5f, center = Offset(c2X + 16f, c2Y + 18f))
            drawLine(
                color = Color(0xFF163C3A).copy(alpha = 0.45f),
                start = Offset(c2X + 28f, c2Y + 18f),
                end = Offset(c2X + c2W - 20f, c2Y + 18f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )

            // Card 1: Front Card (Peach)
            val c1W = w * 0.74f
            val c1H = h * 0.38f
            val c1X = w * 0.13f
            val c1Y = h * 0.44f - floatCard

            drawRoundRect(
                color = peach,
                topLeft = Offset(c1X, c1Y),
                size = Size(c1W, c1H),
                cornerRadius = CornerRadius(20f, 20f)
            )
            drawRoundRect(
                color = navyDark,
                topLeft = Offset(c1X, c1Y),
                size = Size(c1W, c1H),
                cornerRadius = CornerRadius(20f, 20f),
                style = Stroke(width = strokeWidth)
            )

            // Front Card Category Dot & Title
            drawCircle(color = Color(0xFF6F3C28), radius = 4.5f, center = Offset(c1X + 20f, c1Y + 22f))
            drawLine(
                color = Color(0xFF4A3525),
                start = Offset(c1X + 34f, c1Y + 22f),
                end = Offset(c1X + c1W * 0.55f, c1Y + 22f),
                strokeWidth = 3.5f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(0xFF4A3525).copy(alpha = 0.5f),
                start = Offset(c1X + 20f, c1Y + 40f),
                end = Offset(c1X + c1W - 20f, c1Y + 40f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(0xFF4A3525).copy(alpha = 0.35f),
                start = Offset(c1X + 20f, c1Y + 54f),
                end = Offset(c1X + c1W * 0.65f, c1Y + 54f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )

            // Pinned Pin / Star Badge on Top Right
            val starPivot = Offset(w * 0.82f, h * 0.22f)
            drawCircle(
                color = goldStar,
                radius = 16f,
                center = starPivot
            )
            drawCircle(
                color = navyDark,
                radius = 16f,
                center = starPivot,
                style = Stroke(width = 2.5f)
            )
            // Pin/Star center
            drawStar(starPivot, 8f, Color.White)
        }
    }
}

/**
 * Slide 3 Clean & Smooth Illustration: Fast Swipe & Quick Reuse
 * Gentle swipe offset and clean checkmark pill.
 */
@Composable
fun OnboardingReuseIllustration(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "reuse_anim")

    val swipeOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -26f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "swipe_offset"
    )

    Box(
        modifier = modifier.size(230.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(210.dp)) {
            val w = size.width
            val h = size.height
            val strokeWidth = 3f

            val navyDark = Color(0xFF1D4C6B)
            val peach = Color(0xFFF7B98D)
            val mint = Color(0xFFC8DED5)
            val terracotta = Color(0xFFE2594D)
            val shadowColor = Color(0x141D4C6B)

            // Ground Shadow
            drawOval(
                color = shadowColor,
                topLeft = Offset(w * 0.15f, h * 0.84f),
                size = Size(w * 0.7f, h * 0.07f)
            )

            // Background Delete Affordance (Revealed when card swiped left)
            val bgCardW = w * 0.78f
            val bgCardH = h * 0.40f
            val bgCardX = w * 0.11f
            val bgCardY = h * 0.28f

            drawRoundRect(
                color = terracotta,
                topLeft = Offset(bgCardX, bgCardY),
                size = Size(bgCardW, bgCardH),
                cornerRadius = CornerRadius(20f, 20f)
            )

            // Trash Icon inside revealed swipe area (on the right)
            val trashX = bgCardX + bgCardW - 24f
            val trashY = bgCardY + bgCardH / 2f
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(trashX - 7f, trashY - 7f),
                size = Size(14f, 16f),
                cornerRadius = CornerRadius(2f, 2f),
                style = Stroke(width = 2f)
            )
            drawLine(
                color = Color.White,
                start = Offset(trashX - 10f, trashY - 8f),
                end = Offset(trashX + 10f, trashY - 8f),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )

            // Foreground Card Swiping (Mint card)
            val fgCardX = bgCardX + swipeOffset
            val fgCardY = bgCardY

            drawRoundRect(
                color = mint,
                topLeft = Offset(fgCardX, fgCardY),
                size = Size(bgCardW, bgCardH),
                cornerRadius = CornerRadius(20f, 20f)
            )
            drawRoundRect(
                color = navyDark,
                topLeft = Offset(fgCardX, fgCardY),
                size = Size(bgCardW, bgCardH),
                cornerRadius = CornerRadius(20f, 20f),
                style = Stroke(width = strokeWidth)
            )

            // Category Dot & Title
            drawCircle(color = Color(0xFF163C3A), radius = 4.5f, center = Offset(fgCardX + 20f, fgCardY + 20f))
            drawLine(
                color = Color(0xFF164239),
                start = Offset(fgCardX + 34f, fgCardY + 20f),
                end = Offset(fgCardX + bgCardW * 0.45f, fgCardY + 20f),
                strokeWidth = 3.5f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(0xFF164239).copy(alpha = 0.5f),
                start = Offset(fgCardX + 20f, fgCardY + 40f),
                end = Offset(fgCardX + bgCardW - 20f, fgCardY + 40f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )

            // Prominent "Reuse" / Copied Capsule Badge floating above
            val reuseCenter = Offset(w * 0.5f, h * 0.74f)
            val reuseW = 104f
            val reuseH = 32f
            val reuseL = reuseCenter.x - (reuseW / 2f)
            val reuseT = reuseCenter.y - (reuseH / 2f)

            // Reuse Button Body
            drawRoundRect(
                color = navyDark,
                topLeft = Offset(reuseL, reuseT),
                size = Size(reuseW, reuseH),
                cornerRadius = CornerRadius(16f, 16f)
            )
            drawRoundRect(
                color = peach,
                topLeft = Offset(reuseL, reuseT),
                size = Size(reuseW, reuseH),
                cornerRadius = CornerRadius(16f, 16f),
                style = Stroke(width = 2f)
            )

            // Checkmark Icon on button
            val checkX = reuseL + 18f
            val checkY = reuseT + reuseH / 2f
            val checkPath = Path().apply {
                moveTo(checkX - 4f, checkY)
                lineTo(checkX - 1f, checkY + 3.5f)
                lineTo(checkX + 5f, checkY - 4f)
            }
            drawPath(
                path = checkPath,
                color = Color.White,
                style = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // "COPIED" Text Lines on button
            drawLine(
                color = Color.White,
                start = Offset(reuseL + 30f, reuseT + reuseH / 2f),
                end = Offset(reuseL + reuseW - 14f, reuseT + reuseH / 2f),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
    }
}

/**
 * Helper to draw a four-pointed sparkle star.
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSparkle(
    center: Offset,
    radius: Float,
    fillColor: Color,
    strokeColor: Color
) {
    val path = Path().apply {
        moveTo(center.x, center.y - radius)
        quadraticBezierTo(center.x, center.y, center.x + radius, center.y)
        quadraticBezierTo(center.x, center.y, center.x, center.y + radius)
        quadraticBezierTo(center.x, center.y, center.x - radius, center.y)
        quadraticBezierTo(center.x, center.y, center.x, center.y - radius)
        close()
    }
    drawPath(path, color = fillColor)
    drawPath(path, color = strokeColor, style = Stroke(width = 1.8f))
}

/**
 * Helper to draw a 5-pointed star.
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStar(
    center: Offset,
    radius: Float,
    color: Color
) {
    val innerRadius = radius * 0.45f
    val path = Path()
    val points = 5
    val step = Math.PI / points
    var angle = -Math.PI / 2

    for (i in 0 until 2 * points) {
        val r = if (i % 2 == 0) radius else innerRadius
        val x = (center.x + kotlin.math.cos(angle) * r).toFloat()
        val y = (center.y + kotlin.math.sin(angle) * r).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        angle += step
    }
    path.close()
    drawPath(path, color = color)
}
