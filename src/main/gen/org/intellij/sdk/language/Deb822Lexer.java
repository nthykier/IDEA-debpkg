/* The following code was generated by JFlex 1.7.0 tweaked for IntelliJ platform */

// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.sdk.language;

import com.github.nthykier.debpkg.deb822.psi.Deb822Types;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import com.github.nthykier.debpkg.deb822.Deb822LanguageSupport;
import com.github.nthykier.debpkg.deb822.field.KnownFieldTable;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownField;
import com.github.nthykier.debpkg.deb822.field.Deb822KnownFieldValueType;

import java.util.Arrays;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0
 * from the specification file <tt>Deb822.flex</tt>
 */
public class Deb822Lexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int GPG_PARSE_ARMORED_HEADERS = 2;
  public static final int GPG_BEGIN_SIGNATURE_ARMORED_HEADERS = 4;
  public static final int GPG_SIGNATURE_BLOB = 6;
  public static final int SEPARATOR_BEFORE_VALUE = 8;
  public static final int MAYBE_CONT_VALUE = 10;
  public static final int PARSING_VALUE = 12;
  public static final int SEPARATOR_BEFORE_BUILD_PROFILES = 14;
  public static final int MAYBE_CONT_BUILD_PROFILES = 16;
  public static final int PARSING_BUILD_PROFILES = 18;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  1,  2,  3,  2,  4,  5,  6,  7,  7,  8,  9, 10, 10,  7,  7, 
     8,  9, 11, 11
  };

  /** 
   * Translates characters to character classes
   * Chosen bits are [7, 7, 7]
   * Total runtime size is 1928 bytes
   */
  public static int ZZ_CMAP(int ch) {
    return ZZ_CMAP_A[(ZZ_CMAP_Y[ZZ_CMAP_Z[ch>>14]|((ch>>7)&0x7f)]<<7)|(ch&0x7f)];
  }

  /* The ZZ_CMAP_Z table has 68 entries */
  static final char ZZ_CMAP_Z[] = zzUnpackCMap(
    "\1\0\103\200");

  /* The ZZ_CMAP_Y table has 256 entries */
  static final char ZZ_CMAP_Y[] = zzUnpackCMap(
    "\1\0\1\1\53\2\1\3\22\2\1\4\37\2\1\3\237\2");

  /* The ZZ_CMAP_A table has 640 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
    "\11\34\1\15\1\1\1\0\1\3\1\5\22\34\1\2\1\42\1\16\1\4\1\6\5\16\1\35\1\36\1\40"+
    "\1\11\1\14\1\37\12\10\1\13\1\16\1\41\1\37\1\43\2\16\1\30\1\17\1\10\1\26\1"+
    "\20\1\10\1\21\1\10\1\22\3\10\1\27\1\23\1\10\1\24\1\10\1\33\1\25\1\31\1\32"+
    "\5\10\4\16\1\36\1\16\32\10\1\7\1\16\1\12\1\14\6\34\1\0\32\34\1\0\337\34\1"+
    "\0\177\34\13\0\35\34\2\0\5\34\1\0\57\34\1\0\40\34");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\14\0\1\1\1\2\1\3\1\1\1\4\4\1\1\5"+
    "\1\6\1\2\1\1\2\7\1\2\1\10\1\11\1\1"+
    "\1\12\1\13\1\14\2\13\1\15\1\16\1\17\1\20"+
    "\1\21\1\22\7\0\1\7\1\0\1\3\1\23\3\0"+
    "\1\24\1\7\1\0\1\25\1\0\1\7\1\0\1\7"+
    "\1\0\1\7\1\0\1\7\1\0\1\7\60\0\1\26"+
    "\3\0\1\27\4\0\1\30";

  private static int [] zzUnpackAction() {
    int [] result = new int[128];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\44\0\110\0\154\0\220\0\264\0\330\0\374"+
    "\0\u0120\0\u0144\0\u0168\0\u018c\0\u01b0\0\u01d4\0\u01f8\0\u021c"+
    "\0\u0240\0\u0264\0\u0288\0\u02ac\0\u02d0\0\u0288\0\u0288\0\u01b0"+
    "\0\u02f4\0\u0318\0\u033c\0\u0360\0\u01b0\0\u01b0\0\u0384\0\u03a8"+
    "\0\u03cc\0\u01b0\0\u03f0\0\u0414\0\u01b0\0\u01b0\0\u0438\0\u01b0"+
    "\0\u01b0\0\u01b0\0\u021c\0\u045c\0\u0288\0\u02ac\0\u0480\0\u02d0"+
    "\0\u02f4\0\u04a4\0\u0384\0\u01b0\0\u01b0\0\u03a8\0\u04c8\0\u04ec"+
    "\0\u01b0\0\u0510\0\u0534\0\u01b0\0\u0558\0\u057c\0\u05a0\0\u05c4"+
    "\0\u05e8\0\u060c\0\u0630\0\u0654\0\u0678\0\u069c\0\u06c0\0\u06e4"+
    "\0\u0708\0\u072c\0\u0750\0\u0774\0\u0798\0\u07bc\0\u07e0\0\u0804"+
    "\0\u0828\0\u084c\0\u0870\0\u0894\0\u08b8\0\u08dc\0\u0900\0\u0924"+
    "\0\u0948\0\u096c\0\u0990\0\u09b4\0\u09d8\0\u09fc\0\u0a20\0\u0a44"+
    "\0\u0a68\0\u0a8c\0\u0ab0\0\u0ad4\0\u0af8\0\u0b1c\0\u0b40\0\u0b64"+
    "\0\u0b88\0\u0bac\0\u0bd0\0\u0bf4\0\u0c18\0\u0c3c\0\u0c60\0\u0c84"+
    "\0\u0ca8\0\u0ccc\0\u0cf0\0\u0d14\0\u0d38\0\u0d5c\0\u01b0\0\u0d80"+
    "\0\u0da4\0\u0dc8\0\u01b0\0\u0dec\0\u0e10\0\u0e34\0\u0e58\0\u01b0";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[128];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\15\1\16\2\15\1\17\40\15\1\16\2\20\1\17"+
    "\1\15\3\21\1\22\1\21\1\15\1\21\1\20\16\21"+
    "\1\15\7\21\1\15\1\23\2\15\1\24\1\15\7\24"+
    "\1\15\17\24\1\25\6\24\1\15\1\26\2\15\1\24"+
    "\1\15\7\24\1\15\17\24\1\25\6\24\1\15\1\27"+
    "\2\15\1\24\1\15\7\24\1\15\17\24\1\25\6\24"+
    "\1\15\1\30\2\31\4\15\2\32\3\15\1\31\1\15"+
    "\15\32\2\15\2\32\5\15\1\30\2\31\4\15\1\32"+
    "\1\33\3\15\1\31\1\15\15\32\2\15\2\32\6\15"+
    "\2\34\7\15\1\35\1\15\1\34\27\15\1\36\2\15"+
    "\1\37\40\15\1\36\1\40\1\15\1\37\1\15\3\21"+
    "\1\15\1\21\1\15\1\21\1\15\16\21\1\15\7\21"+
    "\1\41\1\42\1\34\1\43\1\41\1\15\1\44\6\41"+
    "\1\43\22\41\1\45\3\41\1\15\1\46\2\34\4\15"+
    "\1\47\4\15\1\34\1\15\15\47\5\15\1\50\1\51"+
    "\1\52\45\0\1\16\42\0\1\17\1\0\3\17\1\0"+
    "\36\17\1\0\1\16\2\53\11\0\1\53\32\0\1\21"+
    "\1\0\5\21\1\0\1\21\1\0\16\21\1\0\7\21"+
    "\11\0\1\54\33\0\1\23\33\0\1\55\12\0\1\56"+
    "\1\0\5\56\1\57\1\56\1\0\26\56\1\0\1\23"+
    "\2\0\1\56\1\0\5\56\1\57\1\56\1\0\17\56"+
    "\1\60\6\56\1\0\1\30\2\61\11\0\1\61\36\0"+
    "\2\32\5\0\15\32\2\0\2\32\14\0\1\32\1\62"+
    "\5\0\15\32\2\0\2\32\6\0\2\34\11\0\1\34"+
    "\26\0\1\63\1\64\3\63\1\0\36\63\1\0\1\65"+
    "\1\66\41\0\1\41\2\0\2\41\2\0\31\41\1\0"+
    "\4\41\1\0\1\34\1\43\1\41\2\0\6\41\1\43"+
    "\22\41\1\0\3\41\7\0\1\67\44\0\2\47\2\0"+
    "\1\47\2\0\15\47\2\0\1\47\16\0\1\70\32\0"+
    "\1\57\1\71\42\57\10\0\1\32\1\72\5\0\15\32"+
    "\2\0\2\32\14\0\1\73\1\0\1\74\4\0\15\73"+
    "\21\0\1\75\42\0\1\32\1\76\5\0\15\32\2\0"+
    "\2\32\14\0\2\73\1\74\1\73\3\0\15\73\21\0"+
    "\1\77\42\0\1\32\1\100\5\0\15\32\2\0\2\32"+
    "\23\0\1\101\34\0\2\32\5\0\1\32\1\102\13\32"+
    "\2\0\2\32\24\0\1\103\33\0\2\32\5\0\4\32"+
    "\1\104\10\32\2\0\2\32\25\0\1\105\32\0\2\32"+
    "\5\0\7\32\1\106\5\32\2\0\2\32\26\0\1\107"+
    "\23\0\1\110\5\0\2\32\5\0\15\32\2\0\2\32"+
    "\27\0\1\111\44\0\1\112\21\0\1\113\62\0\1\114"+
    "\46\0\1\115\43\0\1\116\40\0\1\117\24\0\1\120"+
    "\65\0\1\121\44\0\1\122\20\0\1\123\63\0\1\124"+
    "\46\0\1\125\37\0\1\126\44\0\1\127\44\0\1\130"+
    "\41\0\1\131\52\0\1\132\36\0\1\133\51\0\1\134"+
    "\32\0\1\135\7\0\1\136\45\0\1\137\37\0\1\140"+
    "\46\0\1\141\45\0\1\142\12\0\1\143\73\0\1\144"+
    "\31\0\1\145\52\0\1\146\47\0\1\147\21\0\1\150"+
    "\52\0\1\151\43\0\1\152\34\0\1\153\57\0\1\154"+
    "\27\0\1\155\43\0\1\156\57\0\1\157\27\0\1\160"+
    "\43\0\1\161\62\0\1\162\24\0\1\163\43\0\1\164"+
    "\53\0\1\165\33\0\1\166\33\0\1\167\1\164\12\0"+
    "\1\164\46\0\1\170\34\0\1\171\43\0\1\172\33\0"+
    "\1\173\1\171\12\0\1\171\37\0\1\174\43\0\1\175"+
    "\43\0\1\176\43\0\1\177\33\0\1\200\1\177\12\0"+
    "\1\177\26\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[3708];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\14\0\1\11\12\1\1\11\4\1\2\11\3\1\1\11"+
    "\2\1\2\11\1\1\3\11\7\0\1\1\1\0\2\11"+
    "\3\0\1\11\1\1\1\0\1\11\1\0\1\1\1\0"+
    "\1\1\1\0\1\1\1\0\1\1\1\0\1\1\60\0"+
    "\1\11\3\0\1\11\4\0\1\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[128];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
    private static final int[] NEXT_VALUE_PARSING_STATE_TABLE = buildNextValueParsingStateTable();
    private static final int INVALID_STATE = -1;


    private final Deb822LanguageSupport languageSupport;
    private final KnownFieldTable knownFieldTable;


    private IElementType parsedFieldName() {
        Deb822KnownField knownField = knownFieldTable.getField(yytext().toString());
        Deb822KnownFieldValueType valueType = Deb822KnownFieldValueType.FREE_TEXT_VALUE;
        if (knownField != null) {
            valueType = knownField.getFieldValueType();
        }
        yybegin(valueType.getInitialValueParsingLexerState());
        return Deb822Types.FIELD_NAME;
    }

    private void nextValueParsingState() {
        int newState;
        if (zzLexicalState < 0 || zzLexicalState >= NEXT_VALUE_PARSING_STATE_TABLE.length) {
            newState = INVALID_STATE;
        } else {
            newState = NEXT_VALUE_PARSING_STATE_TABLE[zzLexicalState];
        }
        if (newState == INVALID_STATE) {
                throw new IllegalStateException("The nextValueParsingState was called in the wrong context");
        }
        yybegin(newState);
    }

    private static int[] buildNextValueParsingStateTable() {
        int[] table = new int[ZZ_LEXSTATE.length];
        Arrays.fill(table, INVALID_STATE);

        table[SEPARATOR_BEFORE_VALUE] = PARSING_VALUE;
        table[MAYBE_CONT_VALUE] = PARSING_VALUE;

        table[SEPARATOR_BEFORE_BUILD_PROFILES] = PARSING_BUILD_PROFILES;
        table[MAYBE_CONT_BUILD_PROFILES] = PARSING_BUILD_PROFILES;

        return table;
    }




  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Deb822Lexer(java.io.Reader in, Deb822LanguageSupport languageSupport) {
      this.languageSupport = languageSupport;
    this.knownFieldTable = languageSupport.getKnownFieldTable();
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    int size = 0;
    for (int i = 0, length = packed.length(); i < length; i += 2) {
      size += packed.charAt(i);
    }
    char[] map = new char[size];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < packed.length()) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      {@code false}, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position {@code pos} from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      if (zzMarkedPosL > zzStartRead) {
        switch (zzBufferL.charAt(zzMarkedPosL-1)) {
        case '\n':
        case '\u000B':  // fall through
        case '\u000C':  // fall through
        case '\u0085':  // fall through
        case '\u2028':  // fall through
        case '\u2029':  // fall through
          zzAtBOL = true;
          break;
        case '\r': 
          if (zzMarkedPosL < zzEndReadL)
            zzAtBOL = zzBufferL.charAt(zzMarkedPosL) != '\n';
          else if (zzAtEOF)
            zzAtBOL = false;
          else {
            boolean eof = zzRefill();
            zzMarkedPosL = zzMarkedPos;
            zzEndReadL = zzEndRead;
            zzBufferL = zzBuffer;
            if (eof) 
              zzAtBOL = false;
            else 
              zzAtBOL = zzBufferL.charAt(zzMarkedPosL) != '\n';
          }
          break;
        default:
          zzAtBOL = false;
        }
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      if (zzAtBOL)
        zzState = ZZ_LEXSTATE[zzLexicalState+1];
      else
        zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + ZZ_CMAP(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        zzDoEOF();
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return TokenType.BAD_CHARACTER;
            } 
            // fall through
          case 25: break;
          case 2: 
            { return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 26: break;
          case 3: 
            { return Deb822Types.COMMENT;
            } 
            // fall through
          case 27: break;
          case 4: 
            { return parsedFieldName();
            } 
            // fall through
          case 28: break;
          case 5: 
            { yybegin(YYINITIAL); return Deb822Types.GPG_ARMOR_HEADERS_END;
            } 
            // fall through
          case 29: break;
          case 6: 
            { yybegin(GPG_SIGNATURE_BLOB); return Deb822Types.GPG_ARMOR_HEADERS_END;
            } 
            // fall through
          case 30: break;
          case 7: 
            { return Deb822Types.GPG_SIGNATURE_BLOB_PART;
            } 
            // fall through
          case 31: break;
          case 8: 
            { nextValueParsingState(); return Deb822Types.SEPARATOR;
            } 
            // fall through
          case 32: break;
          case 9: 
            { yybegin(YYINITIAL); return Deb822Types.PARAGRAPH_FINISH;
            } 
            // fall through
          case 33: break;
          case 10: 
            { nextValueParsingState(); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 34: break;
          case 11: 
            { return Deb822Types.VALUE_TOKEN;
            } 
            // fall through
          case 35: break;
          case 12: 
            { yybegin(MAYBE_CONT_VALUE); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 36: break;
          case 13: 
            { return Deb822Types.COMMA;
            } 
            // fall through
          case 37: break;
          case 14: 
            { yybegin(MAYBE_CONT_BUILD_PROFILES); return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 38: break;
          case 15: 
            { return Deb822Types.BUILD_PROFILE_TOKEN;
            } 
            // fall through
          case 39: break;
          case 16: 
            { return Deb822Types.ANGLE_BRACKET_OPEN;
            } 
            // fall through
          case 40: break;
          case 17: 
            { return Deb822Types.NEGATION;
            } 
            // fall through
          case 41: break;
          case 18: 
            { return Deb822Types.ANGLE_BRACKET_CLOSE;
            } 
            // fall through
          case 42: break;
          case 19: 
            { return Deb822Types.HANGING_CONT_VALUE_TOKEN;
            } 
            // fall through
          case 43: break;
          case 20: 
            { return Deb822Types.GPG_ARMOR_HEADER;
            } 
            // fall through
          case 44: break;
          case 21: 
            { return Deb822Types.SUBSTVAR_TOKEN;
            } 
            // fall through
          case 45: break;
          case 22: 
            { yybegin(GPG_PARSE_ARMORED_HEADERS); return Deb822Types.GPG_END_SIGNATURE;
            } 
            // fall through
          case 46: break;
          case 23: 
            { yybegin(GPG_BEGIN_SIGNATURE_ARMORED_HEADERS); return Deb822Types.GPG_BEGIN_SIGNATURE;
            } 
            // fall through
          case 47: break;
          case 24: 
            { yybegin(GPG_PARSE_ARMORED_HEADERS); return Deb822Types.GPG_BEGIN_SIGNED_MESSAGE;
            } 
            // fall through
          case 48: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
