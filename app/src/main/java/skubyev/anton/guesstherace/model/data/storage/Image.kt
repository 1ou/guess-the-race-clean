package skubyev.anton.guesstherace.model.data.storage

import io.requery.Entity
import io.requery.Persistable

@Entity
data class Image constructor(
        val idImage: Int,
        val url: String,
        val urlAnswer: String,
        val race: String
) : Persistable
