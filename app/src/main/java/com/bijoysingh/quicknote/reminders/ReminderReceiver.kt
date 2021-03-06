package com.bijoysingh.quicknote.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bijoysingh.quicknote.database.Note
import com.bijoysingh.quicknote.database.utils.NotesDB
import com.bijoysingh.quicknote.utils.NotificationConfig
import com.bijoysingh.quicknote.utils.NotificationHandler
import com.bijoysingh.quicknote.utils.REMINDER_NOTIFICATION_CHANNEL_ID

class ReminderReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    if (context === null || intent === null) {
      return
    }

    if (!intent.hasExtra(ALARM_ID) || !intent.hasExtra(ALARM_UUID)) {
      return
    }

    val noteID = intent.getIntExtra(ALARM_ID, 0)
    val noteUUID = intent.getStringExtra(ALARM_UUID)
    val note = NotesDB.db.getByUUID(noteUUID)
    if (note === null) {
      val scheduler = ReminderScheduler(context)
      scheduler.removeWithoutNote(noteID, noteUUID)
      return
    }

    val handler = NotificationHandler(context)
    handler.openNotification(NotificationConfig(note, REMINDER_NOTIFICATION_CHANNEL_ID))
  }
}