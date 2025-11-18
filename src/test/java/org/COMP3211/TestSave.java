package org.COMP3211;

import java.nio.file.Files;
import java.nio.file.Path;
import org.COMP3211.Model.Record;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.COMP3211.Model.Save;
import org.COMP3211.View.Replay;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TestSave {

    @Test
    void testSaveRecordCreatesFile(@TempDir Path tempDir) throws Exception {
        // Test that saveRecord creates a non-empty .record file with correct extension handling
        Record r = new Record("TestPlayer1", "TestPlayer2");
        String base = tempDir.resolve("match1").toString(); // no extension
        boolean ok = Save.saveRecord(r, base);
        Path f = tempDir.resolve("match1.record");
        assertTrue(ok, "saveRecord should return true on success");
        assertTrue(Files.exists(f), "Saved .record file should exist");
        assertTrue(Files.size(f) > 0, "Saved .record file should not be empty");
    }

    @Test
    void testSaveRecordWithExistingExtension(@TempDir Path tempDir) throws Exception {
        // Test that saveRecord doesn't duplicate extension when already present
        Record r = new Record("TestPlayer1", "TestPlayer2");
        String pathWithExt = tempDir.resolve("test.record").toString();
        boolean result = Save.saveRecord(r, pathWithExt);
        assertTrue(result, "saveRecord should succeed with existing extension");
        assertTrue(Files.exists(Path.of(pathWithExt)), "File with existing extension should be created");
        // Ensure no double extension like test.record.record
        assertFalse(Files.exists(Path.of(pathWithExt + ".record")), "No duplicate extension should be added");
    }

    @Test
    void testSaveGameCreatesFile(@TempDir Path tempDir) throws Exception {
        // Test that saveGame creates a non-empty .jungle file with correct extension handling
        Record r = new Record("TestPlayer1", "TestPlayer2");
        String base = tempDir.resolve("savegame").toString();
        boolean ok = Save.saveGame(r, base);
        Path f = tempDir.resolve("savegame.jungle");
        assertTrue(ok, "saveGame should return true on success");
        assertTrue(Files.exists(f), "Saved .jungle file should exist");
        assertTrue(Files.size(f) > 0, "Saved .jungle file should not be empty");
    }

    @Test
    void testSaveGameWithExistingExtension(@TempDir Path tempDir) throws Exception {
        // Test that saveGame doesn't duplicate extension when already present
        Record r = new Record("TestPlayer1", "TestPlayer2");
        String pathWithExt = tempDir.resolve("test.jungle").toString();
        boolean result = Save.saveGame(r, pathWithExt);
        assertTrue(result, "saveGame should succeed with existing extension");
        assertTrue(Files.exists(Path.of(pathWithExt)), "File with existing extension should be created");
        // Ensure no double extension like test.jungle.jungle
        assertFalse(Files.exists(Path.of(pathWithExt + ".jungle")), "No duplicate extension should be added");
    }

    @Test
    void testWriteToFileDirect(@TempDir Path tempDir) throws Exception {
        // Test direct writeToFile method with full file path
        Record r = new Record("TestPlayer1", "TestPlayer2");
        String file = tempDir.resolve("direct.record").toString();
        boolean ok = Save.writeToFile(r, file);
        Path p = tempDir.resolve("direct.record");
        assertTrue(ok, "writeToFile should return true for a normal path");
        assertTrue(Files.exists(p));
        assertTrue(Files.size(p) > 0);
    }

    @Test
    void testWriteToFileWithInvalidPath() {
        // Test writeToFile behavior with invalid file path - should return false, not throw
        Record r = new Record("TestPlayer1", "TestPlayer2");
        boolean result = Save.writeToFile(r, "/invalid/path/test.record");
        assertFalse(result, "writeToFile should return false for invalid path");
    }

    @Test
    void testWriteToFileWithReadOnlyDirectory(@TempDir Path tempDir) throws Exception {
        // Test writeToFile behavior with read-only directory (if supported by OS)
        Record r = new Record("TestPlayer1", "TestPlayer2");
        Path readOnlyDir = tempDir.resolve("readonly");
        Files.createDirectories(readOnlyDir);
        
        // Attempt to make directory read-only (may not work on all systems)
        readOnlyDir.toFile().setReadOnly();
        
        try {
            String filePath = readOnlyDir.resolve("test.record").toString();
            boolean result = Save.writeToFile(r, filePath);
            assertFalse(result, "writeToFile should return false when cannot write to directory");
        } finally {
            // Restore permissions for cleanup
            readOnlyDir.toFile().setWritable(true);
        }
    }

    @Test
    void testSaveRecordWithInvalidPath() {
        // Test saveRecord with invalid path - should return false, not throw
        Record r = new Record("TestPlayer1", "TestPlayer2");
        boolean result = Save.saveRecord(r, "/invalid/path/test");
        assertFalse(result, "saveRecord should return false for invalid path");
    }

    @Test
    void testSaveGameWithInvalidPath() {
        // Test saveGame with invalid path - should return false, not throw
        Record r = new Record("TestPlayer1", "TestPlayer2");
        boolean result = Save.saveGame(r, "/invalid/path/test");
        assertFalse(result, "saveGame should return false for invalid path");
    }

    @Test
    void testLoadRecordWithWrongExtensionDoesNotThrow(@TempDir Path tempDir) {
        // Test that loadRecord safely handles wrong file extensions without throwing exceptions
        String bad = tempDir.resolve("notarecord.txt").toString();
        assertDoesNotThrow(() -> Replay.replayRecord(bad), 
            "loadRecord should ignore wrong extensions and not throw");
    }

    @Test
    void testLoadRecordWithNonExistentFile() {
        // Test that loadRecord handles non-existent files gracefully
        assertDoesNotThrow(() -> Replay.replayRecord("nonexistent.record"),
            "loadRecord should handle non-existent files without throwing");
    }

    @Test
    void testLoadGameWithWrongExtensionDoesNotThrow(@TempDir Path tempDir) {
        // Test that loadGame safely handles wrong file extensions without throwing exceptions
        String bad = tempDir.resolve("notajungle.txt").toString();
        assertDoesNotThrow(() -> Replay.replayGame(bad), 
            "loadGame should ignore wrong extensions and not throw");
    }

    @Test
    void testLoadGameWithNonExistentFile() {
        // Test that loadGame handles non-existent files gracefully
        assertDoesNotThrow(() -> Replay.replayGame("nonexistent.jungle"),
            "loadGame should handle non-existent files without throwing");
    }

    @Test
    void testReplayHandlesEmptyRecordWithoutException() {
        // Test that replay method handles records with empty movement history gracefully
        Record r = new Record("TestPlayer1", "TestPlayer2");
        assertDoesNotThrow(() -> Replay.replay(r, false), 
            "replay should handle empty records without throwing");
        assertDoesNotThrow(() -> Replay.replay(r, true), 
            "replay (continueAfterReplay=true) should handle empty records without throwing");
    }

    @Test
    void testReplayWithNullRecord() {
        // Test that replay method handles null record input gracefully
        assertDoesNotThrow(() -> Replay.replay(null, false),
            "replay should handle null record without throwing");
        assertDoesNotThrow(() -> Replay.replay(null, true),
            "replay with continueAfterReplay should handle null record without throwing");
    }

    @Test
    void testSaveAndReplayIntegration(@TempDir Path tempDir) throws Exception {
        // Integration test: verify that saved files can be loaded without throwing exceptions
        Record r = new Record("TestPlayer1", "TestPlayer2");
        String base = tempDir.resolve("inttest").toString();
        
        // Save the record
        assertTrue(Save.saveRecord(r, base));
        Path saved = tempDir.resolve("inttest.record");
        assertTrue(Files.exists(saved));
        
        // Load should not throw exceptions
        assertDoesNotThrow(() -> Replay.replayRecord(saved.toString()));
    }

    @Test
    void testSavedFileContainsPlayerInfo(@TempDir Path tempDir) throws Exception {
        // Test that saved files contain expected player information in content
        String player1 = "TestPlayer1";
        String player2 = "TestPlayer2";
        Record r = new Record(player1, player2);
        
        String filepath = tempDir.resolve("content_test.record").toString();
        assertTrue(Save.saveRecord(r, filepath));
        
        // Read file content and verify it contains player names
        String content = Files.readString(Path.of(filepath));
        assertTrue(content.contains(player1), 
            "Saved file should contain player1 name");
        assertTrue(content.contains(player2), 
            "Saved file should contain player2 name");
    }

    @Test
    void testMultipleSavesToSameFile(@TempDir Path tempDir) throws Exception {
        // Test that multiple saves to the same file overwrite previous content
        Record r1 = new Record("First1", "First2");
        Record r2 = new Record("Second1", "Second2");
        
        String filepath = tempDir.resolve("multiple.record").toString();
        
        // First save
        assertTrue(Save.saveRecord(r1, filepath));
        long firstSize = Files.size(Path.of(filepath));
        
        // Second save to same file
        assertTrue(Save.saveRecord(r2, filepath));
        long secondSize = Files.size(Path.of(filepath));
        
        // File should still exist and have content (size may vary)
        assertTrue(Files.exists(Path.of(filepath)));
        assertTrue(secondSize > 0);
        
        // Verify second save content contains second set of player names
        String content = Files.readString(Path.of(filepath));
        assertTrue(content.contains("Second1"), "File should contain second save's player names");
        assertFalse(content.contains("First1"), "File should not contain first save's player names");
    }

    @Test
    void testSaveWithSpecialCharactersInFilename(@TempDir Path tempDir) throws Exception {
        // Test that filenames with special characters are handled correctly
        Record r = new Record("TestPlayer1", "TestPlayer2");
        String filename = tempDir.resolve("test-file_with.special_chars").toString();
        
        boolean result = Save.saveRecord(r, filename);
        assertTrue(result, "saveRecord should handle special characters in filename");
        
        Path expectedFile = tempDir.resolve("test-file_with.special_chars.record");
        assertTrue(Files.exists(expectedFile), "File with special characters should be created");
    }
}