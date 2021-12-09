package com.compose.sultan.plagiarismchecker.presentaion.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.compose.sultan.plagiarismchecker.MyApplication
import com.compose.sultan.plagiarismchecker.model.MyFile
import com.thoughtleaf.textsumarizex.DocumentReaderUtil
import kotlinx.coroutines.*
import android.text.TextUtils
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.DialogProperties
import java.io.*


private const val TAG = "DialogListOfFiles"

@Composable
fun DialogListOfFiles(
    files: List<MyFile>,
    textPosition: Int,
    setText1: (String) -> Unit,
    setText2: (String) -> Unit={},
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    setProgress1: (Boolean) -> Unit,
    setProgress2: (Boolean) -> Unit={}
) {
    val dialogWidth = 250.dp
    if (showDialog) {
        Dialog(onDismissRequest = {
            setShowDialog(false)
            if (textPosition==1){
                setProgress1(false)
            }else{
                setProgress2(false)
            }
        }) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Box(
                Modifier
                    .size(dialogWidth)
                    .background(MaterialTheme.colors.background, RoundedCornerShape(10.dp)),
            ) {
                LazyColumn {
                    items(files) { file ->
                        FileCard(file, onClick = {
                            setShowDialog(false)
                            if (textPosition == 1) {
                                val uri = Uri.parse(file.path)
                                Log.e(TAG, "uri  ==== ${uri.path}")
                                CoroutineScope(Dispatchers.IO).launch {
                                    val context = MyApplication.getAppInstance()
//                                     uri=Uri.parse(getFilePathFromURI(uri,context, File(file.path?:"")))
                                    val docString: String =
                                        DocumentReaderUtil.readWordDocFromUri(uri, context)
                                    withContext(Dispatchers.Main) {
                                        setText1(docString)
                                        setProgress1(false)
                                    }
                                }
                            } else {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val docString: String? = DocumentReaderUtil.readWordDocFromUri(
                                        Uri.parse(file.path), MyApplication.getAppInstance()
                                    )
                                    withContext(Dispatchers.Main) {
                                        setText2(docString?:"")
                                        setProgress2(false)

                                    }
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}
//
//fun getFilePathFromURI(contentUri: Uri, context: Context, file:File): String {
//    //copy file and send new file path
////    context.contentResolver.takePersistableUriPermission(contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//    val fileName: String = file.name
//    if (!TextUtils.isEmpty(fileName)) {
//        val copyFile = File("${context.externalCacheDir}${File.separator}$fileName")
//        copy(context, contentUri, copyFile)
//        return copyFile.absolutePath
//    }
//    return ""
//}
//
//
//fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
//    try {
//        val inputStream = context.contentResolver.openInputStream(srcUri!!)
//            ?: return
//        val outputStream: OutputStream = FileOutputStream(dstFile)
//        inputStream.close()
//        outputStream.close()
//    } catch (e: IOException) {
//        e.printStackTrace()
//    }
//}
