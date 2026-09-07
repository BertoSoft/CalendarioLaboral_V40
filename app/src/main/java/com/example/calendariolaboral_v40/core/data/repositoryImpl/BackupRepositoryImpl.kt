package com.example.calendariolaboral_v40.core.data.repositoryImpl

import android.net.Uri
import com.example.calendariolaboral_v40.core.data.DatosIO
import com.example.calendariolaboral_v40.core.data.MiSqliteHelper
import com.example.calendariolaboral_v40.core.di.IoDispatcher
import com.example.calendariolaboral_v40.modulos.backup.domain.repository.BackupRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackupRepositoryImpl@Inject constructor(
    private val miSqliteHelper: MiSqliteHelper,
    private val miDatosIO: DatosIO,
    @IoDispatcher private val coroutine: CoroutineDispatcher
): BackupRepository {

    override suspend fun saveBackup(uri: Uri): Boolean {
        return withContext(coroutine){
            miDatosIO.saveBackup(uri)
        }
    }

    override suspend fun readBackup(uri: Uri): Boolean {
        return withContext(coroutine){
            miSqliteHelper.close()
            miDatosIO.readBackup(uri)
        }
    }
}