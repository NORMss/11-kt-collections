import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.normno.data.NoteServiceImpl
import ru.normno.domain.NoteService
import ru.normno.domain.model.Note
import java.lang.Thread.sleep
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NoteServiceImplTest {

    private lateinit var noteService: NoteService

    @BeforeEach
    fun setup() {
        noteService = NoteServiceImpl()
    }

    @Test
    fun `save should create a new note when id is 0`() {
        val note = Note(id = 0, text = "Test note", createdAt = Instant.now(), updatedAt = Instant.now())

        val savedNote = noteService.save(note)

        assertNotNull(savedNote.id)
        assertEquals("Test note", savedNote.text)
        assertTrue(savedNote.createdAt.isBefore(Instant.now()))
        assertTrue(savedNote.updatedAt.isBefore(Instant.now()))
    }

    @Test
    fun `save should update an existing note when valid id is provided`() {
        val note = Note(id = 0, text = "Old text")
        val savedNote = noteService.save(note)

        val updatedNote = savedNote.copy(text = "Updated text")
        sleep(1L)
        val result = noteService.save(updatedNote)

        assertEquals(savedNote.id, result.id)
        assertEquals("Updated text", result.text)
        assertTrue(result.updatedAt.isAfter(savedNote.updatedAt))
    }

    @Test
    fun `save should throw IllegalArgumentException for invalid id`() {
        val invalidNote =
            Note(id = 999, text = "Non-existent note", createdAt = Instant.now(), updatedAt = Instant.now())

        assertThrows<IllegalArgumentException> {
            noteService.save(invalidNote)
        }
    }

    @Test
    fun `getAll should return all notes`() {
        val note1 = Note(id = 0, text = "First note", createdAt = Instant.now(), updatedAt = Instant.now())
        val note2 = Note(id = 0, text = "Second note", createdAt = Instant.now(), updatedAt = Instant.now())

        noteService.save(note1)
        noteService.save(note2)

        val allNotes = noteService.getAll()
        assertEquals(2, allNotes.size)
    }

    @Test
    fun `getAllUniqueTexts should return unique texts`() {
        val note1 = Note(id = 0, text = "Duplicate", createdAt = Instant.now(), updatedAt = Instant.now())
        val note2 = Note(id = 0, text = "Unique", createdAt = Instant.now(), updatedAt = Instant.now())
        val note3 = Note(id = 0, text = "Duplicate", createdAt = Instant.now(), updatedAt = Instant.now())

        noteService.save(note1)
        noteService.save(note2)
        noteService.save(note3)

        val uniqueTexts = noteService.getAllUniqueTexts()
        assertEquals(2, uniqueTexts.size)
        assertTrue(uniqueTexts.contains("Duplicate"))
        assertTrue(uniqueTexts.contains("Unique"))
    }

    @Test
    fun `getBefore should return notes older than given id`() {
        val note1 =
            noteService.save(Note(id = 0, text = "Note 1", createdAt = Instant.now(), updatedAt = Instant.now()))
        val note2 =
            noteService.save(Note(id = 0, text = "Note 2", createdAt = Instant.now(), updatedAt = Instant.now()))
        val note3 =
            noteService.save(Note(id = 0, text = "Note 3", createdAt = Instant.now(), updatedAt = Instant.now()))

        val result = noteService.getBefore(2, note3.id)

        assertEquals(2, result.size)
        assertEquals(note1.text, result[0].text)
        assertEquals(note2.text, result[1].text)
    }

    @Test
    fun `getAfter should return notes newer than given id`() {
        val note1 =
            noteService.save(Note(id = 0, text = "Note 1", createdAt = Instant.now(), updatedAt = Instant.now()))
        val note2 =
            noteService.save(Note(id = 0, text = "Note 2", createdAt = Instant.now(), updatedAt = Instant.now()))
        val note3 =
            noteService.save(Note(id = 0, text = "Note 3", createdAt = Instant.now(), updatedAt = Instant.now()))

        val result = noteService.getAfter(2, note1.id)

        assertEquals(2, result.size)
        assertEquals(note2.text, result[0].text)
        assertEquals(note3.text, result[1].text)
    }

    @Test
    fun `getBefore should throw IllegalArgumentException for invalid id`() {
        assertThrows<IllegalArgumentException> {
            noteService.getBefore(1, 999)
        }
    }

    @Test
    fun `getAfter should throw IllegalArgumentException for invalid id`() {
        assertThrows<IllegalArgumentException> {
            noteService.getAfter(1, 999)
        }
    }
}
