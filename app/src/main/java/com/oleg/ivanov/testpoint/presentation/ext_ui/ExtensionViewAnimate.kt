package com.oleg.ivanov.testpoint.presentation.ext_ui

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import com.oleg.ivanov.testpoint.R

fun View.animateLeftRight() {
    val duration = 200L

    val animation1 = TranslateAnimation(0f, -50f, 0f, 0f)
    animation1.duration = duration
    animation1.fillAfter = true

    val animation2 = TranslateAnimation(-50f, 50f, 0f, 0f)
    animation2.duration = duration
    animation2.fillAfter = true

    val animation3 = TranslateAnimation(50f, 0f, 0f, 0f)
    animation3.duration = duration
    animation3.fillAfter = true

    animation1.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
        }

        override fun onAnimationEnd(animation: Animation) {
            this@animateLeftRight.startAnimation(animation2)
        }

        override fun onAnimationRepeat(animation: Animation) {
        }
    })

    animation2.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
        }

        override fun onAnimationEnd(animation: Animation) {
            this@animateLeftRight.startAnimation(animation3)
        }

        override fun onAnimationRepeat(animation: Animation) {
        }
    })

    this.startAnimation(animation1)
}

fun View.animateSpeedWayFromRightToLeft() {
    val pause = 400
    val offsetX = 1500f
    val scale = ScaleAnimation(
        0.8f, 1.0f, 0.8f, 1.0f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    scale.duration = pause.toLong()

    val translateAnimation = TranslateAnimation(offsetX, 0f, 0f, 0f)
    translateAnimation.duration = pause.toLong()

    val alphaAnimation = AlphaAnimation(0.8f, 1.0f)
    alphaAnimation.duration = pause.toLong()

    val animation = AnimationSet(true)
    animation.addAnimation(scale)
    animation.addAnimation(translateAnimation)
    animation.addAnimation(alphaAnimation)

    animation.interpolator = AnticipateOvershootInterpolator()
    this.startAnimation(animation)
}

fun View.animateAlphaUp(duration: Long = 800) {
    val animation1 = AlphaAnimation(0.2f, 1.0f)
    animation1.duration = duration
    this.startAnimation(animation1)
}

fun View.animateAlphaDown() {
    val animation1 = AlphaAnimation(1.0f, 0.0f)
    animation1.duration = 800
    this.startAnimation(animation1)
}

fun View.animateSpeedWayFromDownToUp(fromAlpha: Float = 1.0f) {
    val pause = 300
    val offsetY = 180f
    val scaleOffset = 0.95f
    val scale = ScaleAnimation(
        scaleOffset, 1.0f, scaleOffset, 1.0f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    scale.duration = pause.toLong()

    val translateAnimation = TranslateAnimation(0f, 0f, offsetY, 0f)
    translateAnimation.duration = pause.toLong()

    val alphaAnimation = AlphaAnimation(fromAlpha, 1.0f)
    alphaAnimation.duration = 400

    val animation = AnimationSet(true)
    animation.addAnimation(scale)
    animation.addAnimation(translateAnimation)
    animation.addAnimation(alphaAnimation)

    animation.interpolator = AnticipateOvershootInterpolator()
    this.startAnimation(animation)
}

fun View.animateFromZeroToBig() {
    val up1 = AnimationUtils.loadAnimation(this.context, R.anim.scale)
    up1.startOffset = 105
    this.startAnimation(up1)
}

fun View.animateUpDown() {
    val sizeAnimation = 1.2f
    val sizeAnimationDown = 0.9f

    val down = ScaleAnimation(
        sizeAnimation,
        1.0f,
        sizeAnimation,
        1.0f,
        Animation.RELATIVE_TO_SELF,
        sizeAnimationDown,
        Animation.RELATIVE_TO_SELF,
        sizeAnimationDown
    )
    down.duration = 200

    val up = ScaleAnimation(
        1.0f,
        sizeAnimation,
        1.0f,
        sizeAnimation,
        Animation.RELATIVE_TO_SELF,
        sizeAnimationDown,
        Animation.RELATIVE_TO_SELF,
        sizeAnimationDown
    )
    up.duration = 200
    up.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}

        override fun onAnimationEnd(animation: Animation) {
            this@animateUpDown.startAnimation(down)
        }

        override fun onAnimationRepeat(animation: Animation) {}
    })

    this.startAnimation(up)
}