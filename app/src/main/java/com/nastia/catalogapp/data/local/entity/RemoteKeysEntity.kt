package com.nastia.catalogapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey val label: String,
    val nextSkip: Int?,
    val isEndReached: Boolean
)