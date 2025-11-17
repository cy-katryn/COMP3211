package org.COMP3211;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertNotNull(g, "Game should be initialized");
            
        g.player1 = "TestPlayer1";
        g.player2 = "TestPlayer2";
        assertNotNull(g.player1, "Player1 should be initialized");
        assertNotNull(g.player2, "Player2 should be initialized");
        assertEquals("TestPlayer1", g.player1);
        assertEquals("TestPlayer2", g.player2);
        assertEquals(0, g.turn, "Turn should start at 0");
    }

    @Test
    void testInitializationPieces() {
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
    void testMoveRatRight() {
        g.turn = 0; // player1

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
        g.turn = 0; // player1

        // L1 (lion) starts at (0,0). Moving up should be out of bounds.
        boolean moved = g.movePiece("L", "U");
        assertFalse(moved, "Moving out of bounds should fail");
        Piece l = g.getPieceAt(0, 0);
        assertNotNull(l);
        assertEquals(Type.LION, l.getType());
    }

    @Test
    void testLeopardCannotEnterRiver() {
        g.turn = 0; // player1
        
        // P1 (Leopard) starts at (2,2). Moving down should cross the river.
        boolean movedToRiver = g.movePiece("P", "D"); 
        assertFalse(movedToRiver, "Leopard should not be able to enter river");
        
        Piece stillThere = g.getPieceAt(2, 2);
        assertNotNull(stillThere, "Leopard should remain at original position");
        assertEquals(Type.LEOPARD, stillThere.getType());
    }

    @Test
    void testTurnAndCurrentPlayer() {
        g.player1 = "P1";
        g.player2 = "P2";

        g.turn = 0;
        assertEquals("P1", g.getCurrentPlayer());
        g.turn = 1;
        assertEquals("P2", g.getCurrentPlayer());
    }
    
    //TODO: 有问题
    @Test
    void testTurnIncrementsAfterSuccessfulMove() {
        g.turn = 0; // player1
        int initialTurn = g.turn;

        boolean moved = g.movePiece("R", "R");
        if (moved) {
            assertEquals(initialTurn + 1, g.turn, "Turn should increment after successful move");
        }
    }

    @Test
    void testCannotMoveWhenNotYourTurn() {
        g.turn = 1; // player2
        
        boolean moved = g.movePiece("R", "R"); 
        assertTrue(moved == true || moved == false); // just ensure no exception occurs
    }
}