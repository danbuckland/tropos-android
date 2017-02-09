package com.thoughtbot.tropos.commons

import android.animation.Animator
import android.animation.TimeInterpolator
import android.content.Context
import android.transition.TransitionValues
import android.transition.Visibility
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils.createCircularReveal
import android.view.ViewGroup


class RevealTransition : Visibility {

  constructor()

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


  override fun getTransitionProperties(): Array<out String> {
    return super.getTransitionProperties()
  }

  override fun isTransitionRequired(startValues: TransitionValues?,
      newValues: TransitionValues?): Boolean {
    return super.isTransitionRequired(startValues, newValues)
  }

  override fun onAppear(sceneRoot: ViewGroup, view: View, startValues: TransitionValues?,
      endValues: TransitionValues?): Animator {
    return revealAnimator(sceneRoot)
  }

  override fun onDisappear(sceneRoot: ViewGroup, view: View,
      startValues: TransitionValues?, endValues: TransitionValues?): Animator {
    return collapseAnimator(sceneRoot)
  }

  private fun collapseAnimator(view: ViewGroup): Animator {
    val attrs = intArrayOf(android.R.attr.actionBarSize)
    val actionBarSize = view.context.theme.obtainStyledAttributes(attrs).getDimension(0, 0F)
    val offset = (actionBarSize / 2).toInt()
    return ciruclarAnimator(view, offset, offset, false)
  }

  private fun revealAnimator(view: ViewGroup): Animator {
    val actionBarSize = view.context.theme.obtainStyledAttributes(
        intArrayOf(android.R.attr.actionBarSize)).getDimension(0, 0F)
    val x = view.right - (actionBarSize / 2).toInt()
    val y = (actionBarSize / 2).toInt()

    return ciruclarAnimator(view, x, y, true)
  }

  private fun ciruclarAnimator(view: ViewGroup, x: Int, y: Int, expanding: Boolean): Animator {
    val endRadius = if (expanding) view.maxRadius else 0F
    val startRadius = if (expanding) 0F else view.maxRadius

    val reveal = createCircularReveal(view, x, y, startRadius, endRadius)
    return WrapAnimator(reveal)
  }

  private val View.maxRadius: Float
    get() {
      return Math.hypot(width.toDouble(), height.toDouble()).toFloat()
    }
}

class WrapAnimator(private val animator: Animator) : Animator() {

  override fun getStartDelay(): Long {
    return animator.startDelay
  }

  override fun setStartDelay(startDelay: Long) {
    animator.startDelay = startDelay
  }

  override fun setDuration(duration: Long): Animator {
    animator.duration = duration
    return this
  }

  override fun getDuration(): Long {
    return animator.duration
  }

  override fun setInterpolator(value: TimeInterpolator) {
    animator.interpolator = value
  }

  override fun isRunning(): Boolean {
    return animator.isRunning
  }

  override fun start() {
    animator.start()
  }

  override fun cancel() {
    animator.cancel()
  }

  override fun addListener(listener: Animator.AnimatorListener) {
    animator.addListener(listener)
  }

  override fun removeAllListeners() {
    animator.removeAllListeners()
  }

  override fun removeListener(listener: Animator.AnimatorListener) {
    animator.removeListener(listener)
  }

}