package com.harshal.androidapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var permissionPanel: View
    private lateinit var permissionMessage: TextView
    private lateinit var grantButton: MaterialButton
    private lateinit var messageList: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var progress: ProgressBar

    private val adapter = SmsAdapter()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    /** True after the user has been asked once, so a later denial means "don't ask again". */
    private var askedBefore = false

    private val requestSmsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            askedBefore = true
            if (granted) loadMessages() else showPermissionPanel()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionPanel = findViewById(R.id.permissionPanel)
        permissionMessage = findViewById(R.id.permissionMessage)
        grantButton = findViewById(R.id.grantButton)
        messageList = findViewById(R.id.messageList)
        emptyText = findViewById(R.id.emptyText)
        progress = findViewById(R.id.progress)

        messageList.layoutManager = LinearLayoutManager(this)
        messageList.adapter = adapter

        askedBefore = savedInstanceState?.getBoolean(KEY_ASKED) ?: false
        grantButton.setOnClickListener { onGrantClicked() }
    }

    override fun onResume() {
        super.onResume()
        // Re-check on every resume: the user may have granted or revoked access in Settings.
        if (hasSmsPermission()) loadMessages() else showPermissionPanel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_ASKED, askedBefore)
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }

    private fun hasSmsPermission() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) ==
            PackageManager.PERMISSION_GRANTED

    private fun isPermanentlyDenied() =
        askedBefore && !shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)

    private fun onGrantClicked() {
        if (isPermanentlyDenied()) {
            startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
            )
        } else {
            requestSmsPermission.launch(Manifest.permission.READ_SMS)
        }
    }

    private fun showPermissionPanel() {
        permissionPanel.visibility = View.VISIBLE
        messageList.visibility = View.GONE
        emptyText.visibility = View.GONE
        progress.visibility = View.GONE
        if (isPermanentlyDenied()) {
            permissionMessage.setText(R.string.permission_denied_forever)
            grantButton.setText(R.string.open_settings)
        } else {
            permissionMessage.setText(R.string.permission_explanation)
            grantButton.setText(R.string.allow_access)
        }
    }

    private fun loadMessages() {
        permissionPanel.visibility = View.GONE
        progress.visibility = View.VISIBLE
        executor.execute {
            val result = runCatching { SmsRepository(contentResolver).loadInbox() }
            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                progress.visibility = View.GONE
                val messages = result.getOrElse { emptyList() }
                adapter.submit(messages)
                messageList.visibility = if (messages.isEmpty()) View.GONE else View.VISIBLE
                emptyText.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE
                emptyText.setText(if (result.isFailure) R.string.load_failed else R.string.no_messages)
            }
        }
    }

    companion object {
        private const val KEY_ASKED = "asked_before"
    }
}
