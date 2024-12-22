package com.example.mytugas_api

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.mytugas_api.databinding.ActivityMainBinding
import com.example.mytugas_api.model.AppDatabase
import com.example.mytugas_api.model.Dogs
import com.example.mytugas_api.model.DogEntity
import com.example.mytugas_api.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val dogList = ArrayList<String>()
    private lateinit var adapter: DataAdapter
    private lateinit var db: AppDatabase
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Membuat Notification Channel
        createNotificationChannel()

        // Meminta izin notifikasi (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)

        // Cek apakah pengguna sudah login
        if (!sharedPreferences.getBoolean("is_logged_in", false)) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
            return
        }

        // Menampilkan Toast setelah login berhasil
        showLoginToast()

        // Menampilkan notifikasi login berhasil dengan tombol logout
        showLoginNotificationWithLogout()

        // Inisialisasi database Room
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "dog-database").build()

        // Mengatur RecyclerView
        setupRecyclerView()

        // Memanggil API untuk mengambil gambar anjing
        fetchDogImages(9)

        // Tombol untuk membuka aktivitas favorit
        val btnFavorite: Button = findViewById(R.id.btn_favorite)
        btnFavorite.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }

        // Tombol logout
        val btnLogout: Button = findViewById(R.id.btn_logout)
        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Login Notifications"
            val channelDescription = "Notification channel for login success"
            val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
            val channel = android.app.NotificationChannel("LOGIN_CHANNEL_ID", channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager = getSystemService(android.app.NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun showLoginNotificationWithLogout() {
        // Intent untuk logout
        val logoutIntent = Intent(this, LoginActivity::class.java).apply {
            putExtra("logout", true)
        }

        // PendingIntent untuk logout
        val pendingLogoutIntent = PendingIntent.getActivity(
            this,
            0,
            logoutIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Membuat notifikasi dengan tombol logout
        val builder = NotificationCompat.Builder(this, "LOGIN_CHANNEL_ID")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle("Login Berhasil")
            .setContentText("Selamat datang kembali!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .addAction(android.R.drawable.ic_lock_power_off, "Logout", pendingLogoutIntent)

        // Menampilkan notifikasi
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }

    private fun setupRecyclerView() {
        adapter = DataAdapter(dogList, onClickData = { imageUrl ->
            Toast.makeText(this@MainActivity, "Anda mengklik: $imageUrl", Toast.LENGTH_SHORT).show()
        }, onFavoriteClick = { imageUrl ->
            saveToFavorites(imageUrl)
        })

        binding.rvData.apply {
            adapter = this@MainActivity.adapter
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            setHasFixedSize(true)
        }
    }

    private fun saveToFavorites(imageUrl: String) {
        Thread {
            val dogEntity = DogEntity(picture = imageUrl, status = "favorit")
            db.dogDao().insert(dogEntity)
            runOnUiThread {
                Toast.makeText(this, "Gambar ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun fetchDogImages(count: Int) {
        val client = ApiClient.getInstance()
        repeat(count) {
            client.getAllDogs().enqueue(object : Callback<Dogs> {
                override fun onResponse(call: Call<Dogs>, response: Response<Dogs>) {
                    if (response.isSuccessful && response.body() != null) {
                        val dogs = response.body()
                        dogs?.picture?.let {
                            dogList.add(it)
                            adapter.notifyItemInserted(dogList.size - 1)
                        }
                    } else {
                        Log.e("MainActivity", "Response failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@MainActivity, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Dogs>, t: Throwable) {
                    Log.e("MainActivity", "Network request failed: ${t.message}")
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showLoginToast() {
        Toast.makeText(this, "Login berhasil!", Toast.LENGTH_LONG).show()
    }

    private fun logout() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_logged_in", false)
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin notifikasi diberikan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Izin notifikasi ditolak!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
