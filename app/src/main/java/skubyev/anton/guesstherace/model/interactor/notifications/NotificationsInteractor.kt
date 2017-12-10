package skubyev.anton.guesstherace.model.interactor.notifications

import skubyev.anton.guesstherace.entity.Notification
import skubyev.anton.guesstherace.model.mapper.NotificationEntityMapper
import skubyev.anton.guesstherace.model.mapper.NotificationMapper
import skubyev.anton.guesstherace.model.repository.notifications.NotificationsRepository
import javax.inject.Inject

class NotificationsInteractor @Inject constructor(
        private val notificationsRepository: NotificationsRepository,
        private val notificationMapper: NotificationMapper,
        private val notificationEntityMapper: NotificationEntityMapper
) {

    fun updateNotification(notification: Notification) = notificationsRepository.updateNotification(
            notificationEntityMapper.invoke(notification)
    )

    fun getNotifications() = notificationsRepository.getNotifications()
            .map { it.map { notificationMapper.invoke(it) } }
            .map { it.reversed() }
}