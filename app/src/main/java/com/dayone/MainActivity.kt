package com.dayone

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import java.io.File
import java.io.FileOutputStream

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var notificationService: com.dayone.notification.NotificationService
    @Inject lateinit var repository: com.dayone.data.DayOneRepository
    @Inject lateinit var preferencesStore: com.dayone.data.PreferencesStore
    @Inject lateinit var widgetUpdater: com.dayone.widget.DayOneWidgetUpdater
    @Inject lateinit var alarmScheduler: com.dayone.alarm.DayOneAlarmScheduler

    private lateinit var webView: WebView
    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    private val fileChooserLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val uris = if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { arrayOf(it) } ?: emptyArray()
            } else {
                emptyArray()
            }
            filePathCallback?.onReceiveValue(uris)
            filePathCallback = null
        }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            showToast(if (granted) "Notifications enabled" else "Notifications disabled")
        }

    private val soundPickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    runBlocking {
                        preferencesStore.setNotificationSound(uri.toString())
                    }
                    webView.evaluateJavascript("onSoundPicked('${uri.toString()}')", null)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureWindow()
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.dayOneWebView)
        configureWebView()
        configureBackButton()
        webView.loadUrl("file:///android_asset/dayone.html")
    }

    private fun configureWindow() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
    }

    private fun configureWebView() {
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false
            mediaPlaybackRequiresUserGesture = false
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            cacheMode = WebSettings.LOAD_DEFAULT
        }
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.addJavascriptInterface(AndroidBridge(this), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean = false
            
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                android.util.Log.e("DayOneUI", "WebView Error: ${error.description}")
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                android.util.Log.d("DayOneUI", "JS Console: ${consoleMessage?.message()}")
                return true
            }
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                this@MainActivity.filePathCallback?.onReceiveValue(emptyArray())
                this@MainActivity.filePathCallback = filePathCallback
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "application/json"
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                fileChooserLauncher.launch(Intent.createChooser(intent, "Import DayOne backup"))
                return true
            }
        }
    }

    private fun configureBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                webView.evaluateJavascript(
                    """
                    (function() {
                      if (document.getElementById('milestoneOverlay')?.classList.contains('open')) {
                        closeMilestone(); return true;
                      }
                      if (document.getElementById('confirmDialog')?.classList.contains('open')) {
                        closeConfirm(); return true;
                      }
                      if (document.getElementById('createModal')?.classList.contains('open')) {
                        closeModal(); return true;
                      }
                      if (document.getElementById('habitDetailPanel')?.classList.contains('open')) {
                        closeHabitDetail(); return true;
                      }
                      if (document.getElementById('defaultPickerModal')?.classList.contains('open')) {
                        closeDefaultPicker(); return true;
                      }
                      if (document.getElementById('resetPanel')?.classList.contains('open')) {
                        closeResetPanel(); return true;
                      }
                      return false;
                    })()
                    """.trimIndent()
                ) { result ->
                    if (result != "true") {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        })
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    class AndroidBridge(private val activity: MainActivity) {
        private val handler = Handler(Looper.getMainLooper())

        @JavascriptInterface
        fun saveFile(filename: String, content: String) {
            activity.runOnUiThread {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val values = ContentValues().apply {
                            put(MediaStore.Downloads.DISPLAY_NAME, filename)
                            put(MediaStore.Downloads.MIME_TYPE, "application/json")
                            put(MediaStore.Downloads.IS_PENDING, 1)
                        }
                        val resolver = activity.contentResolver
                        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                        if (uri != null) {
                            resolver.openOutputStream(uri)?.use { it.write(content.toByteArray()) }
                            values.clear()
                            values.put(MediaStore.Downloads.IS_PENDING, 0)
                            resolver.update(uri, values, null, null)
                            activity.showToast("Exported to Downloads/$filename")
                        } else {
                            activity.showToast("Export failed")
                        }
                    } else {
                        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        if (!dir.exists()) dir.mkdirs()
                        FileOutputStream(File(dir, filename)).use { it.write(content.toByteArray()) }
                        activity.showToast("Exported to Downloads/$filename")
                    }
                } catch (e: Exception) {
                    activity.showToast("Export failed: ${e.message ?: "unknown error"}")
                }
            }
        }

        @JavascriptInterface
        fun requestNotificationPermission() {
            activity.runOnUiThread { activity.requestNotificationPermission() }
        }

        @JavascriptInterface
        fun getPreferences(): String {
            val prefs = runBlocking { activity.preferencesStore.preferences.first() }
            val json = JSONObject().apply {
                put("darkTheme", prefs.darkTheme)
                put("notificationsEnabled", prefs.notificationsEnabled)
                put("notificationTime", prefs.notificationTime)
                put("notificationSound", prefs.notificationSound)
                put("widgetColor", prefs.widgetColor)
                put("widgetGloss", prefs.widgetGloss)
                put("dynamicIcon", prefs.dynamicIcon)
            }
            return json.toString()
        }

        @JavascriptInterface
        fun setNotificationTime(time: String) {
            runBlocking { 
                activity.preferencesStore.setNotificationTime(time)
                activity.alarmScheduler.scheduleDailyAlarms()
            }
        }

        @JavascriptInterface
        fun pickNotifSound() {
            activity.runOnUiThread {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "audio/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                activity.soundPickerLauncher.launch(Intent.createChooser(intent, "Select Notification Sound"))
            }
        }

        @JavascriptInterface
        fun setWidgetSettings(color: String, gloss: Boolean, dynamicIcon: Boolean) {
            runBlocking {
                activity.preferencesStore.setWidgetColor(color)
                activity.preferencesStore.setWidgetGloss(gloss)
                activity.preferencesStore.setDynamicIcon(dynamicIcon)
                activity.widgetUpdater.updateAllWidgets()
            }
        }

        @JavascriptInterface
        fun testNotification() {
            CoroutineScope(Dispatchers.IO).launch {
                val state = activity.repository.currentState()
                activity.runOnUiThread {
                    activity.notificationService.showNotification(state, "Test Notification", "This is how your streak alerts will look!")
                }
            }
        }

        @JavascriptInterface
        fun showTestNotification(title: String, body: String, delayMs: Long) {
            activity.runOnUiThread {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    activity.requestNotificationPermission()
                    return@runOnUiThread
                }
                
                CoroutineScope(Dispatchers.IO).launch {
                    val state = activity.repository.currentState()
                    activity.runOnUiThread {
                        handler.postDelayed({
                            activity.notificationService.showNotification(state, title, body)
                        }, delayMs.coerceAtLeast(0L))
                    }
                }
            }
        }

        @JavascriptInterface
        fun cancelScheduledNotifications() {
            activity.runOnUiThread {
                val manager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.cancelAll()
            }
        }

        @JavascriptInterface
        fun syncWidgetState(json: String) {
            // We will implement this to update widgets via a broadcast or direct repository update
            val intent = Intent("com.dayone.SYNC_WIDGET")
            intent.putExtra("data", json)
            intent.setPackage(activity.packageName)
            activity.sendBroadcast(intent)
        }
    }
}
