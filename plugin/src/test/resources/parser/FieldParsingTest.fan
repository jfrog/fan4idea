//
// Copyright (c) 2006, Brian Frank and Andy Frank
// Licensed under the Academic Free License version 3.0
//
// History:
//   15 Sep 05  Brian Frank  Creation
//   24 Jun 06  Brian Frank  Ported from Java to Fan
//

**
** AstWriter
**
class AstWriter
{
	Void test() {
    verifySer("[sys::Int:sys::Bool[]][[2:[true]]]", [Int:Bool[]][[2:[true]]])
    verifySer("sys::Int:sys::Uri[,]", Int:Uri[,])
    msg := setter ? "setter of field" : (slot is CMethod ? "method" : "field")
    verifyEq([Int:Str][]#get.returns, [Int:Str]#)
      Str["a", "b"]
        .each |Str name| { verifyStorage(foo.field(name), true) }
    verifyEq(obj->b, [Int#, 'b'])
    verifyEq(obj->s, [Int#, 1000])
    verifyEq(obj->c, [Int#, 'c'])
    verifyEq(obj->i, [Int#, 1000])
    verifyEq(obj->f, [Float#, 'f'.toFloat])
    return total == 1
     ? "1 " + Flux#.loc("find.match")
      : "$total " + Flux#.loc("find.matches")

    flux::Main.exit(frame)
        res.code = line[0..2].toInt 
    verifyLink(|Bool a|#, "|<a href='Bool'>Bool</a>|")
    verifyLink(|Bool a, Str b|#, "|<a href='Bool'>Bool</a>, <a href='Str'>Str</a>|")
    verifyLink(|Bool a, Str b -> Int|#, "|<a href='Bool'>Bool</a>, <a href='Str'>Str</a> -> <a href='Int'>Int</a>|")
    verifyLink(|Bool a, Str[] b -> Int|#, "|<a href='Bool'>Bool</a>, <a href='Str'>Str</a>[] -> <a href='Int'>Int</a>|")
    verifyLink(|Int:Str a -> Bool|#, "|<a href='Int'>Int</a>:<a href='Str'>Str</a> -> <a href='Bool'>Bool</a>|")
    this.slots = slots == null ? Str:Int[:] : slots
    list :=
    [
      "abstract",  "finally",    "readonly",
      "as",        "for",        "return",
      "assert",    "foreach",    "static",
      "break",     "goto",       "super",
      "case",      "if",         "switch",
      "catch",     "internal",   "this",
      "class",     "is",         "throw",
      "const",     "mixin",      "true",
      "continue",  "native",     "try",
      "default",   "new",        "using",
      "do",        "null",       "virtual",
      "else",      "override",   "volatile",
      "enum",      "private",    "void",
      "false",     "protected",  "while",
      "final",     "public",     "once"
     ]
     map := Str:Bool[:]
		d := Str[]["r"]
		if (p["R"] != Void#) buf.add(" -> ").add(makeTypeLink(p["R"], map))
	}

  ** Map of element names to functions to parse a string
  internal const static Str:|Str,XElem->Obj| elemNameToFromStrFunc :=
  [
    "bool":    |Str s->Obj| { return Bool.fromStr(s, true) },
    "int":     |Str s->Obj| { return Int.fromStr(s, 10, true) },
    "real":    |Str s->Obj| { return Float.fromStr(s, true) },
    "str":     |Str s->Obj| { return s },
    "uri":     |Str s->Obj| { return Uri.decode(s) },
    "enum":    |Str s->Obj| { return s },
    "abstime": |Str s, XElem elem->Obj| { return parseAbstime(s, elem) },
    "reltime": |Str s->Obj| { return Duration.fromIso(s, true) },
    "date":    |Str s->Obj| { return Date.fromIso(s, true) },
    "time":    |Str s->Obj| { return Time.fromIso(s, true) }
  ]

  override CType? base
  {
    get
    {
      if (@base == null) @base = fpod.toType(fbase)
      return @base
    }
  }


  Bool leave := true { protected set } // leave this expression on the stack

Matcher toMatcher(Str? tok, Int esc := 0)
{
    verifyErr(UnsupportedErr#) |,| { Str:Str[:] { ordered = true; caseInsensitive = true } }
    verifyErr(ReadonlyErr#) |,| { xro := Str:Str[:].ro; xro.caseInsensitive = true }
    verifyEq(((Str[])m.reduce(Str[,])
      |Obj r, Int v, Str k->Obj| { return ((Str[])r).add(k) }).sort,
      ["B", "Charlie", "a"])
    verifyErr(ArgErr#) |,| { m.add("B", 'x') }
    verifyErr(ArgErr#) |,| { m.add("b", 'x') }
    verifyErr(ArgErr#) |,| { m.add("A", 'x') }
    actual := map.join(sep.toChar, f).split(sep)
    verifyEq(actual.sort, expected.sort)
    verifyEq(map.join(sep.toChar), map.join(sep.toChar, null))
        if (qcolon == null) break
  tok = tok?.trim ?: ""
  switch (tok.size)
  {
    case 0:
      return Matcher(0, &noMatch, |,| {})
    case 1:
      if (esc > 0)
        return Matcher(1, &match1Esc(tok[0], esc), |,| { consume })
      else
        return Matcher(1, &match1(tok[0]), |,| { consume })
    case 2:
      if (esc > 0)
        return Matcher(2, &match2Esc(tok[0], tok[1], esc), |,| { consume; consume })
      else
        return Matcher(2, &match2(tok[0], tok[1]), |,| { consume; consume })
    default:
      return Matcher(tok.size, &matchN(tok), &consumeN(tok.size))
  }
}

  private Void invokeCall(CallExpr call, Bool leave := call.leave)
  {
	  if (targetId == ExprId.superExpr ||
		  (targetId == ExprId.thisExpr && !m.isVirtual && !m.parent.isObj))
		op(FOp.CallNonVirtual, index)
	  else
		op(FOp.CallVirtual, index)

    m := call.method
    index := fpod.addMethodRef(m, call.args.size)

    // write CallVirtual, CallNonVirtual, CallStatic, CallNew, or CallCtor;
    // note that if a constructor call has a target (this or super), then it
    // is a CallCtor instance call because we don't want to allocate
    // a new instance
    if (m.parent.isMixin)
    {
      if (m.isStatic)
        op(FOp.CallMixinStatic, index)
      else if (call.target.id == ExprId.superExpr)
        op(FOp.CallMixinNonVirtual, index)
      else
        op(FOp.CallMixinVirtual, index)
    }
    else if (m.isStatic)
    {
      op(FOp.CallStatic, index)
    }
    else if (m.isCtor)
    {
      if (call.target == null || call.target.id == ExprId.staticTarget)
        op(FOp.CallNew, index)
      else
        op(FOp.CallCtor, index)
    }
    else
    {
      // because CallNonVirtual maps to Java's invokespecial, we can't
      // use it for calls outside of the class (consider it like calling
      // protected method); we also don't want to use non-virtual for
      // any Obj methods since those are implemented as static wrappers
      // in the Java/.NET runtime
      targetId := call.target.id
      if (targetId == ExprId.superExpr ||
          (targetId == ExprId.thisExpr && !m.isVirtual && !m.parent.isObj))
        op(FOp.CallNonVirtual, index)
      else
        op(FOp.CallVirtual, index)
    }

    // if we are leaving a value on the stack of a method which
    // has a parameterized return value or is covariant, then we
    // need to insert a cast operation
    //   Int.toStr    => non-generic - no cast
    //   Str[].toStr  => return isn't parameterized - no cast
    //   Str[].get()  => actual return is Obj, but we want Str - cast
    //   covariant    => actual call is against inheritedReturnType
    if (leave)
    {
      if (m.isParameterized)
      {
        ret := m.generic.returnType
        if (ret.isGenericParameter)
          coerceOp(ns.objType, m.returnType)
      }
      else if (m.isCovariant)
      {
        coerceOp(m.inheritedReturnType, m.returnType)
      }
    }

    // if the method left a value on the stack, and we
    // aren't going to use it, then pop it off
    if (!leave)
    {
      // note we need to use the actual method signature (not parameterized)
      x := m.isParameterized ? m.generic : m
      if (!x.returnType.isVoid || x.isCtor)
        opType(FOp.Pop, x.returnType)
    }
  }

  @containerProvider=Container
  once ServiceInteface myService() {} // { return Container.get(AstWriter#myService) }

  **
  ** This method is used for complex assignments: prefix/postfix
  ** increment and special dual assignment operators like "+=".
  **
  private Void shortcutAssign(ShortcutExpr c)
  {
    var := c.target
    leaveUsingTemp := false

    // if var is a coercion set that aside and get real variable
    TypeCheckExpr? coerce := null
    if (var.id == ExprId.coerce)
    {
      coerce = (TypeCheckExpr)var
      var = coerce.target
    }

    // load the variable
    switch (var.id)
    {
      case ExprId.localVar:
        loadLocalVar((LocalVarExpr)var)
      case ExprId.field:
        fexpr := (FieldExpr)var
        loadField(fexpr, true) // dup target on stack for upcoming set
        leaveUsingTemp = !fexpr.field.isStatic  // used to determine how to duplicate
      case ExprId.shortcut:
        // since .NET sucks when it comes to stack manipulation,
        // we use two scratch locals to get the stack into the
        // following format:
        //   index  \  used for get
        //   target /
        //   index  \  used for set
        //   target /
        index := (IndexedAssignExpr)c
        get := (ShortcutExpr)var
        expr(get.target)  // target
        opType(FOp.Dup, get.target.ctype)
        op(FOp.StoreVar, index.scratchA.register)
        expr(get.args[0]) // index expr
        opType(FOp.Dup, get.args[0].ctype)
        op(FOp.StoreVar, index.scratchB.register)
        op(FOp.LoadVar, index.scratchA.register)
        op(FOp.LoadVar, index.scratchB.register)
        invokeCall(get, true)
        leaveUsingTemp = true
      default:
        throw err("Internal error", var.location)
    }

    // if we have a coercion do it
    if (coerce != null) coerceOp(var.ctype, coerce.check)

    // if postfix leave, duplicate value before we preform computation
    if (c.leave && c.isPostfixLeave)
    {
      opType(FOp.Dup, c.ctype)
      if (leaveUsingTemp)
        op(FOp.StoreVar, c.tempVar.register)
    }

    // load args and invoke call
    c.args.each |Expr arg| { expr(arg) }
    invokeCall(c, true)

    // if prefix, duplicate after we've done computation
    if (c.leave && !c.isPostfixLeave)
    {
      opType(FOp.Dup, c.ctype)
      if (leaveUsingTemp)
        op(FOp.StoreVar, c.tempVar.register)
    }

    // if we have a coercion then uncoerce
    if (coerce != null) coerceOp(coerce.check, var.ctype)

    // save the variable back
    switch (var.id)
    {
      case ExprId.localVar:
        storeLocalVar((LocalVarExpr)var)
      case ExprId.field:
        storeField((FieldExpr)var)
      case ExprId.shortcut:
        set := (CMethod)c->setMethod
        // if calling setter we have to ensure unboxed
        if (c.ctype.isValue && coerce == null) coerceOp(c.ctype, ns.objType)
        op(FOp.CallVirtual, fpod.addMethodRef(set, 2))
        if (!set.returnType.isVoid) opType(FOp.Pop, set.returnType)
      default:
        throw err("Internal error", var.location)
    }

    // if field leave, then load back from temp local
    if (c.leave && leaveUsingTemp)
      op(FOp.LoadVar, c.tempVar.register)
  }

  internal static const Type:|Obj->Str| valTypeToStrFunc :=
  [
    Uri#:      |Uri v->Str|      { return v.encode },
    DateTime#: |DateTime v->Str| { return v.toIso },
    Duration#: |Duration v->Str| { return v.toIso },
    Date#:     |Date v->Str|     { return v.toIso },
    Time#:     |Time v->Str|     { return v.toIso },
  ]

  Void verifyJoin(Int:Str map, Int sep, Str[] expected, |Str,Int->Str|? f)
  {
    verifySame(t.slots[-1], x)
    verifyErr(UnsupportedErr#) |,| { Int:Str[:].caseInsensitive = true }
    verifyErr(UnsupportedErr#) |,| { Obj:Str[:].caseInsensitive = true }
    verifyErr(UnsupportedErr#) |,| { ["a":0].caseInsensitive = true }
    verifyErr(UnsupportedErr#) |,| { Str:Str[:] { ordered = true; caseInsensitive = true } }
    verifyErr(ReadonlyErr#) |,| { xro := Str:Str[:].ro; xro.caseInsensitive = true }
    verifyEq(((Str[])m.reduce(Str[,])
      |Obj r, Int v, Str k->Obj| { return ((Str[])r).add(k) }).sort,
      ["B", "Charlie", "a"])
    verifyErr(ArgErr#) |,| { m.add("B", 'x') }
    verifyErr(ArgErr#) |,| { m.add("b", 'x') }
    verifyErr(ArgErr#) |,| { m.add("A", 'x') }
    actual := map.join(sep.toChar, f).split(sep)
    verifyEq(actual.sort, expected.sort)
    verifyEq(map.join(sep.toChar), map.join(sep.toChar, null))
  }

//////////////////////////////////////////////////////////////////////////
// Case Insensitive
//////////////////////////////////////////////////////////////////////////

  Void testCaseInsensitive()
  {
    m := Str:Int[:]
    m.caseInsensitive = true

    // add, get, containsKey
    m.add("a", 'a')
    verifyEq(m["a"], 'a')
    verifyEq(m["A"], 'a')
    verifyEq(m.containsKey("a"), true)
    verifyEq(m.containsKey("A"), true)
    verifyEq(m.containsKey("ab"), false)

    // add, get, containsKey
    m.add("B", 'b')
    verifyEq(m["b"], 'b')
    verifyEq(m["B"], 'b')
    verifyEq(m.containsKey("b"), true)
    verifyEq(m.containsKey("B"), true)

    // add existing
    verifyErr(ArgErr#) |,| { m.add("B", 'x') }
    verifyErr(ArgErr#) |,| { m.add("b", 'x') }
    verifyErr(ArgErr#) |,| { m.add("A", 'x') }

    // get, set, containsKey
    m.set("Charlie", 'x')
    m.set("CHARLIE", 'c')
    verifyEq(m["a"], 'a')
    verifyEq(m["A"], 'a')
    verifyEq(m["b"], 'b')
    verifyEq(m["B"], 'b')
    verifyEq(m["charlie"], 'c')
    verifyEq(m["charlIE"], 'c')
    verifyEq(m.containsKey("a"), true)
    verifyEq(m.containsKey("A"), true)
    verifyEq(m.containsKey("b"), true)
    verifyEq(m.containsKey("B"), true)
    verifyEq(m.containsKey("charlie"), true)
    verifyEq(m.containsKey("CHARLIE"), true)

    // keys, values
    verifyEq(m.keys.sort, ["B", "Charlie", "a"])
    verifyEq(m.values.sort, ['a', 'b', 'c'])

    // each
    x := Str:Int[:]
    m.each |Int v, Str k| { x[k] = v }
    verifyEq(x, ["a":'a', "B":'b', "Charlie":'c'])

    // find, findAll, exclude, reduce, map
    verifyEq(m.find |Int v, Str k->Bool| { return k == "a" }, 'a')
    verifyEq(m.find |Int v, Str k->Bool| { return k == "B" }, 'b')
    verifyEq(m.findAll |Int v, Str k->Bool| { return k == "B" }, ["B":'b'])
    verifyEq(m.exclude |Int v, Str k->Bool| { return k == "B" }, ["a":'a', "Charlie":'c'])
    verifyEq(m.map(Str:Str[:]) |Int v, Str k->Obj| { return k }, ["a":"a", "B":"B", "Charlie":"Charlie"])

    // dup
    d := m.dup
    verifyEq(d.keys.sort, ["B", "Charlie", "a"])
    verifyEq(d.values.sort, ['a', 'b', 'c'])
    d["charlie"] = 'x'
    verifyEq(m["Charlie"], 'c')
    verifyEq(m["charlIE"], 'c')
    verifyEq(d["Charlie"], 'x')
    verifyEq(d["charlIE"], 'x')

    // remove
    verifyEq(m.remove("CHARLIE"), 'c')
    verifyEq(m["charlie"], null)
    verifyEq(m.containsKey("Charlie"), false)
    verifyEq(m.keys.sort, ["B", "a"])

    // addAll (both not insensitive, and insensitive)
    m.addAll(["DAD":'d', "Egg":'e'])
    q := Str:Int[:]; q.caseInsensitive = true; q["foo"] = 'f'
    m.addAll(q)
    verifyEq(m.keys.sort, ["B", "DAD", "Egg", "a", "foo"])
    verifyEq(m["dad"], 'd')
    verifyEq(m["egg"], 'e')
    verifyEq(m["b"], 'b')
    verifyEq(m["FOO"], 'f')

    // setAll (both not insensitive, and insensitive)
    m.setAll(["dad":'D', "EGG":'E'])
    q["FOO"] = 'F'
    m.setAll(q)
    verifyEq(m.keys.sort, ["B", "DAD", "Egg", "a", "foo"])
    verifyEq(m["DaD"], 'D')
    verifyEq(m["eGg"], 'E')
    verifyEq(m["b"], 'b')
    verifyEq(m["Foo"], 'F')
    verifyEq(m.containsKey("EgG"), true)
    verifyEq(m.containsKey("A"), true)

    // to readonly
    r := m.ro
    verifyEq(r.caseInsensitive, true)
    verifyEq(r.keys.sort, ["B", "DAD", "Egg", "a", "foo"])
    verifyEq(r["DaD"], 'D')
    verifyEq(r["eGg"], 'E')
    verifyEq(r["b"], 'b')
    verifyEq(r["Foo"], 'F')
    verifyEq(r.containsKey("EgG"), true)
    verifyEq(r.containsKey("A"), true)

    // to immutable
    i := m.toImmutable
    verifyEq(i.caseInsensitive, true)
    verifyEq(i.keys.sort, ["B", "DAD", "Egg", "a", "foo"])
    verifyEq(i["DaD"], 'D')
    verifyEq(i["eGg"], 'E')
    verifyEq(i["b"], 'b')
    verifyEq(i["Foo"], 'F')
    verifyEq(i.containsKey("EgG"), true)
    verifyEq(i.containsKey("A"), true)

    // to rw
    rw := r.rw
    verifyEq(rw.caseInsensitive, true)
    verifyEq(rw.remove("Dad"), 'D')
    rw["fOo"] = '^'
    verifyEq(r.keys.sort, ["B", "DAD", "Egg", "a", "foo"])
    verifyEq(rw.keys.sort, ["B", "Egg", "a", "foo"])
    verifyEq(r["DaD"], 'D')
    verifyEq(r["eGg"], 'E')
    verifyEq(r["b"], 'b')
    verifyEq(r["Foo"], 'F')
    verifyEq(rw["DaD"], null)
    verifyEq(rw["eGg"], 'E')
    verifyEq(rw["b"], 'b')
    verifyEq(rw["Foo"], '^')

    // set false
    m.clear
    m.caseInsensitive = false
    m.add("Alpha", 'a').add("Beta", 'b')
    verifyEq(m["Alpha"], 'a')
    verifyEq(m["alpha"], null)
    verifyEq(m["ALPHA"], null)
    verifyEq(m.containsKey("Beta"), true)
    verifyEq(m.containsKey("beta"), false)

    // equals
    m.clear
    m.caseInsensitive = true
    m.add("Alpha", 'a').add("Beta", 'b')
    verifyEq(m, ["Alpha":'a', "Beta":'b'])
    verifyNotEq(m, ["alpha":'a', "Beta":'b'])
    verifyNotEq(m, ["Alpha":'x', "Beta":'b'])
    verifyNotEq(m, ["Beta":'b'])
    verifyNotEq(m, ["Alpha":'a', "Beta":'b', "C":'c'])

    // errors
    verifyErr(UnsupportedErr#) |,| { Int:Str[:].caseInsensitive = true }
    verifyErr(UnsupportedErr#) |,| { Obj:Str[:].caseInsensitive = true }
    verifyErr(UnsupportedErr#) |,| { ["a":0].caseInsensitive = true }
    verifyErr(UnsupportedErr#) |,| { Str:Str[:] { ordered = true; caseInsensitive = true } }
    verifyErr(ReadonlyErr#) |,| { xro := Str:Str[:].ro; xro.caseInsensitive = true }
  }

  internal static const Type:|Obj->Str| valTypeToStrFunc :=
  [
    Uri#:      |Uri v->Str|      { return v.encode },
    DateTime#: |DateTime v->Str| { return v.toIso },
    Duration#: |Duration v->Str| { return v.toIso },
    Date#:     |Date v->Str|     { return v.toIso },
    Time#:     |Time v->Str|     { return v.toIso },
  ]

  @transient readonly EventListeners onKeyDown := EventListeners()
    { onModify = &checkKeyListeners }
  internal native Void checkKeyListeners()

  const static Color purple := make(0x80_00_80)

  static native Color sysDarkShadow()

  |Graphics g, Size size, Insets insets|? onBorder := null

  override Str text
  {
    get { return lines.join(delimiter) |ConsoleLine line->Str| { return line.text } }
    set { modify(0, size, val) }
  }

  virtual Expr coerce(Expr expr, CType expected, |,| onErr)
  {
    return CheckErrors.doCoerce(expr, expected, onErr)
  }

  override once CType[] mixins()
  {
    return fpod.resolveTypes(fmixins)
  }

  ** Directory to zip up.  The contents of this dir are recursively
  ** zipped up with zip paths relative to this root directory.
  File inDir

  ** This function is called on each file under 'inDir'; if true
  ** returned it is included in the zip, if false then it is excluded.
  ** Returning false for a directory will skip recursing the entire
  ** directory.
  |File f, Str path->Bool| filter

  override Bool synthetic := false

  Location inputLoc := Location.make("CompilerInput")

  static Weekday toWeekday(Str s)
  {
    x := weekdays[s]
    if (x == null) throw ParseErr.make(s)
    return x
  }

  static const Str:Weekday weekdays :=
  [
    "Sun":Weekday.sun,
    "Mon":Weekday.mon,
    "Tue":Weekday.tue,
    "Wed":Weekday.wed,
    "Thu":Weekday.thu,
    "Fri":Weekday.fri,
    "Sat":Weekday.sat,
  ].toImmutable

  ** From type if coerce
  CType? from { get { return @from ?: target.ctype } }

  Expr target
  CType check    // to type if coerce

//////////////////////////////////////////////////////////////////////////
// Construction
//////////////////////////////////////////////////////////////////////////

  **
  ** Make for specified output stream
  **
  new make(OutStream out := Sys.out)
  {
    this.out = out
  }

  new make2(Location location, ExprId id, Expr? target, Str? name)
    : super(location, id)
  {
    this.target = target
    this.name   = name
    this.isSafe = false
  }

//////////////////////////////////////////////////////////////////////////
// Methods
//////////////////////////////////////////////////////////////////////////

  **
  ** Write and then return this.
  **
  AstWriter w(Obj o)
  {
    if (needIndent)
    {
      out.writeChars(Str.spaces(indentation*2))
      needIndent = false
    }
    out.writeChars(o.toStr)
    return this
  }

//////////////////////////////////////////////////////////////////////////
// Fields
//////////////////////////////////////////////////////////////////////////

  OutStream out
  Int indentation := 0
  Bool needIndent := false

}

const class TestMethod : Method
{
  new make1(Str name, Func f) : super.make(name, f) {}
  new make2(Str name, Func f, Str:Obj x) : super.make(name, f, x) {}
}

class ExprX { This add(ExprX? k) { kids.add(k); return this } ExprX?[] kids := ExprX?[,]  }
class ExprY : ExprX { ExprX a }
class ExprZ : ExprX  {}
