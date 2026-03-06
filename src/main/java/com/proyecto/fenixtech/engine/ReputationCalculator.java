package com.proyecto.fenixtech.engine;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ReputationCalculator {


    public record ItemMetrics(Double basePoints, Double eWasteKg, Double co2Kg) {}

    private final Map<String, Map<String, ItemMetrics>> categoriesPoints = new HashMap<>();

    public ReputationCalculator() {
        
        Map<String, ItemMetrics> laptops = new HashMap<>();

        laptops.put("Gama alta", new ItemMetrics(90.00, 2.5, 350.0));
        laptops.put("Gama media", new ItemMetrics(75.00, 2.0, 250.0));
        laptops.put("Gama baja", new ItemMetrics(30.00, 1.8, 150.0));
        categoriesPoints.put("Ordenadores", laptops);
        
        Map<String, ItemMetrics> smartphones = new HashMap<>();
        smartphones.put("Gama alta", new ItemMetrics(85.00, 0.2, 80.0));
        smartphones.put("Gama media", new ItemMetrics(45.00, 0.18, 60.0));
        smartphones.put("Gama baja", new ItemMetrics(20.00, 0.15, 40.0));
        categoriesPoints.put("Smartphones", smartphones);

        Map<String, ItemMetrics> periferics = new HashMap<>();
        periferics.put("Monitores", new ItemMetrics(30.00, 5.0, 150.0));
        periferics.put("Teclados y Ratones", new ItemMetrics(10.00, 0.8, 20.0));
        periferics.put("Audio", new ItemMetrics(12.00, 0.5, 15.0));
        categoriesPoints.put("Perifericos", periferics);

        Map<String, ItemMetrics> servers = new HashMap<>();
        servers.put("Torre", new ItemMetrics(100.00, 15.0, 800.0));
        servers.put("Rack", new ItemMetrics(155.00, 25.0, 1200.0));
        categoriesPoints.put("Servidores", servers);

        Map<String, ItemMetrics> components = new HashMap<>();
        components.put("Almacenamiento", new ItemMetrics(15.00, 0.3, 10.0));
        components.put("Procesadores y Memorias RAM", new ItemMetrics(95.00, 0.1, 5.0));
        components.put("Placas Base", new ItemMetrics(30.00, 0.8, 40.0));
        categoriesPoints.put("Componentes", components);
    }

    public ItemMetrics calculateMetrics(String category, String subcategory, String conditionStatus) {
        Map<String, ItemMetrics> subcategoryMap = categoriesPoints.get(category);


        ItemMetrics metrics = new ItemMetrics(5.00, 0.5, 10.0);
        
        if (subcategoryMap != null) {
            metrics = subcategoryMap.getOrDefault(subcategory, metrics);
        }

       
        Double multiplicador = switch(conditionStatus) {
            case "NEW" -> 2.0;
            case "USED_GOOD" -> 1.5;
            case "USED_FAIR" -> 1.0;
            default -> 1.0;
        };

        return new ItemMetrics(
            metrics.basePoints() * multiplicador,
            metrics.eWasteKg(),
            metrics.co2Kg()
        );
    }
}