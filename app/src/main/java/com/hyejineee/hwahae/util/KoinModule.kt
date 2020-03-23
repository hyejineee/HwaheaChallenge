package com.hyejineee.hwahae.util

import com.hyejineee.hwahae.BaseSchedulers
import com.hyejineee.hwahae.Scheduler
import com.hyejineee.hwahae.datasource.ProductDataSource
import com.hyejineee.hwahae.datasource.ProductRemoteDataSource
import com.hyejineee.hwahae.datasource.APIService
import com.hyejineee.hwahae.viewModels.ProductDetailViewModel
import com.hyejineee.hwahae.viewModels.ProductViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun getNetworkModule(baseUrl: String) = module {
    single {
        OkHttpClient.Builder()
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(ResponseInterceptor())
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
    single<ProductDataSource> { ProductRemoteDataSource(get()) }
}

val androidScheduler = module {
    single<BaseSchedulers> { Scheduler() }
}

val productViewModelModule = module {
    viewModel { ProductViewModel(get(),get()) }
}

val productDetailViewModelModule = module {
    viewModel { ProductDetailViewModel(get()) }
}

val mModules = listOf(
    getNetworkModule("https://6uqljnm1pb.execute-api.ap-northeast-2.amazonaws.com"),
    androidScheduler,
    productViewModelModule,
    productDetailViewModelModule
)
