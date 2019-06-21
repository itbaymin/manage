package com.byc.dao.support;

import javax.persistence.AttributeConverter;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by baiyc
 * 2019/5/13/013 17:28
 * Description：
 */
public class DateConverter implements AttributeConverter<Date,Long> {
    @Override
    public Long convertToDatabaseColumn(Date attribute) {
        return attribute == null ? 0L : Duration.ofMillis(attribute.getTime()).getSeconds();
    }

    @Override
    public Date convertToEntityAttribute(Long dbData) {
        if(dbData == null || dbData.longValue() == 0) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Duration.ofSeconds(dbData.longValue()).toMillis());
        return calendar.getTime();
    }
}
