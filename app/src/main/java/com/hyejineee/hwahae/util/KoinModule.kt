package com.hyejineee.hwahae.util

import com.hyejineee.hwahae.network.ProductRepo
import com.hyejineee.hwahae.network.ProductRepoImpl
import com.hyejineee.hwahae.network.APIService
import com.hyejineee.hwahae.mViewModel.ProductDetailViewModel
import com.hyejineee.hwahae.mViewModel.ProductViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val androidScheduler = module {
    single<BaseSchedulers> { mScheduler() }
}

fun getNetworkModule(baseUrl: String) = module {
    single {
        OkHttpClient.Builder()
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .build()
    }

    single {
        GsonConverterFactory.create() as Converter.Factory
    }

    single {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(get())
            .baseUrl(baseUrl)
            .build()
            .create(APIService::class.java)
    }
    single<ProductRepo> { ProductRepoImpl(get()) }
}

val productViewModelModule = module {
    viewModel { ProductViewModel(get(), get()) }
}

val productDetailViewModelModule = module {
    viewModel { ProductDetailViewModel(get(), get()) }
}


val mModules = listOf(
    androidScheduler,
    getNetworkModule("https://6uqljnm1pb.execute-api.ap-northeast-2.amazonaws.com"),
    productViewModelModule,
    productDetailViewModelModule
)
