package com.oddlyspaced.updater

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val getUpdate by lazy { GetUpdate(getString(R.string.updater_link)) }
    private val getProp by lazy { GetProp() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeStatusBarTransparent()
        if (checkSupport() == 1) {
            loadDetails()
            setOnClick()
        }
    }

    // https://stackoverflow.com/questions/44170028/android-how-to-detect-if-night-mode-is-on-when-using-appcompatdelegate-mode-ni
    // https://learnpainless.com/android/material/make-fully-android-transparent-status-bar
    private fun makeStatusBarTransparent() {
        val mode = applicationContext?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
                window.statusBarColor = Color.TRANSPARENT
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
                window.statusBarColor = Color.TRANSPARENT
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    private fun checkSupport(): Int {
        return if (getProp.getProp("ro.cos.builddate").trim().isEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.app_unsupported_title))
            builder.setMessage(getString(R.string.app_unsupported_message))
            builder.setCancelable(false)
            builder.setPositiveButton(getString(R.string.app_unsupported_exit)) { _, _ ->
                finish()
            }
            builder.show()
            0
        } else {
            1
        }
    }

    private fun checkUpdateJson(counter: Int) {
        if (counter < 100) {
            Handler().postDelayed(
                {
                    val modal: UpdateModal? = getUpdate.getModal()
                    if (modal != null) {
                        updateScreen(modal)
                    } else {
                        checkUpdateJson(counter + 1)
                    }
                }, 100
            )
        } else {
            txCheck.text = getString(R.string.button_unable_check)
            pbCheck.visibility = View.INVISIBLE
            txCheck.visibility = View.VISIBLE
        }
    }

    private fun loadDetails() {
        val versionString =
            getString(R.string.body_android_version) + getProp.getProp("ro.build.version.release").trim()
        val versionCosmic =
            getString(R.string.body_cosmic_version) + getProp.getProp("ro.cos.version").trim()
        txMain1.text = versionString
        txMain2_1.text = versionCosmic
        txMain2_2.text = dateCodeToString(getProp.getProp("ro.cos.builddate").trim())
        txMain3_2.text =
            securityPatchToString(getProp.getProp("ro.build.version.security_patch").trim())
    }

    private fun setOnClick() {
        viewCheckTouch.setOnClickListener {
            txCheck.visibility = View.INVISIBLE
            pbCheck.visibility = View.VISIBLE
            getUpdate.getUpdate()
            checkUpdateJson(1)
        }
    }

    private fun dateCodeToString(code: String): String {
        val year = code.substring(0, 4).toInt()
        val month = code.substring(4, 6).toInt()
        println("$year $month")
        val day = code.substring(6).toInt()
        val monthString = monthToString(month)
        return "$monthString $day, $year"
    }

    private fun securityPatchToString(code: String): String {
        // ro.build.version.security_patch=
        //2019-11-05
        val year = code.substring(0, 4).toInt()
        val month = code.substring(5, 7).toInt()
        //println(code.substring(8))
        val day = code.substring(8).toInt()
        val monthString = monthToString(month)
        return "$monthString $day, $year"
    }

    private fun monthToString(month: Int): String {
        return when (month) {
            1 -> getString(R.string.month_january)
            2 -> getString(R.string.month_february)
            3 -> getString(R.string.month_march)
            4 -> getString(R.string.month_april)
            5 -> getString(R.string.month_may)
            6 -> getString(R.string.month_june)
            7 -> getString(R.string.month_july)
            8 -> getString(R.string.month_august)
            9 -> getString(R.string.month_september)
            10 -> getString(R.string.month_october)
            11 -> getString(R.string.month_november)
            12 -> getString(R.string.month_december)
            else -> ""
        }
    }

    private fun updateScreen(modal: UpdateModal) {
        if (getProp.getProp("ro.cos.builddate").trim().toInt() >= modal.build.toInt()) {
            consHolder.visibility = View.GONE
            txHeader.text = getString(R.string.header_update_unavailable)
            cardCheck.visibility = View.GONE
        } else {
            imgMain1.setImageDrawable(getDrawable(R.drawable.ic_download))
            imgMain2.setImageDrawable(getDrawable(R.drawable.ic_calendar))
            imgMain3.setImageDrawable(getDrawable(R.drawable.ic_changelog))
            txHeader.text = getString(R.string.header_update_available)
            txMain1.text =
                getString(R.string.body_size_prefix) + modal.size + getString(R.string.body_size_suffix)
            txMain2_1.text = dateCodeToString(modal.build).trim()
            txMain2_2.visibility = View.GONE
            txMain3_1.text = getString(R.string.body_changelog)
            txMain3_2.visibility = View.GONE
            txCheck.text = getString(R.string.button_download)
            pbCheck.visibility = View.INVISIBLE
            txCheck.visibility = View.VISIBLE
        }
    }
}
