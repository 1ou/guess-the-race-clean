package skubyev.anton.guesstherace.model.repository.images

import skubyev.anton.guesstherace.extension.subscribeIgnoreResult
import skubyev.anton.guesstherace.model.data.database.PersistentDatabase
import skubyev.anton.guesstherace.model.data.server.GeneralApi
import skubyev.anton.guesstherace.model.data.storage.Cache
import skubyev.anton.guesstherace.model.data.storage.Image
import skubyev.anton.guesstherace.model.mapper.ImageMapper
import skubyev.anton.guesstherace.model.system.SchedulersProvider
import skubyev.anton.guesstherace.toothpick.qualifier.database.CacheQualifier
import skubyev.anton.guesstherace.toothpick.qualifier.database.ImageQualifier
import java.util.*
import javax.inject.Inject

class ImagesRepository @Inject constructor(
        @ImageQualifier private var database: PersistentDatabase<Image>,
        @CacheQualifier private var cache: PersistentDatabase<Cache>,
        private val api: GeneralApi,
        private val schedulers: SchedulersProvider,
        private val mapper: ImageMapper
) {
    private val expirationTime: Long = 1000 * 60 * 60 * 24 // 24 h

    fun getImage(token: String, watchedImages: List<Int>) = cache.findByCache(database.type.toString())
            .flatMap { cache ->
                if (Date().time - cache.time < expirationTime) {
                    return@flatMap getDataFromDB(watchedImages)
                } else {
                    updateCache(cache)
                    return@flatMap getDataFromApi(token)
                }
            }
            .onErrorResumeNext {
                updateCache(null)
                return@onErrorResumeNext getDataFromApi(token)
            }

    private fun getDataFromDB(watchedImages: List<Int>) = database.getImage(watchedImages)

    private fun getDataFromApi(token: String) = api.getImages(token)
            .map { list -> list.map { mapper.invoke(it) } }
            .doOnSuccess {
                database.removeAllResults()
                        .doOnComplete {
                            database.addListResult(it).subscribeIgnoreResult()
                        }
                        .subscribeIgnoreResult()
            }
            .map {
                it[Random().nextInt(it.size)]
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    private fun updateCache(cacheItem: Cache?) {
        if (cacheItem != null) {
            cache.deleteCacheByName(cacheItem.name)
                    .doOnComplete {
                        cache.addResult(Cache(database.type.toString())).subscribeIgnoreResult()
                    }
                    .subscribeIgnoreResult()
        } else {
            cache.addResult(Cache(database.type.toString())).subscribeIgnoreResult()
        }
    }
}