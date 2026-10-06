package com.parabellum.app.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.parabellum.app.MainActivity
import com.parabellum.app.ParabellumApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ParabellumWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as ParabellumApp
        val (totalPending, immediatePending) = withContext(Dispatchers.IO) {
            app.taskRepository.getWidgetData()
        }

        provideContent {
            WidgetContent(
                totalPending = totalPending,
                immediatePending = immediatePending
            )
        }
    }

    @Composable
    private fun WidgetContent(totalPending: Int, immediatePending: Int) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color(0xFF1B3B2B))
                .padding(12.dp)
                .clickable(actionStartActivity<MainActivity>())
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.Vertical.Top,
                horizontalAlignment = Alignment.Horizontal.Start
            ) {
                // Widget Header
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Vertical.CenterVertically
                ) {
                    Text(
                        text = "■ PARABELLUM",
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFFF4F1EA)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text(
                        text = "SYS-OK",
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFF80A892)),
                            fontSize = 10.sp
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.height(6.dp))

                Text(
                    text = "MACRODATA WORK QUEUE",
                    style = TextStyle(
                        color = androidx.glance.unit.ColorProvider(Color(0xFFB5C9BC)),
                        fontSize = 10.sp
                    )
                )

                Spacer(modifier = GlanceModifier.height(10.dp))

                // Counts
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Vertical.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "IMMEDIATE:",
                            style = TextStyle(
                                color = androidx.glance.unit.ColorProvider(Color(0xFFE2DDD0)),
                                fontSize = 11.sp
                            )
                        )
                        Text(
                            text = "$immediatePending TASK(S)",
                            style = TextStyle(
                                color = androidx.glance.unit.ColorProvider(Color(0xFFFFFFFF)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        )
                    }

                    Spacer(modifier = GlanceModifier.defaultWeight())

                    Column {
                        Text(
                            text = "TOTAL OPEN:",
                            style = TextStyle(
                                color = androidx.glance.unit.ColorProvider(Color(0xFFE2DDD0)),
                                fontSize = 11.sp
                            )
                        )
                        Text(
                            text = "$totalPending TASK(S)",
                            style = TextStyle(
                                color = androidx.glance.unit.ColorProvider(Color(0xFFFFFFFF)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        )
                    }
                }

                Spacer(modifier = GlanceModifier.defaultWeight())

                // Action Footer Button
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .background(Color(0xFF12281D))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶ OPEN PARABELLUM QUEUE",
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFFF4F1EA)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }

    companion object {
        suspend fun updateAll(context: Context) {
            val manager = GlanceAppWidgetManager(context)
            val glanceIds = manager.getGlanceIds(ParabellumWidget::class.java)
            glanceIds.forEach { glanceId ->
                ParabellumWidget().update(context, glanceId)
            }
        }
    }
}
