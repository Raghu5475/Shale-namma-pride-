package com.shalenammapride.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shalenammapride.app.ui.navigation.ShaleNavHost
import com.shalenammapride.app.ui.theme.LocalAppLanguage
import com.shalenammapride.app.ui.theme.ShaleNammaPrideTheme
import com.shalenammapride.app.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appViewModel: AppViewModel = viewModel()
            val state by appViewModel.state
            ShaleNammaPrideTheme {
                CompositionLocalProvider(LocalAppLanguage provides state.language) {
                    ShaleNavHost(appViewModel = appViewModel)
                }
            }
        }
    }
}
