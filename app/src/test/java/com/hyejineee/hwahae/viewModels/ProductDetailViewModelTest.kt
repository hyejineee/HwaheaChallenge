package com.hyejineee.hwahae.viewModels

import com.hyejineee.hwahae.model.ProductDetail
import com.hyejineee.hwahae.datasource.ProductDataSource
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito

internal class ProductDetailViewModelTest {
    private var productDataSource: ProductDataSource = Mockito.mock(ProductDataSource::class.java)
    private var scheduler: BaseSchedulers = Mockito.mock(BaseSchedulers::class.java)

    val productId = 1
    val product = ProductDetail(
        productId,
        "image_url",
        "화장품 이름",
        "화장품 설명"
    )

    @BeforeEach
    fun setUp() {
        given(productDataSource.getProductDetail(productId))
            .willReturn(Observable.just(product))
        given(scheduler.io()).willReturn(Schedulers.single())
        given(scheduler.ui()).willReturn(Schedulers.single())
    }

    @Test
    fun getProductDetail() {
        val viewModel = ProductDetailViewModel(productDataSource, scheduler)

        val testObserver: TestObserver<ProductDetail> = TestObserver()

        viewModel.productDeailSubject.subscribe(testObserver)

        viewModel.getProductDetail(productId)

        testObserver.awaitCount(1)
        assertThat(testObserver.values().first()).isEqualTo(product)
    }
}