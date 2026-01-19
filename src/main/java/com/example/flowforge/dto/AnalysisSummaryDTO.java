package com.example.flowforge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnalysisSummaryDTO {
    private String description;
    private int totalDepth;
}
