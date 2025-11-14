package org.COMP3211;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class TestBoard {

  private Board board;

  @BeforeEach
  void setUp() {
    board = new Board();
  }

  @Test
  void testInitialTerrain() {
    // trap checks
    assertTrue(Board.isTrap(0, 2), "Expected trap at (0,2)");
    assertTrue(Board.isTrap(0, 4), "Expected trap at (0,4)");
    assertTrue(Board.isTrap(1, 3), "Expected trap at (1,3)");
    assertTrue(Board.isTrap(8, 2), "Expected trap at (8,2)");
    assertTrue(Board.isTrap(8, 4), "Expected trap at (8,4)");
    assertTrue(Board.isTrap(7, 3), "Expected trap at (7,3)");

    // den checks
    assertTrue(Board.isDen(0, 3), "Expected den at (0,3)");
    assertTrue(Board.isDen(8, 3), "Expected den at (8,3)");

    // river checks (sample points)
    assertTrue(Board.isRiver(3, 1), "Expected river at (3,1)");
    assertTrue(Board.isRiver(5, 5), "Expected river at (5,5)");
    assertTrue(Board.isRiver(4, 2), "Expected river at (4,2)");
    assertTrue(Board.isRiver(5, 1), "Expected river at (5,1)");
  }

  @Test
  void testInBounds() {
    assertTrue(Board.inBounds(0,0));
    assertTrue(Board.inBounds(Board.ROWS - 1, Board.COLS - 1));
    assertFalse(Board.inBounds(-1,0));
    assertFalse(Board.inBounds(Board.ROWS, 0));
    assertFalse(Board.inBounds(0, Board.COLS));
  }
}