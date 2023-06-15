package com.programasoft.application.reservation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.client.id = :clientId AND EXISTS (SELECT t FROM AccountBalanceTransaction t WHERE t.reservation = r)")
    fun findPaidReservationsByClient(clientId: Long): List<Reservation>

    @Query("SELECT r FROM Reservation r WHERE r.client.id = :clientId AND NOT EXISTS (SELECT t FROM AccountBalanceTransaction t WHERE t.reservation = r) AND NOT EXISTS (SELECT t FROM AccountBalanceTransaction t WHERE t.reservation = r) AND NOT EXISTS (SELECT a FROM AvailabilityUnit a WHERE a.reservation = r AND a.start < NOW())")
    fun findNotPaidReservationsByClient(clientId: Long): List<Reservation>

    @Query("SELECT r FROM Reservation r WHERE r.client.id = :clientId AND EXISTS (SELECT t FROM AccountBalanceTransaction t WHERE t.reservation = r) AND EXISTS (SELECT a FROM AvailabilityUnit a WHERE a.reservation = r AND :now < a.end AND a.start >= :startOfDay)")
    fun findPaidReservationsOfCurrentDayByClient(
        clientId: Long,
        @Param("now") now : LocalDateTime,
        @Param("startOfDay") startOfDay: LocalDateTime,
    ): List<Reservation>

}