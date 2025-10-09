public class InputHandler {
    public enum CommandType { MOVE, QUIT, SAVE, REPLAY, INVALID }

    public static class Move {
        public final int fromR, fromC;
        public final Movement direction;
        
        public Move(int fr, int fc, Movement dir) {
            this.fromR = fr; this.fromC = fc; this.direction = dir;
        }
    }

    public static class Command {
        public final CommandType type;
        public final Move move; // non-null only when type == MOVE
        public final String raw;
        public Command(CommandType t, Move m, String raw) { this.type = t; this.move = m; this.raw = raw; }
    }

    public Command parseInput(String input) {
        input = input.trim();
        if (input == null || input.length() == 0) return new Command(CommandType.INVALID, null, input);

        // process "Quit” command
        if (input.equals("Quit")) {
            return new Command(CommandType.QUIT, null, input);
        }

        // try to parse as movement command
        try {
            Move m = parseMove(input);
            if (m != null) return new Command(CommandType.MOVE, m, input);
        } catch (IllegalArgumentException e) { // TODO: idk abouot this part
           return new Command(CommandType.INVALID, null, input);
        }
        
        return new Command(CommandType.INVALID, null, input);
    }
    
    // Parse input in the format: fromRow fromCol direction
    public Move parseMove(String input) {
        try {
            String[] parts = input.split(" ");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Expected 3 parts as input: fromRow fromCol direction");
            }
            int fromR = Integer.parseInt(parts[0]);
            int fromC = Integer.parseInt(parts[1]);

            if("U".equals(parts[2])) {
                return new Move(fromR, fromC, Movement.UP);
            } else if("D".equals(parts[2])) {
                return new Move(fromR, fromC, Movement.DOWN);
            } else if("L".equals(parts[2])) {
                return new Move(fromR, fromC, Movement.LEFT);
            } else if("R".equals(parts[2])) {
                return new Move(fromR, fromC, Movement.RIGHT);
            }
            throw new IllegalArgumentException("Invalid direction");
            
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter: fromRow fromCol direction");
            return null;
        }
    }
}