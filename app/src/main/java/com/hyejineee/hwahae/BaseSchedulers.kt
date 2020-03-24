package com.hyejineee.hwahae

import io.reactivex.Scheduler

interface BaseSchedulers {
    fun io(): Scheduler
    fun ui(): Scheduler
}