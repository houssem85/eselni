package com.programasoft.application.psychologist

import com.programasoft.application.account.Account
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class PsychologistService(
    private val psychologistRepository: PsychologistRepository
) {

    fun create(psychologist: Psychologist): Psychologist {
        return psychologistRepository.save(psychologist)
    }

    fun getByAccount(account: Account): Psychologist? {
        return psychologistRepository.findByAccount(account = account)
    }

    fun getById(id: Long): Psychologist =
        psychologistRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun getAll(): List<Psychologist> = psychologistRepository.findAll()

    fun getPsychologistByAvailabilityUnitId(availabilityUnitId: Long): Psychologist {
        return psychologistRepository.findPsychologistByAvailabilityUnitId(availabilityUnitId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND
        )
    }
}