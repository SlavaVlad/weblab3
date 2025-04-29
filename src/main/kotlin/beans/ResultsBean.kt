package beans

import dao.ResultDAO
import jakarta.annotation.PostConstruct
import model.HitResult

class ResultsBean {
    val hitResults: MutableList<HitResult> = mutableListOf()

    @PostConstruct
    fun init() {
        hitResults.addAll(ResultDAO.findAll())
    }

    fun addResult(r: HitResult) {
        hitResults.add(0, r)  // вставляем в начало списка
    }
}
