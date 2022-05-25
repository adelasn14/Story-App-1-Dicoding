package com.example.latihan_camerax.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.latihan_camerax.R
import com.example.latihan_camerax.ViewModelFactory
import com.example.latihan_camerax.api.UserPreference
import com.example.latihan_camerax.databinding.ActivityMainBinding
import com.example.latihan_camerax.login.LoginSession
import com.example.latihan_camerax.story.AddStoryActivity
import com.example.latihan_camerax.story.AllStoryActivity
import com.example.latihan_camerax.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupView()
        setupViewModel()
        setupAction()
        setupAnimation()
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val addStory =
            ObjectAnimator.ofFloat(binding.addStoryButton, View.ALPHA, 1f).setDuration(300)
        val allStory =
            ObjectAnimator.ofFloat(binding.allStoryButton, View.ALPHA, 1f).setDuration(300)
        val logout = ObjectAnimator.ofFloat(binding.logoutButton, View.ALPHA, 1f).setDuration(300)
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(300)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(name, message, addStory, allStory, logout)
            start()
        }
    }

    @SuppressLint("NewApi")
    private fun setupView() {
        @Suppress("DEPRECATION")
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        supportActionBar?.hide()
    }

    @SuppressLint("StringFormatInvalid")
    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                binding.nameTextView.text = getString(R.string.greeting, user.name)
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            val loginSession = LoginSession(this)
            loginSession.logoutSession()
            mainViewModel.logout()
        }

        binding.addStoryButton.setOnClickListener {
            val intentUpload = Intent(this, AddStoryActivity::class.java)
            startActivity(
                intentUpload,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
        }

        binding.allStoryButton.setOnClickListener {
            val intentUpload = Intent(this, AllStoryActivity::class.java)
            startActivity(
                intentUpload,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
        }
    }
}