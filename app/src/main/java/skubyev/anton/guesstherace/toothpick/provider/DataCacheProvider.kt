package skubyev.anton.guesstherace.toothpick.provider

import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import skubyev.anton.guesstherace.model.data.database.PersistentDatabase
import skubyev.anton.guesstherace.model.data.storage.Cache
import javax.inject.Inject
import javax.inject.Provider

class DataCacheProvider @Inject constructor(
        databaseSource: DatabaseSource
) : Provider<PersistentDatabase<Cache>> {

    private val persistentDatabase: PersistentDatabase<Cache> =
            PersistentDatabase(Cache::class, KotlinReactiveEntityStore(KotlinEntityDataStore(databaseSource.configuration)))

    override fun get() = persistentDatabase
}