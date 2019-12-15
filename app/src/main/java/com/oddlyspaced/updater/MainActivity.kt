package com.oddlyspaced.updater

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val getUpdate by lazy {GetUpdate(getString(R.string.updater_link))}
    private val getProp by lazy {GetProp()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (checkSupport() == 1) {
            loadDetails()
            setOnClick()
        }
    }

    private fun checkSupport(): Int {
        return if (getProp.getProp("ro.cos.builddate").trim().isEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Unsupported!")
            builder.setMessage("It seems like this updater app is not meant to be used with this ROM!")
            builder.setCancelable(false)
            builder.setPositiveButton("Exit") { _, _ ->
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
                    println("called")
                    if (modal != null) {
                        println("done")
                        updateScreen(modal)
                    } else {
                        checkUpdateJson(counter + 1)
                    }
                }, 100
            )
        }
        else {
            txCheck.text = "Unable to check for Updates!"
            pbCheck.visibility = View.INVISIBLE
            txCheck.visibility = View.VISIBLE
        }
    }

    private fun loadDetails() {
        val versionString = "Android Version " + getProp.getProp("ro.build.version.release").trim()
        val versionCosmic = "CosmicOS Version " + getProp.getProp("ro.cos.version").trim()
        txMain1.text = versionString
        txMain2_1.text = versionCosmic
        txMain2_2.text = dateCodeToString("20190101")
        txMain3_2.text = securityPatchToString("2019-11-05")
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
        return when(month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> ""
        }
    }

    private fun updateScreen(modal: UpdateModal) {
        if (getProp.getProp("ro.cos.builddate").trim().toInt() >= modal.build.toInt()) {
            consHolder.visibility = View.GONE
            txHeader.text = "No Updates\nAvailable"
            cardCheck.visibility = View.GONE
        }
        else {
            imgMain1.setImageDrawable(getDrawable(R.drawable.ic_download))
            imgMain2.setImageDrawable(getDrawable(R.drawable.ic_calendar))
            imgMain3.setImageDrawable(getDrawable(R.drawable.ic_changelog))
            txHeader.text = "Update Available"
            txMain1.text = "Size ${modal.size}mb"
            txMain2_1.text = dateCodeToString(modal.build).trim()
            txMain2_2.visibility = View.GONE
            txMain3_1.text = "Changelog"
            txMain3_2.visibility = View.GONE
            txCheck.text = "Download"
            pbCheck.visibility = View.INVISIBLE
            txCheck.visibility = View.VISIBLE
        }
    }
}
