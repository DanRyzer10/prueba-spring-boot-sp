package com.danam.iroute.routest.controllers;

import com.danam.iroute.routest.services.FileCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
     @Autowired
    private FileCsvService fileCsvService;
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
        if(file==null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha seleccionado un archivo");
        }

        var transactionResponse = fileCsvService.save(file);
        if(transactionResponse==null){
            return ResponseEntity.status(503).body("No se ha podido guardar el archivo");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
    }
    @PostMapping("/separate")
    public ResponseEntity<?> separateRegisters(@RequestParam("processDate") String processDate){
        if(processDate==null || processDate.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha seleccionado una fecha");
        }
        var transactionResponse = fileCsvService.SeparateRegisters(processDate);
        if(transactionResponse==-1){
            return ResponseEntity.status(503).body("No se ha podido separar los registros");
        }
        String message = "Se han separado "+transactionResponse+" registros";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/quarantine")
    public ResponseEntity<?> getAllFromCommerceQuarantine(){
        var transactionResponse = fileCsvService.getAllFromCommerceQuarantine();
        if(transactionResponse==null){
            return ResponseEntity.status(503).body("No se ha podido obtener los registros");
        }

        return ResponseEntity.status(HttpStatus.OK).body(transactionResponse);
    }


}
