package com.example.screenvideo.data

class FileRepository {

    fun createFile():FileSave{
        return
    }

    fun deleteFile(fileSave: FileSave):Boolean{

        if(fileSave){
            return true
        }else{
            return false
        }
    }
}