package ru.normno

import arrow.core.Either
import ru.normno.data.NoteServiceImpl
import ru.normno.domain.model.Note

fun main() {
    val noteService = NoteServiceImpl()

    noteService.save(Note(0, "TestTest")).let { response ->
        when (response) {
            is Either.Left -> {
                println(response.value)
            }

            is Either.Right -> {
                println(noteService.getAll())
            }
        }
    }
    println()
    noteService.save(Note(999, "TestTest")).let { response ->
        when (response) {
            is Either.Left -> {
                println(response.value)
            }

            is Either.Right -> {
                println(noteService.getAll())
            }
        }
    }
}