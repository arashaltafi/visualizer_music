package com.arash.altafi.visualizer_music

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arash.altafi.visualizer_music.databinding.ActivityMainBinding
import com.arash.altafi.visualizer_music.sample1.Sample1Activity
import com.arash.altafi.visualizer_music.sample2.Sample2Activity
import com.arash.altafi.visualizer_music.sample3.Sample3Activity
import com.arash.altafi.visualizer_music.sample4.Sample4Activity
import com.arash.altafi.visualizer_music.sample5.Sample5Activity
import com.arash.altafi.visualizer_music.sample6.Sample6Activity
import com.arash.altafi.visualizer_music.sample7.Sample7Activity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            btnSample1.setOnClickListener {
                startActivity(Intent(this@MainActivity, Sample1Activity::class.java))
            }
            btnSample2.setOnClickListener {
                startActivity(Intent(this@MainActivity, Sample2Activity::class.java))
            }
            btnSample3.setOnClickListener {
                startActivity(Intent(this@MainActivity, Sample3Activity::class.java))
            }
            btnSample4.setOnClickListener {
                startActivity(Intent(this@MainActivity, Sample4Activity::class.java))
            }
            btnSample5.setOnClickListener {
                startActivity(Intent(this@MainActivity, Sample5Activity::class.java))
            }
            btnSample6.setOnClickListener {
                startActivity(Intent(this@MainActivity, Sample6Activity::class.java))
            }
            btnSample7.setOnClickListener {
                startActivity(Intent(this@MainActivity, Sample7Activity::class.java))
            }
        }
    }

}