package com.krushkov.virtualwallet.di

import android.content.Context
import com.krushkov.virtualwallet.data.api.RetrofitClient
import com.krushkov.virtualwallet.data.api.interfaces.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitClient(
        @ApplicationContext context: Context
    ): RetrofitClient {
        RetrofitClient.init(context)
        return RetrofitClient
    }

    @Provides
    @Singleton
    fun provideAuthApi(client: RetrofitClient): AuthApi {
        return client.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCardApi(client: RetrofitClient): CardApi {
        return client.create(CardApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyApi(client: RetrofitClient): CurrencyApi {
        return client.create(CurrencyApi::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentApi(client: RetrofitClient): PaymentApi {
        return client.create(PaymentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTopUpApi(client: RetrofitClient): TopUpApi {
        return client.create(TopUpApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionApi(client: RetrofitClient): TransactionApi {
        return client.create(TransactionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTransferApi(client: RetrofitClient): TransferApi {
        return client.create(TransferApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(client: RetrofitClient): UserApi {
        return client.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWalletApi(client: RetrofitClient): WalletApi {
        return client.create(WalletApi::class.java)
    }
}