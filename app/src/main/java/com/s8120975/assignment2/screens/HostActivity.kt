package com.s8120975.assignment2.screens

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.s8120975.assignment2.databinding.ActivityHostBinding

/**
 * The only Activity. Note: launcher activity, NavHost, layout activity_host.
 * Its layout is res/layout/activity_host.xml. That layout is only a NavHost.
 * Sign-in, catalogue and detail are fragments inside that NavHost.
 * The navigation graph is res/navigation/nav_graph.xml.
 */
class HostActivity : AppCompatActivity() {
    /** Function that shows activity_host.xml and keeps the screens below the status bar. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.navHost) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, 0)
            insets
        }
    }
}
