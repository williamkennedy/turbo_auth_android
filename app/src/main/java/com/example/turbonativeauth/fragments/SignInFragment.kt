package com.example.turbonativeauth.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.turbonativeauth.API_SIGN_IN_URL

import com.example.turbonativeauth.ui.theme.AppTheme
import dev.hotwire.turbo.fragments.TurboFragment
import dev.hotwire.turbo.nav.TurboNavDestination
import dev.hotwire.turbo.nav.TurboNavGraphDestination

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Cookie.Companion.parse

import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request

import okhttp3.Response
import java.io.IOException

private const val AUTH_TOKEN_KEY = "auth_token"
private const val SHARED_PREFS_NAME = "turbo_native_auth"




@TurboNavGraphDestination(uri = "turbo://fragment/sign_in")
class SignInFragment : TurboFragment(), TurboNavDestination {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    SignInForm()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SignInForm() {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        val context = LocalContext.current

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = email,
                onValueChange = { newText ->
                    email = newText.trimEnd()
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { performSignIn(context, email, password) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Sign In")
            }
        }
    }

    private fun performSignIn(context: Context, email: String, password: String) {
        val client = OkHttpClient()

        val requestBody = FormBody.Builder()
            .add("email", email)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url(API_SIGN_IN_URL)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle network failure or API error
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val authToken = response.header("X-Session-Token")
                    val cookies = response.headers("Set-Cookie")

                    saveAuthToken(context, authToken)
                    saveCookies(cookies)

                    // execute on main thread
                    requireActivity().runOnUiThread {
                        navigateUp()
                        sessionNavHostFragment.reset()
                    }

                } else {
                    // Raise error
                }
            }
        })
    }

    private fun saveAuthToken(context: Context, authToken: String?) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(AUTH_TOKEN_KEY, authToken).apply()
    }

    private fun saveCookies(cookies: List<String>) {
        val cookieManager = CookieManager.getInstance()

        for (cookie in cookies) {
            parse(API_SIGN_IN_URL.toHttpUrlOrNull()!!, cookie)?.let {
                cookieManager.setCookie(API_SIGN_IN_URL, it.toString())
            }
        }
    }

}