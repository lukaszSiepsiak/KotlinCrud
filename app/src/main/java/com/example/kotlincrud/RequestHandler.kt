package com.example.kotlincrud

import android.os.Environment
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

object RequestHandler {
    const val GET: String = "GET"
    const val POST: String = "POST"
    const val DELETE: String = "DELETE"
    const val PUT: String = "PUT"
    const val TIMEOUT = 8000

    @Throws(java.lang.Exception::class)
    fun requestPOST(urlString: String?, data: String): String? {
        val url = URL(urlString)
        val startTime = System.currentTimeMillis()
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.readTimeout = TIMEOUT
        conn.connectTimeout = TIMEOUT
        conn.requestMethod = POST
        conn.doOutput = true
        conn.doInput = true
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        try {
            val os = conn.outputStream
            val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            writer.write(data)
            writer.flush()
            writer.close()
            os.close()
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = BufferedInputStream(conn.inputStream)
                val output: String = readStream(inputStream = stream)
                println(output)
            } else {
                println("ERROR ${conn.responseCode}")
            }

            val difference = System.currentTimeMillis() - startTime
            println("Difference: $difference")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(java.lang.Exception::class)
    fun requestGET(urlString: String?): String? {
        val result: String
        val url = URL(urlString)
        val startTime = System.currentTimeMillis()
        val conn = url.openConnection() as HttpURLConnection
        conn.readTimeout = TIMEOUT
        conn.connectTimeout = TIMEOUT
        conn.requestMethod = GET
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        try {
            result =
                conn.inputStream.use { it.reader().use { reader -> reader.readText() } }
            val difference = System.currentTimeMillis() - startTime

            val filename = "getRequestKotlin"

            println("Difference: $difference")
        }
        finally {
            conn.disconnect()
        }
        return result
    }


    @Throws(java.lang.Exception::class)
    fun requestDELETE(urlString: String?): String? {
        val result: String
        val url = URL(urlString)
        val startTime = System.currentTimeMillis()
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.readTimeout = TIMEOUT
        conn.connectTimeout = TIMEOUT
        conn.requestMethod = DELETE
        conn.doOutput = true
        conn.doInput = true
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.inputStream

        val difference = System.currentTimeMillis() - startTime
        println("Difference: $difference")

        return null
    }

    @Throws(java.lang.Exception::class)
    fun requestPUT(urlString: String?, data: String): String? {
        val url = URL(urlString)
        val startTime = System.currentTimeMillis()
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.readTimeout = TIMEOUT
        conn.connectTimeout = TIMEOUT
        conn.requestMethod = PUT
        conn.doOutput = true
        conn.doInput = true
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        try {
            val os = conn.outputStream
            val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            writer.write(data)
            writer.flush()
            writer.close()
            os.close()
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = BufferedInputStream(conn.inputStream)
                val output: String = readStream(inputStream = stream)
                println(output)
            } else {
                println("ERROR ${conn.responseCode}")
            }

            val difference = System.currentTimeMillis() - startTime
            println("Difference: $difference")

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(java.lang.Exception::class)
    fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }
}