package com.programasoft.application.reservation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.client.id = :clientId AND EXISTS (SELECT t FROM AccountBalanceTransaction t WHERE t.reservation = r)")
    fun findPaidReservationsByClient(clientId: Long): List<Reservation>

    @Query("SELECT r FROM Reservation r WHERE r.client.id = :clientId AND NOT EXISTS (SELECT t FROM AccountBalanceTransaction t WHERE t.reservation = r)")
    fun findNotPaidReservationsByClient(clientId: Long): List<Reservation>

}