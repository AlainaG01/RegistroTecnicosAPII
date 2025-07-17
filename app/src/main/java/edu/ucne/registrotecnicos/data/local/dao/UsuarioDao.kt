package edu.ucne.registrotecnicos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnicos.data.local.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Upsert()
    suspend fun save(usuarios: List<UsuarioEntity>)
    @Query(
        """
        SELECT * 
        FROM Usuarios 
        WHERE usuarioId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): UsuarioEntity?
    @Delete
    suspend fun delete(usuario: UsuarioEntity)
    @Query("SELECT * FROM Usuarios")
    fun getAll(): Flow<List<UsuarioEntity>>
}