package com.hyejineee.hwahae.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Scheduler : BaseSchedulers {
    override fun io(): Scheduler = Schedulers.io()
    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}