package com.nastia.catalogapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nastia.catalogapp.data.local.entity.RemoteKeysEntity

@Dao
interface RemoteKeysDao {

    @Query("SELECT * FROM remote_keys WHERE label = :label")
    suspend fun getRemoteKeys(label: String): RemoteKeysEntity?

    @Upsert
    suspend fun upsert(remoteKeys: RemoteKeysEntity)

    @Query("DELETE FROM remote_keys WHERE label = :label")
    suspend fun deleteByLabel(label: String)
}