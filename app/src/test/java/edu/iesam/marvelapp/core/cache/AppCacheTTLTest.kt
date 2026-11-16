package edu.iesam.marvelapp.core.cache

import android.content.Context
import android.content.SharedPreferences
import edu.iesam.marvelapp.core.time.AppTime
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AppCacheTTLTest {

    private val context: Context = mockk()
    private val sharedPref: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk(relaxed = true)
    private val appTime: AppTime = mockk()

    private lateinit var appCache: AppCacheTTL

    @Before
    fun setUp() {
        // Ojo: sharedPref se crea en el constructor, asi que estos mocks
        // tienen que estar listos ANTES de crear AppCacheTTL.
        every { context.getString(any()) } returns "app_cache"
        every { context.getSharedPreferences("app_cache", Context.MODE_PRIVATE) } returns sharedPref
        every { sharedPref.edit() } returns editor
        every { editor.putLong(any(), any()) } returns editor

        appCache = AppCacheTTL(context, appTime)
    }

    @Test
    fun `updateTime guarda la hora actual con la clave indicada`() {
        // Given
        every { appTime.currentTimeToLong() } returns 1_000L

        // When
        appCache.updateTime("heroes")

        // Then
        verify(exactly = 1) { editor.putLong("heroes", 1_000L) }
        verify(exactly = 1) { editor.apply() }
    }



}