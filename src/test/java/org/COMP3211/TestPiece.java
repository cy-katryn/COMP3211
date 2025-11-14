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
    Piece rat = new Piece(Piece.Type.RAT, 2, 0);
    assertNotNull(rat);
    assertEquals(Piece.Type.RAT, rat.getType());
    assertEquals(2, rat.getRow());
    assertEquals(0, rat.getCol());
    assertTrue(rat.canSwim(), "Rat should be able to swim");
    assertEquals(1, rat.getRank());

    Piece elephant = new Piece(Piece.Type.ELEPHANT, 6, 6);
    assertFalse(elephant.canSwim(), "Elephant cannot swim");
    assertEquals(8, elephant.getRank());
  }

  @Test
  void testJumpAndSwimFlags() {
    Piece tiger = new Piece(Piece.Type.TIGER, 3, 1);
    Piece lion = new Piece(Piece.Type.LION, 3, 2);
    assertTrue(tiger.canJumpOverRiver(), "Tiger can jump over river");
    assertTrue(lion.canJumpOverRiver(), "Lion can jump over river");
    assertFalse(tiger.canSwim(), "Tiger cannot swim");
  }

  @Test
  void testSetInWaterAndCapturedBehaviour() {
    Piece p = new Piece(Piece.Type.CAT, 4, 3);
    assertFalse(p.isInWater());
    p.setInWater(true);
    assertTrue(p.isInWater());

    p.setCaptured(true);
    assertTrue(p.isCaptured());
    assertEquals(-1, p.getRow(), "Captured piece row should be -1");
    assertEquals(-1, p.getCol(), "Captured piece col should be -1");
  }
}