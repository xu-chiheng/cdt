/*******************************************************************************
* Copyright (c) 2006, 2008 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl_v10.html
*
* Contributors:
*     IBM Corporation - initial API and implementation
*********************************************************************************/

// This file was generated by LPG

package org.eclipse.cdt.internal.core.dom.lrparser.cpp;

public interface CPPTemplateTypeParameterParsersym {
    public final static int
      TK_asm = 63,
      TK_auto = 48,
      TK_bool = 15,
      TK_break = 78,
      TK_case = 79,
      TK_catch = 119,
      TK_char = 16,
      TK_class = 59,
      TK_const = 33,
      TK_const_cast = 34,
      TK_continue = 80,
      TK_default = 81,
      TK_delete = 65,
      TK_do = 82,
      TK_double = 17,
      TK_dynamic_cast = 35,
      TK_else = 122,
      TK_enum = 68,
      TK_explicit = 49,
      TK_export = 75,
      TK_extern = 12,
      TK_false = 36,
      TK_float = 18,
      TK_for = 83,
      TK_friend = 50,
      TK_goto = 84,
      TK_if = 85,
      TK_inline = 51,
      TK_int = 19,
      TK_long = 20,
      TK_mutable = 52,
      TK_namespace = 57,
      TK_new = 66,
      TK_operator = 7,
      TK_private = 103,
      TK_protected = 104,
      TK_public = 105,
      TK_register = 53,
      TK_reinterpret_cast = 37,
      TK_return = 86,
      TK_short = 21,
      TK_signed = 22,
      TK_sizeof = 38,
      TK_static = 54,
      TK_static_cast = 39,
      TK_struct = 69,
      TK_switch = 87,
      TK_template = 29,
      TK_this = 40,
      TK_throw = 60,
      TK_try = 76,
      TK_true = 41,
      TK_typedef = 55,
      TK_typeid = 42,
      TK_typename = 10,
      TK_union = 70,
      TK_unsigned = 23,
      TK_using = 58,
      TK_virtual = 32,
      TK_void = 24,
      TK_volatile = 43,
      TK_wchar_t = 25,
      TK_while = 77,
      TK_integer = 44,
      TK_floating = 45,
      TK_charconst = 46,
      TK_stringlit = 30,
      TK_identifier = 1,
      TK_Completion = 2,
      TK_EndOfCompletion = 9,
      TK_Invalid = 123,
      TK_LeftBracket = 56,
      TK_LeftParen = 3,
      TK_LeftBrace = 61,
      TK_Dot = 120,
      TK_DotStar = 96,
      TK_Arrow = 106,
      TK_ArrowStar = 90,
      TK_PlusPlus = 26,
      TK_MinusMinus = 27,
      TK_And = 8,
      TK_Star = 6,
      TK_Plus = 13,
      TK_Minus = 14,
      TK_Tilde = 5,
      TK_Bang = 31,
      TK_Slash = 91,
      TK_Percent = 92,
      TK_RightShift = 88,
      TK_LeftShift = 89,
      TK_LT = 28,
      TK_GT = 62,
      TK_LE = 93,
      TK_GE = 94,
      TK_EQ = 97,
      TK_NE = 98,
      TK_Caret = 99,
      TK_Or = 100,
      TK_AndAnd = 101,
      TK_OrOr = 102,
      TK_Question = 117,
      TK_Colon = 71,
      TK_ColonColon = 4,
      TK_DotDotDot = 95,
      TK_Assign = 67,
      TK_StarAssign = 107,
      TK_SlashAssign = 108,
      TK_PercentAssign = 109,
      TK_PlusAssign = 110,
      TK_MinusAssign = 111,
      TK_RightShiftAssign = 112,
      TK_LeftShiftAssign = 113,
      TK_AndAssign = 114,
      TK_CaretAssign = 115,
      TK_OrAssign = 116,
      TK_Comma = 64,
      TK_RightBracket = 118,
      TK_RightParen = 73,
      TK_RightBrace = 72,
      TK_SemiColon = 11,
      TK_ERROR_TOKEN = 74,
      TK_0 = 47,
      TK_EOF_TOKEN = 121;

      public final static String orderedTerminalSymbols[] = {
                 "",
                 "identifier",
                 "Completion",
                 "LeftParen",
                 "ColonColon",
                 "Tilde",
                 "Star",
                 "operator",
                 "And",
                 "EndOfCompletion",
                 "typename",
                 "SemiColon",
                 "extern",
                 "Plus",
                 "Minus",
                 "bool",
                 "char",
                 "double",
                 "float",
                 "int",
                 "long",
                 "short",
                 "signed",
                 "unsigned",
                 "void",
                 "wchar_t",
                 "PlusPlus",
                 "MinusMinus",
                 "LT",
                 "template",
                 "stringlit",
                 "Bang",
                 "virtual",
                 "const",
                 "const_cast",
                 "dynamic_cast",
                 "false",
                 "reinterpret_cast",
                 "sizeof",
                 "static_cast",
                 "this",
                 "true",
                 "typeid",
                 "volatile",
                 "integer",
                 "floating",
                 "charconst",
                 "0",
                 "auto",
                 "explicit",
                 "friend",
                 "inline",
                 "mutable",
                 "register",
                 "static",
                 "typedef",
                 "LeftBracket",
                 "namespace",
                 "using",
                 "class",
                 "throw",
                 "LeftBrace",
                 "GT",
                 "asm",
                 "Comma",
                 "delete",
                 "new",
                 "Assign",
                 "enum",
                 "struct",
                 "union",
                 "Colon",
                 "RightBrace",
                 "RightParen",
                 "ERROR_TOKEN",
                 "export",
                 "try",
                 "while",
                 "break",
                 "case",
                 "continue",
                 "default",
                 "do",
                 "for",
                 "goto",
                 "if",
                 "return",
                 "switch",
                 "RightShift",
                 "LeftShift",
                 "ArrowStar",
                 "Slash",
                 "Percent",
                 "LE",
                 "GE",
                 "DotDotDot",
                 "DotStar",
                 "EQ",
                 "NE",
                 "Caret",
                 "Or",
                 "AndAnd",
                 "OrOr",
                 "private",
                 "protected",
                 "public",
                 "Arrow",
                 "StarAssign",
                 "SlashAssign",
                 "PercentAssign",
                 "PlusAssign",
                 "MinusAssign",
                 "RightShiftAssign",
                 "LeftShiftAssign",
                 "AndAssign",
                 "CaretAssign",
                 "OrAssign",
                 "Question",
                 "RightBracket",
                 "catch",
                 "Dot",
                 "EOF_TOKEN",
                 "else",
                 "Invalid"
             };

    public final static boolean isValidForParser = true;
}
