package com.example.turbonativeauth.fragments

import com.example.turbonativeauth.SIGN_IN_URL
import dev.hotwire.turbo.fragments.TurboWebFragment
import dev.hotwire.turbo.nav.TurboNavDestination
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import dev.hotwire.turbo.visit.TurboVisitAction.REPLACE
import dev.hotwire.turbo.visit.TurboVisitOptions

@TurboNavGraphDestination(uri = "turbo://fragment/web")
open class WebFragment : TurboWebFragment(), TurboNavDestination {

    override fun onVisitErrorReceived(location: String, errorCode: Int) {
        when (errorCode) {
            401 -> navigate(SIGN_IN_URL, TurboVisitOptions(action = REPLACE))
            else -> super.onVisitErrorReceived(location, errorCode)
        }
    }

}