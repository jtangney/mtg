package com.bagbert.mtg.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class MissingIntegerConverter<T> extends AbstractBeanField<T> {

  @Override
  protected Object convert(String s)
      throws CsvDataTypeMismatchException, CsvConstraintViolationException {
    return null;
  }
}
