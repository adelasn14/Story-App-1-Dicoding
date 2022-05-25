package com.example.latihan_camerax.story

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihan_camerax.R
import com.example.latihan_camerax.ViewModelFactory
import com.example.latihan_camerax.api.ListStory
import com.example.latihan_camerax.api.UserPreference
import com.example.latihan_camerax.databinding.ActivityAllStoryBinding
import com.example.latihan_camerax.login.LoginSession
import com.example.latihan_camerax.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AllStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllStoryBinding
    private lateinit var adapter: StoriesAdapter
    private lateinit var allStoryViewModel: AllStoryViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = StoriesAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : StoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStory) {
                val intentToDetail = Intent(this@AllStoryActivity, DetailStoryActivity::class.java)
                intentToDetail.putExtra(DetailStoryActivity.EXTRA_NAME, data)
                startActivity(intentToDetail)
            }
        })

        binding.apply {
            rvAllstory.layoutManager = LinearLayoutManager(this@AllStoryActivity)
            rvAllstory.setHasFixedSize(true)
            rvAllstory.adapter = adapter
        }

        allStoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[AllStoryViewModel::class.java]

        allStoryViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                binding.nameTextView.text = getString(R.string.greetingStory, user.name)
            }
        }

        val loginSession = LoginSession(this)
        val token = loginSession.passToken().toString()
        allStoryViewModel.getAllStories("Bearer $token")
        showLoading(true)
        allStoryViewModel.allStories.observe(this) {
            if (it != null) {
                adapter.setListStory(it.ListStory)
                showLoading(false)
            }
            if (it == null) {
                binding.notFoundAnimation.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val backTo = Intent(this, MainActivity::class.java)
        startActivity(backTo)
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
