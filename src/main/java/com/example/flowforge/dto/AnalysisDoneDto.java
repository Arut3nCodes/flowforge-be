package com.example.flowforge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnalysisDoneDto {

    private String event;
    private boolean ok;
    private String description;

    @JsonProperty("session_id")
    private int sessionId;

    @JsonProperty("jobs_count")
    private int jobsCount;

    @JsonProperty("totalDepth")
    private int totalDepth;

    @JsonProperty("pdf_filename")
    private String pdfFilename;

    @JsonProperty("pdf_size_bytes")
    private int pdfSizeBytes;

    @JsonProperty("pdf_b64")
    private String pdfB64;
}