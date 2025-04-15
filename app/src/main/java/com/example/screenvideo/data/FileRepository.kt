package com.example.screenvideo.data

interface FileRepository {

    fun createFile():FileSave{
        return FileSave(

            name = TODO(),
            type = TODO(),
            location = TODO()
        )

    }

    fun deleteFile(fileSave: FileSave):Boolean{

        return false
    }
}