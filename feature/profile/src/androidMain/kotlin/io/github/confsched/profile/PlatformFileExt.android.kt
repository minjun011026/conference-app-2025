package io.github.confsched.profile

import android.content.Intent
import androidx.core.net.toUri
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.context

actual fun PlatformFile.persistPermission() {
    FileKit.context.contentResolver.takePersistableUriPermission(
        absolutePath().toUri(),
        Intent.FLAG_GRANT_READ_URI_PERMISSION,
    )
}
