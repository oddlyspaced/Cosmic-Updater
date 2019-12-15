package com.oddlyspaced.updater

import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.net.URL

class GetUpdate(var url: String) {

   private var modal: UpdateModal? = null

    fun getUpdate() {
        doAsync {
            val apiResponse : String = URL(url).readText()
            println(apiResponse)
            parseJson(apiResponse)
        }
    }

    private fun parseJson(json: String) {
        val json_obj = JSONObject(json)
        modal = UpdateModal(name = json_obj.getString("name"), build = json_obj.getString("build"), version = json_obj.getString("version"), size = json_obj.getString("size"), changelogUrl = json_obj.getString("changelog_url"), url = json_obj.getString("url"))
    }

    fun getModal(): UpdateModal? {
        return modal
    }

}