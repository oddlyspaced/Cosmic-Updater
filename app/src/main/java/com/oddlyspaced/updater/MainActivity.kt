package com.oddlyspaced.updater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val txHeader by lazy {findViewById<TextView>(R.id.txHeader)}
    private val txMain1 by lazy {findViewById<TextView>(R.id.txMain1)}
    private val txMain2_1 by lazy {findViewById<TextView>(R.id.txMain2_1)}
    private val txMain2_2 by lazy {findViewById<TextView>(R.id.txMain2_2)}
    private val txMain3_1 by lazy {findViewById<TextView>(R.id.txMain3_1)}
    private val txMain3_2 by lazy {findViewById<TextView>(R.id.txMain3_2)}
    private val iconMain1 by lazy {findViewById<ImageView>(R.id.imgMain1)}
    private val iconMain2 by lazy {findViewById<ImageView>(R.id.imgMain2)}
    private val iconMain3 by lazy {findViewById<ImageView>(R.id.imgMain3)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
