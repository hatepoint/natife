package com.hatepoint.natife.di

import com.hatepoint.natife.data.GiphyRepository
import com.hatepoint.natife.retrofit.RetrofitClient
import com.hatepoint.natife.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetrofitClient.retrofitClient }
    single { GiphyRepository(get()) }
    viewModel { MainViewModel(get()) }
}