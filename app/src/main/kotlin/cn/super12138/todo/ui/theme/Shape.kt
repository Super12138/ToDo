package cn.super12138.todo.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.ListItemShapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Interpolatable
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

val ButtonShapes.hasRoundedCornerShapes: Boolean
    get() = shape is RoundedCornerShape && pressedShape is RoundedCornerShape

val ButtonShapes.hasCornerBasedShapes: Boolean
    get() = shape is CornerBasedShape && pressedShape is CornerBasedShape

val ListItemShapes.hasRoundedCornerShapes: Boolean
    get() =
        shape is RoundedCornerShape &&
                selectedShape is RoundedCornerShape &&
                pressedShape is RoundedCornerShape &&
                focusedShape is RoundedCornerShape &&
                hoveredShape is RoundedCornerShape &&
                draggedShape is RoundedCornerShape

val ListItemShapes.hasCornerBasedShapes: Boolean
    get() =
        shape is CornerBasedShape &&
                selectedShape is CornerBasedShape &&
                pressedShape is CornerBasedShape &&
                focusedShape is CornerBasedShape &&
                hoveredShape is CornerBasedShape &&
                draggedShape is CornerBasedShape

/**
 * 来自 Compose 内部类
 *
 * 根据交互状态返回对应的形状
 *
 * * 如果 *原形状* 和 *按下形状* **均为圆角形状**则在形状变换时使用动画过渡
 *
 * @param shapes 按钮形状（需包含原形状和按下形状）
 * @param pressed 是否处于按下状态
 * @param animationSpec 动画曲线
 */
@Composable
fun shapeByInteraction(
    shapes: ButtonShapes,
    pressed: Boolean,
    animationSpec: FiniteAnimationSpec<Float>,
): Shape {
    val shape =
        if (pressed) {
            shapes.pressedShape
        } else {
            shapes.shape
        }
    if (shapes.hasRoundedCornerShapes)
        return key(shapes) { rememberAnimatedShape(shape as RoundedCornerShape, animationSpec) }
    else if (shapes.hasCornerBasedShapes)
        return key(shapes) { rememberAnimatedShape(shape as CornerBasedShape, animationSpec) }

    return shape
}

/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Stable
class AnimatedShapeState(
    initialShape: CornerBasedShape,
    val spec: FiniteAnimationSpec<Float>,
) {
    var startShape: CornerBasedShape = initialShape
    var targetShape: CornerBasedShape = initialShape
    val progress = Animatable(1f)

    private var cachedProgress: Float = -1f
    private var cachedMorphedShape: CornerBasedShape? = null

    /**
     * Returns the interpolated shape based on [progress], caching the result to avoid allocations.
     *
     * This memoization avoids redundant calculations and allocations when querying the morphed
     * shape multiple times during the same frame (e.g., in layout and draw phases).
     *
     * @return the interpolated [CornerBasedShape]
     */
    fun getMorphedShape(): CornerBasedShape {
        val currentProgress = progress.value
        val cached = cachedMorphedShape

        if (currentProgress == cachedProgress && cached != null) {
            return cached
        }

        val morphed =
            Interpolatable.lerp(startShape, targetShape, currentProgress) as CornerBasedShape
        cachedProgress = currentProgress
        cachedMorphedShape = morphed
        return morphed
    }

    suspend fun animateToShape(newTarget: CornerBasedShape) {
        if (targetShape == newTarget) return

        if (newTarget == startShape) {
            // The user reversed their action before the animation finished.
            // To preserve a smooth momentum, we flip the progress and reverse the velocity.
            startShape = targetShape
            targetShape = newTarget
            cachedMorphedShape = null

            val p = progress.value
            val v = progress.velocity

            progress.snapTo(1f - p)
            progress.animateTo(1f, spec, initialVelocity = -v)
        } else {
            // A new target. We must freeze the currently visible shape so we can morph gracefully
            // towards the new one.
            val currentProgress = progress.value
            val capturedStart = startShape
            val capturedTarget = targetShape

            // Check if we are at the exact boundaries to prevent deep nesting of
            // interpolated shapes which could otherwise lead to a StackOverflowError if
            // the shape is toggled repeatedly.
            startShape =
                when (currentProgress) {
                    1f -> capturedTarget
                    0f -> capturedStart
                    else -> {
                        Interpolatable.lerp(capturedStart, capturedTarget, currentProgress)
                                as CornerBasedShape
                    }
                }

            targetShape = newTarget
            cachedMorphedShape = null
            progress.snapTo(0f)
            progress.animateTo(1f, spec)
        }
    }
}

@Composable
fun rememberAnimatedShape(state: AnimatedShapeState): Shape {
    return remember(state) {
        object : ShapeWithHorizontalCenterOptically {
            override fun createOutline(
                size: Size,
                layoutDirection: LayoutDirection,
                density: Density,
            ): Outline {
                // Returns the cached shape or allocates a new one if progress advanced
                val morphedShape = state.getMorphedShape()

                // Delegate outline creation to the morphed shape
                return morphedShape.createOutline(size, layoutDirection, density)
            }

            override fun offset(size: Size, density: Density): Float {
                val range = 0f..(size.height / 2f)
                val morphedShape = state.getMorphedShape()

                val tsVal = morphedShape.topStart.toPx(size, density).coerceIn(range)
                val teVal = morphedShape.topEnd.toPx(size, density).coerceIn(range)
                val bsVal = morphedShape.bottomStart.toPx(size, density).coerceIn(range)
                val beVal = morphedShape.bottomEnd.toPx(size, density).coerceIn(range)

                return CenterOpticallyCoefficient *
                        (((tsVal + bsVal) / 2f) - ((teVal + beVal) / 2f))
            }
        }
    }
}

/**
 * Resolves and remembers a [Shape] that smoothly morphs between different [CornerBasedShape]s.
 *
 * Note that this animation utility is designed specifically for animating the corner sizes of the
 * same shape family (e.g., from a [androidx.compose.foundation.shape.RoundedCornerShape] to another
 * [androidx.compose.foundation.shape.RoundedCornerShape]). If the provided shapes belong to
 * different families (e.g., from a [androidx.compose.foundation.shape.CutCornerShape] to a
 * [androidx.compose.foundation.shape.RoundedCornerShape]), no smooth interpolation will occur, and
 * the shape will immediately snap to the target shape.
 *
 * @param currentShape the current [CornerBasedShape] to display or morph to
 * @param animationSpec the [FiniteAnimationSpec] to use for the morphing animation
 * @return a [Shape] that smoothly animates the corner radii
 */
@Composable
fun rememberAnimatedShape(
    currentShape: CornerBasedShape,
    animationSpec: FiniteAnimationSpec<Float>,
): Shape {
    val state = remember(animationSpec) {
        AnimatedShapeState(initialShape = currentShape, spec = animationSpec)
    }

    LaunchedEffect(currentShape, state) { state.animateToShape(currentShape) }

    return rememberAnimatedShape(state)
}


/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
interface ShapeWithHorizontalCenterOptically : Shape {
    fun offset(size: Size, density: Density): Float
}

const val CenterOpticallyCoefficient = 0.11f


@Composable
fun ListItemShapes.shapeForInteraction(
    selected: Boolean,
    pressed: Boolean,
    focused: Boolean,
    hovered: Boolean,
    dragged: Boolean,
    animationSpec: FiniteAnimationSpec<Float>,
): Shape {
    val shape =
        when {
            pressed -> pressedShape
            dragged -> draggedShape
            selected -> selectedShape
            focused -> focusedShape
            hovered -> hoveredShape
            else -> shape
        }

    if (hasRoundedCornerShapes) {
        return key(this) { rememberAnimatedShape(shape as RoundedCornerShape, animationSpec) }
    } else if (hasCornerBasedShapes) {
        return key(this) { rememberAnimatedShape(shape as CornerBasedShape, animationSpec) }
    }

    return shape
}
