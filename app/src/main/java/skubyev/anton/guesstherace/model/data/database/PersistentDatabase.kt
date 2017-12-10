package skubyev.anton.guesstherace.model.data.database

import io.reactivex.Completable
import io.reactivex.Single
import io.requery.kotlin.eq
import io.requery.kotlin.notIn
import io.requery.query.function.Random
import io.requery.reactivex.KotlinReactiveEntityStore
import skubyev.anton.guesstherace.model.data.storage.Cache
import skubyev.anton.guesstherace.model.data.storage.Image
import javax.inject.Inject
import kotlin.reflect.KClass

open class PersistentDatabase<T : Any> @Inject constructor(val type: KClass<T>, val store: KotlinReactiveEntityStore<T>) : Database<T> {

    override fun addResult(item: T): Completable = store.insert(item).toCompletable()

    override fun addListResult(item: List<T>): Completable = store.insert(item).toCompletable()

    override fun deleteResult(item: T): Completable = store.delete(item)

    override fun getAllResults(): Single<List<T>> = (store select type).get().observable().toList()

    override fun removeAllResults(): Completable = store.delete(type).get().single().toCompletable()

    override fun update(item: T): Single<T> = store.update(item).toObservable().singleOrError()

    override fun getFirstElement(): Single<T> = (store select type).limit(1).get().observable().firstOrError()

    // Cache
    fun findByCache(str: String): Single<T> = (store select type).where(Cache::name eq str).get().observable().firstOrError()

    fun deleteCacheByName(name: String): Completable = store.delete(type).where(Cache::name eq name).get().single().toCompletable()

    // Images
    fun getImage(watchedImages: List<Int>): Single<T> = (store select type)
            .where(Image::idImage.notIn(watchedImages))
            .orderBy(Random())
            .limit(1)
            .get()
            .observable()
            .firstOrError()

}
