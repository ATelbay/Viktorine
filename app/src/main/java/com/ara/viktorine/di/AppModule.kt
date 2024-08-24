package com.ara.viktorine.di

import com.ara.viktorine.data.repository.QuestionsRepositoryImpl
import com.ara.viktorine.domain.repository.QuestionsRepository
import com.ara.viktorine.ui.GameViewModel
import com.ara.viktorine.data.remote.OpenTdbApi
import com.ara.viktorine.util.Constants.BASE_URL
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(OpenTdbApi::class.java)
    }

    singleOf(::QuestionsRepositoryImpl) { bind<QuestionsRepository>() }

    viewModelOf(::GameViewModel)
}