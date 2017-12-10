package skubyev.anton.guesstherace.model.data.storage

import io.requery.Entity
import io.requery.Persistable

@Entity
data class WatchedImage constructor(
        var idImage: Int
) : Persistable