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

package com.popinnow.android.fluidresizer.internal

import android.view.ViewTreeObserver
import com.popinnow.android.fluidresizer.KeyboardVisibilityChanged

internal object KeyboardVisibilityDetector {

  internal fun listen(
    viewHolder: ActivityViewHolder,
    listener: (event: KeyboardVisibilityChanged) -> Unit
  ) {
    val detector = Detector(viewHolder, listener)
    viewHolder.nonResizableLayout.viewTreeObserver.addOnPreDrawListener(detector)
    viewHolder.onDetach {
      viewHolder.nonResizableLayout.viewTreeObserver.removeOnPreDrawListener(detector)
    }
  }

  private class Detector internal constructor(
    private val viewHolder: ActivityViewHolder,
    private val listener: (event: KeyboardVisibilityChanged) -> Unit
  ) : ViewTreeObserver.OnPreDrawListener {

    private var previousHeight: Int = -1

    override fun onPreDraw(): Boolean {
      val detected = detect()

      // The layout flickers for a moment, usually on the first
      // animation. Intercepting this pre-draw seems to solve the problem.
      return !detected
    }

    private fun detect(): Boolean {
      val contentHeight = viewHolder.resizableLayout.height
      if (contentHeight == previousHeight) {
        return false
      }

      if (previousHeight != -1) {
        val isKeyboardVisible = contentHeight < previousHeight

        listener(
          KeyboardVisibilityChanged(
            visible = isKeyboardVisible,
            contentHeight = contentHeight,
            contentHeightBeforeResize = previousHeight
          )
        )
      }

      previousHeight = contentHeight
      return true
    }
  }
}
