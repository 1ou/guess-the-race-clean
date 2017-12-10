package skubyev.anton.guesstherace.model.data.database

import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryDatabase <T: Any> @Inject constructor() : Database<T> {
    val results: MutableList<T> = mutableListOf()

    override fun addResult(item: T): Completable = Completable.fromAction { results.add(item) }

    override fun addListResult(item: List<T>): Completable = Completable.fromAction { results.addAll(item) }

    override fun deleteResult(item: T): Completable = Completable.fromAction { results.remove(item) }

    override fun getAllResults(): Single<List<T>> = Single.just(results)

    override fun removeAllResults(): Completable = Completable.fromAction { results.clear() }

    override fun update(item: T): Single<T> = Single.just( results[0] ) // TODO working is not correct

    override fun getFirstElement(): Single<T> = Single.just(results[0])
}
