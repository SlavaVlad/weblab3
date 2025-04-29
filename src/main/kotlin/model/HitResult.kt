package model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
@Table(name = "RESULTS")
class HitResult() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    var id: Long? = null

    @Column(name = "X_VAL")
    var x: Double = 0.0

    @Column(name = "Y_VAL")
    var y: Double = 0.0

    @Column(name = "R_VAL")
    var r: Double = 0.0

    @Column(name = "HIT")
    var hit: Boolean = false

    @Column(name = "TS")
    var timestamp: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    constructor(x: Double, y: Double, r: Double, hit: Boolean) : this() {
        this.x = x
        this.y = y
        this.r = r
        this.hit = hit
    }
}
