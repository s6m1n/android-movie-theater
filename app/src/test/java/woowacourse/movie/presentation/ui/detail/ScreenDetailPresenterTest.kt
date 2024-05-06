package woowacourse.movie.presentation.ui.detail

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import woowacourse.movie.domain.repository.ScreenRepository
import woowacourse.movie.presentation.ui.utils.DummyData.findByScreenId

@ExtendWith(MockKExtension::class)
class ScreenDetailPresenterTest {
    @MockK
    private lateinit var view: ScreenDetailContract.View

    private lateinit var presenter: ScreenDetailContract.Presenter

    @MockK
    private lateinit var repository: ScreenRepository

    @BeforeEach
    fun setUp() {
        presenter = ScreenDetailPresenter(view, repository)
    }

    @Test
    fun `영화와 상영관 id로 상영 세부 정보를 찾아 뷰에 넘겨준다`() {
        // given
        every { repository.findByScreenId(any(), any()) } returns
            Result.success(
                findByScreenId(
                    0,
                    0,
                ),
            )
        //        every { view.showScreenAdapter(any()) } just runs
        //        every { view.showTicketCount(any()) } just runs

        // when
        presenter.loadScreenDetail(0, 0)

        // then
        //        verify { view.showScreenAdapter(dummyScreen) }
        //        verify { view.showTicketCount(any()) }
    }

    @Test
    fun `DetailPresenter가 유효하지 않은 상영 id값으로 loadScreen()을 했을 때, view에게 back과 throwable를 전달한다`() {
        // given
        every { repository.findByScreenId(any(), any()) } returns
            Result.failure(
                NoSuchElementException(),
            )
        every { view.showToastMessage(e = any()) } just runs
        every { view.back() } just runs

        // when
        presenter.loadScreenDetail(1, 1)

        // then
        verify { view.showToastMessage(e = any()) }
        verify { view.back() }
    }

//    @Test
//    fun `DetailPresenter가 ticket 값이 1일 때 plusTicket()을 하면, view에게 티켓 개수를 전달한다`() {
//        // given
//        every { view.showTicketCount(any()) } just runs
//
//        // when
//        presenter.plusTicket(detailModel.ticket.count)
//
//        // then
//        verify { view.showTicketCount(2) }
//    }
//
//    @Test
//    fun `DetailPresenter가 ticket 값이 티켓의 최대 개수일 때 plusTicket()을 하면, view에게 snackbar message(TicketMaxCountMessage)를 전달한다`() {
//        // given
//        every { view.showTicketCount(any()) } just runs
//        every { view.showSnackBar(MessageType.TicketMaxCountMessage(Ticket.MAX_TICKET_COUNT)) } just runs
//
//        // when
//        repeat(Ticket.MAX_TICKET_COUNT - 1) {
//            presenter.plusTicket(detailModel.ticket.count)
//        }
//        presenter.plusTicket(detailModel.ticket.count)
//
//        // then
//        verify { view.showSnackBar(MessageType.TicketMaxCountMessage(Ticket.MAX_TICKET_COUNT)) }
//    }
//
//    @Test
//    fun `DetailPresenter가 ticket 값이 1일 때 minusTicket()을 하면, view에게 snackbar message(TicketMinCountMessage)를 전달한다`() {
//        // given
//        every { view.showSnackBar(MessageType.TicketMinCountMessage(Ticket.MIN_TICKET_COUNT)) } just runs
//
//        // when
//        presenter.minusTicket(detailModel.ticket)
//
//        // then
//        verify { view.showSnackBar(MessageType.TicketMinCountMessage(Ticket.MIN_TICKET_COUNT)) }
//    }
//
//    @Test
//    fun `DetailPresenter가 ticket 값이 티켓의 최대 개수일 때 minusTicket()을 하면, view에게 티켓 개수를 전달한다`() {
//        // given && when
//        repeat(Ticket.MAX_TICKET_COUNT - 1) {
//            every { view.showTicketCount(it + 2) } just runs
//            presenter.plusTicket(detailModel.ticket.count)
//        }
//        presenter.minusTicket(detailModel.ticket)
//
//        // then
//        verify(exactly = 2) { view.showTicketCount(Ticket.MAX_TICKET_COUNT - 1) }
//    }
//
//    @Test
//    fun `DetailPresenter가 selectSeat()를 누르면, view에게 좌석 선택 페이지로 이동하라고 전달한다`() {
//        // given
//        every { repository.findByScreenId(any(), any()) } returns Result.success(dummyScreen)
//        every { view.showScreenAdapter(any()) } just runs
//        every { view.showTicketCount(any()) } just runs
//        every { view.onSeatSelectionButtonClicked(any()) } just runs
//
//        // when
//        presenter.loadScreenDetail(1, 1)
//        presenter.selectSeat(detailModel.selectedDate)
//
//        // then
//        verify { view.onSeatSelectionButtonClicked(any()) }
//    }
//
//    @Test
//    fun `DetailPresenter가 티켓 초기값을 설정하고 좌석 선택을 요청하면, view에게 티켓 개수를 설정하고 좌석 선택 페이지로 이동하라고 전달한다`() {
//        // given
//        every { repository.findByScreenId(any(), any()) } returns Result.success(dummyScreen)
//        every { view.showScreenAdapter(any()) } just runs
//        every { view.showTicketCount(any()) } just runs
//
//        // when
//        presenter.loadScreenDetail(1, 1)
//
//        // then
//        verify { view.showTicketCount(5) }
//    }
}
