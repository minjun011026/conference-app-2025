package io.github.droidkaigi.confsched

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.vinceglb.filekit.dialogs.compose.util.encodeToByteArray
import io.github.vinceglb.filekit.utils.toNSData
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.datetime.toNSDate
import platform.EventKit.EKEntityType
import platform.EventKit.EKEvent
import platform.EventKit.EKEventStore
import platform.EventKit.EKSpan
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage

@Composable
actual fun rememberExternalNavController(): ExternalNavController {
    return IosExternalNavController()
}

internal class IosExternalNavController : ExternalNavController {
    override fun navigate(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl == null) {
            println("Failed to navigate to URL (invalid URL): $url")
            return
        }

        if (!UIApplication.sharedApplication.canOpenURL(nsUrl)) {
            println("Cannot open URL (scheme not allowed or no handler): $url")
            return
        }

        UIApplication.sharedApplication.openURL(
            url = nsUrl,
            options = emptyMap<Any?, Any?>(),
            completionHandler = { success ->
                if (!success) {
                    println("Failed to open URL via UIApplication.open(_:options:completionHandler:): $url")
                }
            },
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun navigateToCalendarRegistration(timetableItem: TimetableItem) {
        val eventStore = EKEventStore()

        eventStore.requestAccessToEntityType(EKEntityType.EKEntityTypeEvent) { granted, error ->
            if (granted) {
                val event = EKEvent.eventWithEventStore(eventStore)
                event.startDate = timetableItem.startsAt.toNSDate()
                event.endDate = timetableItem.endsAt.toNSDate()
                event.title = timetableItem.title.currentLangTitle
                event.notes = timetableItem.url
                event.location = timetableItem.room.name.currentLangTitle
                event.calendar = eventStore.defaultCalendarForNewEvents

                memScoped {
                    val errorPtr = alloc<ObjCObjectVar<NSError?>>()
                    val success = eventStore.saveEvent(event, span = EKSpan.EKSpanThisEvent, error = errorPtr.ptr)
                    if (success) {
                        println("Event added to calendar: ${event.title}")
                    } else {
                        println("Failed to add event to calendar: ${error?.localizedDescription ?: "Unknown error"}")
                    }
                }
            } else {
                println("Access to calendar not granted: $error")
            }
        }
    }

    @OptIn(BetaInteropApi::class)
    override fun onShareClick(timetableItem: TimetableItem) {
        val text = "[${timetableItem.room.name.currentLangTitle}] ${timetableItem.formattedMonthAndDayString} " +
            "${timetableItem.startsTimeString} - ${timetableItem.endsTimeString}\n" +
            "${timetableItem.title.currentLangTitle}\n" +
            timetableItem.url

        share(listOf(NSString.create(text)))
    }

    override fun onShareProfileCardClick(shareText: String, imageBitmap: ImageBitmap) {
        MainScope().launch {
            val image = UIImage(data = imageBitmap.encodeToByteArray().toNSData())
            share(listOf(NSString.create(shareText), image))
        }
    }

    private fun share(items: List<*>) {
        val activityViewController = UIActivityViewController(
            activityItems = items,
            applicationActivities = null,
        )

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null,
        )
    }
}
