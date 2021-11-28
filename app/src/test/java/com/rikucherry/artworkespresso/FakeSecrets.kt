package com.rikucherry.artworkespresso

/**
 * Mock up implementation of ISecrets
 */
class FakeSecrets : ISecrets {
    override fun getClientId(packageName: String): String {
        return "1"
    }

    override fun getClientSecret(packageName: String): String {
        return "fake secret"
    }
}