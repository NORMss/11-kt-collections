package ru.normno

import ru.normno.data.NoteServiceImpl
import ru.normno.domain.model.Note
import java.lang.Thread.sleep

fun main() {
    val noteService = NoteServiceImpl()

    noteService.save(Note(0,"Test"))
    println(noteService.getAll())
    noteService.save(Note(1,"TestTest"))
    println(noteService.getAll())
}