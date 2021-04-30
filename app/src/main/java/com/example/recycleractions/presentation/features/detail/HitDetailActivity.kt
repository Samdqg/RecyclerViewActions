package com.example.recycleractions.presentation.features.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recycleractions.R
import com.example.recycleractions.presentation.features.list.HitsActivity
import kotlinx.android.synthetic.main.activity_hit_detail.*

class HitDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hit_detail)

        val url = intent.getStringExtra(HitsActivity.HIT_URL)

        wbHitDetail.loadUrl(url)
    }
}