package ru.lefty.subsun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.lefty.subsun.ui.MainNavGraph
import ru.lefty.subsun.ui.theme.SubsunTheme

@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as SubsunApplication).container
        setContent {
            SubsunTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainNavGraph(appContainer)
                }
            }
        }
    }
}
