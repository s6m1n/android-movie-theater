package woowacourse.movie.presentation.ui.seatselection

import woowacourse.movie.domain.model.Seat
import woowacourse.movie.domain.repository.ReservationRepository
import woowacourse.movie.domain.repository.ScreenRepository
import woowacourse.movie.presentation.model.MessageType
import woowacourse.movie.presentation.model.MessageType.ReservationSuccessMessage
import woowacourse.movie.presentation.model.ReservationInfo

class SeatSelectionPresenter(
    private val view: SeatSelectionContract.View,
    private val repository: ScreenRepository,
    private val reservationRepository: ReservationRepository,
) : SeatSelectionContract.Presenter {
    private var _uiModel: SeatSelectionUiModel = SeatSelectionUiModel()
    val uiModel: SeatSelectionUiModel
        get() = _uiModel

    override fun updateUiModel(reservationInfo: ReservationInfo) {
        _uiModel =
            uiModel.copy(
                id = reservationInfo.theaterId,
                dateTime = reservationInfo.dateTime,
                ticketCount = reservationInfo.ticketCount,
            )
    }

    override fun loadScreen(id: Int) {
        repository.findByScreenId(theaterId = id, movieId = id).onSuccess { screen ->
            _uiModel = uiModel.copy(screen = screen)
        }.onFailure { e ->
            when (e) {
                is NoSuchElementException -> {
                    view.showToastMessage(e)
                    view.back()
                }

                else -> {
                    view.showToastMessage(e)
                    view.back()
                }
            }
        }
    }

    override fun loadSeatBoard(id: Int) {
        repository.loadSeatBoard(id).onSuccess { seatBoard ->
            _uiModel = _uiModel.copy(seatBoard = seatBoard)
            view.showSeatBoard(seatBoard.seats)
        }.onFailure { e ->
            when (e) {
                is NoSuchElementException -> {
                    view.showToastMessage(e)
                    view.back()
                }

                else -> {
                    view.showToastMessage(e)
                    view.back()
                }
            }
        }
    }

    override fun clickSeat(seat: Seat) {
        val column = seat.column.toColumnIndex()
        val row = seat.row

        if (seat in uiModel.userSeat.seats) {
            _uiModel = uiModel.copy(userSeat = uiModel.userSeat.removeAt(seat))
            view.unselectSeat(column, row)
            calculateSeat()
            return
        }
        if (uiModel.userSeat.seats.size == uiModel.ticketCount) {
            view.showSnackBar(MessageType.AllSeatsSelectedMessage(uiModel.ticketCount))
        } else {
            view.selectSeat(column, row)
            _uiModel = _uiModel.copy(userSeat = _uiModel.userSeat + seat)
            calculateSeat()
        }
    }

    override fun calculateSeat() {
        var newPrice = 0
        uiModel.userSeat.seats.forEach { seat ->
            newPrice += seat.seatRank.price
        }

        _uiModel = uiModel.copy(totalPrice = newPrice)
        view.showTotalPrice(uiModel.totalPrice)
    }

    override fun showConfirmDialog() {
        view.showReservationDialog()
    }

    override fun reserve() {
        uiModel.screen?.let { screen ->
            uiModel.dateTime?.let { dateTime ->
                reservationRepository.saveReservation(
                    screen.movie,
                    uiModel.id,
                    uiModel.ticketCount,
                    uiModel.userSeat.seats,
                    dateTime,
                ).onSuccess { id ->
                    view.showToastMessage(ReservationSuccessMessage)
                    view.navigateToReservation(id)
                }.onFailure { e ->
                    view.showSnackBar(e)
                    view.back()
                }
            }
        }
    }

    private fun String.toColumnIndex(): Int = this[0].code - 'A'.code
}
