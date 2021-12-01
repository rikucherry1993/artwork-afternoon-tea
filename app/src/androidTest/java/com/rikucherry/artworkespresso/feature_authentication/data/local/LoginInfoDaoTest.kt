package com.rikucherry.artworkespresso.feature_authentication.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rikucherry.artworkespresso.common.database.ArtworkEspressoDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginInfoDaoTest {

    private lateinit var database: ArtworkEspressoDatabase
    private lateinit var dao: LoginInfoDao

    @Before
    fun setup() {
        //TODO setup db
    }

    @After
    fun tearUp() {
        //TODO close db
    }

    @Test
    fun `getLoginInfo`() {
        //TODO
    }

    @Test
    fun `insertLogInfo`() {
        //TODO
    }

    @Test
    fun `truncateLoginIngo`() {
        //TODO
    }
}