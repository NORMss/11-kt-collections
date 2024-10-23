package ru.normno.data

import ru.normno.domain.NoteService
import ru.normno.domain.model.Note
import java.time.Instant
import java.util.concurrent.atomic.AtomicLong

class NoteServiceImpl : NoteService {
    private val notes = mutableListOf<Note>()
    private val idGenerator = AtomicLong(1)

    override fun save(note: Note): Note {
        if (note.id == 0L) {
            note.run {
                copy(
                    id = idGenerator.getAndIncrement(),
                    createdAt = Instant.now(),
                    updatedAt = Instant.now(),
                )
            }.let {
                notes.add(it)
                return it
            }
        } else {
            notes.indexOfFirst { it.id == note.id }.let { index ->
                if (index == -1) {
                    throw IllegalArgumentException("Invalid id: ${note.id}")
                }
                notes[index].run {
                    copy(
                        text = note.text,
                        updatedAt = Instant.now(),
                    )
                }.let {
                    notes.add(it)
                    return it
                }
            }
        }
    }

    override fun getAll(): List<Note> {
        return notes.toList()
    }

    override fun getAllUniqueTexts(): List<String> {
        return notes.asSequence().map { it.text }.distinct().toList()
    }

    override fun getBefore(count: Int, id: Long): List<Note> {
        notes.indexOfFirst { it.id == id }.let { startIndex ->
            if (startIndex == -1) {
                throw IllegalArgumentException("Invalid id: $id")
            }
            return notes.subList(0, startIndex).takeLast(count)
        }
    }

    override fun getAfter(count: Int, id: Long): List<Note> {
        notes.indexOfFirst { it.id == id }.let { startIndex ->
            if (startIndex == -1) {
                throw IllegalArgumentException("Invalid id: $id")
            }
            return notes.subList(startIndex + 1, notes.size).take(count)
        }
    }
}