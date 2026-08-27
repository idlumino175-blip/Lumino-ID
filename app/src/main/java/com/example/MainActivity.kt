package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.ClipRepository
import com.example.ui.screens.ClipVaultScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ClipVaultViewModel
import com.example.viewmodel.ClipVaultViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ClipRepository(database.clipDao())
        val viewModelFactory = ClipVaultViewModelFactory(repository)

        setContent {
            MyApplicationTheme {
                val viewModel: ClipVaultViewModel = viewModel(factory = viewModelFactory)
                ClipVaultScreen(viewModel = viewModel)
            }
        }
    }
}
