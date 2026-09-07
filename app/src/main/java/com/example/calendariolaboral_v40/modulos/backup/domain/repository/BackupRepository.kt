package com.example.calendariolaboral_v40.modulos.backup.domain.repository

import android.net.Uri
import javax.inject.Inject

interface BackupRepository {
    suspend fun saveBackup(uri: Uri): Boolean
    suspend fun readBackup(uri: Uri): Boolean
}