package showmethe.github.core.base

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * showmethe.github.core.base
 * 2019/12/3
 **/
class AppProvider : ContentProvider() {



    override fun insert(uri: Uri, values: ContentValues?): Uri?  = null

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int =- -1

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int =  -1

    override fun getType(uri: Uri): String?  = null

    override fun onCreate(): Boolean {
        ContextProvider.get().attach(context)
        return false
    }
}