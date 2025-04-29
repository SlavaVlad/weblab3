package dao

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import model.HitResult

object ResultDAO {
    private val emf: EntityManagerFactory =
        Persistence.createEntityManagerFactory("myPersistenceUnit")

    fun persist(hitResult: HitResult) {
        val em: EntityManager = emf.createEntityManager()
        try {
            em.transaction.begin()
            em.persist(hitResult)
            em.transaction.commit()
        } catch (ex: Exception) {
            if (em.transaction.isActive) em.transaction.rollback()
            throw ex
        } finally {
            em.close()
        }
    }

    fun findAll(): List<HitResult> {
        val em: EntityManager = emf.createEntityManager()
        return try {
            em.createQuery("SELECT r FROM HitResult r ORDER BY r.id DESC", HitResult::class.java)
                .resultList
        } finally {
            em.close()
        }
    }
}
