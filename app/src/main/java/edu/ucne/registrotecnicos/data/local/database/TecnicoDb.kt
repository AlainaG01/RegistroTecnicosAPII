package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import edu.ucne.registrotecnicos.data.local.dao.MensajeDao
import edu.ucne.registrotecnicos.data.local.dao.PrioridadDao
import edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

@Database(
    entities = [
        TecnicoEntity::class,
        PrioridadEntity::class,
        TicketEntity::class,
        MensajeEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun TecnicoDao(): TecnicoDao
    abstract fun PrioridadDao(): PrioridadDao
    abstract fun TicketDao(): TicketDao
    abstract fun MensajeDao(): MensajeDao
}
