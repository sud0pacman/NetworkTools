package com.example.networktools

import android.annotation.SuppressLint
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.Toast
import com.example.networktools.databinding.ActivitySecondBinding
import com.example.networktools.model.IP
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class SecondActivity : AppCompatActivity() {
    lateinit var binding: ActivitySecondBinding
    lateinit var ip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySecondBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ip = intent.getStringExtra("ip").toString()

        urlLoader(ip).start()

        binding.back.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun urlLoader(ip: String): Thread {
        return Thread {

            val url = URL("http://ip-api.com/json/$ip")

            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode == 200 ) {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val userIP = Gson().fromJson(inputStreamReader, IP::class.java)

                updateUI(userIP, ip)
                inputStreamReader.close()
                inputSystem.close()
            }
            else {
                binding.ipData.text = "Ulanish amalga oshmadi\n" +
                        "Iltimos IP ni to'g'ri kiriting"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(url: IP, ip: String) {
        runOnUiThread {
            kotlin.run {
                if (urlTester(ip)) {
                    binding.ipData.text =
                        "Country: ${url.country}\n" +
                                "City: ${url.city}\n" +
                                "ISP: ${url.isp}\n" +
                                "Time zone ${url.timezone}\n" +
                                "Query: ${url.query}\n" +
                                "LAT: ${url.lat}\n" +
                                "LON: ${url.lon}"
                }
                else {
                    binding.ipData.text = "Bunday formatdagi IP manzil xato!"
                }
            }
        }
    }

    private fun urlTester(ip: String): Boolean {
        val dots = ArrayList<Int>()
        val numbers = ArrayList<Int>()
        var response = false

        for (c in ip.indices) {
            if (ip[c] == '.') {
                dots.add(c)
            }
        }

        val number1 = ip.substring(0, dots[0]);numbers.add(number1.toInt())
        val number2 = ip.substring(dots[0]+1, dots[1]);numbers.add(number2.toInt())
        val number3 = ip.substring(dots[1]+1, dots[2]);numbers.add(number3.toInt())
        val number4 = ip.substring(dots[2]+1, ip.length);numbers.add(number4.toInt())

        for (number in numbers) {
            response = !(number > 255 || number < 0)
        }

        return response
    }

//    private fun String.toaster() {
//        Toast.makeText(this@SecondActivity, this, Toast.LENGTH_SHORT).show()
//    }

}