package org.COMP3211;
import org.COMP3211.Piece;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


class TestPiece{

    @Test
    void testPieceBasics() {
        Piece rat = new Piece(Type.RAT, 1, 2, 0);
        assertNotNull(rat);
        assertEquals(Type.RAT, rat.getType());
        assertEquals(2, rat.getRow());
        assertEquals(0, rat.getCol());
        assertTrue(rat.canSwim(), "Rat should be able to swim");
        assertEquals(1, rat.getRank());

        Piece elephant = new Piece(Type.ELEPHANT,2, 6, 6);
        assertFalse(elephant.canSwim(), "Elephant cannot swim");
        assertEquals(8, elephant.getRank());
    }

    @Test
    void testJumpAndSwimFlags() {
        Piece tiger = new Piece(Type.TIGER, 1, 3, 1);
        Piece lion = new Piece(Type.LION, 1, 3, 2);
        assertTrue(tiger.canJumpOverRiver(), "Tiger can jump over river");
        assertTrue(lion.canJumpOverRiver(), "Lion can jump over river");
        assertFalse(tiger.canSwim(), "Tiger cannot swim");
    }

    @Test
    void testSetInWaterAndCapturedBehaviour() {
        Piece p = new Piece(Type.CAT, 1, 4, 3);
        // initial position is not river
        assertFalse(Board.isRiver(p.getRow(), p.getCol()));
        // move piece into a river cell and verify it's on a river square
        p.setCurrentPosition(4, 2);
        assertTrue(Board.isRiver(p.getRow(), p.getCol()));
        // CAT cannot swim
        assertFalse(p.canSwim());

        p.setCaptured(true);
        assertTrue(p.isCaptured());
        assertEquals(-1, p.getRow(), "Captured piece row should be -1");
        assertEquals(-1, p.getCol(), "Captured piece col should be -1");
    }
}