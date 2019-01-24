package utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bagbert.commons.football.opencsv.CsvUtils;
import com.bagbert.mtg.utils.FormattingConverter;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class FormattingConverterTest {

  @Test
  public void testWrite() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
    TestObj obj = new TestObj("Hello\n, World ");
    String str = CsvUtils.beanToCsv(obj, TestObj.class);
    System.out.println(str);
    assertTrue(str.contains("\""));
    assertTrue(str.contains("\"Hello, World\""));
  }

  public class TestObj {

    @CsvCustomBindByPosition(position = 0, converter = FormattingConverter.class)
    private String str;

    public TestObj() {
    }

    public TestObj(String str) {
      this.str = str;
    }

    public String getStr() {
      return str;
    }
  }

}
