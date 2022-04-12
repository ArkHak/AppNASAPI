package com.example.appnasapi.bd

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class PODRepository(private val podDao: PODDao) {
    val allPods: Flow<MutableList<POD>> = podDao.getAllPods()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(pod: POD) {
        podDao.insert(pod)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(pod: POD) {
        podDao.delete(pod.date)
    }
}