package com.nerdsaladstudios.superbingo.data.repository

import com.nerdsaladstudios.superbingo.data.local.dao.PlayerDao
import com.nerdsaladstudios.superbingo.data.local.entity.PlayerEntity
import com.nerdsaladstudios.superbingo.data.local.entity.toDomain
import com.nerdsaladstudios.superbingo.domain.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(private val playerDao: PlayerDao) {
    fun getAllPlayers(): Flow<List<Player>> = playerDao.getAllPlayers().map { list -> 
        list.map { it.toDomain() } 
    }

    suspend fun getPlayerById(id: Long): Player? = playerDao.getById(id)?.toDomain()

    suspend fun getPlayersByIds(ids: List<Long>): List<Player> = 
        playerDao.getByIds(ids).map { it.toDomain() }

    suspend fun insertPlayer(name: String): Long = 
        playerDao.insert(PlayerEntity(name = name.trim(), createdAt = System.currentTimeMillis()))

    suspend fun updatePlayer(id: Long, name: String) { 
        playerDao.getById(id)?.let { playerDao.update(it.copy(name = name.trim())) } 
    }

    suspend fun deletePlayer(id: Long) { 
        playerDao.getById(id)?.let { playerDao.delete(it) } 
    }
}
