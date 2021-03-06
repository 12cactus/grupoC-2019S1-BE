package edu.unq.desapp.eventeando.guest

import edu.unq.desapp.eventeando.checkingAccount.Movement
import edu.unq.desapp.eventeando.checkingAccount.MovementType
import edu.unq.desapp.eventeando.event.Event
import edu.unq.desapp.eventeando.event.Party
import edu.unq.desapp.eventeando.guest.exception.CannotConfirmAssitanceException
import java.time.LocalDate

/**
 * Models a person that has events and movements
 */
class User(val name: String) {
    val unconfirmedEvents: MutableList<Event> = mutableListOf()
    val confirmedEvents: MutableList<Event> = mutableListOf()
    val movements: MutableList<Movement> = mutableListOf()

    /**
     * Returns true if this attends to an event
     */
    fun isConfirmedFor(anEvent: Event): Boolean {
        return this.confirmedEvents.contains(anEvent)
    }

    /**
     *
     */
    fun putDown(cost: Double): Movement{
        val movement = Movement(LocalDate.now(), cost, MovementType.BANKDEPOSIT)
        movements.add(movement)
        return movement
    }

    /**
     * Gets bank deposits
     */
    fun getBankDeposits(): List<Movement>{
        return movements.filter { movement -> movement.type == MovementType.BANKDEPOSIT }
    }

    /**
     * Withdrawal money
     */
    fun retirar(amount: Double): Movement{
        val withdrawal = Movement(LocalDate.now(),amount,MovementType.BANKWITHDRAWAL)
        movements.add(withdrawal)
        return withdrawal
    }

    /**
     * Gets movements of withdrawal type
     */
    fun getBankWithdrawals():List<Movement>{
        return movements.filter { movement -> movement.type == MovementType.BANKWITHDRAWAL }
    }

    /**
     * Gets summary or resume of movements
     */
    fun getSummary(): Double {
        return  getBankDeposits().fold(0.00) { total, movement -> total + movement.cost } -
                getBankWithdrawals().fold(0.00) { total, movement -> total + movement.cost }
    }

    fun invitationFrom(event: Event) {
        unconfirmedEvents.add(event)
    }

    /**
     * Confirm if the event has not happened yet or throw exception custom..
     */
    fun confirmAssistanceTo(event: Event) {
        event.confirmAssistance(this)
    }

    /**
     * Attend an event that involves, remove it from unconfirmed events, and add it to confirmed events.
     */
    fun attendTo(event: Event) {
        unconfirmedEvents.remove(event)
        confirmedEvents.add(event)
    }
}
