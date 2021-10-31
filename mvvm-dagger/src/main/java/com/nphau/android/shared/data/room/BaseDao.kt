/*
 * Created by nphau on 31/10/2021, 22:08
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 31/10/2021, 21:37
 */

package com.nphau.android.shared.data.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg obj: T)

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)

}