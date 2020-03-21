package com.hyejineee.hwahae.util

import com.hyejineee.hwahae.util.BaseSchedulers
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class mScheduler : BaseSchedulers {
    override fun io(): Scheduler = Schedulers.newThread()
    override fun ui(): Scheduler  = AndroidSchedulers.mainThread()
}