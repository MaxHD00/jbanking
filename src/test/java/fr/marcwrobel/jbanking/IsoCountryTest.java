package fr.marcwrobel.jbanking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for the {@link IsoCountry} class.
 *
 * @author Marc Wrobel
 */
public class IsoCountryTest {

  private static final String COUNTRY_FILE_URL =
      "http://www.iso.org/iso/home/standards/country_codes/country_names_and_code_elements_txt.htm";

  private static final InputSupplier<? extends InputStream> ISO_FILE_SUPPLIER =
      new InputSupplier<InputStream>() {
        @Override
        public InputStream getInput() throws IOException {
          return new URL(COUNTRY_FILE_URL).openStream();
        }
      };

  private static final Splitter ISO_FILE_SPLITTER =
      Splitter.on(";").omitEmptyStrings().trimResults();

  @Test
  public void fromCodeAllowsNull() {
    assertNull(IsoCountry.fromCode(null));
  }

  @Test
  public void fromCodeAllowsUnknownOrInvalidCodes() {
    assertNull(IsoCountry.fromCode("XXX"));
  }

  @Test
  public void fromCodeIsNotCaseSensitive() {
    assertEquals(IsoCountry.FRANCE, IsoCountry.fromCode(IsoCountry.FRANCE.getCode().toLowerCase()));
  }

  @Test
  public void fromCodeWorksWithExistingValues() {
    for (IsoCountry country : IsoCountry.values()) {
      assertEquals(country, IsoCountry.fromCode(country.getCode()));
    }
  }

  @Test
  @Ignore("The external file this test depends on is no longer available")
  public void ensureEnumCompleteness() throws IOException {
    InputSupplier<InputStreamReader> readerSupplier =
        CharStreams.newReaderSupplier(ISO_FILE_SUPPLIER, Charsets.UTF_8);

    for (String line : CharStreams.readLines(readerSupplier)) {
      List<String> elements = Lists.newArrayList(ISO_FILE_SPLITTER.split(line));
      if (elements.size() != 2) {
        continue;
      }

      String code = elements.get(1).toUpperCase();
      if (code.length() != 2) {
        continue;
      }

      assertNotNull(IsoCountry.fromCode(code));
    }
  }
}
