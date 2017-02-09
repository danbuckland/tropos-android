package com.thoughtbot.tropos.commons

import android.animation.Animator
import android.content.Context
import android.graphics.Point
import android.support.annotation.IdRes
import android.transition.TransitionValues
import android.transition.Visibility
import android.util.AttributeSet
import android.view.View
import android.view.View.NO_ID
import android.view.ViewAnimationUtils
import android.view.ViewGroup


class CircularReveal : Visibility {

  var center: Point? = null
  var startRadius: Float = 0.toFloat()
  var endRadius: Float = 0.toFloat()
  @IdRes var centerOnId = NO_ID
  var centerOn: android.view.View? = null

  constructor() : super() {
    excludeTarget(android.R.id.statusBarBackground, true)
    excludeTarget(android.R.id.navigationBarBackground, true)
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    startRadius = 0f
    endRadius = 0f
    centerOnId = NO_ID
    excludeTarget(android.R.id.statusBarBackground, true)
    excludeTarget(android.R.id.navigationBarBackground, true)
  }

  override fun onAppear(sceneRoot: ViewGroup, view: android.view.View?,
      startValues: TransitionValues,
      endValues: TransitionValues): Animator? {
    if (view == null || view.getHeight() === 0 || view.getWidth() === 0) return null
    ensureCenterPoint(sceneRoot, view)
    val animator = WrapAnimator(ViewAnimationUtils.createCircularReveal(
        view,
        center!!.x,
        center!!.y,
        startRadius,
        getFullyRevealedRadius(view)))
    animator.duration = 500
    return animator
  }

  override fun onDisappear(sceneRoot: ViewGroup, view: android.view.View?,
      startValues: TransitionValues,
      endValues: TransitionValues): Animator? {
    if (view == null || view.getHeight() === 0 || view.getWidth() === 0) return null
    ensureCenterPoint(sceneRoot, view)
    val animator = WrapAnimator(ViewAnimationUtils.createCircularReveal(
        view,
        center!!.x,
        center!!.y,
        getFullyRevealedRadius(view),
        endRadius))
    animator.duration = 500
    return animator
  }

  private fun ensureCenterPoint(sceneRoot: ViewGroup, view: android.view.View) {
    if (center != null) return
    if (centerOn != null || centerOnId != NO_ID) {
      val source: android.view.View?
      if (centerOn != null) {
        source = centerOn
      } else {
        source = sceneRoot.findViewById(centerOnId)
      }
      if (source != null) {
        // use window location to allow views in diff hierarchies
        val loc = IntArray(2)
        source.getLocationInWindow(loc)
        val srcX = loc[0] + source.getWidth() / 2
        val srcY = loc[1] + source.getHeight() / 2
        view.getLocationInWindow(loc)
        center = Point(srcX - loc[0], srcY - loc[1])
      }
    }
    // else use the pivot point
    if (center == null) {
//      center = Point(Math.round(view.getPivotX()), Math.round(view.getPivotY()))
      center = startPoint(view)
    }
  }

  private fun getFullyRevealedRadius(view: android.view.View): Float {
    return Math.hypot(
        Math.max(center!!.x, view.getWidth() - center!!.x).toDouble(),
        Math.max(center!!.y, view.getHeight() - center!!.y).toDouble()).toFloat()
  }

  private fun startPoint(view: View): Point {
    val attrs = intArrayOf(android.R.attr.actionBarSize)
    val actionBarSize = view.context.theme.obtainStyledAttributes(attrs).getDimension(0, 0F)
    val x = (view.right - (actionBarSize / 2)).toInt()
    val y = (actionBarSize / 2).toInt()
    return Point(x, y)
  }
}

