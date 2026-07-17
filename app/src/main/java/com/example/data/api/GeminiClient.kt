package com.example.data.api

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val MODEL = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun Bitmap.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        // Compress more to fit in request easily and stay fast
        compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    suspend fun generateCoachingQuote(
        onboardingGoal: String, 
        recentStatsSummary: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Discipline is the bridge between goals and accomplishment. Do not quit."
        }

        val prompt = """
            You are the strict, supportive Discipline OS AI Coach. 
            The user's biggest goal is: '$onboardingGoal'.
            Here are their recent statistics:
            $recentStatsSummary
            
            Generate a short, powerful, action-oriented discipline statement or wake-up directive.
            Rule: Max 15 words. NO generic quotes, no fluff, no flowery language. Direct and striking.
        """.trimIndent()

        try {
            val responseText = executeGeminiRequest(apiKey, prompt, null)
            return@withContext responseText ?: "Excuses burn like paper. Keep moving."
        } catch (e: Exception) {
            Log.e(TAG, "Quote generation error", e)
            return@withContext "You are what you repeatedly do. Excellence is a habit."
        }
    }

    suspend fun getCoachFeedback(
        name: String,
        goal: String,
        habitsCompleted: String,
        journalEntries: String,
        scoreHistory: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Your AI Coach is currently in offline standby mode. To activate full psych-accountability and daily reviews, configure a valid GEMINI_API_KEY in the Secrets Panel. Focus on finishing your daily missions."
        }

        val prompt = """
            You are the strict, supportive Discipline OS AI Coach. 
            User name: $name
            Primary goal: $goal
            Completed habits list:
            $habitsCompleted
            
            Journal entries logs:
            $journalEntries
            
            Discipline Score history:
            $scoreHistory
            
            Write a daily review report. Act like a strict but highly supportive personal coach who tracks their metrics.
            Analyze their performance, identify their weak points, and recommend exactly 2 concrete actions to improve tomorrow.
            Never offer generic praise. Make it sharp, professional, psychological, and highly focused on extreme accountability.
        """.trimIndent()

        try {
            val responseText = executeGeminiRequest(apiKey, prompt, null)
            return@withContext responseText ?: "Error generating review. Maintain your routine."
        } catch (e: Exception) {
            Log.e(TAG, "Coach review error", e)
            return@withContext "Standing by. Ensure you complete all required tasks. No excuses."
        }
    }

    suspend fun verifyBathroomPhoto(bitmap: Bitmap): Boolean = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            // Offline fallback: If no API key is set, automatically succeed after a short simulated wait.
            return@withContext true
        }

        val prompt = "Is there a bathroom sink, a faucet, a toilet, a bathroom mirror, bathroom tiles, a shower head, or a bathtub visible in this image? Answer with exactly YES or NO with no extra text or characters."
        
        try {
            val responseText = executeGeminiRequest(apiKey, prompt, bitmap)
            val cleanResponse = responseText?.trim()?.uppercase() ?: "NO"
            Log.d(TAG, "Verification result: $cleanResponse")
            return@withContext cleanResponse.contains("YES")
        } catch (e: Exception) {
            Log.e(TAG, "Verification error", e)
            return@withContext true // Safe fallback in case of connection timeouts, so user is not stuck with alarm
        }
    }

    private fun executeGeminiRequest(apiKey: String, prompt: String, bitmap: Bitmap?): String? {
        val url = "$BASE_URL?key=$apiKey"
        val mediaType = "application/json; charset=utf-8".toMediaType()

        val jsonRequest = JSONObject()
        val contentsArray = JSONArray()
        val contentObject = JSONObject()
        val partsArray = JSONArray()

        // Add text part
        val textPart = JSONObject().put("text", prompt)
        partsArray.put(textPart)

        // Add image part if present
        if (bitmap != null) {
            val imagePart = JSONObject()
            val inlineData = JSONObject()
            inlineData.put("mimeType", "image/jpeg")
            inlineData.put("data", bitmap.toBase64())
            imagePart.put("inlineData", inlineData)
            partsArray.put(imagePart)
        }

        contentObject.put("parts", partsArray)
        contentsArray.put(contentObject)
        jsonRequest.put("contents", contentsArray)

        // Add system/safety parameters if needed
        val requestBodyText = jsonRequest.toString()
        val body = requestBodyText.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Log.e(TAG, "HTTP error: ${response.code} ${response.message}")
                return null
            }
            val bodyString = response.body?.string() ?: return null
            val responseJson = JSONObject(bodyString)
            val candidates = responseJson.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                if (content != null) {
                    val parts = content.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return parts.getJSONObject(0).optString("text")
                    }
                }
            }
            return null
        }
    }
}
