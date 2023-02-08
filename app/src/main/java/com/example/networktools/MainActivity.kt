package com.example.networktools

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.networktools.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var ip: String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.sendBtn.setOnClickListener {
            ip = binding.ip.text.toString()
            if (ip == "" || ip.length < 7) {
                Toast.makeText(this, "IP manzil to'liq kiritilmadi", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("ip", ip)

                startActivity(intent)
            }

            binding.ip.setText("")
        }
    }
}