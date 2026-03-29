package io.legado.app.ui.browser

object VerificationResultPolicy {

    fun shouldUseCurrentPageHtml(
        refetchAfterSuccess: Boolean,
        sawCloudflareChallenge: Boolean
    ): Boolean {
        return !refetchAfterSuccess || sawCloudflareChallenge
    }
}
