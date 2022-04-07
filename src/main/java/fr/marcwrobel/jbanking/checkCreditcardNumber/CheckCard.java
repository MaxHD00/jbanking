package fr.marcwrobel.jbanking.checkCreditcardNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CheckCard {

  public CheckCard() {}

  public boolean validate(long longToValidate) {
    String stringToValidate = Long.toString(longToValidate);

    List<Pattern> patternList = new ArrayList<>();

    patternList.add(Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$")); // Visa
    patternList.add(Pattern.compile("^(5[1-5][0-9]{14}|2(22[1-9][0-9]{12}|2[3-9][0-9]{13}|[3-6][0-9]{14}|7[0-1][0-9]{13}|720[0-9]{12}))$")); // MasterCard
    patternList.add(Pattern.compile("^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$")); // Discover
    patternList.add(Pattern.compile("^3[47][0-9]{13}$")); // Amex
    patternList.add(Pattern.compile("^3(?:0[0-5]|[68][0-9])[0-9]{11}$")); // Diners
    patternList.add(Pattern.compile("^(?:2131|1800|35\\d{3})\\d{11}$")); //JCB

    for (Pattern pattern : patternList)
      if (pattern.matcher(stringToValidate).matches()) return checksumValidation(stringToValidate);
    return false;
  }

  private boolean checksumValidation(String number) {
    int sum = 0;
    boolean alternate = false;
    for (int i = number.length() - 1; i >= 0; i--) {
      int n = Integer.parseInt(number.substring(i, i + 1));
      if (alternate) {
        n *= 2;
        if (n > 9)
          n = (n % 10) + 1;
      }
      sum += n;
      alternate = !alternate;
    }
    return (sum % 10 == 0);
  }


  String validateWithVendor(long numberToValidate) {
    String stringToValidate = Long.toString(numberToValidate);

    Pattern visaPattern = Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$");
    Pattern masterCardPattern = Pattern.compile("^(5[1-5][0-9]{14}|2(22[1-9][0-9]{12}|2[3-9][0-9]{13}|[3-6][0-9]{14}|7[0-1][0-9]{13}|720[0-9]{12}))$");
    Pattern discoverPattern = Pattern.compile("^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$");
    Pattern amexPattern = Pattern.compile("^3[47][0-9]{13}$");
    Pattern dinersPattern = Pattern.compile("^3(?:0[0-5]|[68][0-9])[0-9]{11}$");
    Pattern jcbPattern = Pattern.compile("^(?:2131|1800|35\\d{3})\\d{11}$");

    if (visaPattern.matcher(stringToValidate).matches()) return "Visa";
    if (masterCardPattern.matcher(stringToValidate).matches()) return "MasterCard";
    if (discoverPattern.matcher(stringToValidate).matches()) return "Discover";
    if (amexPattern.matcher(stringToValidate).matches()) return "American Express";
    if (dinersPattern.matcher(stringToValidate).matches()) return "Diners";
    if (jcbPattern.matcher(stringToValidate).matches()) return "JCB";
    return "This Card is Not Valid";
  }
}
