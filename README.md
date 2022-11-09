# StatisticsPercentageTorusView
统计百分比圆环图

[![](https://jitpack.io/v/FPhoenixCorneaE/StatisticsPercentageTorusView.svg)](https://jitpack.io/#FPhoenixCorneaE/StatisticsPercentageTorusView)

<p align="center"> <img src="https://github.com/FPhoenixCorneaE/StatisticsPercentageTorusView/blob/main/image/statistics_percentage_torus.png" alt="预览图片"  width="320"></p>


How to include it in your project:
--------------
**Step 1.** Add the JitPack repository to your build file
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency
```groovy
dependencies {
	implementation("com.github.FPhoenixCorneaE:StatisticsPercentageTorusView:$latest")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.5.1")
}
```

代码中使用
-------------------
```kotlin
findViewById<StatisticsPercentageTorusView>(R.id.sptvStatistics1)
    // 添加统计项
    .add(Color.parseColor("#ffa514"), 10)
    .add(Color.parseColor("#f26034"), 8)
    .add(Color.parseColor("#e42f2f"), 4)
    .add(Color.parseColor("#34dede"), 2)
    // 设置生命周期
    .setLifecycle(lifecycle)
    // 显示动画
    .showAnim()
```