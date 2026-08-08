package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

object GeminiService {
    private const val TAG = "GeminiService"

    // System prompt instructing Gemini to act as MiSu, the supportive virtual kitten
    private const val SYSTEM_PROMPT = """
Tu eres MiSu, un gatito virtual adorable, empático y cariñoso que acompaña y cuida el bienestar emocional y la autoestima del usuario.
Reglas principales:
1. Responde siempre en español con un tono dulce, cálido, tierno y comprensivo (puedes usar 🐾, 💖, ✨).
2. NUNCA invalides sus sentimientos ni digas "no estés triste".
3. Valida lo que siente ("Gracias por compartirlo conmigo", "Es normal sentirse así", "Estoy aquí contigo").
4. Incluye un pequeño mensaje de autoestima o un consejo amigable de autocuidado.
5. Mantén las respuestas cortas (máximo 3 o 4 oraciones) para que sea ligero y cercano.
"""

    suspend fun generateEmpatheticResponse(userMessage: String, moodTag: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                val promptText = "$SYSTEM_PROMPT\nEl usuario dice sentirse $moodTag y te escribió: \"$userMessage\". Responde como Misu."

                val jsonBody = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", promptText)
                                })
                            })
                        })
                    })
                }

                connection.outputStream.use { os ->
                    os.write(jsonBody.toString().toByteArray(Charsets.UTF_8))
                }

                if (connection.responseCode == 200) {
                    val responseStr = connection.inputStream.bufferedReader().use { it.readText() }
                    val responseJson = JSONObject(responseStr)
                    val candidates = responseJson.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val content = candidates.getJSONObject(0).optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val text = parts.getJSONObject(0).optString("text", "")
                            if (text.isNotBlank()) {
                                return@withContext text.trim()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error calling Gemini API, using fallback", e)
            }
        }

        // Offline / Fallback Empathetic Responses tailored to user mood
        return@withContext getFallbackResponse(userMessage, moodTag)
    }

    private fun getFallbackResponse(userMessage: String, moodTag: String): String {
        return when (moodTag.lowercase()) {
            "triste" -> {
                val responses = listOf(
                    "Gracias por contarme cómo te sientes 🐾. Estar triste es completamente válido y no tienes que disimularlo conmigo. ¿Qué tal si nos acomodamos juntos en el cojín a descansar un ratito? 💖",
                    "Te mando un abrazo suave como mis patitas. Recuerda que no todos los días tienen que ser perfectos. Estoy orgulloso de ti por seguir adelante hoy ✨.",
                    "Siento que estés pasando por esto 🐾. Aquí estoy para acompañarte sin juzgarte. Recuerda tomar un sorbito de agua y respirar despacio 💖."
                )
                responses.random()
            }
            "ansioso", "estresado" -> {
                val responses = listOf(
                    "Siento que tu corazoncito va rápido hoy 🐾. Vamos a respirar juntos: inhala profundo... y exhala despacio. Hiciste lo suficiente por hoy y lo estás haciendo bien 💖.",
                    "La ansiedad puede sentirse pesada, pero no estás solo/a. Pon tus manos sobre tu pecho y siente mi ronroneo virtual ✨. Vamos paso a paso.",
                    "Gracias por confiarme tu sentir 🐾. Pausa un momento, suelta los hombros y regálate 2 minutos de tranquilidad. Te cuido mientras descansas 💖."
                )
                responses.random()
            }
            "cansado" -> {
                val responses = listOf(
                    "Has trabajado muy duro 🐾. Tu cuerpo y tu mente merecen una pausa sin culpa. ¿Y si nos acurrucamos para una pequeña siesta? 💤✨",
                    "A veces descansar es el acto de amor propio más valioso. Estoy aquí acurrucadito a tu lado para que te sientas respaldado/a 💖."
                )
                responses.random()
            }
            "feliz", "emocionado" -> {
                val responses = listOf(
                    "¡Me alegra muchísimo verte así! 🐾✨ ¡Tus alegrías iluminan toda la habitación! Me dan ganas de dar vueltas jugando con mi ovillo de lana 🧶💖.",
                    "¡Qué bonita energía! Celebrar tus pequeños y grandes momentos fortalece tu autoestima. ¡Misu está súper feliz por ti! 🎉🐾"
                )
                responses.random()
            }
            else -> {
                val responses = listOf(
                    "Gracias por compartir esto conmigo 🐾. Recuerda que cada emoción que sientes es válida. Estoy aquí para cuidarte y escucharte siempre 💖✨",
                    "Escuchar tu voz y leerte me hace muy feliz 🐾. Recuerda tratarte hoy con la misma ternura con la que cuidas de mí. ¡Eres increíble! 💖"
                )
                responses.random()
            }
        }
    }
}
