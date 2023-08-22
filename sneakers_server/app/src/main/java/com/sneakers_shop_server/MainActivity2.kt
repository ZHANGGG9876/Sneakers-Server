package com.sneakers_shop_server

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sneakers_shop_server.databinding.ActivityBottomNavigationBinding
import com.sneakers_shop_server.retrofit.NotificationModel
import com.sneakers_shop_server.retrofit.PushNotification
import com.sneakers_shop_server.retrofit.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity2 : AppCompatActivity() {
    companion object {
        private const val REQUEST_SELECT_IMAGE = 100
    }

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.requestFragment,
                R.id.orderFragment,
                R.id.formFragment,
                R.id.settingsFragment
            )
        )

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNav = binding.mainMenu
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)

        // Get Token
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.i("notis", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val token = task.result
                Log.i("notis", token)
                Toast.makeText(this, token, Toast.LENGTH_SHORT).show()
            })

        val notification = NotificationModel("Bienvenido","Muy buenas, bienvenido a Sneakers")
        val pushNotification  = PushNotification("d3paNborTveLKRhs-Ysn3v:APA91bEZCfH5ZD8D3IZrAhcmFvDZUVpHczlnT38OTLkS3dF5VQInNMq5cjqTKLcrVtW2x5O7j4OQY43EVDXnKegmhBsWL4ur04nzvRjfxmHoaKiiRIKdEwhljgLsZeUiGv080oc8ze0h",notification)

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiCall = retrofit.create(ApiInterface::class.java)
        val call: Call<ResponseModel?>? = apiCall.postNotification(pushNotification)

        call?.enqueue(object : Callback<ResponseModel?> {
            override fun onResponse(call: Call<ResponseModel?>, response: Response<ResponseModel?>) {
                if (response.code() !== 200) {
                    Log.i("testApi", "checkConnection")
                    return
                }
                Log.i(
                    "testApi",
                    response.body()?.success.toString()+"---"
                )
            }

            override fun onFailure(call: Call<ResponseModel?>, t: Throwable) {}
        })


        /*bottomNav.setOnItemSelectedListener { item: MenuItem ->
           when (item.itemId) {
                R.id.nav_home -> {
                   // navController.navigate(R.id.action_settingsFragment_to_homeFragment)
                   loadFragment(HomeFragment())
                    true
                }
                R.id.nav_request -> {
                  //  navController.navigate(R.id.action_homeFragment_to_requestFragment)
                    loadFragment(RequestFragment())
                    true
                }
                R.id.nav_add -> {
                    //navController.navigate(R.id.action_requestFragment_to_formFragment)
                    loadFragment(FormFragment())
                    true
                }
                R.id.nav_order -> {
                   // navController.navigate(R.id.action_requestFragment_to_orderFragment)
                   loadFragment(OrderFragment())
                    true
                }
                R.id.nav_profile -> {
                    //navController.navigate(R.id.action_orderFragment_to_settingsFragment)
                    loadFragment(SettingsFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }*/
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    //The funtion is to create fragment.
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}