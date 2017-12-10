package skubyev.anton.guesstherace.model.data.storage

import io.requery.Entity
import io.requery.Persistable
import java.util.*

@Entity
data class Cache constructor(
        val name: String,
        val time: Long = Date().time
) : Persistable