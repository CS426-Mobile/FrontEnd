// BookStoreDatabase.kt
package com.example.bookstore.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookstore.model.AuthorEntity
import com.example.bookstore.model.BookEntity
import com.example.bookstore.model.CategoryEntity
import com.example.bookstore.model.UserEntity

@Database(entities = [UserEntity::class, AuthorEntity::class, BookEntity::class, CategoryEntity::class], version = 1)
abstract class BookStoreDatabase : RoomDatabase() {
    abstract fun bookStoreDao(): BookStoreDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: BookStoreDatabase? = null

        fun getDatabase(context: Context): BookStoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookStoreDatabase::class.java,
                    "bookstore_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
