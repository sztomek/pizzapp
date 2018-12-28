package hu.sztomek.pizzapp.domain

import io.reactivex.Scheduler

interface WorkSchedulers {

    fun worker(): Scheduler

    fun ui(): Scheduler

}