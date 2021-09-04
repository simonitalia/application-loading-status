package com.udacity.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity: AppCompatActivity() {

    companion object {
        fun newInstance() = DetailActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
    }
}
