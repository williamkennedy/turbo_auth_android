package com.example.turbonativeauth.fragments

import android.annotation.SuppressLint
import android.view.View
import com.example.turbonativeauth.R
import dev.hotwire.turbo.fragments.TurboWebBottomSheetDialogFragment
import dev.hotwire.turbo.nav.TurboNavDestination
import dev.hotwire.turbo.nav.TurboNavGraphDestination


@TurboNavGraphDestination(uri = "turbo://fragment/web/modal/sheet")
class WebModalFragment: TurboWebBottomSheetDialogFragment(), TurboNavDestination {

    @SuppressLint("InflateParams")
    override fun createErrorView(statusCode: Int): View {
        when (statusCode) {
            401 -> return layoutInflater.inflate(R.layout.turbo_auth_error, null)
            else -> return super.createErrorView(statusCode)
        }
    }
}