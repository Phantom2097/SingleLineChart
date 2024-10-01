package ru.phantom2097.singlelinechart

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import ru.phantom2097.singlelinechart.ui.theme.SingleLineChartTheme
import kotlin.math.roundToInt
import kotlin.random.Random
const val STEPS = 10
class MainActivity : ComponentActivity() {
    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val layoutDirection = LocalLayoutDirection.current
            val displayCutout = WindowInsets.displayCutout.asPaddingValues()
            val startPadding = displayCutout.calculateStartPadding(layoutDirection)
            val endPadding = displayCutout.calculateEndPadding(layoutDirection)

            val pointsList = getPointsList()
            val max = getMax(pointsList)
            val min = getMin(pointsList)
            // Отображаемые данные по оси x
            val xAxisData = AxisData.Builder()
                .axisStepSize(50.dp)
                .backgroundColor(Color.White)
                .steps(pointsList.size - 1)
                .labelData { i -> "$i day" }
                .labelAndAxisLinePadding(15.dp)
                .build()

            // Отображаемые данные по оси y
            val yAxisData = AxisData.Builder()
                .steps(STEPS)
                .backgroundColor(Color.White)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    val yScale = (max - min) / STEPS.toFloat()
//                    ((((i * yScale) + min) * 10).roundToInt().toFloat() / 10).toString()
                    String.format("%.1f", ((i * yScale) + min))
                }.build()
            SingleLineChartTheme {
                Box(
                    modifier = Modifier
                        .background(color = Color.White)
                        .navigationBarsPadding()
                        .statusBarsPadding()
                        .padding(
                            PaddingValues(
                                start = startPadding,
                                end = endPadding
                            )
                        )
                ) {
                    val lineChartData = LineChartData(
                        paddingRight = 0.dp,
                        linePlotData = LinePlotData(
                            lines = listOf(
                                Line(
                                    dataPoints = pointsList,
                                    LineStyle(color = Color.Green, width = 1.0f),
                                    IntersectionPoint(color = Color.Blue, radius = 5.dp),
                                    SelectionHighlightPoint(Color.Red),
                                    ShadowUnderLine(Color.LightGray),
                                    SelectionHighlightPopUp(backgroundColor = Color.White)
                                )
                            ),
                        ),
                        xAxisData = xAxisData,
                        yAxisData = yAxisData,
                        gridLines = GridLines(),
                        backgroundColor = Color.White
                    )
                    LineChart(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        lineChartData = lineChartData
                    )
                }
            }
        }
    }
}

fun getPointsList(): List<Point> {
    val list = ArrayList<Point>()
    for (i in 0..31) {
        list.add(
            Point(
                i.toFloat(),
                Random.nextInt(50, 90).toFloat()
            )
        )
    }
    return list
}

private fun getMax(list: List<Point>): Float {
    var max = 0f
    list.forEach { point ->
        if (max < point.y) max = point.y
    }
    return max
}

private fun getMin(list: List<Point>): Float {
    var min = 91f
    list.forEach { point ->
        if (min > point.y) min = point.y
    }
    return min
}