package org.COMP3211;

public class Player {
   private String playerName;
   private final boolean isFirstPlayer;

   public Player(String playerName, boolean isFirstPlayer) {
       this.playerName = playerName;
       this.isFirstPlayer = isFirstPlayer;
   }

   public boolean isFirstPlayer() {
       return isFirstPlayer;
   }

   public void setPlayerName(String playerName) {
       this.playerName = playerName;
   }

   public String getPlayerName() {
       return playerName;
   }
}