package org.COMP3211;

public enum Type {
    RAT(1, 'R'), CAT(2, 'C'), DOG(3, 'D'), WOLF(4, 'W'), LEOPARD(5, 'P'), TIGER(6, 'T'), LION(7, 'L'), ELEPHANT(8, 'E');

    private final int rank;
    private final char symbol;

    Type(int r, char s) { this.rank = r; this.symbol = s; }

    public int getRank() { return rank; }
    public char getSymbol() { return symbol; }
}