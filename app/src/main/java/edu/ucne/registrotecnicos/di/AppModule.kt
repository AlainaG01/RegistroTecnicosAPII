package edu.ucne.registrotecnicos.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideTecnicoDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTecnicoDao(tecnicoDb: TecnicoDb) = tecnicoDb.TecnicoDao()
    @Provides
    fun providePrioridadDao(tecnicoDb: TecnicoDb) = tecnicoDb.PrioridadDao()
    @Provides
    fun provideTicketDao(tecnicoDb: TecnicoDb) = tecnicoDb.TicketDao()
    @Provides
    fun provideMensajeDao(tecnicoDb: TecnicoDb) = tecnicoDb.MensajeDao()
}