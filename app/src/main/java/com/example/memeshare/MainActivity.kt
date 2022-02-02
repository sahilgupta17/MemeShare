package com.example.memeshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.memeshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.generateMemeButton.setOnClickListener{
            val subreddit = binding.inputSubreddit.editableText.toString()
            if(subreddit.isBlank() || subreddit.isEmpty()) {
                Toast.makeText(this, "Enter a valid Subreddit", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val url = "https://meme-api.herokuapp.com/gimme/$subreddit"
            showMeme(url)
        }

        binding.randomMemeButton.setOnClickListener {
            showMeme("https://meme-api.herokuapp.com/gimme/")
        }
    }

    private fun showMeme(url: String){
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            {
                val intent = Intent(this, ShowMemeActivity::class.java)
                intent.putExtra("url", url)
                startActivity(intent)
            },
            {
                Toast.makeText(this, "Invalid Subreddit", Toast.LENGTH_SHORT).show()
            }
        )
        // Access the RequestQueue through your singleton class
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}