package com.katsman.alfabattle.task1.service

import com.katsman.alfabattle.task1.dto.Group
import com.katsman.alfabattle.task1.dto.Item
import org.apache.commons.csv.CSVRecord

interface CsvConverter {

    fun convertCsvRecordToItem(record: CSVRecord): Item

    fun convertCsvRecordToGroup(record: CSVRecord): Group
}