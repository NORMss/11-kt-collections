package ru.normno.domain

import ru.normno.domain.model.Note

interface NoteService {
    /**
     * Если id == 0, создаёт новую, если id равен существующей заметке, сохраняет
     * В случае, если указан некорректный id, выбрасывает IllegalArgumentException
     * При обновлении updatedAt должно заполняться текущим временем
     * @throws IllegalArgumentException
     */
    fun save(note: Note): Note

    /**
     * Возвращает копию внутреннего списка
     */
    fun getAll(): List<Note>

    /**
     * Возвращает список текстов без повторов
     */
    fun getAllUniqueTexts(): List<String>

    /**
     * Возвращает несколько заметок старше указанного id
     * @param count – сколько заметок отсчитать
     * @param id - относительно какого элемента отсчитывать
     */
    fun getBefore(count: Int, id: Long): List<Note>

    /**
     * Возвращает несколько заметок новее указанного id
     * @param count – сколько заметок отсчитать
     * @param id - относительно какого элемента отсчитывать
     */
    fun getAfter(count: Int, id: Long): List<Note>
}