package com.nerdsalad.superbingo.data.repository

import com.nerdsalad.superbingo.data.local.dao.CalledNumberDao
import com.nerdsalad.superbingo.data.local.dao.MatchDao
import com.nerdsalad.superbingo.data.local.dao.PlayerDao
import com.nerdsalad.superbingo.data.local.dao.PrizeCategoryDao
import com.nerdsalad.superbingo.data.local.dao.WinnerDao
import com.nerdsalad.superbingo.data.local.entity.CalledNumberEntity
import com.nerdsalad.superbingo.data.local.entity.MatchEntity
import com.nerdsalad.superbingo.data.local.entity.PrizeCategoryEntity
import com.nerdsalad.superbingo.data.local.entity.WinnerEntity
import com.nerdsalad.superbingo.data.local.entity.toDomain
import com.nerdsalad.superbingo.domain.model.CalledNumber
import com.nerdsalad.superbingo.domain.model.Match
import com.nerdsalad.superbingo.domain.model.MatchDetail
import com.nerdsalad.superbingo.domain.model.PrizeCategory
import com.nerdsalad.superbingo.domain.model.Winner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepository @Inject constructor(
    private val matchDao: MatchDao,
    private val prizeCategoryDao: PrizeCategoryDao,
    private val winnerDao: WinnerDao,
    private val calledNumberDao: CalledNumberDao,
    private val playerDao: PlayerDao
) {
    fun getAllMatches(): Flow<List<Match>> = matchDao.getAllMatches().map { list -> 
        list.map { it.toDomain() } 
    }

    suspend fun createMatch(potAmount: Double, playerIds: List<Long>, prizeCategories: List<PrizeCategory>): Long {
        val matchEntity = MatchEntity(
            createdAt = System.currentTimeMillis(), 
            totalPotAmount = potAmount, 
            playerIds = playerIds.joinToString(",")
        )
        val matchId = matchDao.insert(matchEntity)
        val categoryEntities = prizeCategories.mapIndexed { index, cat ->
            PrizeCategoryEntity(matchId = matchId, name = cat.name, amount = cat.amount, sortOrder = index)
        }
        prizeCategoryDao.insertAll(categoryEntities)
        return matchId
    }

    suspend fun getMatchDetail(id: Long): MatchDetail? {
        val matchEntity = matchDao.getById(id) ?: return null
        val match = matchEntity.toDomain()
        val players = playerDao.getByIds(match.playerIds).map { it.toDomain() }
        val categories = prizeCategoryDao.getForMatch(id).map { it.toDomain() }
        val winners = winnerDao.getForMatch(id).map { w ->
            val player = playerDao.getById(w.playerId)
            val category = categories.find { it.id == w.prizeCategoryId }
            w.toDomain(playerName = player?.name ?: "", categoryName = category?.name ?: "")
        }
        val calledNumbers = calledNumberDao.getForMatch(id).map { it.toDomain() }
        return MatchDetail(match, players, categories, winners, calledNumbers)
    }

    suspend fun completeMatch(id: Long) {
        matchDao.getById(id)?.let { 
            matchDao.update(it.copy(isCompleted = true, completedAt = System.currentTimeMillis())) 
        }
    }

    suspend fun addCalledNumber(matchId: Long, number: Int, callOrder: Int) {
        calledNumberDao.insert(
            CalledNumberEntity(matchId = matchId, number = number, calledAt = System.currentTimeMillis(), callOrder = callOrder)
        )
    }

    suspend fun removeLastCalledNumber(matchId: Long): Int? {
        val last = calledNumberDao.getLastNumber(matchId)
        calledNumberDao.deleteLastForMatch(matchId)
        return last
    }

    suspend fun getCalledNumbers(matchId: Long): List<CalledNumber> = 
        calledNumberDao.getForMatch(matchId).map { it.toDomain() }

    suspend fun recordWinner(matchId: Long, prizeCategoryId: Long, playerId: Long, prizeAmount: Double) {
        winnerDao.insert(
            WinnerEntity(
                matchId = matchId, 
                prizeCategoryId = prizeCategoryId, 
                playerId = playerId, 
                prizeAmount = prizeAmount, 
                recordedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun getWinnersForMatch(matchId: Long): List<Winner> {
        val winners = winnerDao.getForMatch(matchId)
        val categories = prizeCategoryDao.getForMatch(matchId)
        return winners.map { w ->
            val player = playerDao.getById(w.playerId)
            val category = categories.find { it.id == w.prizeCategoryId }
            w.toDomain(playerName = player?.name ?: "", categoryName = category?.name ?: "")
        }
    }

    suspend fun removeWinner(winnerId: Long) {
        winnerDao.deleteById(winnerId)
    }

    suspend fun getPrizeCategoriesForMatch(matchId: Long): List<PrizeCategory> = 
        prizeCategoryDao.getForMatch(matchId).map { it.toDomain() }

    suspend fun resetMatch(matchId: Long) {
        calledNumberDao.deleteAllForMatch(matchId)
        winnerDao.deleteForMatch(matchId)
    }

    suspend fun deleteMatch(id: Long) {
        matchDao.getById(id)?.let { matchDao.delete(it) }
    }
}
