package com.danam.iroute.routest.repositories;

import com.danam.iroute.routest.db.config.ConnectionManager;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileCsvRepository {
    public String saveFile(MultipartFile file){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            CSVReader csvReader = new CSVReader(reader);
            Connection conn = ConnectionManager.createConnection();

            CallableStatement statement = conn.prepareCall("{call sp_create_commerce(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            String[] headers = csvReader.readNext();
           extracted(csvReader, statement);
            statement.executeBatch();
            conn.close();
            csvReader.close();
            return "registros ingresados";
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Map<String,String>> getAllFromCommerceQuarantine(){
        try{
            List<Map<String,String>> results =  new ArrayList<>();
            Connection conn = ConnectionManager.createConnection();
            CallableStatement statement = conn.prepareCall("{call sp_get_all_commerce_quarantine()}");
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while(resultSet.next()) {
                Map<String,String> row = new HashMap<>();
                for(int i=1;i<=columnCount;i++){
                    String columName = metaData.getColumnName(i);
                    String columnValue = resultSet.getString(i);
                    row.put(columName,columnValue);
                }
                results.add(row);
            }
            Map<String,String> metadata = new HashMap<>();
            resultSet.close();
            statement.close();
            conn.close();
            return results;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public int SeparateRegisters(String processDate){
        try{
            Connection conn = ConnectionManager.createConnection();
            CallableStatement statement = conn.prepareCall("{call sp_extract_commerce(?,?)}");
            statement.setString(1, processDate );
            statement.registerOutParameter(2, Types.INTEGER);
            statement.execute();
            int result = statement.getInt(2);
            conn.close();
            return result;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }
    }

    private static void  extracted(CSVReader csvReader, CallableStatement statement) throws IOException, CsvValidationException, SQLException {
        String[] csvValues;
        while((csvValues = csvReader.readNext())!=null){
            for(int i = 0;i<csvValues.length;i++){
                statement.setString(i+1,csvValues[i]);
            }
            statement.addBatch();
        }


    }
}
