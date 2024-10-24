package ru.normno.util

sealed class NoteError : Error {
    data class InvalidId(val id: Long) : NoteError()
}