package woowacourse.movie.presentation.ui.seatselection

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import woowacourse.movie.R
import woowacourse.movie.databinding.ActivitySeatSelectionBinding
import woowacourse.movie.domain.model.SeatModel
import woowacourse.movie.domain.repository.DummyReservation
import woowacourse.movie.domain.repository.DummyScreens
import woowacourse.movie.presentation.base.BaseActivity
import woowacourse.movie.presentation.model.ReservationInfo
import woowacourse.movie.presentation.model.UserSeat
import woowacourse.movie.presentation.ui.reservation.ReservationActivity
import woowacourse.movie.presentation.ui.seatselection.SeatSelectionContract.View
import java.io.Serializable

class SeatSelectionActivity : BaseActivity<ActivitySeatSelectionBinding>(), View {
    override val layoutResourceId: Int
        get() = R.layout.activity_seat_selection
    val presenter: SeatSelectionPresenter by lazy {
        SeatSelectionPresenter(this, DummyScreens(), DummyReservation)
    }

    override fun initStartView() {
        binding.handler = this
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val reservationInfo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(
                    PUT_EXTRA_KEY_RESERVATION_INFO,
                    ReservationInfo::class.java,
                )
            } else {
                intent.getSerializableExtra(PUT_EXTRA_KEY_RESERVATION_INFO) as ReservationInfo
            }
        val movieId = intent.getIntExtra(PUT_EXTRA_KEY_MOVIE_ID, -1)

        reservationInfo?.let { reservationInfoItem ->
            presenter.updateUiModel(reservationInfoItem, movieId)
        }
    }

    override fun showSeatModel(seatModel: SeatSelectionUiModel) {
        binding.seatSelectionModel = seatModel
    }

    override fun onSeatClicked(seatModel: SeatModel) {
        presenter.clickSeat(seatModel)
    }

    override fun onReservationButtonClicked() {
        showReservationDialog()
    }

    override fun showReservationDialog() {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setCancelable(false)
            setTitle(getString(R.string.dialog_reservation_title))
            setMessage(getString(R.string.dialog_reservation_message))
            setPositiveButton(getString(R.string.reservation_done)) { _, _ ->
                presenter.reserve()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    override fun navigateToReservation(reservationId: Int) {
        ReservationActivity.startActivity(this, reservationId)
        back()
    }

    override fun back() = finish()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        back()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(PUT_STATE_KEY_USER_SEAT, presenter.seatSelectionModel.userSeat)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val savedUserSeat = savedInstanceState.getSerializable(PUT_STATE_KEY_USER_SEAT) as UserSeat?
        savedUserSeat?.let { userSeat ->
            userSeat.seatModels.forEach { seat ->
                presenter.clickSeat(seat)
            }
        }
    }

    companion object {
        private const val PUT_EXTRA_KEY_RESERVATION_INFO = "reservationInfo"
        private const val PUT_STATE_KEY_USER_SEAT = "userSeat"
        private const val PUT_EXTRA_KEY_MOVIE_ID = "movieId"

        fun startActivity(
            context: Context,
            reservationInfo: ReservationInfo,
            movieId: Int,
        ) {
            val intent = Intent(context, SeatSelectionActivity::class.java)
            intent.putExtra(PUT_EXTRA_KEY_RESERVATION_INFO, reservationInfo as Serializable)
            intent.putExtra(PUT_EXTRA_KEY_MOVIE_ID, movieId)
            context.startActivity(intent)
        }
    }
}
