package com.ProyectoGPS.Backend.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ProyectoGPS.Backend.dto.PrecioUploadRequest;

public class CsvHelper {
    public static List<PrecioUploadRequest> parseCSV(MultipartFile file) throws Exception {
        List<PrecioUploadRequest> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) { isFirst = false; continue; }  // saltar cabecera
                String[] fields = line.split(",");
                PrecioUploadRequest dto = new PrecioUploadRequest();
                dto.setProductoCodigo(fields[0].trim());
                dto.setPrecioUnitario(new BigDecimal(fields[1].trim()));
                lista.add(dto);
            }
        }
        return lista;
    }
}
