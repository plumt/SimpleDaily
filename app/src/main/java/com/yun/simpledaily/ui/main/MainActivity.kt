package com.yun.simpledaily.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yun.simpledaily.R
import com.yun.simpledaily.databinding.ActivityMainBinding
import com.yun.simpledaily.util.PreferenceManager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

//    lateinit var dialog: LoadingDialog

    val sharedPreferences: PreferenceManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.main = mainViewModel

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        dialog = LoadingDialog(this)

//        mainViewModel.isLoading.observe(this) {
//            if (it) {
//                dialog.show()
//            } else {
//                dialog.dismiss()
//            }
//        }

    }
}