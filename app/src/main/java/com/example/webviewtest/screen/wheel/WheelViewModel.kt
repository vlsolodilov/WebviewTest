package com.example.webviewtest.screen.wheel

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class WheelViewModel : ViewModel() {

    private val _spinWheelAvailable = MutableLiveData(true)
    val spinWheelAvailable: LiveData<Boolean> = _spinWheelAvailable

    private val _rotation = MutableLiveData<Animation>()
    val rotation: LiveData<Animation> = _rotation

    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int> = _score

    private var lastSpin = 0L

    fun startSpin() {
        val currentSpin = lastSpin + Random.nextLong(2 * 360) + 360
        val rotateAnimation = RotateAnimation(
            lastSpin.toFloat(),
            currentSpin.toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.apply {
            duration = currentSpin * SPIN_SPEED
            interpolator = LinearInterpolator()
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    _spinWheelAvailable.value = false
                }

                override fun onAnimationEnd(p0: Animation?) {
                    lastSpin = currentSpin % 360
                    _score.value = getScoreBySpin(lastSpin)
                    _spinWheelAvailable.value = true
                    val endedSpin = RotateAnimation(
                        currentSpin.toFloat(),
                        currentSpin.toFloat(),
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    )
                    endedSpin.fillAfter = true
                    _rotation.value = endedSpin
                }

                override fun onAnimationRepeat(p0: Animation?) {}
            })
        }
        _rotation.value = rotateAnimation
    }

    private fun getScoreBySpin(spin: Long): Int =
        (spin / (360/ MAX_SCORE) + 1).toInt()

    companion object {
        private const val MAX_SCORE = 5
        private const val SPIN_SPEED = 10
    }
}