import java.io.*;
import java.util.*;

class StringMod {
  private static Scanner scanner;
  private static String original;
  private static String current;
  private static Stack<String> stages = new Stack<>();

  public static void main(String[] args) {
    scanner = new Scanner(System.in);
    // initialize the String with command line input
    String init = "";
    for (String s : args) {
      init += s + " ";
      //init += " ";
    }
    initString(init);
    displayString();
    // asking user for modifications
    ask();
    String selection = scanner.nextLine();

    while(!selection.equals("q")) {
      switch (selection) {
        case "c":
          options();
          break;
        case "r":
          reverseString();
          break;
        case "l":
          lowerCase();
          break;
        case "u":
          upperCase();
          break;
        case "sub":
          allSubstrings();
          break;
        case "pal":
          isPalindrome();
          break;
        case "longpal":
          longestPalindrome();
          break;
        case "longuni":
          longestUnique();
          break;
        case "b":
          back();
          break;
        case "o":
          origin();
          break;
        default:
          System.out.println("Invalid option!");
          break;
      }
      displayString();
      // keep on asking
      ask();
      selection = scanner.nextLine();
    }

  }

  /*
  initialize the String (set its original state)
  */
  public static void initString (String init) {
    original = init;
    current = init;
    stages.push(init);
  }

  /*
  Displays the String's current stage
  */
  public static void displayString() {
    System.out.println("Current String: " + current);
  }

  public static void ask() {
    System.out.println("How would you like to modify this String? (Press 'c' to see your choices)");
  }

  /*
  Displays options for String modifications
  */
  public static void options() {
    System.out.println("~~~ Here are your modification choices: ~~~");
    System.out.println("\"r\" — Reversal\t‘l’ or \"u\" — Lower/uppercase\t\"sub\" — Print all possible substrings");
    System.out.println("\"pal\" — Check if it’s a palindrome\t\"longpal\" — Print length of longest palindromic substring\t\"longuni\" — Print longest substring of unique chars");
    System.out.println("\"b\" — revert String to previous state\t\"o\" — revert String to original state");
    System.out.println("\"q\" — quit program");
    System.out.println("~~~~~~");
  }

  /*
  reverses the String
  */
  public static void reverseString() {
    String old = current;

    char[] currChar = current.toCharArray();
    int l = 0;
    int r = current.length()-1;

    while (l < r) {
      // swapping chars at left and right pointers
      char temp = currChar[l];
      currChar[l] = currChar[r];
      currChar[r] = temp;

      l++;
      r--;
    }
    current = String.valueOf(currChar);
    if (!current.equals(old)) stages.push(old);
  }

  /*
  makes current String all lower-case
  */
  public static void lowerCase() {
    String old = current;
    current = current.toLowerCase();
    if (!current.equals(old)) stages.push(old);
  }
  /*
  makes current String all upper-case
  */
  public static void upperCase() {
    String old = current;
    current = current.toUpperCase();
    if (!current.equals(old)) stages.push(old);
  }

  /*
  Print all subsets of the current String
  */
  public static void allSubstrings() {
    System.out.println("All substrings of " + current + ":");
    substringHelper(current, "");
    System.out.println();
  }
   /*
   recursive helper method for allSubstrings()
   `soFar` keeps track of the characters currently in the substring being built
   */
  public static void substringHelper(String str, String soFar) {
    if (str.length() == 0) {
      System.out.println(soFar + " "); //base case empty string, print what's added
      return;   //ends method
    }
    //case 1: excludes first character
    substringHelper(str.substring(1), soFar);
    //case 2: includes first character
    substringHelper(str.substring(1), soFar + str.charAt(0));
  }

  /*
  Returns if current String is a palindrome
  */
  public static void isPalindrome() {
    int l = 0;
    int r = current.length()-1;

    while (l <= r) {
      if (current.charAt(l) != current.charAt(r)) {
        System.out.println(current + " IS NOT a palindrome!");
        return;
      }
      l++;
      r--;
    }
    System.out.println(current + " IS a palindrome!");
  }

  /*
  Prints the longest palindromic substring of current String
  */
  public static void longestPalindrome() {
    String old = current;

    /*
    for every letter in String s, we try to "extend" the palindrome and
    return the longest palindromic string we found
    */
    // starting and ending indices of longest palindromic substring
    int start = 0;
    int end = 0;

    for (int i = 0; i < current.length(); i++) {
      char c = current.charAt(i);
      // start the palindrome extension at the current middle
      int left = i;
      int right = i;

      // keep shifting the starting index back if it's the same
      while (left >= 0 && current.charAt(left) == c) {
        left--;
      }
      // keep shifting the ending index forward if it's the same
      while (right < current.length() && current.charAt(right) == c) {
        right++;
      }
      // extending out with both left and right pointers simultaneously
      // for chars that aren't the same as the starting char c
      while (left >= 0 && right < current.length()) {
        if (current.charAt(left) != current.charAt(right)) break;
        left--;
        right++;
      }

      // left+1 and right-1 are actually the start and end indices
      // but we don't subtract 1 from "right" because substring() function's
      // ending index is exclusive
      left++;
      // update the longest palindromic substring
      int currDiff = right - left;
      int maxDiff = end - start;
      if (currDiff > maxDiff) {
        start = left;
        end = right;
      }
    }
    current = current.substring(start, end);
    if (!current.equals(old)) stages.push(old);
  }

  /*
  Return the length of longest substring of unique chars from current String
  */
  public static void longestUnique() {
    Set<Character> set = new HashSet<>();
    int maxLength = 0;
    // pointers to expand window
    int left = 0;
    int right = 0;

    while (right < current.length()) {
      char c = current.charAt(right);
      // unique char
      if (!set.contains(c)) {
        set.add(c);
        right++;
      }
      // seen char before, so remove it from set
      else {
        set.remove(current.charAt(left));
        // move window over (make smaller)
        left++;
      }
      // update max length as necessary
      maxLength = Math.max(maxLength, set.size());
    }
    System.out.println("The length of the longest substring of unique chars is " + maxLength + ".");
  }

  /*
  reverts current String to its previous stage
  if its previous stage is the original, then call origin()
  */
  public static void back() {
    if (stages.size() > 1) {
      current = stages.pop();
    } else {
      origin();
    }
  }

  /*
  reverts current String to its original stage
  clears the stages Stack and have only the original String in it
  */
  public static void origin() {
    current = original;
    stages.clear();
    stages.push(current);
  }
}
