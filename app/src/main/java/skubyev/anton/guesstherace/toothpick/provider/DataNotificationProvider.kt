package skubyev.anton.guesstherace.toothpick.provider

import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import skubyev.anton.guesstherace.model.data.database.PersistentDatabase
import skubyev.anton.guesstherace.model.data.storage.NotificationEntity
import javax.inject.Inject
import javax.inject.Provider

class DataNotificationProvider @Inject constructor(
        databaseSource: DatabaseSource
) : Provider<PersistentDatabase<NotificationEntity>> {

    private val persistentDatabase: PersistentDatabase<NotificationEntity> =
            PersistentDatabase(NotificationEntity::class, KotlinReactiveEntityStore(KotlinEntityDataStore(databaseSource.configuration)))

    override fun get() = persistentDatabase
}