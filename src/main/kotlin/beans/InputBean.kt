package beans

import dao.ResultDAO
import jakarta.faces.application.FacesMessage
import jakarta.faces.context.FacesContext
import model.HitResult

class InputBean {
    var x: Int? = null
    var y: Double? = null
    var r: Double? = null

    fun check(): String? {
        val xVal = x ?: return addError("X не задан")
        val yVal = y ?: return addError("Y не задан")
        val rVal = r ?: return addError("R не задан")

        val hit = isHit(xVal.toDouble(), yVal, rVal)
        val res = HitResult(xVal.toDouble(), yVal, rVal, hit)
        ResultDAO.persist(res)

        // Добавляем в сессионный ResultsBean
        val sessionMap = FacesContext.getCurrentInstance()
            .externalContext.sessionMap
        val rb = sessionMap["resultsBean"] as? ResultsBean
        rb?.addResult(res)

        return null  // остаёмся на той же странице
    }

    fun handleClick() {
        val params = FacesContext.getCurrentInstance()
            .externalContext.requestParameterMap
        val cx = params["clickX"]?.toDoubleOrNull()
        val cy = params["clickY"]?.toDoubleOrNull()
        if (cx != null && cy != null && r != null) {
            // округляем X до целого
            x = cx.toInt()
            y = cy
            check()
        }
    }

    private fun isHit(x: Double, y: Double, r: Double): Boolean {
        // rectangle: 0<=x<=r, -r<=y<=0
        if (x >= 0 && y <= 0 && x <= r && y >= -r) return true
        // triangle: -r/2 <= x <= 0, 0<=y<=2x+R
        if (x <= 0 && y >= 0 && x >= -r/2 && y <= 2*x + r) return true
        // quarter-circle: x^2+y^2 <= R^2, x<=0, y<=0
        if (x <= 0 && y <= 0 && x*x + y*y <= r*r) return true
        return false
    }

    private fun addError(msg: String): String? {
        FacesContext.getCurrentInstance()
            .addMessage(null, FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null))
        return null
    }
}
