/**
 Copyright (c) 2015, Bartłomiej Konieczny
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 1. Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 3. All advertising materials mentioning features or use of this software
 must display the following acknowledgement:
 This product includes software developed by the Bartłomiej Konieczny.
 4. Neither the name of the Bartłomiej Konieczny nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY Bartłomiej Konieczny ''AS IS'' AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL Bartłomiej Konieczny BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.konieczny.bartlomiej.helpers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.konieczny.bartlomiej.model.Action;
import org.konieczny.bartlomiej.model.State;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Utility class which prints data to a calculation sheet.
 * </p>
 * Created by Bartłomiej Konieczny on 2015-11-30.
 */
public class ExcelPrinter {

    private static XSSFWorkbook workbook = new XSSFWorkbook();
    private static XSSFSheet sheet;

    static int rownum = 0;

    /**
     * Saves a simulation data to new sheet.
     * @param id id of a sheet
     */
    public static void createNewRunData(int id){
        //Create a blank sheet
        sheet = workbook.createSheet("Run #" + id);

        rownum = 0;

    }

    /**
     * Creates row with all definied states.
     * @see State
     * @param states definied states
     */
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

    /**
     * Saves the results of a algorithm run.
     * @param result algorithm results
     */
    public static void saveSimulationResult(String result){
        Row simulationRow;
        sheet.createRow(rownum++);
        simulationRow = sheet.createRow(rownum++);

        simulationRow.createCell(0).setCellValue(result);
        sheet.autoSizeColumn(0);
    }

    /**
     * Saves Q-Value array.
     * @param qValues qValue array to save
     */
    public static void saveData(Map<State, Map<Action, Float>> qValues){

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
            Map <Action, Float> actionIntegerMap = qValues.get(state);
            for(Map.Entry <Action, Float> entry : actionIntegerMap.entrySet()){
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
    }

    /**
     * Saves to an file in current filesystem.
     */
    public static void saveToFile() {
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
