/*
 * Copyright (C) 2019 POP Inc.
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

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.popinnow.android.fluidresizer.internal.FluidResizeListener

/**
 * This class attaches to Activity instances and watches them for resize events
 *
 * Resize events are generally caused by the soft keyboard, though this is not an exhaustive list
 * of potential causes
 */
object FluidResizer {

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
    activity: FragmentActivity,
    onChange: (event: KeyboardVisibilityChanged) -> Unit = DEFAULT_ON_CHANGE
  ) {
    listen(activity, activity, onChange)
  }

  /**
   * Listen for resize events and react to them with a smooth animation
   *
   * @param activity Activity instance
   * @param lifecycleOwner LifecycleOwner instance
   * @param onChange Optional callback to handle additional reactions to screen resize events
   */
  @JvmStatic
  @JvmOverloads
  fun listen(
    activity: Activity,
    lifecycleOwner: LifecycleOwner,
    onChange: (event: KeyboardVisibilityChanged) -> Unit = DEFAULT_ON_CHANGE
  ) {
    listen(activity, lifecycleOwner.lifecycle, onChange)
  }

  /**
   * Listen for resize events and react to them with a smooth animation
   *
   * @param activity Activity instance
   * @param lifecycle Lifecycle instance
   * @param onChange Optional callback to handle additional reactions to screen resize events
   */
  @JvmStatic
  @JvmOverloads
  fun listen(
    activity: Activity,
    lifecycle: Lifecycle,
    onChange: (event: KeyboardVisibilityChanged) -> Unit = DEFAULT_ON_CHANGE
  ) {
    FluidResizeListener(activity, lifecycle)
        .beginListening(onChange)
  }

}
