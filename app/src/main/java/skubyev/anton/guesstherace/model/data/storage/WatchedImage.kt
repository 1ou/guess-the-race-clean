package skubyev.anton.guesstherace.model.data.storage

import io.requery.Entity
import io.requery.Key
import io.requery.Persistable

@Entity
interface WatchedImage: Persistable {
        @get:Key
        var idImage: Int
}