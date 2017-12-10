package skubyev.anton.guesstherace.toothpick.provider

import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import skubyev.anton.guesstherace.model.data.database.PersistentDatabase
import skubyev.anton.guesstherace.model.data.storage.WatchedImage
import javax.inject.Inject
import javax.inject.Provider

class DataWatchedImagesProvider @Inject constructor(
        databaseSource: DatabaseSource
) : Provider<PersistentDatabase<WatchedImage>> {

    private val persistentDatabase: PersistentDatabase<WatchedImage> =
            PersistentDatabase(WatchedImage::class, KotlinReactiveEntityStore(KotlinEntityDataStore(databaseSource.configuration)))

    override fun get() = persistentDatabase
}