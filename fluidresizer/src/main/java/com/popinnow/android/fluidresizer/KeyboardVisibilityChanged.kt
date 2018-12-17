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

/**
 * Event representing a resize via soft keyboard visibility toggle
 */
data class KeyboardVisibilityChanged internal constructor(
  /**
   * Is the soft keyboard visible
   */
  val visible: Boolean,

  /**
   * Current height of page content
   */
  val contentHeight: Int,

  /**
   * Original height of page content before resize event
   */
  val contentHeightBeforeResize: Int
)
