package skubyev.anton.guesstherace.model.repository.notifications

import skubyev.anton.guesstherace.model.data.database.PersistentDatabase
import skubyev.anton.guesstherace.model.data.storage.NotificationEntity
import skubyev.anton.guesstherace.toothpick.qualifier.database.NotificationQualifier
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
        @NotificationQualifier private var database: PersistentDatabase<NotificationEntity>
) {
    fun updateNotification(notification: NotificationEntity) = database.update(notification)

    fun addNotification(notification: NotificationEntity) = database.addResult(notification)

    fun getNotifications() = database.getAllResults()
}