package com.hyejineee.hwahae.viewModels

import com.hyejineee.hwahae.ActionType
import com.hyejineee.hwahae.BaseSchedulers
import com.hyejineee.hwahae.datasource.ProductDataSource
import com.hyejineee.hwahae.model.Product
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

internal class ProductViewModelTest {
    private var productDataSource: ProductDataSource = mock(ProductDataSource::class.java)
    private var scheduler: BaseSchedulers = mock(BaseSchedulers::class.java)

    private lateinit var viewModel: ProductViewModel
    private val mockProducts = listOf(
        Product(
            536,
            "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-birdview/thumbnail/ef792a79-435c-44eb-b9dc-285750ae1517.jpg",
            "플라멜엠디 밀크러스트필 마일드 워시오프 앰플 5ml x 2개",
            "19650",
            0,
            0,
            0
        ),
        Product(
            828,
            "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-birdview/thumbnail/27feebd6-d863-48e6-85b8-336778f3c455.jpg",
            "엔오에이치제이 코리안 에스테틱 마스크 포어버블",
            "3560",
            0,
            0,
            100
        )
    )

    @BeforeEach
    fun setUp() {
        given(scheduler.io()).willReturn(Schedulers.single())
        given(scheduler.ui()).willReturn(Schedulers.single())

        given(productDataSource.getProductList(any(), any(), any()))
            .willReturn(Observable.just(mockProducts))

        given(productDataSource.getProductList("sensitive", 1, null))
            .willReturn(Observable.just(mockProducts.filter { it.sensitive_score ==100 }))

        given(productDataSource.getProductList(null, 1, "플라멜엠디"))
            .willReturn(Observable.just(mockProducts.filter { it.title == "플라멜엠디 밀크러스트필 마일드 워시오프 앰플 5ml x 2개" }))

        viewModel = ProductViewModel(productDataSource,scheduler)
    }

    @Test
    fun `다음 페이지 제품 리스트 가져오기 테스트`(){

        val originPageNum = viewModel.pageNum
        val originProductsSize = viewModel.products.size

        viewModel.actionDispatch(ActionType.NEXT_PAGE, 0)

        val products = viewModel.onProductChange.blockingFirst()

        assertThat(viewModel.pageNum - originPageNum).isEqualTo(1)
        assertThat(products.size - originProductsSize).isEqualTo(2)
    }

    @Test
    fun `해당 스킨 타입의 제품만 가져오기 테스트`(){

        viewModel.actionDispatch(ActionType.FILTERING, "sensitive")

        val products = viewModel.onProductChange.blockingFirst()

        assertThat(viewModel.pageNum).isEqualTo(1)
        assertThat(products.size).isEqualTo(1)
        assertThat(products).isEqualTo(mockProducts.filter { it.sensitive_score == 100 })
    }

    @Test
    fun `검색 키워드가 포함된 제품만 가져오기 테스트 `(){
        viewModel.actionDispatch(ActionType.SEARCH, "플라멜엠디")

        val products = viewModel.onProductChange.blockingFirst()

        assertThat(viewModel.pageNum).isEqualTo(1)
        assertThat(products.size).isEqualTo(1)
        assertThat(products).isEqualTo(mockProducts.filter { it.title == "플라멜엠디 밀크러스트필 마일드 워시오프 앰플 5ml x 2개" })
    }
}
