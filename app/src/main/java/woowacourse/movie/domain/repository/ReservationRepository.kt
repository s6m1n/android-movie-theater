package woowacourse.movie.domain.repository

import woowacourse.movie.domain.model.Reservation
import woowacourse.movie.domain.model.ScreenView.Movie
import woowacourse.movie.domain.model.Seat
import java.time.LocalDateTime

interface ReservationRepository {
    fun saveReservation(
        movie: Movie,
        theaterId: Int,
        ticketCount: Int,
        seats: List<Seat>,
        dateTime: LocalDateTime,
    ): Result<Int>

    fun findByReservationId(id: Int): Result<Reservation>
}
