package io.legado.app.ui.browser

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VerificationResultPolicyTest {

    @Test
    fun `cloudflare challenge uses current page html even when refetch is enabled`() {
        assertTrue(
            VerificationResultPolicy.shouldUseCurrentPageHtml(
                refetchAfterSuccess = true,
                sawCloudflareChallenge = true
            )
        )
    }

    @Test
    fun `normal verification still refetches when enabled`() {
        assertFalse(
            VerificationResultPolicy.shouldUseCurrentPageHtml(
                refetchAfterSuccess = true,
                sawCloudflareChallenge = false
            )
        )
    }

    @Test
    fun `disabled refetch always uses current page html`() {
        assertTrue(
            VerificationResultPolicy.shouldUseCurrentPageHtml(
                refetchAfterSuccess = false,
                sawCloudflareChallenge = false
            )
        )
    }
}
