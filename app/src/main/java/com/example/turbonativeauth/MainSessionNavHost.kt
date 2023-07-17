package com.example.turbonativeauth

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.turbonativeauth.fragments.SignInFragment
import com.example.turbonativeauth.fragments.WebFragment
import com.example.turbonativeauth.fragments.WebModalFragment
import dev.hotwire.turbo.config.TurboPathConfiguration
import dev.hotwire.turbo.session.TurboSessionNavHostFragment
import kotlin.reflect.KClass

val BASE_URL = "http://10.0.2.2:3005"
val SIGN_IN_URL = "${BASE_URL}/sign_in"
val PATH_CONFIGURATION_URL = "${BASE_URL}/turbo/android/path_configuration"
val API_SIGN_IN_URL = "${BASE_URL}/api/v1/sessions"

class MainSessionNavHost : TurboSessionNavHostFragment() {
    override var sessionName = "main"
    override var startLocation = BASE_URL

    override val registeredFragments: List<KClass<out Fragment>>
        get() = listOf(
            WebFragment::class,
            WebModalFragment::class,
            SignInFragment::class
        )


    override val registeredActivities: List<KClass<out AppCompatActivity>>
        get() = listOf()

    override val pathConfigurationLocation: TurboPathConfiguration.Location
        get() = TurboPathConfiguration.Location(
            assetFilePath = "json/path_configuration.json",
            remoteFileUrl = PATH_CONFIGURATION_URL
        )
}