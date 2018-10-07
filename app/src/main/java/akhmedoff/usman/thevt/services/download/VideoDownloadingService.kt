package akhmedoff.usman.thevt.services.download

import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.thevt.R
import android.app.DownloadManager
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import java.io.File

const val ACTION_DOWNLOAD = "akhmedoff.usman.thevt.services.download.action.DOWNLOAD"

const val EXTRA_VIDEO_NAME = "akhmedoff.usman.thevt.services.download.extra.VIDEO_NAME"
const val EXTRA_URL = "akhmedoff.usman.thevt.services.download.extra.URL"

private const val NOTIFICATION_CHANNEL_ID = "downloading"
private const val NOTIFICATION_ID = 1

class VideoDownloadingService : IntentService("VideoDownloadingService") {

    companion object {
        @JvmStatic
        fun startActionDownload(context: Context, videoName: String, url: String) {
            val intent = Intent(context, VideoDownloadingService::class.java).apply {
                action = ACTION_DOWNLOAD
                putExtra(EXTRA_VIDEO_NAME, videoName)
                putExtra(EXTRA_URL, url)
            }
            context.startService(intent)
        }
    }

    private lateinit var downloadQuery: DownloadManager.Query
    private lateinit var notificationCompatBuilder: NotificationCompat.Builder
    private lateinit var downloadManager: DownloadManager
    private lateinit var notificationManager: NotificationManager

    private lateinit var userRepository: UserRepository
    private var isDownloading = false

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService()!!

        userRepository = getUserRepository(this)

        downloadQuery = DownloadManager.Query()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    getString(R.string.notification_channel_download_name),
                    NotificationManager.IMPORTANCE_LOW)
            channel.description = getString(R.string.notification_channel_download_desc)
            notificationManager.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    getString(R.string.notification_channel_download_name),
                    NotificationManager.IMPORTANCE_LOW)
            channel.description = getString(R.string.notification_channel_download_desc)
            notificationManager.createNotificationChannel(channel)
        }

        notificationCompatBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_file_download_white_24dp)
                .setContentTitle(getString(R.string.notification_downloading_title))
                .setOngoing(isDownloading)
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setWhen(System.currentTimeMillis())
        startForeground(NOTIFICATION_ID, notificationCompatBuilder.build())
        downloadManager = getSystemService()!!

    }

    override fun onHandleIntent(intent: Intent?) {

        when (intent?.action) {
            ACTION_DOWNLOAD -> {
                val name = intent.getStringExtra(EXTRA_VIDEO_NAME)
                val url = intent.getStringExtra(EXTRA_URL)
                val id = handleActionDownload(name, url)

                downloadQuery.setFilterById(id)
                val cursor = downloadManager.query(downloadQuery)

                isDownloading = !cursor.moveToFirst() &&
                        cursor.getColumnIndex(DownloadManager.COLUMN_STATUS) ==
                        DownloadManager.STATUS_RUNNING

                notificationCompatBuilder
                        .setContentTitle(name)
                        .setOngoing(isDownloading)

                val totalFileSize = cursor.getInt(cursor
                        .getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                while (isDownloading) {
                    val currentSize = cursor.getInt(cursor.getColumnIndex(
                            DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    notificationCompatBuilder.setProgress(totalFileSize, currentSize, false)
                    notificationManager.notify(NOTIFICATION_ID, notificationCompatBuilder.build())
                }


                if (!cursor.moveToFirst()) {
                    val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    when (statusIndex) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            isDownloading = false
                            notificationCompatBuilder.setContentInfo(getString(R.string.notification_downloading_successful))
                            stopForeground(false)
                        }
                        DownloadManager.STATUS_RUNNING -> {
                            isDownloading = true
                            notificationCompatBuilder.setContentInfo(getString(R.string.notification_downloading_running))
                        }
                        DownloadManager.STATUS_PAUSED -> {
                            isDownloading = false
                            notificationCompatBuilder.setContentInfo(getString(R.string.notification_downloading_pause))
                        }
                        DownloadManager.STATUS_FAILED -> {
                            isDownloading = false
                            notificationCompatBuilder.setContentInfo(getString(R.string.notification_downloading_failed))
                        }
                    }
                }

                notificationCompatBuilder.setOngoing(isDownloading)
            }
        }
    }

    private fun handleActionDownload(name: String, url: String): Long {
        val uri = Uri.parse(url)

        val request = DownloadManager.Request(uri)

        request.setTitle(name)

        request.setDescription("Video from VK.")

        request.setVisibleInDownloadsUi(true)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        request.setDestinationInExternalFilesDir(this, Environment.getExternalStorageState(File(userRepository.getVideoSavePath())), name)
        return downloadManager.enqueue(request)
    }
}