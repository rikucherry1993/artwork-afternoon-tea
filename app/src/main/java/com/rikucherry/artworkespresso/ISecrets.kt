package com.rikucherry.artworkespresso

interface ISecrets {
    fun getClientId(packageName: String): String
    fun getClientSecret(packageName: String): String
}