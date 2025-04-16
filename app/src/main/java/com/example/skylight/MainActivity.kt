package com.example.skylight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skylight.ui.theme.SkylightTheme
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkylightTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBanner(
                            title = "Skylight Weather"
                        )
                    }
                    ) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TopBanner(title: String, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .padding(24.dp)

    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally

        ){

        Text(text = stringResource(R.string.loc), fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(text = stringResource(R.string.temp), fontSize = 64.sp)
            Image(
                painter = painterResource(id = R.drawable.sunny64),
                contentDescription = "Sunny Weather Icon",
                modifier = Modifier.size(80.dp).weight(1f)
            )
        }

        Row{
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = stringResource(R.string.feels_like), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row{
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.low_temp))
                Text(text = stringResource(R.string.high_temp))
                Text(text = stringResource(R.string.humidity))
                Text(text = stringResource(R.string.pressure))
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    SkylightTheme {
        HomeScreen()
    }
}