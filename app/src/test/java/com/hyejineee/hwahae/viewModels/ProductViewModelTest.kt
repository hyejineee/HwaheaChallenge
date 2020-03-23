package com.hyejineee.hwahae.viewModels

import com.hyejineee.hwahae.ActionType
import com.hyejineee.hwahae.Scheduler
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
    private var scheduler: Scheduler = mock(Scheduler::class.java)
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
        viewModel = ProductViewModel(productDataSource,scheduler)
        given(productDataSource.getProductList(null, 1, null))
            .willReturn(Observable.just(mockProducts))

        given(productDataSource.getProductList("sensitive", 1, null))
            .willReturn(Observable.just(mockProducts.filter { it.sensitive_score ==100 }))

        given(scheduler.io()).willReturn(Schedulers.single())
        given(scheduler.ui()).willReturn(Schedulers.single())
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
    fun `스킨 타입 필터 지정 테스트`(){

        viewModel.actionDispatch(ActionType.FILTERING, "sensitive")

        val products = viewModel.onProductChange.blockingFirst()

        assertThat(viewModel.pageNum).isEqualTo(1)
        assertThat(products.size).isEqualTo(1)
        assertThat(products).isEqualTo(mockProducts.filter { it.sensitive_score == 100 })
    }



//    @Test
//    fun increasePageNumber() {
//        val viewModel = ProductViewModel(productDataSource, scheduler)
//
//        val originPageNum = viewModel.pageNum
//        val originProductsSize = viewModel.products.size
//
//        viewModel.actionDispatch(ActionType.NEXT_PAGE, 0)
//
//        val products = viewModel.onProductChange.blockingFirst()
//
//        assertThat(viewModel.pageNum - originPageNum).isEqualTo(1)
//        assertThat(products.size - originProductsSize).isEqualTo(1)
//    }

//    @Test
//    fun selectSkinType() {
//        val viewModel = ProductViewModel(productDataSource, scheduler)
//        viewModel.pageNum = 5
//
//        val testObserver: TestObserver<List<Product>> = TestObserver()
//
//        viewModel.productsSubject.subscribe(testObserver)
//
//        viewModel.selectSkinType("oil")
//
//        testObserver.awaitCount(1)
//
//        assertThat(viewModel.pageNum).isEqualTo(1)
//        assertThat(testObserver.values().first()).isEqualTo(products)
//    }
//
//    @Test
//    fun search() {
//        val viewModel = ProductViewModel(productDataSource, scheduler)
//        viewModel.pageNum = 5
//
//        val testObserver: TestObserver<List<Product>> = TestObserver()
//
//        viewModel.productsSubject.subscribe(testObserver)
//
//        viewModel.search("cosmetic")
//
//        testObserver.awaitCount(1)
//
//        assertThat(viewModel.pageNum).isEqualTo(1)
//        assertThat(testObserver.values().first()).isEqualTo(products)
//    }
}
