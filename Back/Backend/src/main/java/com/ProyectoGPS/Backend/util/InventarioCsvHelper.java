package com.ProyectoGPS.Backend.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ProyectoGPS.Backend.dto.InventarioUploadRequest;

public class InventarioCsvHelper {
    public static List<InventarioUploadRequest> parseCSV(MultipartFile file) throws Exception {
        List<InventarioUploadRequest> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) { isFirst = false; continue; }
                String[] fields = line.split(",");
                InventarioUploadRequest dto = new InventarioUploadRequest();
                dto.setProductoCodigo(fields[0].trim());
                dto.setCantidadInicial(Integer.valueOf(fields[1].trim()));
                lista.add(dto);
            }
        }
        return lista;
    }
}