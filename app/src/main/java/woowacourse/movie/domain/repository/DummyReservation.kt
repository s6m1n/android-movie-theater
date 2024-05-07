package woowacourse.movie.domain.repository

import woowacourse.movie.domain.model.Reservation
import woowacourse.movie.domain.model.ScreenView.Movie
import woowacourse.movie.domain.model.Seat
import java.time.LocalDateTime

object DummyReservation : ReservationRepository {
    private val reservations = mutableListOf<Reservation>()

    override fun saveReservation(
        movie: Movie,
        theaterId: Int,
        ticketCount: Int,
        seats: List<Seat>,
        dateTime: LocalDateTime,
    ): Result<Int> {
        return runCatching {
            val id = reservations.size + 1
            val reservation = Reservation(id, theaterId, movie, ticketCount, seats, dateTime)
            reservations.add(reservation)
            id
        }
    }

    override fun findByReservationId(id: Int): Result<Reservation> {
        return runCatching {
            val reservation = reservations.find { reservation -> reservation.id == id }
            reservation ?: throw NoSuchElementException("예약 정보를 찾을 수 없습니다.")
        }
    }
}
