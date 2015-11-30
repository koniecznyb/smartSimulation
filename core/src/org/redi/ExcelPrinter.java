package org.redi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;

/**
 * Created by Bartłomiej Konieczny on 2015-11-30.
 */
public class ExcelPrinter {

    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;

    static int rownum = 0;

    public static void createNewRunData(int id){
        //Blank workbook
        workbook = new XSSFWorkbook();

        //Create a blank sheet
        sheet = workbook.createSheet("Run #" + id);

    }

    private static void createStateRow(Set<State> states) {
        //to enable newlines you need set a cell styles with wrap=true
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);

        Row stateRow = sheet.createRow(rownum++);
        stateRow.setHeightInPoints((3*sheet.getDefaultRowHeightInPoints()));

        int cellnum = 0;
        Cell cell = stateRow.createCell(cellnum++);
        for(State state : states ){
            cell = stateRow.createCell(cellnum++);
            cell.setCellStyle(cs);
            cell.setCellValue(state.toString());
            sheet.autoSizeColumn(cellnum-1);
        }
    }

    public static void saveData(Map<State, Map<Action, Integer>> qValues){

        createStateRow(qValues.keySet());

        Row moveDownRow, moveUpRow, moveLeftRow, moveRightRow;
        moveDownRow = sheet.createRow(rownum++);
        moveUpRow = sheet.createRow(rownum++);
        moveLeftRow = sheet.createRow(rownum++);
        moveRightRow = sheet.createRow(rownum++);

        moveDownRow.createCell(0).setCellValue(Action.MOVE_DOWN.toString());
        moveUpRow.createCell(0).setCellValue(Action.MOVE_UP.toString());
        moveLeftRow.createCell(0).setCellValue(Action.MOVE_LEFT.toString());
        moveRightRow.createCell(0).setCellValue(Action.MOVE_RIGHT.toString());

        sheet.autoSizeColumn(0);

        Set<State> states = qValues.keySet();
        for(State state : states){
            Map <Action, Integer> actionIntegerMap = qValues.get(state);
            for(Map.Entry <Action, Integer> entry : actionIntegerMap.entrySet()){
                switch(entry.getKey()){
                    case MOVE_DOWN:{
                        moveDownRow.createCell(state.getStateID()+1).setCellValue(entry.getValue());
                        break;
                    }
                    case MOVE_UP:{
                        moveUpRow.createCell(state.getStateID()+1).setCellValue(entry.getValue());
                        break;
                    }
                    case MOVE_LEFT:{
                        moveLeftRow.createCell(state.getStateID()+1).setCellValue(entry.getValue());
                        break;
                    }
                    case MOVE_RIGHT:{
                        moveRightRow.createCell(state.getStateID()+1).setCellValue(entry.getValue());
                        break;
                    }
                }
            }
        }

        saveToFile();
    }

    private static void saveToFile() {
        try {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("data.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Zapisano dane do pliku data.xlsx");
        }
        catch (FileNotFoundException e){
            System.out.println("Nie można zapisać, plik w otwarciu");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private ExcelPrinter() {
    }
}
