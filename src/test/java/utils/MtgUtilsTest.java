package utils;

import com.bagbert.mtg.utils.MtgUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MtgUtilsTest {

  @Test
  public void testFormatCardName() {
    assertEquals("xantcha-sleeper-agent", MtgUtils.formatCardName("Xantcha, Sleeper Agent"));
    assertEquals("sol-ring", MtgUtils.formatCardName("Sol Ring"));
    assertEquals("wars-toll", MtgUtils.formatCardName("War's Toll"));
  }
}
