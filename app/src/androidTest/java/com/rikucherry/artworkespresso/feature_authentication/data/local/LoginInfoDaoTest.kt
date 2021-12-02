package com.rikucherry.artworkespresso.feature_authentication.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.rikucherry.artworkespresso.common.database.ArtworkEspressoDatabase
import com.rikucherry.artworkespresso.feature_authentication.data.local.data_source.LoginInfoItem
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LoginInfoDaoTest {

    private lateinit var database: ArtworkEspressoDatabase
    private lateinit var dao: LoginInfoDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ArtworkEspressoDatabase::class.java
        ).build()

        dao = database.loginInfoDao()
    }

    @After
    fun tearUp() {
        database.clearAllTables()
        database.close()
    }


    @Test
    fun `InsertLoginInfo_getLoginInfo_getTheSameInfoAsInserted`() = runBlocking {
        //Given
        val testData = LoginInfoItem(1,"test", "test","url", 1)
        //When
        dao.insertLoginInfo(testData)
        //Then
        Truth.assertThat(dao.getLoginInfo()).isEqualTo(testData)
    }


    @Test
    fun `InsertLoginInfo_truncateLoginInfo_insertedItemIsDeleted`() = runBlocking {
        //Given
        val testData = LoginInfoItem(1,"test", "test","url", 1)
        //When
        dao.insertLoginInfo(testData)
        dao.truncateLoginInfo()
        //Then
        Truth.assertThat(dao.getLoginInfo()).isNull()
    }


    @Test
    fun `InsertLoginInfo_InsertWithSameIdAgain_getReplacedItem`() = runBlocking {
        //Given
        val testData01 = LoginInfoItem(1,"test01", "test","url", 1)
        val testData02 = LoginInfoItem(1,"test02","test","url", 1)
        //When
        dao.insertLoginInfo(testData01)
        dao.insertLoginInfo(testData02)
        //Then
        Truth.assertThat(dao.getLoginInfo()?.userId).isEqualTo(testData02.userId)
    }
}