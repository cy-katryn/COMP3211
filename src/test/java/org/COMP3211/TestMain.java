package org.COMP3211;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.COMP3211.Controller.Main;
import org.COMP3211.Model.Move;
import org.COMP3211.Model.Read;
import org.COMP3211.Model.Record;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestMain {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        Main.game = null;
    }

    @Test
    void testBothEmptyNames() throws Exception {
        // conditon1: '' + ''
        // expect both get default random names
        String input = "1\n\n\nquit\n4\n";
        provideInput(input);

        runMain();

        assertNotNull(Main.game);
        System.out.println(Main.game.getPlayer1Name());
        assertTrue(Main.game.getPlayer1Name().startsWith("Player1_"));
        assertTrue(Main.game.getPlayer2Name().startsWith("Player2_"));
    }

    @Test
    void testDuplicateNames() throws Exception {
        // conditon2: 'player' + 'player' + 'player2'
        // expect re-prompt for player2 name
        String input = "1\nplayer\nplayer\nplayer2\nquit\n4\n";
        provideInput(input);

        runMain();

        String output = outContent.toString();
        assertTrue(output.contains("Name already taken"));

        assertNotNull(Main.game);
        assertEquals("player", Main.game.getPlayer1Name());
        assertEquals("player2", Main.game.getPlayer2Name());
    }

    @Test
    void testDifferentNames() throws Exception {
        // conditon3: 'player1' + 'player2'
        // expect names set correctly
        String input = "1\nplayer1\nplayer2\nquit\n4\n";
        provideInput(input);

        runMain();

        assertNotNull(Main.game);
        assertEquals("player1", Main.game.getPlayer1Name());
        assertEquals("player2", Main.game.getPlayer2Name());
    }

    @Test
    void testInvalidMenuChoice() throws Exception {
        // input invalid menu choice '5'
        // expect re-prompt for valid choice
        String input = "5\n4\n"; 
        provideInput(input);
        
        runMain();
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid number"));
    }

    @Test
    void testRecordSave() throws Exception {
        // test record command
        String testFilename = "test_game_record.record";
        String input = "1\nPlayerA\nPlayerB\n" +
                "move R R\n" +                    // Make a move to record
                "record " + testFilename + "\n" + // Save record with specific filename
                "quit\n4\n";
        provideInput(input);
        
        runMain();
        
        assertNotNull(Main.game.getGameRecord(), "Game should have a record object");
        Record record = Main.game.getGameRecord();
        List<Move> moves = record.getRecords();
        assertEquals(1, moves.size(), "record should have 1 move");
    }

    @Test
    void testGameSave() throws Exception {
        // test save command
        String testFilename = "test_game_save.jungle";
        String input = "1\nPlayerX\nPlayerY\n" +
                "move R R\n" +                  // Make a move
                "move C R\n" +                  // Another move
                "save " + testFilename + "\n" + // Save game with specific filename
                "quit\n4\n";
        provideInput(input);
        
        runMain();
        
        assertNotNull(Main.game.getGameRecord(), "Game should have a record object");

        Record record = Main.game.getGameRecord();
        List<Move> moves = record.getRecords();
        assertEquals(2, moves.size(), "record should have 2 moves");

        boolean foundRatMove = false;
        boolean foundCatMove = false;
        for (Move move : moves) {
            if ("R".equals(move.getPieceKey()) && "R".equals(move.getDir())) {
                foundRatMove = true;
            }
            if ("C".equals(move.getPieceKey()) && "R".equals(move.getDir())) {
                foundCatMove = true;
            }
        }
        assertTrue(foundRatMove, "Should record rat move");
        assertTrue(foundCatMove, "Should record cat move");
    }

    @Test
    void testUndoMove() throws Exception {
        // test undo command
        // expect last move undone and record file correctly
        String testFile = "test_undo_record.record";
        String input = "1\nPlayerA\nPlayerB\nmove R R\nundo\nrecord " + testFile + "\nquit\n4\n";
        provideInput(input);
        
        runMain();
        
        String output = outContent.toString();
        assertTrue(output.contains("undone"), "Should show undo success message");

        // the rat should be at the initial position
        assertNull(Main.game.getPieceAt(2, 1), "After undo, position (2,1) should be empty");

        java.io.File file = new java.io.File(testFile);
        assertTrue(file.exists(), "Record file should be created");
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(testFile)));
            System.out.println("Record file content:\n" + fileContent);

            assertTrue(fileContent.contains("R"), "File should contain rat move information");
            assertTrue(fileContent.contains("Game History"), "File should have game history section");

            Record loadedRecord = Read.readFile(testFile);
            assertNotNull(loadedRecord, "Should be able to load the record file");      
            
            List<Move> moves = loadedRecord.getRecords();
            assertTrue(moves.isEmpty(), "Reasonable number of moves should be recorded");
            
        } finally {
            try {
                Files.deleteIfExists(Paths.get(testFile));
            } catch (Exception e) {
                System.out.println("Cleanup warning: " + e.getMessage());
            }
        }
    }

    @Test
    void testDirectExit() throws Exception {
        // test direct exit from main menu
        String input = "4\n";
        provideInput(input);
        
        runMain();
        
        String output = outContent.toString();
        assertTrue(output.contains("Goodbye"), "Should display goodbye message");
        assertNull(Main.game, "Game should not be initialized when exiting directly");
    
    }

    private void runMain() throws InterruptedException {
        Thread thread = new Thread(() -> Main.main(new String[]{}));
        thread.start();
        Thread.sleep(2000);
        thread.interrupt();
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }
}