package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeshare.databinding.ActivityShowMemeBinding

class ShowMemeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowMemeBinding
    var memeImageUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowMemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("url").toString()
        loadMeme(url)

        binding.nextButton.setOnClickListener{
            loadMeme(url)
        }

        binding.shareButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Meme Url: $memeImageUrl")
            val chooser = Intent.createChooser(intent, "Share")
            startActivity(chooser)
        }
    }

    private fun loadMeme(url: String) {
        binding.progressBar.visibility = View.VISIBLE
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                memeImageUrl = response.getString("url")
                Glide.with(this).load(memeImageUrl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }
                }).into(binding.memeImageView)
            },
            {error->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }
        )
        // Access the RequestQueue through your singleton class
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}