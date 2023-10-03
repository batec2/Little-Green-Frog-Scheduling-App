package com.example.f23hopper.data
import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface HopperDao {

    @RawQuery
    suspend fun executeRawQuery(query: SupportSQLiteQuery): Int
}
