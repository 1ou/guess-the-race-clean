package skubyev.anton.guesstherace.toothpick.module

import io.requery.android.sqlite.DatabaseSource
import skubyev.anton.guesstherace.model.data.database.PersistentDatabase
import skubyev.anton.guesstherace.toothpick.provider.*
import skubyev.anton.guesstherace.toothpick.qualifier.database.CacheQualifier
import skubyev.anton.guesstherace.toothpick.qualifier.database.ImageQualifier
import skubyev.anton.guesstherace.toothpick.qualifier.database.NotificationQualifier
import skubyev.anton.guesstherace.toothpick.qualifier.database.WatchedImageQualifier
import toothpick.config.Module

class DatabaseModule : Module() {
    init {
        bind(DatabaseSource::class.java).toProvider(DatabaseSourceProvider::class.java)
        bind(PersistentDatabase::class.java).withName(CacheQualifier::class.java).toProvider(DataCacheProvider::class.java)
        bind(PersistentDatabase::class.java).withName(WatchedImageQualifier::class.java).toProvider(DataWatchedImagesProvider::class.java)
        bind(PersistentDatabase::class.java).withName(ImageQualifier::class.java).toProvider(DataImagesProvider::class.java)
        bind(PersistentDatabase::class.java).withName(NotificationQualifier::class.java).toProvider(DataNotificationProvider::class.java)
    }
}