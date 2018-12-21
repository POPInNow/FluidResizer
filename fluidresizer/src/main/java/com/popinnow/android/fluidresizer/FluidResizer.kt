/*
 * Copyright (C) 2018 POP Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.popinnow.android.fluidresizer

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.popinnow.android.fluidresizer.internal.ActivityViewHolder
import com.popinnow.android.fluidresizer.internal.KeyboardVisibilityDetector

/**
 * This class attaches to Activity instances and watches them for resize events
 *
 * Resize events are generally caused by the soft keyboard, though this is not an exhaustive list
 * of potential causes
 */
object FluidResizer {

  private var heightAnimator: ValueAnimator? = null
  private val DEFAULT_ON_CHANGE: (event: KeyboardVisibilityChanged) -> Unit = {}

  /**
   * Listen for resize events and react to them with a smooth animation
   *
   * @param activity Activity instance
   * @param onChange Optional callback to handle additional reactions to screen resize events
   */
  @JvmStatic
  @JvmOverloads
  fun listen(
    activity: Activity,
    onChange: (event: KeyboardVisibilityChanged) -> Unit = DEFAULT_ON_CHANGE
  ) {
    beginListening(activity, onChange)
  }

  private inline fun beginListening(
    activity: Activity,
    crossinline onChange: (event: KeyboardVisibilityChanged) -> Unit
  ) {
    val viewHolder = ActivityViewHolder.createFrom(activity)

    KeyboardVisibilityDetector.listen(viewHolder) {
      animateHeight(viewHolder, it)
      onChange(it)
    }
    viewHolder.onDetach {
      heightAnimator?.cancel()
    }
  }

  private fun animateHeight(
    viewHolder: ActivityViewHolder,
    event: KeyboardVisibilityChanged
  ) {
    val contentView = viewHolder.contentView
    contentView.setHeight(event.contentHeightBeforeResize)

    heightAnimator?.cancel()

    // Warning: animating height might not be very performant. Try turning on
    // "Profile GPI rendering" in developer options and check if the bars stay
    // under 16ms in your app. Using Transitions API would be more efficient, but
    // for some reason it skips the first animation and I cannot figure out why.
    val animator = ObjectAnimator.ofInt(event.contentHeightBeforeResize, event.contentHeight)
        .apply {
          interpolator = FastOutSlowInInterpolator()
          duration = 300
        }
    animator.addUpdateListener { contentView.setHeight(it.animatedValue as Int) }
    animator.start()
    heightAnimator = animator
  }

  private fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
  }
}
