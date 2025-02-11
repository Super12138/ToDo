package cn.super12138.todo.utils

import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object FileUtils {
    fun addFileToZip(file: File, fileName: String, zipOut: ZipOutputStream) {
        if (file.isHidden) {
            return // 忽略隐藏文件
        }
        if (file.isDirectory) {
            // 如果是文件夹，递归处理
            val children = file.listFiles()
            if (children != null) {
                for (childFile in children) {
                    addFileToZip(childFile, "$fileName/${childFile.name}", zipOut)
                }
            }
        } else {
            // 如果是文件，写入 ZIP
            FileInputStream(file).use { fis ->
                val zipEntry = ZipEntry(fileName)
                zipOut.putNextEntry(zipEntry)
                fis.copyTo(zipOut)
            }
        }
    }
}