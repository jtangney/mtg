package com.bagbert.mtg.utils;

import org.apache.commons.text.StringEscapeUtils;

import com.bagbert.commons.football.tools.StringUtils;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class FormattingConverter<T> extends AbstractBeanField<T> {

  @Override
  protected Object convert(String value)
      throws CsvDataTypeMismatchException, CsvConstraintViolationException {
    return StringEscapeUtils.unescapeCsv(value);
  }

  protected String convertToWrite(Object value) throws CsvDataTypeMismatchException {
    if (value == null || !(value instanceof String)) {
      return super.convertToWrite(value);
    }
    String trimmed = StringUtils.extTrim((String) value);
    String noNewLines = trimmed.replaceAll("[\\n\\r]", "");
    return StringEscapeUtils.escapeCsv(noNewLines);
  }

}
