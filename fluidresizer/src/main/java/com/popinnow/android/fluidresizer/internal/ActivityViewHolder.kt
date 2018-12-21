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

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.CheckResult

internal data class ActivityViewHolder internal constructor(
  internal val nonResizableLayout: ViewGroup,
  internal val resizableLayout: ViewGroup,
  internal val contentView: ViewGroup
) {

  @CheckResult
  internal inline fun onDetach(crossinline onDetach: () -> Unit): Listener {
    val listener = object : View.OnAttachStateChangeListener {
      override fun onViewDetachedFromWindow(v: View?) {
        onDetach()
      }

      override fun onViewAttachedToWindow(v: View?) {}
    }
    nonResizableLayout.addOnAttachStateChangeListener(listener)

    return object : Listener {
      override fun stopListening() {
        nonResizableLayout.removeOnAttachStateChangeListener(listener)
      }
    }
  }

  companion object {

    /**
     * The Activity View tree usually looks like this:
     *
     * DecorView <- does not get resized, contains space for system Ui bars.
     * - LinearLayout
     * -- FrameLayout <- gets resized
     * --- LinearLayout
     * ---- Activity content
     */
    internal fun createFrom(activity: Activity): ActivityViewHolder {
      val decorView = activity.window.decorView as ViewGroup
      val contentView = decorView.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
      val actionBarRootLayout = contentView.parent as ViewGroup
      val resizableLayout = actionBarRootLayout.parent as ViewGroup

      return ActivityViewHolder(
          nonResizableLayout = decorView,
          resizableLayout = resizableLayout,
          contentView = contentView
      )
    }
  }
}
