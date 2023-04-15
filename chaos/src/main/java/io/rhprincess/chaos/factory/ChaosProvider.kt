package io.rhprincess.chaos.factory

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import io.rhprincess.chaos.main.ChaosException

/**
 * 这是一个全局上下文提供器，提供的是Application的上下文
 * 详情请看： [无侵入式Context][https://www.jianshu.com/p/6f6d1352fb43]
 */
class ChaosProvider : ContentProvider() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        private var isInitialized = false
        val idMap = HashMap<String, Int>()
        val idMapReflect = HashMap<Int, String>()
        var initialIdValue = 0x01000000
    }

    override fun onCreate(): Boolean {
        if (context == null) throw ChaosException.ContextNotFound()
        if (!isInitialized) {
            Companion.context = context!!
            isInitialized = true
        }
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}