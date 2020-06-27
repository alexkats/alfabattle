package com.katsman.alfabattle.task1.service.impl

import com.katsman.alfabattle.task1.dto.Group
import com.katsman.alfabattle.task1.dto.Item
import com.katsman.alfabattle.task1.service.CsvConverter
import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Component
class CsvConverterImpl : CsvConverter {

    override fun convertCsvRecordToItem(record: CSVRecord): Item {
        return Item(
            id = record[0],
            name = record[1],
            groupId = record[2],
            price = BigDecimal(record[3])
        )
    }

    override fun convertCsvRecordToGroup(record: CSVRecord): Group {
        return Group(
            id = record[0],
            name = record[1]
        )
    }
}