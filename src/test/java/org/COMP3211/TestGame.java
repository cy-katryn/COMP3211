package org.COMP3211;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.COMP3211.Controller.Main;
import org.COMP3211.Model.Game;
import org.COMP3211.Model.Piece;
import org.COMP3211.Model.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestGame {

    private Game g;

    @BeforeEach
    void setUp() {
        g = new Game();
        Main.game = g;
    }

    @Test
    void testGameInitialization() {
        // Test that the game initializes correctly
        assertNotNull(g, "Game should be initialized");
        g.setPlayerNames("TestPlayer1", "TestPlayer2");

        assertNotNull(g.getCurrentPlayer(), "Player1 should be initialized");
        assertEquals("TestPlayer1", g.getCurrentPlayer());
        assertEquals(0, g.turn, "Turn should start at 0");
    }

    @Test
    void testInitializationPieces() {
        // Test that pieces are initialized at correct positions
        // initial rat for player1 is at (2,0)
        Piece p = g.getPieceAt(2, 0);
        assertNotNull(p, "Expected a piece at (2,0)");
        assertEquals(Type.RAT, p.getType());
        assertEquals(1, p.getPlayer());

        // initial elephant for player2 is at (6,0)
        Piece e = g.getPieceAt(6, 0);
        assertNotNull(e, "Expected a piece at (6,0)");
        assertEquals(Type.ELEPHANT, e.getType());
        assertEquals(2, e.getPlayer());
    }

    @Test
    void testMoveRight() {
        // Test moving a piece to the right
        g.turn = 0; // player1
        g.setPlayerNames("TestPlayer1", "TestPlayer2");

        // R1 starts at (2,0). Move right to (2,1)
        boolean moved = g.movePiece("R", "R"); // will target "R1" because turn=0
        assertTrue(moved, "Expected R1 to be able to move right into (2,1)");

        assertNull(g.getPieceAt(2, 0), "Original square should be empty after move");
        Piece p = g.getPieceAt(2, 1);
        assertNotNull(p);
        assertEquals(Type.RAT, p.getType());
        assertEquals(1, p.getPlayer());
    }

    @Test
    void testInvalidOutOfBoundsMove() {
        // Test moving a piece out of bounds
        g.turn = 0; // player1
        g.setPlayerNames("TestPlayer1", "TestPlayer2");

        // L1 (lion) starts at (0,0). Moving up should be out of bounds.
        boolean moved = g.movePiece("L", "U");
        assertFalse(moved, "Moving out of bounds should fail");
        Piece l = g.getPieceAt(0, 0);
        assertNotNull(l);
        assertEquals(Type.LION, l.getType());
    }

    @Test
    void testLeopardCannotEnterRiver() {
        // Test that Leopard cannot enter river squares
        g.turn = 0; // player1
        g.setPlayerNames("TestPlayer1", "TestPlayer2");

        // P1 (Leopard) starts at (2,2). Moving down should cross the river.
        boolean movedToRiver = g.movePiece("P", "D"); 
        assertFalse(movedToRiver, "Leopard should not be able to enter river");
        
        Piece stillThere = g.getPieceAt(2, 2);
        assertNotNull(stillThere, "Leopard should remain at original position");
        assertEquals(Type.LEOPARD, stillThere.getType());
    }


    @Test
    void testLionCannotJumpOverRatInRiver() {
        // Test that Lion cannot jump over river when there's a rat in the river
        g.turn = 0; // player1
        g.setPlayerNames("TestPlayer1", "TestPlayer2");

        g.movePiece("R", "D");
        g.turn = 0;
        g.movePiece("R", "R");
        // Now Rat is at (3,1) in the river
        // Try to make Lion jump over the river from (2,1) to (6,1)
        g.turn = 0;
        g.movePiece("D", "R");
        g.turn = 0;
        g.movePiece("L", "R");
        g.turn = 0;
        g.movePiece("L", "D");
        g.turn = 0;
        g.movePiece("L", "D");
        g.turn = 0;
        boolean moved = g.movePiece("L", "D");
        assertFalse(moved, "Lion should not be able to jump over river with rat in it");

        // Verify Lion is still at original position
        Piece lion = g.getPieceAt(2, 1);
        assertNotNull(lion, "Lion should remain at original position");
        assertEquals(Type.LION, lion.getType());
    }

    @Test
    void testTigerCanJumpOverRiverWithoutRat() {
        // Test that Tiger can jump over river when there's no rat in the river
        g.turn = 0; // player1
        g.setPlayerNames("TestPlayer1", "TestPlayer2");

        // Try to make Tiger jump over the river (from (2,5) to (6,5))
        g.movePiece("C", "R");
        g.turn = 0;
        g.movePiece("T", "L");
        g.turn = 0;
        g.movePiece("T", "D");
        g.turn = 0;
        g.movePiece("T", "D");
        g.turn = 0;
        boolean moved = g.movePiece("T", "D");
        assertTrue(moved, "Tiger should not be able to jump over river with rat in it");

        // Verify Tiger is still at original position
        Piece tiger = g.getPieceAt(6, 5);
        assertNotNull(tiger, "Tiger should remain at original position");
        assertEquals(Type.TIGER, tiger.getType());
    }

    @Test
    void testTurnAndCurrentPlayer() {
        // Test that turn increments and current player switches correctly
        g.setPlayerNames("TestPlayer1", "TestPlayer2");

        g.turn = 0;
        assertEquals("TestPlayer1", g.getCurrentPlayer());
        g.turn = 1;
        assertEquals("TestPlayer2", g.getCurrentPlayer());
    }

    @Test
    void testTurnIncrementsAfterSuccessfulMove() {
        // Test that turn increments after a successful move
        g.turn = 0; // player1
        int initialTurn = g.turn;

        boolean moved = g.movePiece("R", "R");
        if (moved) {
            assertEquals(initialTurn + 1, g.turn, "Turn should increment after successful move");
        }
    }

    @Test
    void testCannotMoveWhenNotYourTurn() {
        // Test that a player cannot move opponent's pieces
        g.turn = 1; // player2
        
        boolean moved = g.movePiece("R", "R");
        assertFalse(moved); // just ensure no exception occurs
    }

    @Test
    void testMoveAddsRecord() {
        // Test that a move is recorded in Records
        g.start(); // initialize Records
        boolean move = g.movePiece("R", "R");
        assertTrue(move, "Expected move to succeed");
        assertEquals(1, g.getGameRecord().getRecords().size(), "One move should be recorded");
    }

    @Test
    void testInvalidPieceKeyFalse() {
        // Test that moving a non-existent piece returns false
        g.turn = 0;
        boolean moved = g.movePiece("Z", "R"); // no such key
        assertFalse(moved, "Moving a non-existent piece should return false");
    }

    @Test
    void testNoWinnerInitially() {
        // Test that there is no winner at the start of the game
        g.setPlayerNames("TestPlayer1", "TestPlayer2");
        // should be no winner at the start of the game
        assertNull(g.getWinner(), "No winner at game start");
    }

    @Test
    void testWinnerByDenCapture() {
        g.setPlayerNames("TestPlayer1", "TestPlayer2");
        // put player1's rat to player2's den
        // player2 should be the winner
        g.boardPieces[0][3] = new Piece(Type.RAT, 2, 0, 3);
        assertEquals("TestPlayer2", g.getWinner(), "Player2 should win when occupying opponent den");
    }

    @Test
    void testWinnerByNoPiecesRemaining() {
        g.setPlayerNames("TestPlayer1", "TestPlayer2");
        //clear all pieces of player1
        // player2 should be the winner
        g.piece1.clear();
        assertEquals("TestPlayer2", g.getWinner(), "Player2 should win when Player1 has no pieces left");
    }
}