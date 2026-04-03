package com.abramyango.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.abramyango.data.db.AbramyanGoDatabase
import com.abramyango.domain.model.Ability
import com.abramyango.domain.model.AbilityInventory
import com.abramyango.domain.repository.AbilityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AbilityRepositoryImpl(
    private val database: AbramyanGoDatabase
) : AbilityRepository {
    
    private val queries = database.abramyanGoDatabaseQueries
    
    override fun getInventory(): Flow<AbilityInventory> {
        return queries.getAbilityInventory()
            .asFlow()
            .mapToOne(Dispatchers.IO)
            .map { entity ->
                AbilityInventory(
                    hints = entity.hints.toInt(),
                    removeWrong = entity.remove_wrong.toInt(),
                    secondChance = entity.second_chance.toInt(),
                    xray = entity.xray.toInt()
                )
            }
    }
    
    override suspend fun useAbility(ability: Ability): Boolean = withContext(Dispatchers.IO) {
        val inventory = queries.getAbilityInventory().executeAsOne()
        
        val canUse = when (ability) {
            Ability.HINT -> inventory.hints > 0
            Ability.REMOVE_WRONG -> inventory.remove_wrong > 0
            Ability.SECOND_CHANCE -> inventory.second_chance > 0
            Ability.XRAY -> inventory.xray > 0
        }
        
        if (canUse) {
            val newInventory = when (ability) {
                Ability.HINT -> inventory.copy(hints = inventory.hints - 1)
                Ability.REMOVE_WRONG -> inventory.copy(remove_wrong = inventory.remove_wrong - 1)
                Ability.SECOND_CHANCE -> inventory.copy(second_chance = inventory.second_chance - 1)
                Ability.XRAY -> inventory.copy(xray = inventory.xray - 1)
            }
            
            queries.updateAbilityInventory(
                hints = newInventory.hints,
                remove_wrong = newInventory.remove_wrong,
                second_chance = newInventory.second_chance,
                xray = newInventory.xray
            )
        }
        
        canUse
    }

    override suspend fun purchaseAbility(ability: Ability): Boolean = withContext(Dispatchers.IO) {
        val inventory = queries.getAbilityInventory().executeAsOne()
        
        val newInventory = when (ability) {
            Ability.HINT -> inventory.copy(hints = inventory.hints + 1)
            Ability.REMOVE_WRONG -> inventory.copy(remove_wrong = inventory.remove_wrong + 1)
            Ability.SECOND_CHANCE -> inventory.copy(second_chance = inventory.second_chance + 1)
            Ability.XRAY -> inventory.copy(xray = inventory.xray + 1)
        }
        
        queries.updateAbilityInventory(
            hints = newInventory.hints,
            remove_wrong = newInventory.remove_wrong,
            second_chance = newInventory.second_chance,
            xray = newInventory.xray
        )
        true
    }
}
