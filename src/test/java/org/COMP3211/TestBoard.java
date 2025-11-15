// java
package org.COMP3211;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TestBoard {

    @Test
    void testTrapValues() {
        // top traps should return 1
        assertEquals(1, Board.isTrap(0, 2), "Expected top trap value 1 at (0,2)");
        assertEquals(1, Board.isTrap(0, 4), "Expected top trap value 1 at (0,4)");
        assertEquals(1, Board.isTrap(1, 3), "Expected top trap value 1 at (1,3)");

        // bottom traps should return 2
        assertEquals(2, Board.isTrap(8, 2), "Expected bottom trap value 2 at (8,2)");
        assertEquals(2, Board.isTrap(8, 4), "Expected bottom trap value 2 at (8,4)");
        assertEquals(2, Board.isTrap(7, 3), "Expected bottom trap value 2 at (7,3)");

        // non-trap
        assertEquals(0, Board.isTrap(4, 3), "Expected no trap at (4,3)");
    }

    @Test
    void testDenValues() {
        assertEquals(1, Board.isDen(0, 3), "Expected top den value 1 at (0,3)");
        assertEquals(2, Board.isDen(8, 3), "Expected bottom den value 2 at (8,3)");
        assertEquals(0, Board.isDen(4, 3), "Expected no den at (4,3)");
    }

    @Test
    void testRiverFlags() {
        // sample river points
        assertTrue(Board.isRiver(3, 1), "Expected river at (3,1)");
        assertTrue(Board.isRiver(5, 5), "Expected river at (5,5)");
        assertTrue(Board.isRiver(4, 2), "Expected river at (4,2)");
        assertTrue(Board.isRiver(5, 1), "Expected river at (5,1)");

        // non-river point
        assertFalse(Board.isRiver(0, 0), "Expected no river at (0,0)");
    }

    @Test
    void testInBounds() {
        assertTrue(Board.inBounds(0, 0));
        assertTrue(Board.inBounds(Board.ROWS - 1, Board.COLS - 1));
        assertFalse(Board.inBounds(-1, 0));
        assertFalse(Board.inBounds(Board.ROWS, 0));
        assertFalse(Board.inBounds(0, Board.COLS));
    }
}