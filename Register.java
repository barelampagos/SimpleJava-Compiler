class Register {

  public static Register FP() {
    if (FP_ == null) {
      FP_ = new Register("$fp");
    }
    return FP_;
  }

  public static Register SP() {
    if (SP_ == null) {
      SP_ = new Register("$sp");
    }
    return SP_;
  }

  public static Register ESP() {
    if (ESP_ == null) {
      ESP_ = new Register("$t1");
    }
    return ESP_;
  }
  
  public static Register ACC() {
    if (ACC_ == null) {
      ACC_ = new Register("$t0");
    }
    return ACC_;
  }


  public static Register Result() {
    if (Result_ == null) {
      Result_ = new Register("$v0");
    }
    return Result_;
  }

  public static Register ReturnAddr() {
    if (ReturnAddr_ == null) {
      ReturnAddr_ = new Register("$ra");
    }
    return ReturnAddr_;
  }

  public static Register Zero() {
    if (Zero_ == null) {
      Zero_ = new Register("$zero");
    }
    return Zero_;
  }

  public static Register Tmp1() {
    if (Tmp1_ == null) {
      Tmp1_ = new Register("$t2");
    }
    return Tmp1_;
  }

  public static Register Tmp2() {
    if (Tmp2_ == null) {
      Tmp2_ = new Register("$t3");
    }
    return Tmp2_;
  }

  public static Register Tmp3() {
    if (Tmp3_ == null) {
      Tmp3_ = new Register("$t4");
    }
    return Tmp3_;
  }


  public String toString() {
    return name_;
  }
    
  static private Register FP_ = null;
  static private Register SP_ = null;
  static private Register ESP_ = null;
  static private Register ACC_ = null;
  static private Register Result_ = null;
  static private Register ReturnAddr_ = null;
  static private Register Zero_ = null;
  static private Register Tmp1_ = null;
  static private Register Tmp2_ = null;
  static private Register Tmp3_ = null;
  static private Register Tmp4_ = null;
  static private Register Heap_ = null;  

  private Register(String name) {
    name_ = name;
  }

  private String name_;
}
