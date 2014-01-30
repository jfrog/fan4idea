/********************
* My test of comments
/* Another test of comments
** He voila
*/
// Test for all king of Int, Str and Uri literals
// Should be able to put " ' and ` has much has I want :)
** Some fandoc here
** Cool no?
class StringTest {
  Void showHelp()
  {
    msgDsl :=
    Str<|Goto File Cheat Sheet:\n
       - Glob any file name such as "SideBar.fan" or "SideBar*.fan"\n
       - Glob any file base name (without extension) such as "SideBar" or "SideBar*"\n
       - Match camel case abbreviation such as "SB" for "SideBar"\n
       Indexing is configured with GeneralOption.indexDirs (very primitive right
       now). Use refresh button to manually rebuild index.  See Regex.glob for
       definition of glob syntax.|>
    msgTriple :=
    """Goto File Cheat Sheet:\n
       - Glob any file name such as "SideBar.fan" or "SideBar*.fan"\n
       - Glob any file base name (without extension) such as "SideBar" or "SideBar*"\n
       - Match camel case abbreviation such as "SB" for "SideBar"\n
       Indexing is configured with GeneralOption.indexDirs (very primitive right
       now). Use refresh button to manually rebuild index.  See Regex.glob for
       definition of glob syntax."""
  }

  Int[] integers := Int[
   'a', 'b', '\b', '\f', '\n', '\r',
    '\t', '\'', '\`', '\$', '\\', '\u0123' ]
/**
 Should be able to put " ' and ` has much has I want :)
 Tough one
 */
  Str[] strings := Str[
    "", // Empty String
    "\"", // Escaping double Quote
    "start
     multiline strings",
        "class Foo
         {
           // does this work?
           Int str := \"cool & \\\"foo\\\" > 'rock' < weee!\"
           Int x := 5  // andy rules!
         }
         ",""
     ]
** Should be able to put " ' and ` has much has I want :)
  Uri[] uris := Uri[
  `super`, ``, `/ok/doki`, `With Space_-56` ]
}
