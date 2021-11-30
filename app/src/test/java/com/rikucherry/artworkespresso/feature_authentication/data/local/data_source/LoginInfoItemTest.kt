package com.rikucherry.artworkespresso.feature_authentication.data.local.data_source

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoginInfoItemTest {

    private lateinit var SUT: LoginInfoItem

    @Test
    fun `GetStatus_status1_returnsLoggedOut`() {
        //Given
        SUT = LoginInfoItem(
            id = 1,
            userId = "test",
            userName = "test",
            userIconUrl = "test.png",
            status = 1
        )
        //When
        val expected = LoginStatus.LOGGED_OUT
        val actual = SUT.getStatus()
        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `GetStatus_status2_returnsUserLoggedIn`() {
        //Given
        SUT = LoginInfoItem(
            id = 1,
            userId = "test",
            userName = "test",
            userIconUrl = "test.png",
            status = 2
        )
        //When
        val expected = LoginStatus.USER_LOGGED_IN
        val actual = SUT.getStatus()
        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `GetStatus_status3_returnsClientLoggedIn`() {
        //Given
        SUT = LoginInfoItem(
            id = 1,
            userId = "test",
            userName = "test",
            userIconUrl = "test.png",
            status = 3
        )
        //When
        val expected = LoginStatus.CLIENT_LOGGED_IN
        val actual = SUT.getStatus()
        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `GetStatus_status4_returnsInvalid`() {
        //Given
        SUT = LoginInfoItem(
            id = 1,
            userId = "test",
            userName = "test",
            userIconUrl = "test.png",
            status = 4
        )
        //When
        val expected = LoginStatus.INVALID
        val actual = SUT.getStatus()
        //Then
        assertEquals(expected, actual)
    }

}