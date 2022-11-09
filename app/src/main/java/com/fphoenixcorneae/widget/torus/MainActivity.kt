package com.fphoenixcorneae.widget.torus

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * @desc：
 * @date：2022/11/09 10:48
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<StatisticsPercentageTorusView>(R.id.sptvStatistics1)
            .add(Color.parseColor("#ffa514"), 10)
            .add(Color.parseColor("#f26034"), 8)
            .add(Color.parseColor("#e42f2f"), 4)
            .add(Color.parseColor("#34dede"), 2)
            .setLifecycle(lifecycle)
            .showAnim()
        findViewById<StatisticsPercentageTorusView>(R.id.sptvStatistics2)
            .add(Color.parseColor("#30acf4"), 5)
            .add(Color.parseColor("#19d57b"), 2)
            .add(Color.parseColor("#ffa514"), 4)
            .add(Color.parseColor("#d93618"), 1)
            .setLifecycle(lifecycle)
            .showAnim()
        findViewById<StatisticsPercentageTorusView>(R.id.sptvStatistics3)
            .add(Color.parseColor("#ffa514"), 3)
            .add(Color.parseColor("#30acf4"), 2)
            .add(Color.parseColor("#34dede"), 6)
            .setLifecycle(lifecycle)
            .showAnim()
        findViewById<StatisticsPercentageTorusView>(R.id.sptvStatistics4)
            .add(Color.parseColor("#30acf4"), 2)
            .add(Color.parseColor("#34dede"), 3)
            .setLifecycle(lifecycle)
            .showAnim()
    }
}