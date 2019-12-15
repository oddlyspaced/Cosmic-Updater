package com.oddlyspaced.updater

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception


class GetProp {

    // https://www.mkyong.com/java/how-to-execute-shell-command-from-java/
    fun getProp(prop: String): String {
        try {
            val process: Process = Runtime.getRuntime().exec("getprop $prop")
            val output: StringBuilder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?

            while (true) {
                line = reader.readLine()
                if (line == null)
                    break
                else
                    output.append(line + "\n");
            }

            return output.toString()
        }
        catch (e: Exception) {
            Log.e("GetProp", e.toString())
            return ""
        }
    }

}