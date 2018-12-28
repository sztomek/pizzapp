package hu.sztomek.pizzapp.device.impl

import hu.sztomek.pizzapp.domain.WorkSchedulers
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WorkSchedulersImpl : WorkSchedulers {

    override fun worker(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

}