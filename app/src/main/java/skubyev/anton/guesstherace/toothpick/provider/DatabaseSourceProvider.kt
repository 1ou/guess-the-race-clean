package skubyev.anton.guesstherace.toothpick.provider

import android.content.Context
import io.requery.android.sqlite.DatabaseSource
import skubyev.anton.guesstherace.model.data.storage.Models
import javax.inject.Inject
import javax.inject.Provider

class DatabaseSourceProvider @Inject constructor(
        context: Context
) : Provider<DatabaseSource> {

    companion object {
        const val DB_NAME = "database.db"
        const val DB_VERSION = 1
    }

    private val databaseSource: DatabaseSource = DatabaseSource(context, Models.DEFAULT, DB_NAME, DB_VERSION)

    override fun get() = databaseSource
}