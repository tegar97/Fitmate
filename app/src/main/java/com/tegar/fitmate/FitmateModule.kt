package com.tegar.fitmate

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.tegar.fitmate.data.local.dao.SchenduleExerciseDao
import com.tegar.fitmate.data.local.database.FitmateDatabase
import com.tegar.fitmate.data.model.ApiConstant
import com.tegar.fitmate.data.remote.retrofit.ExerciseApiService
import com.tegar.fitmate.data.remote.retrofit.MainApi
import com.tegar.fitmate.data.remote.retrofit.MlApi
import com.tegar.fitmate.data.remote.retrofit.MlApiService
import com.tegar.fitmate.repository.SchenduleExerciseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FitmateModule {

    @Singleton
    @Provides
    fun getRepository(dao: SchenduleExerciseDao): SchenduleExerciseRepository {
        return SchenduleExerciseRepository(dao)
    }

    @Singleton
    @Provides
    fun getDao(database: FitmateDatabase): SchenduleExerciseDao {
        return database.schenduleDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): FitmateDatabase {
        return Room.databaseBuilder(
            context.applicationContext, FitmateDatabase::class.java, "fitmate"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    @MainApi
    fun mainApi(

    ): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
        val client = clientBuilder.build()

       return  Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiConstant.EXERCISE_BASE_URL).build()
    }


    @Provides
    @Singleton
    @MlApi
    fun mlApi(

    )  : Retrofit   {

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
        val client = clientBuilder.build()


        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiConstant.ML_BASE_URL).build()
    }


    // bUILD API SERVICE
    @Provides
    @Singleton
    fun mainApiService(@MainApi retrofit : Retrofit) : ExerciseApiService = retrofit.create(ExerciseApiService::class.java)

    @Provides
    @Singleton
    fun mlApiService(@MlApi retrofit : Retrofit) : MlApiService = retrofit.create(MlApiService::class.java)

}