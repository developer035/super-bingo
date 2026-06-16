package com.nerdsaladstudios.superbingo.data.repository

import com.nerdsaladstudios.superbingo.data.local.dao.MatchDao
import com.nerdsaladstudios.superbingo.data.local.dao.PlayerDao
import com.nerdsaladstudios.superbingo.data.local.dao.PrizeCategoryDao
import com.nerdsaladstudios.superbingo.data.local.dao.WinnerDao
import com.nerdsaladstudios.superbingo.data.local.entity.toDomain
import com.nerdsaladstudios.superbingo.domain.model.Player
import com.nerdsaladstudios.superbingo.domain.model.PlayerStatistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatisticsRepository @Inject constructor(
    private val playerDao: PlayerDao,
    private val winnerDao: WinnerDao,
    private val prizeCategoryDao: PrizeCategoryDao,
    private val matchDao: MatchDao
) {
    fun getAllPlayerStatistics(): Flow<List<PlayerStatistics>> = playerDao.getAllPlayers().map { players ->
        players.map { playerEntity -> computeStats(playerEntity.toDomain()) }
    }

    private suspend fun computeStats(player: Player): PlayerStatistics {
        val allWinners = winnerDao.getForPlayer(player.id)
        val completedMatches = matchDao.getCompletedMatchesForPlayer(player.id)
        var corners = 0; var lines = 0; var houses = 0; var laddus = 0; var bps = 0
        
        allWinners.forEach { w ->
            val categories = prizeCategoryDao.getForMatch(w.matchId)
            val catName = categories.find { it.id == w.prizeCategoryId }?.name?.lowercase() ?: ""
            when {
                catName.contains("corner") -> corners++
                catName.contains("line") -> lines++
                catName.contains("house") -> houses++
                catName.contains("laddu") -> laddus++
                catName.contains("bp") -> bps++
            }
        }
        
        return PlayerStatistics(
            player = player,
            gamesPlayed = completedMatches.size,
            totalWins = allWinners.size,
            totalPrizeMoney = allWinners.sumOf { it.prizeAmount },
            cornersWon = corners, 
            linesWon = lines, 
            housesWon = houses, 
            laddusWon = laddus, 
            bpWon = bps,
            lastWinDate = allWinners.maxOfOrNull { it.recordedAt }
        )
    }
}
