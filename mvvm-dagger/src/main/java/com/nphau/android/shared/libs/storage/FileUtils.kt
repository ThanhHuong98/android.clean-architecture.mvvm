/*
 * Created by IMStudio on 5/29/21 10:43 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/29/21 10:43 AM
 */

package com.nphau.android.shared.libs.storage

import com.nphau.android.shared.common.functional.Transformer
import java.io.File

object FileUtils {

    @JvmStatic
    fun files(folder: String, isAccept: Transformer<String, Boolean>): List<File> {
        return File(folder).listFiles { _, name -> isAccept(name) }?.toList<File>() ?: listOf()
    }
    
}