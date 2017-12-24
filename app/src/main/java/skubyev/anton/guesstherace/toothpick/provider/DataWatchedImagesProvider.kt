package skubyev.anton.guesstherace.toothpick.provider

import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import skubyev.anton.guesstherace.model.data.database.PersistentDatabase
import skubyev.anton.guesstherace.model.data.storage.WatchedImageEntity
import javax.inject.Inject
import javax.inject.Provider

class DataWatchedImagesProvider @Inject constructor(
        databaseSource: DatabaseSource
) : Provider<PersistentDatabase<WatchedImageEntity>> {

    private val persistentDatabase: PersistentDatabase<WatchedImageEntity> =
            PersistentDatabase(WatchedImageEntity::class, KotlinReactiveEntityStore(KotlinEntityDataStore(databaseSource.configuration)))

    override fun get() = persistentDatabase
}