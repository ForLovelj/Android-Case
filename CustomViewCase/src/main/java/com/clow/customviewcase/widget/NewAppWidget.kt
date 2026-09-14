package com.clow.customviewcase.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.clow.customviewcase.MainActivity
import com.clow.customviewcase.R

/**
 * 最小桌面小组件：显示一段文字，点击按钮后打开应用首页。
 */
class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val openAppIntent = Intent(context, MainActivity::class.java)
    val openAppPendingIntent = PendingIntent.getActivity(
        context,
        0,
        openAppIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val views = RemoteViews(context.packageName, R.layout.new_app_widget).apply {
        setTextViewText(R.id.appwidget_text, context.getString(R.string.appwidget_text))
        setOnClickPendingIntent(R.id.open_app_button, openAppPendingIntent)
    }

    appWidgetManager.updateAppWidget(appWidgetId, views)
}
