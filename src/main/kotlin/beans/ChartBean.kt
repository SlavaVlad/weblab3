package beans

import jakarta.faces.context.FacesContext
import org.primefaces.model.DefaultStreamedContent
import org.primefaces.model.StreamedContent
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class ChartBean {
    var width: Int = 400
    var height: Int = 400

    private val resultsBean: ResultsBean
        get() = FacesContext.getCurrentInstance()
            .externalContext.sessionMap["resultsBean"] as ResultsBean

    fun getImageStream(): StreamedContent {
        val r = FacesContext.getCurrentInstance()
            .externalContext.requestParameterMap["r"]?.toDoubleOrNull() ?: 1.0

        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2 = img.createGraphics()
        drawChart(g2, r)
        g2.dispose()

        val baos = ByteArrayOutputStream()
        ImageIO.write(img, "png", baos)
        val bais = baos.toByteArray().inputStream()

        return DefaultStreamedContent.builder()
            .contentType("image/png")
            .stream { bais }
            .build()
    }

    private fun drawChart(g: Graphics2D, r: Double) {
        // Anti‐aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val ox = width / 2
        val oy = height / 2

        // фон
        g.color = Color.WHITE
        g.fillRect(0, 0, width, height)

        // область
        g.color = Color.BLUE
        // 1) треугольник (2-й квадрант)
        val tri = java.awt.Polygon()
        tri.addPoint(ox, oy - scaleY(r, r))            // (0,R)
        tri.addPoint(ox + scaleX(-r/2, r), oy)         // (-R/2,0)
        tri.addPoint(ox, oy)                           // (0,0)
        g.fillPolygon(tri)
        // 2) прямоугольник (4-й квадрант)
        g.fillRect(
            ox,
            oy,
            scaleX(r, r),
            scaleY(r, r)
        )
        // 3) «четверть круга» (3-й квадрант)
        g.fillArc(0, 0, width, height, 180, -90)

        // оси
        g.color = Color.BLACK
        g.stroke = BasicStroke(2f)
        g.drawLine(0, oy, width, oy)
        g.drawLine(ox, 0, ox, height)

        // точки
        for (res in resultsBean.hitResults.toList()) {
            val px = ox + (res.x / r * (width/2)).toInt()
            val py = oy - (res.y / r * (height/2)).toInt()
            g.color = if (res.hit) Color.GREEN else Color.RED
            g.fillOval(px-3, py-3, 6, 6)
        }
    }

    private fun scaleX(x: Double, r: Double) = ((x / r) * (width/2)).toInt()
    private fun scaleY(y: Double, r: Double) = ((y / r) * (height/2)).toInt()
}
