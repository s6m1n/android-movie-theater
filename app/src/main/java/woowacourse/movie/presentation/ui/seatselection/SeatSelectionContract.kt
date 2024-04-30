package woowacourse.movie.presentation.ui.seatselection

import woowacourse.movie.domain.model.ScreenView.Movie
import woowacourse.movie.domain.model.Seat
import woowacourse.movie.presentation.base.BasePresenter
import woowacourse.movie.presentation.base.BaseView
import woowacourse.movie.presentation.model.ReservationInfo

interface SeatSelectionContract {
    interface View : BaseView {
        fun showScreen(movie: Movie)

        fun showSeatBoard(seats: List<Seat>)

        fun initClickListener(seats: List<Seat>)

        fun selectSeat(
            column: Int,
            row: Int,
        )

        fun unselectSeat(
            column: Int,
            row: Int,
        )

        fun showTotalPrice(totalPrice: Int)

        fun buttonEnabled(isActivate: Boolean)

        fun navigateToReservation(id: Int)

        fun back()
    }

    interface Presenter : BasePresenter {
        fun updateUiModel(reservationInfo: ReservationInfo)

        fun loadScreen(id: Int)

        fun loadSeatBoard(id: Int)

        fun clickSeat(seat: Seat)

        fun calculateSeat()

        fun checkAllSeatsSelected()

        fun reserve()
    }
}
