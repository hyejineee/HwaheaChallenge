package com.hyejineee.hwahae.util

import io.reactivex.Scheduler

interface BaseSchedulers {
    fun io(): Scheduler
    fun ui(): Scheduler
}