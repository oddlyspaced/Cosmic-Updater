package com.oddlyspaced.updater

data class UpdateModal(val name: String, val version: String, val build: String, val size: String, val url: String, val changelogUrl: String)