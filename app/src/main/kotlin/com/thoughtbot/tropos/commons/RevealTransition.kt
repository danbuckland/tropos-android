package com.thoughtbot.tropos.commons

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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

  override fun onAppear(sceneRoot: ViewGroup?, view: View, startValues: TransitionValues?,
      endValues: TransitionValues?): Animator {
    view.alpha = 0f
    val reveal = circularRevealAnimator(view, 0f, view.maxRadius)
    reveal.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationStart(animation: Animator) {
        view.alpha = 1f
      }
    })
    return reveal
  }

  override fun onDisappear(sceneRoot: ViewGroup?, view: View,
      startValues: TransitionValues?, endValues: TransitionValues?): Animator {
    return circularRevealAnimator(view, view.maxRadius, 0f)
  }

  private fun circularRevealAnimator(view: View, startRadius: Float, endRadius: Float): Animator {
    val centerX = view.width / 2
    val centerY = view.height / 2

    val reveal = createCircularReveal(view, centerX, centerY, startRadius, endRadius)
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