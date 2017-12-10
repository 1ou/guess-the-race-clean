package skubyev.anton.guesstherace.toothpick.provider

import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import skubyev.anton.guesstherace.model.data.database.PersistentDatabase
import skubyev.anton.guesstherace.model.data.storage.Image
import javax.inject.Inject
import javax.inject.Provider

class DataImagesProvider @Inject constructor(
        databaseSource: DatabaseSource
) : Provider<PersistentDatabase<Image>> {

    private val persistentDatabase: PersistentDatabase<Image> =
            PersistentDatabase(Image::class, KotlinReactiveEntityStore(KotlinEntityDataStore(databaseSource.configuration)))

    override fun get() = persistentDatabase
}