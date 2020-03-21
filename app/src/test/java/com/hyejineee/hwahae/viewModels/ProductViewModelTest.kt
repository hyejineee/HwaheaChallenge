package com.hyejineee.hwahae.viewModels

import com.hyejineee.hwahae.model.Product
import com.hyejineee.hwahae.datasource.ProductDataSource
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
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

    val products = listOf(
        Product(
            536,
            "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-birdview/thumbnail/ef792a79-435c-44eb-b9dc-285750ae1517.jpg",
            "19650",
            "플라멜엠디 밀크러스트필 마일드 워시오프 앰플 5ml x 2개",
            0,
            0,
            0
        )
    )

    @BeforeEach
    fun setUp() {
        given(productDataSource.getProductList(any(), any(), any()))
            .willReturn(Observable.just(products))
        given(scheduler.io()).willReturn(Schedulers.single())
        given(scheduler.ui()).willReturn(Schedulers.single())
    }

    @Test
    fun increasePageNumber() {
        val viewModel = ProductViewModel(productDataSource, scheduler)
        viewModel.products = products

        val originPageNum = viewModel.pageNum
        val originProductsSize = viewModel.products.size

        val testObserver: TestObserver<List<Product>> = TestObserver()

        viewModel.productsSubject.subscribe(testObserver)

        viewModel.increasePageNumber()

        testObserver.awaitCount(1)

        assertThat(viewModel.pageNum - originPageNum).isEqualTo(1)
        assertThat(testObserver.values().first().size - originProductsSize).isEqualTo(1)
    }

    @Test
    fun selectSkinType() {
        val viewModel = ProductViewModel(productDataSource, scheduler)
        viewModel.pageNum = 5

        val testObserver: TestObserver<List<Product>> = TestObserver()

        viewModel.productsSubject.subscribe(testObserver)

        viewModel.selectSkinType("oil")

        testObserver.awaitCount(1)

        assertThat(viewModel.pageNum).isEqualTo(1)
        assertThat(testObserver.values().first()).isEqualTo(products)
    }

    @Test
    fun search() {
        val viewModel = ProductViewModel(productDataSource, scheduler)
        viewModel.pageNum = 5

        val testObserver: TestObserver<List<Product>> = TestObserver()

        viewModel.productsSubject.subscribe(testObserver)

        viewModel.search("cosmetic")

        testObserver.awaitCount(1)

        assertThat(viewModel.pageNum).isEqualTo(1)
        assertThat(testObserver.values().first()).isEqualTo(products)
    }
}
