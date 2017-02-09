package com.thoughtbot.tropos.settings

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.commons.BaseActivity
import com.thoughtbot.tropos.commons.CircularReveal
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.find


class SettingsActivity : BaseActivity(), SettingsView {

  val presenter: SettingsPresenter by lazy { SettingsPresenter(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)

    window.enterTransition = CircularReveal()

    val toolbar = find<Toolbar>(R.id.settings_toolbar)
    setSupportActionBar(toolbar)

    settings_unit_radio_group.setOnCheckedChangeListener(presenter)
    settings_privacy_policy.setOnClickListener { presenter.onPrivacyClicked() }

    presenter.init()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.done_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.action_close -> {
        onBackPressed()
        return true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override val context: Context = this

  override fun checkUnitPreference(preferenceId: Int) {
    settings_unit_radio_group.check(preferenceId)
  }

//  private fun animateReveal() {
//    val actionBarSize = theme.obtainStyledAttributes(
//        intArrayOf(android.R.attr.actionBarSize)).getDimension(0, 0F)
//    val x = settings_root.right - (actionBarSize / 2).toInt()
//    val y = (actionBarSize / 2).toInt()
//
//    val anim = ciruclarAnimator(settings_root, x, y, true)
//    anim.duration = 500L
//    anim.interpolator = AccelerateDecelerateInterpolator()
//    anim.start()
//  }
//
//  private fun animateCollapse() {
//    val attrs = intArrayOf(android.R.attr.actionBarSize)
//    val actionBarSize = theme.obtainStyledAttributes(attrs).getDimension(0, 0F)
//    val offset = (actionBarSize / 2).toInt()
//    val anim = ciruclarAnimator(settings_root, offset, offset, false)
//    anim.duration = 500L
//    anim.interpolator = AccelerateDecelerateInterpolator()
//    anim.start()
//  }
//
//  private fun ciruclarAnimator(viewRoot: ViewGroup, x: Int, y: Int, expanding: Boolean): Animator {
//    val maxRadius = Math.hypot(viewRoot.width.toDouble(), viewRoot.height.toDouble()).toFloat()
//    val endRadius = if (expanding) maxRadius else 0F
//    val startRadius = if (expanding) 0F else maxRadius
//
//    return ViewAnimationUtils.createCircularReveal(viewRoot, x, y, startRadius, endRadius)
//  }

}

