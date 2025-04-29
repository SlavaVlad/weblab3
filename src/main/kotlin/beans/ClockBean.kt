package beans

import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ClockBean : Serializable {
    var currentTime: String = ""
        private set

    init {
        updateTime()
    }

    fun updateTime() {
        currentTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}
