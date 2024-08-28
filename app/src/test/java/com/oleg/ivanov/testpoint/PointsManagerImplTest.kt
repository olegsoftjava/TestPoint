package com.oleg.ivanov.testpoint

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.oleg.ivanov.testpoint.network.NetworkService
import com.oleg.ivanov.testpoint.repository.PointsManagerImpl
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response

class PointsManagerImplTest {

    @Mock
    private lateinit var networkService: NetworkService

    private lateinit var pointsManager: PointsManagerImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        pointsManager = PointsManagerImpl(networkService)
    }

    @Test
    fun `getPoints returns points on successful response`() = runBlocking {
        // Arrange
        val mockCall = mock(Call::class.java) as Call<JsonElement>
        val mockResponse = createMockResponse(200, createPointsJson())

        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(networkService.getPoints(2)).thenReturn(mockCall)

        // Act
        val result = pointsManager.getPoints(2)

        // Assert
        assertNotNull(result.pointModel)
        assertNull(result.errorModel)
        assertEquals(2, result.pointModel?.size)
        assertEquals(1.0, result.pointModel?.get(0)?.x)
        assertEquals(2.0, result.pointModel?.get(1)?.x)
    }

    @Test
    fun `getPoints returns error on non-200 response`() = runBlocking {
        // Arrange
        val mockCall = mock(Call::class.java) as Call<JsonElement>
        val mockResponse = createMockErrorResponse(404, "Response.error()")

        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(networkService.getPoints(5)).thenReturn(mockCall)

        // Act
        val result = pointsManager.getPoints(5)

        // Assert
        assertNull(result.pointModel)
        assertNotNull(result.errorModel)
        assertEquals(404, result.errorModel?.code)
        assertEquals("Response.error()", result.errorModel?.description)
    }

    @Test
    fun `getPoints returns error on exception`() = runBlocking {
        // Arrange
        `when`(networkService.getPoints(5)).thenThrow(RuntimeException("Network error"))

        // Act
        val result = pointsManager.getPoints(5)

        // Assert
        assertNull(result.pointModel)
        assertNotNull(result.errorModel)
        assertEquals(-1, result.errorModel?.code)
        assertEquals("Network error", result.errorModel?.description)
    }

    private fun createMockResponse(code: Int, json: String): Response<JsonElement> {
        val jsonElement: JsonElement = JsonParser.parseString(json)
        return Response.success(jsonElement)
    }

    private fun createMockErrorResponse(code: Int, message: String): Response<JsonElement> {
        val responseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "{\"error\":\"$message\"}")
        return Response.error(code, responseBody)
    }

    private fun createPointsJson(): String {
        val jsonArray = JSONArray().apply {
            put(JSONObject().apply {
                put("x", 1.0)
                put("y", 1.0)
            })
            put(JSONObject().apply {
                put("x", 2.0)
                put("y", 2.0)
            })
        }
        return JSONObject().put("points", jsonArray).toString()
    }
}
