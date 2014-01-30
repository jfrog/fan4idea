//
// Copyright (c) 2009, Fred Simon and Dror Bereznitsky
// Licensed under the Academic Free License version 3.0
//
// History:
//   16 Jan 09  Fred Simon  Creation
//

**
** ExpressionTest
**
class ExpressionTest
{
  Int a
  Int b := 1 + 3 + a
  Int c := 34 + (3*2) / 6;
  Str s; Str t := "ok dokki"
  Str u;

  new make(Str in // test assign default expression with returns
    := "annoying
        returns",
        Str err) {
    // First test assign expression
    a = 1;
    s = in
  }
}