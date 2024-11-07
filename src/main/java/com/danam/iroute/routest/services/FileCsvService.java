package com.danam.iroute.routest.services;

import com.danam.iroute.routest.repositories.FileCsvRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class FileCsvService {
    private final FileCsvRepository fileRepository;
    public FileCsvService(){
        this.fileRepository = new FileCsvRepository();
    }

    public String save(MultipartFile file){
        return fileRepository.saveFile(file);
    }


    public int SeparateRegisters(String processDate){
        return   fileRepository.SeparateRegisters(processDate);
    }

    public List<Map<String, String>> getAllFromCommerceQuarantine(){
        return fileRepository.getAllFromCommerceQuarantine();
    }


}
