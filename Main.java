package numbers;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Amazing Numbers!\n\n");
        System.out.println("Supported requests:");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter two natural numbers to obtain the properties of the list:");
        System.out.println("  * the first parameter represents a starting number;");
        System.out.println("  * the second parameter show how many consecutive numbers are to be processed;");
        System.out.println("- two natural numbers and properties to search for;");
        System.out.println("- a property preceded by minus must not be present in numbers;");
        System.out.println("- separate the parameters with one space;");
        System.out.println("- enter 0 to exit.");

        String numberLine;
        String[] numbers;

        long firstPar;

        do {
            System.out.print("\nEnter a request: ");
            numberLine = scanner.nextLine();
            numbers = numberLine.split(" ");
            firstPar = isNumeric(numbers[0]);

            String [] propertyPresentOld = {};
            String [] propertyNotPresentOld = {};

            if (numbers.length >= 3) {
                for (int i = 2; i < numbers.length; i++) {
                    if (numbers[i].charAt(0) == '-') {
                        propertyNotPresentOld = Arrays.copyOf(propertyNotPresentOld, propertyNotPresentOld.length + 1);
                        propertyNotPresentOld[propertyNotPresentOld.length - 1]
                                = numbers[i].substring(1).toUpperCase(); // Assign property to the last element
                    } else {
                        propertyPresentOld = Arrays.copyOf(propertyPresentOld, propertyPresentOld.length + 1);
                        propertyPresentOld[propertyPresentOld.length - 1]
                                = numbers[i].toUpperCase();
                    }
                }
            }

            HashSet<String> inputSet1 = new HashSet<>(Arrays.asList(propertyPresentOld));
            String[] propertyPresent = new String[inputSet1.size()];
            inputSet1.toArray(propertyPresent);

            HashSet<String> inputSet2 = new HashSet<>(Arrays.asList(propertyNotPresentOld));
            String[] propertyNotPresent = new String[inputSet2.size()];
            inputSet2.toArray(propertyNotPresent);

            if (firstPar < 0) {
                System.out.println("The first parameter should be a natural number or zero.");
            } else if (numbers.length >= 2 && isNumeric(numbers[1]) <= 0) {
                System.out.println("The second parameter should be a natural number.");
            } else if (numbers.length >= 3
                    && (allValidProperties(propertyPresent).length != 0
                    || allValidProperties(propertyNotPresent).length != 0)) {
                String[] allInvalidProperties = allValidProperties(propertyPresent);
                String[] invalidPropertiesNotPresent = allValidProperties(propertyNotPresent);

                for (String string : invalidPropertiesNotPresent) {
                    allInvalidProperties
                            = Arrays.copyOf(allInvalidProperties, allInvalidProperties.length + 1);
                    allInvalidProperties[allInvalidProperties.length - 1]
                            = "-".concat(string);
                }
                if (allInvalidProperties.length == 1) {
                    System.out.printf("The property [%s] is wrong.%n", Arrays.toString(allInvalidProperties));
                } else {System.out.printf("The properties [%s] are wrong.%n", Arrays.toString(allInvalidProperties));
                }
                System.out.printf("Available properties: %s%n", Arrays.toString(VALID_PROPERTIES));
            } else if (numbers.length >= 4
                    && someMutuallyExclusive(propertyPresent).length != 0) {
                System.out.printf("The request contains mutually exclusive properties: %s%n",
                            Arrays.toString(someMutuallyExclusive(propertyPresent)));
                System.out.printf("There are no numbers with these properties.%n");
            } else if (numbers.length >= 4
                    && someMutuallyExclusiveNegative(propertyNotPresent).length != 0) {
                System.out.printf("The request contains mutually exclusive properties: %s%n",
                        Arrays.toString(someMutuallyExclusiveNegative(propertyNotPresent)));
                System.out.printf("There are no numbers with these properties.%n");
            } else if (numbers.length >= 4
                    && hasMutuallyExclusivePositiveNegative(propertyPresent, propertyNotPresent).length != 0) {
                System.out.printf("The request contains mutually exclusive properties: %s%n",
                        Arrays.toString(hasMutuallyExclusivePositiveNegative(propertyPresent, propertyNotPresent)));
                System.out.printf("There are no numbers with these properties.%n");
            } else if (numbers.length == 1 && firstPar > 0) {
                properties(firstPar);
            } else if (numbers.length == 2 && firstPar > 0 && isNumeric(numbers[1]) > 0) {
                for (int i = 0; i < isNumeric(numbers[1]); i++) {
                    checkProperties(firstPar + i);
                }
            } else if (numbers.length >= 3 && firstPar > 0 && isNumeric(numbers[1]) > 0) {
                int control = 0;
                int i = 0;

                while (control < isNumeric(numbers[1])) {
                    if ((checkSpecificProperties(firstPar + i, propertyPresent)
                            && propertyNotPresent.length >= 1
                            && !checkSpecificProperties(firstPar + i, propertyNotPresent))
                            || (checkSpecificProperties(firstPar + i, propertyPresent)
                            && propertyNotPresent.length == 0)) {
                        checkProperties(firstPar + i);
                        control++;
                    }
                    i++;
                }
            }
        } while (firstPar != 0);

        System.out.println("\nGoodbye!");
    }

    static long isNumeric(String number) {
        if (number == null) return -1;
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            return -1;
        }

    }

    final static String[] VALID_PROPERTIES = {"BUZZ", "DUCK", "PALINDROMIC", "GAPFUL", "SPY", "SUNNY", "SQUARE",
            "JUMPING", "EVEN", "ODD", "HAPPY", "SAD"};
    final static String[] MUTUAL_EXCLUSIVE_1 = {"EVEN", "ODD"};
    final static String[] MUTUAL_EXCLUSIVE_2 = {"DUCK", "SPY"};
    final static String[] MUTUAL_EXCLUSIVE_3 = {"SUNNY", "SQUARE"};
    final static String[] MUTUAL_EXCLUSIVE_4 = {"HAPPY", "SAD"};

    static String[] allValidProperties(String[] solicitedProperties) {
        String[] invalidProperties = {};

        for (String property : solicitedProperties) {
            if (!isValidProperty(property)) {
                invalidProperties = Arrays.copyOf(invalidProperties, invalidProperties.length + 1);
                invalidProperties[invalidProperties.length - 1] = property; // Assign property to the last element
            }
        }
        return invalidProperties;
    }

    static boolean isValidProperty(String solicitedProperty) {
        return Arrays.asList(VALID_PROPERTIES).contains(solicitedProperty.toUpperCase());
    }

    static String[] someMutuallyExclusive(String[] solicitedProperties) {
        String[] mutuallyExclusiveProperties = {};
        int mutuallyExclusiveOne = 0;
        int mutuallyExclusiveTwo = 0;
        int mutuallyExclusiveThree = 0;
        int mutuallyExclusiveFour = 0;

        for (String property : solicitedProperties) {
            if (isMutuallyExclusive(1, property)) {
                mutuallyExclusiveOne++;
                if (mutuallyExclusiveOne == 2) {
                    return MUTUAL_EXCLUSIVE_1;
                }
            } else if (isMutuallyExclusive(2, property)) {
                mutuallyExclusiveTwo++;
                if (mutuallyExclusiveTwo == 2) {
                    return MUTUAL_EXCLUSIVE_2;
                }
            } else if (isMutuallyExclusive(3, property)) {
                mutuallyExclusiveThree++;
                if (mutuallyExclusiveThree == 2) {
                    return  MUTUAL_EXCLUSIVE_3;
                }
            } else if (isMutuallyExclusive(4, property)) {
                mutuallyExclusiveFour++;
                if (mutuallyExclusiveFour == 2) {
                    return  MUTUAL_EXCLUSIVE_4;
                }
            }
        }

        return mutuallyExclusiveProperties;
    }

    static String[] someMutuallyExclusiveNegative(String[] solicitedProperties) {
        String[] mutuallyExclusiveProperties = {};
        int mutuallyExclusiveOne = 0;
        int mutuallyExclusiveFour = 0;
        String[] mE1 =  {"-EVEN", "-ODD"};
        String[] mE4  = {"-HAPPY", "-SAD"};

        for (String property : solicitedProperties) {
            if (isMutuallyExclusive(1, property)) {
                mutuallyExclusiveOne++;
                if (mutuallyExclusiveOne == 2) {
                    return mE1;
                }
            }  else if (isMutuallyExclusive(4, property)) {
                mutuallyExclusiveFour++;
                if (mutuallyExclusiveFour == 2) {
                    return  mE4;
                }
            }
        }

        return mutuallyExclusiveProperties;
    }


    static boolean isMutuallyExclusive(int mutualExclusiveProperties, String solicitedProperties) {

        if (mutualExclusiveProperties == 1) {
            return Arrays.asList(MUTUAL_EXCLUSIVE_1).contains(solicitedProperties.toUpperCase());
        } else if (mutualExclusiveProperties == 2) {
            return Arrays.asList(MUTUAL_EXCLUSIVE_2).contains(solicitedProperties.toUpperCase());
        } else if (mutualExclusiveProperties == 3) {
            return Arrays.asList(MUTUAL_EXCLUSIVE_3).contains(solicitedProperties.toUpperCase());
        } else if (mutualExclusiveProperties == 4) {
            return Arrays.asList(MUTUAL_EXCLUSIVE_4).contains(solicitedProperties.toUpperCase());
        }

        return false;
    }

    static String[] hasMutuallyExclusivePositiveNegative(String[] positiveProperties, String[] negativeProperties) {
        String[] mutuallyExclusivePN = {};
        for (String string : negativeProperties) {
            if (Arrays.asList(positiveProperties).contains(string)) {
                mutuallyExclusivePN = Arrays.copyOf(mutuallyExclusivePN, mutuallyExclusivePN.length + 2);
                mutuallyExclusivePN[mutuallyExclusivePN.length - 2] = string;
                mutuallyExclusivePN[mutuallyExclusivePN.length - 1] = "-".concat(string);
            }
        }

        return mutuallyExclusivePN;
    }

    static void properties(long number) {
        System.out.printf("Properties of %d%n", number);
        System.out.printf("        buzz: %b%n", isBuzz(number));
        System.out.printf("        duck: %b%n", isDuck(number));
        System.out.printf(" palindromic: %b%n", isPalindromic(number));
        System.out.printf("      gapful: %b%n", isGapful(number));
        System.out.printf("         spy: %b%n", isSpy(number));
        System.out.printf("       sunny: %b%n", isSquare( number + 1));
        System.out.printf("      square: %b%n", isSquare(number));
        System.out.printf("     jumping: %b%n", isJumping(number));
        System.out.printf("       happy: %b%n", isHappy(number));
        System.out.printf("         sad: %b%n", !isHappy(number));
        System.out.printf("        even: %b%n", isEven(number));
        System.out.printf("         odd: %b%n", !isEven(number));
    }

    static void checkProperties(long number) {
        boolean oneTrue;
        String isB = isBuzz(number) ? "buzz" : "";
        oneTrue = isBuzz(number);
        String isD = isDuck(number) ? (oneTrue ? ", duck" : "duck") : "";
        oneTrue = isDuck(number) || oneTrue;
        String isP = isPalindromic(number) ? (oneTrue ? ", palindromic" : "palindromic") : "";
        oneTrue = isPalindromic(number) || oneTrue;
        String isG = isGapful(number) ? (oneTrue ? ", gapful" : "gapful") : "";
        oneTrue = isGapful(number) || oneTrue;
        String isS = isSpy(number) ? (oneTrue ? ", spy" : "spy") : "";
        oneTrue = isSpy(number) || oneTrue;
        String isSu = isSquare(number + 1) ? (oneTrue ? ", sunny" : "sunny") : "";
        oneTrue = isSquare(number + 1) || oneTrue;
        String isSq = isSquare(number) ? (oneTrue ? ", square" : "square") : "";
        oneTrue = isSquare(number) || oneTrue;
        String isJ = isJumping(number) ? (oneTrue ? ", jumping" : "jumping") : "";
        oneTrue = isJumping(number) || oneTrue;
        String isE = isEven(number) ? (oneTrue ? ", even" : "even") : (oneTrue ? ", odd" : "odd");
        String isH = isHappy(number) ? ", happy" : ", sad";


        System.out.printf("%d is %s%s%s%s%s%s%s%s%s%s%n", number, isB, isD, isP, isG, isS, isSu, isSq, isJ, isE, isH);
    }

    static boolean checkSpecificProperties(long number, String[] properties) {
        int numberHasProperty = 0;
        int numberAllProperties = properties.length;

        for (String property : properties) {
            switch (property.toLowerCase()) {
                case "buzz":
                    if (isBuzz(number)) {
                        numberHasProperty++;
                    }
                    break;
                case "duck":
                    if (isDuck(number)) {
                        numberHasProperty++;
                    }
                    break;
                case "palindromic":
                    if (isPalindromic(number)) {
                        numberHasProperty++;
                    }
                    break;
                case "gapful":
                    if (isGapful(number)) {
                        numberHasProperty++;
                    }
                    break;
                case "spy":
                    if (isSpy(number)) {
                        numberHasProperty++;
                    }
                    break;
                case "sunny":
                    if(isSquare(number + 1)) {
                        numberHasProperty++;
                    }
                    break;
                case "square":
                    if(isSquare(number)) {
                        numberHasProperty++;
                    }
                    break;
                case "jumping":
                    if(isJumping(number)) {
                        numberHasProperty++;
                    }
                    break;
                case "even":
                    if (isEven(number)) {
                        numberHasProperty++;
                    }
                    break;
                case "odd":
                    if (!isEven(number)) {
                        numberHasProperty++;
                    }
                    break;
                case "happy":
                    if (isHappy(number)) {
                        numberHasProperty++;
                    }
                    break;
                case "sad":
                    if (!isHappy(number)) {
                        numberHasProperty++;
                    }
                    break;
                default:
                    break;
            }
        }

        return numberAllProperties == numberHasProperty;
    }

    static boolean isEven(long number) {

        return number % 2 == 0;
    }

    static boolean isBuzz(long number) {
       return number % 7 == 0 || number % 10 == 7;
    }

    static boolean isDuck(long number) {
        long control = number;

        while (control != 0) {
            if (control % 10 == 0) {
                return true;
            }
            control = control / 10;
        }
        return false;
    }

    static boolean isPalindromic(long number) {
        long control = number;
        long currDigit;
        long reverseNumber = 0;

        do {
            currDigit = control % 10; // takes the last digit
            reverseNumber = (reverseNumber * 10) + currDigit; // creates the reverse number
            control = control / 10; // creates new number
        } while (control != 0);

        return number == reverseNumber;
    }

    static boolean isGapful(long number) {

        if (number < 100) {
            return false;
        }

        long firstDigit = number % 10;
        long control = number / 10;
        long lastDigit = 0;

        while (control != 0) {
            lastDigit = control % 10;
            control = control / 10;
        }

        long gapful = lastDigit * 10 + firstDigit;

        return number % gapful == 0;
    }

    static boolean isSpy(long number) {
        long multiplication = 1;
        long addition = 0;
        long currDigit;
        long control = number;

        while (control != 0) {
            currDigit = control % 10;
            multiplication *= currDigit;
            addition += currDigit;
            control /= 10;
        }

        return addition == multiplication;
    }

    static boolean isSquare(long number) {
        long squareRoot = (long) Math.sqrt(number);
        long compareNumber = squareRoot * squareRoot;

        return compareNumber == number;
    }

    static boolean isJumping(long number) {
        long control = number;
        long currDigit;
        long previousDigit;

        currDigit = control % 10;
        control /= 10;

        while (control != 0) {
            previousDigit = currDigit;
            currDigit = control % 10;
            if (!(currDigit == previousDigit + 1 || currDigit == previousDigit - 1)) {
                return false;
            }
            control /= 10;
        }
        return true;
    }

    static boolean isHappy(long number) {
        long control = number;
        long secondControl = control;
        long newNumber = 0;
        long currDigit;

        while (control != 0) {
            newNumber = 0;
            while (secondControl != 0) {
                currDigit = secondControl % 10;
                newNumber += Math.pow(currDigit, 2);
                secondControl /= 10;
            }
            control = newNumber;
            secondControl = newNumber;
            control /= 10;
        }

        return  newNumber == 1;
    }
}
