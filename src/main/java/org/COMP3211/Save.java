//package org.COMP3211;
//
//
///**
// * save load file system
// */
//public class Save implements Serializable {
//    /**
//     * the current Save
//     */
//    public static Save Inst;
//    /**
//     * current Game
//     */
//    public Game game = new Game();
//    /**
//     * current turn
//     */
//    public int turn = 0;
//
//    /**
//     * @param path save to path
//     */
//    public static void diskSave(String path) {
//        // try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
//        //     oos.writeObject(Inst);
//        //     Main.output("disk and criteria saved to " + path);
//        // } catch (IOException e) {
//        //     Main.output("error saving disk: " + e.getMessage());
//        // }
//    }
//
//    /**
//     * @param path load from path
//     */
//    public static void diskLoad(String path) {
//        // try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
//        //     Inst = (Save) ois.readObject();
//
//        //     Main.output("loaded from " + path);
//        // } catch (IOException | ClassNotFoundException e) {
//        //     Main.output("error loading disk: " + e.getMessage());
//        // }
//    }
//
//} 

package org.COMP3211;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Save {

    private void writeHeader(PrintWriter writer, Records record) {
        writer.println("----Game Record----");
        writer.println("Generated on: " + LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        writer.println("File Format Version: 1.0");
        writer.println();
    }

    private void writePlayerInfo(PrintWriter writer, Records record) {
        writer.println("== Player Info ==");
        writer.println("Player1: " + record.getPlayer1Name());
        writer.println("Player2: " + record.getPlayer2Name());
        writer.println();
    }

    private void writeGameSettings(PrintWriter writer, Records record) {
        writer.println("== Game ==");
        writer.println("Date: " + record.getGameDate());
        writer.println("StartTime: " + record.getStartTime());
        writer.println();
    }

    private void writeMovementHistory(PrintWriter writer, Records record) {
        writer.println("== Moves ==");
        for (MoveRecord move : record.getMoves()) {
            writer.println(move.toFileFormat());
        }
        writer.println();
    }  

    private void writeGameResult(PrintWriter writer, Records record) {
        writer.println("== Result ==");
        if (record.getWinner() != null) {
            writer.println("Winner: " + record.getWinner());
        } else {
            writer.println("Winner: None, game incomplete");
        }
    }

    //save record to file
    public boolean saveRecord(Records record, String filename){
        if(!filename.endsWith(".record")){
            filename += ".record";
        }   

        try{
            PrintWriter writer = new PrintWriter(new FileWriter(filename)); 
            //write player infos and game data to file
            writeHeader(writer, record);
            writePlayerInfo(writer, record);
            writeGameSettings(writer, record);
            writeGameResult(writer, record);
            writeMovementHistory(writer, record);

            System.out.println("Record successfully saved to: " + filename);
            return true;
        }catch(IOException e){
            System.err.println("Failed to save record: " + e.getMessage());
            return false;

        }
    }

    //load record to replay
    public Records loadRecord(String filename){
        if(!filename.endsWith(".record")){
            filename+=".record";
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            Records record =null;
            boolean movementInfo = false;
            String line;

            while((line = reader.readLine())!=null){
                line = line.trim();
                if(line.isEmpty() || line.startsWith("#")){
                    continue;
                }

                if(line.equals("== Player Info ==")){
                    //match two players
                    String player1 = reader.readLine().split(":")[1].trim();
                    String player2 = reader.readLine().split(":")[1].trim();
                    record = new Records(player1,player2);
                }else if(line.equals("== Moves ==")){
                    movementInfo = true;
                    getMovemets(record, line);
                }else if(line.equals("== Result ==")){
                    movementInfo = false;
                    
                    String winnerLine = reader.readLine();
                    if (winnerLine != null && !winnerLine.contains("*")) {
                        String winner = winnerLine.split(":")[1].trim();
                        String reason = reader.readLine().split(":")[1].trim();
                        record.gameResult(winner);
                    }

                }
            }
            System.out.println("Record successfully loaded from: " + filename);
            return record;
        } catch (IOException e) {
            System.err.println("Failed to load record: " + e.getMessage());
            return null;   
        }
    }

    private void getMovemets(Records record, String line) {

    }


}