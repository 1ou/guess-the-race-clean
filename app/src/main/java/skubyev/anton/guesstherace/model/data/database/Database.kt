package skubyev.anton.guesstherace.model.data.database

import io.reactivex.Completable
import io.reactivex.Single

interface Database <T> {
    fun addResult(item: T): Completable

    fun addListResult(item: List<T>): Completable

    fun deleteResult(item: T): Completable

    fun getAllResults(): Single<List<T>>

    fun removeAllResults(): Completable

    fun update(item: T): Single<T>

    fun getFirstElement(): Single<T>
}