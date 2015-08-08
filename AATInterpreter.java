import java.util.Vector;
import java.io.*;

class AATInterpreter {
    static final int HEAPSIZE = 5000;
    static final int STACKSIZE = 5000;
    static final int ESPSTACKSIZE = 50;



    int countStatements(AATStatement program) {
	if (program instanceof AATSequential) {
	    AATSequential s = (AATSequential) program;
	    return countStatements(s.left()) +
		countStatements(s.right());
	}
	return 1;
    }


    void SetMemory(int index, int value) {
	Memory[index / 4] = value;
    }


    void SetRegister(AATRegister reg, int value) {
	if (reg.register() == Register.FP()) {
	    FP = value;
	} else if (reg.register() == Register.SP()) {
	    SP = value;
	} else if (reg.register() == Register.ESP()) {
	    ESP = value;
	} else if (reg.register() == Register.ACC()) {
	    ACC = value;
	} else if (reg.register() == Register.Result()) {
	    Result = value;
	} else if (reg.register() == Register.ReturnAddr()) {
	    ReturnAddr = value;
	} else if (reg.register() == Register.Zero()) {
	    System.out.println("WARNING:  Setting zero register");
	} else if (reg.register() == Register.Tmp1()) {
	    temp1 = value;
	} else if (reg.register() == Register.Tmp2()) {
	    temp2 = value;
	} else if (reg.register() == Register.Tmp3()) {
	    temp3 = value;
	} else {
	    System.out.println("ERROR:  Can't find register " + reg.register());
	}
    }

   int GetRegister(AATRegister reg) {
	if (reg.register() == Register.FP()) {
	    return FP;
	} else if (reg.register() == Register.SP()) {
	    return SP;
	} else if (reg.register() == Register.ESP()) {
	    return ESP;
	} else if (reg.register() == Register.ACC()) {
	    return ACC;
	} else if (reg.register() == Register.Result()) {
	    return Result;
	} else if (reg.register() == Register.ReturnAddr()) {
	    return ReturnAddr;
	} else if (reg.register() == Register.Zero()) {
	    return 0;
	} else if (reg.register() == Register.Tmp1()) {
	    return temp1;
	} else if (reg.register() == Register.Tmp2()) {
	    return temp2;
	} else if (reg.register() == Register.Tmp3()) {
	    return temp3;
	} else {
	    System.out.println("ERROR:  Can't find register " + reg.register());
	    return 0;
	}
    }

    int FlattenCode(AATStatement program, int IP) {
	if (program instanceof AATSequential) {
	    IP = FlattenCode(((AATSequential) program).left(),IP);
	    IP = FlattenCode(((AATSequential) program).right(),IP);
	    return IP;
	}
	code[IP] = program;
	if (program instanceof AATLabel) {
	    labels.insert(((AATLabel) program).label().toString(), new Integer(IP));
	}
	return IP+1;
    }

    boolean EvalCallExp(AATCallExpression exp) {
	int i;
	try {
	    if (exp.label().toString().compareTo("Read") == 0) {
		String message = stdin.readLine();
		ACC = Integer.parseInt(message);
		return false;
	    }
	} catch (Exception e) {
	    System.out.println("I/O Exception:" + e);
	    return true;
	}

	if (exp.label().toString().compareTo("allocate") == 0) {
	    if (EvaluateExpression((AATExpression) exp.actuals().elementAt(0)))
		return true;
	    //	    System.out.println("allocating " + ACC + " bytes");
	    temp1 = ACC;
	    ACC = heap;
	    heap = heap - temp1;
		return false;
	}

	for (i=exp.actuals().size()-1;i>=0;i--) {
	    Vector v = exp.actuals();
	    if (EvaluateExpression((AATExpression) (v.elementAt(i))))
		return true;
	    //		System.out.println("Parameter:"+ACC);
		SetMemory(SP, ACC);
		SP = SP - 4;
	}
	Integer jumpIndex = (Integer) labels.find(exp.label().toString());
	if (jumpIndex == null) {
	    System.out.println("ERROR:  CallExpression");
	    System.out.println("        Undefined label:" + exp.label());
	    return true;
	    }
	ReturnAddr = IP;
	IP = jumpIndex.intValue();
	if (RunFunction()) return true;
	SP = SP + 4 * exp.actuals().size();
	ACC = Result;
	return false;
    }



    int GetMemory(int index) {
	return Memory[index / 4];
    }

    boolean EvaluateExpression(AATExpression exp) {
	if (exp instanceof AATCallExpression) {
	    if (EvalCallExp((AATCallExpression) exp))
		return true;
	} else if (exp instanceof AATConstant) {
	    ACC = ((AATConstant) exp).value();
	} else if (exp instanceof AATMemory) {
	    if (EvaluateExpression(((AATMemory) exp).mem()))
		return true;
	    ACC = GetMemory(ACC);
	} else if (exp instanceof AATOperator) {
	    AATOperator opexp = (AATOperator) exp;
	    if (EvaluateExpression(opexp.left()))
		return true;
	    if (opexp.operator() == AATOperator.NOT) {
		if (ACC != 0)
		    ACC = 0;
		else
		    ACC = 1;
		return false;
	    }

	    ESPStack[ESP++] = ACC;
	    if (EvaluateExpression(opexp.right())) {
		return true;
	    }
	    ESP--;
	    switch (opexp.operator()) {
	    case AATOperator.PLUS:
		ACC = ESPStack[ESP] + ACC;
		break;
	    case AATOperator.MINUS:
		ACC = ESPStack[ESP] - ACC;
		break;
	    case AATOperator.MULTIPLY:
		ACC = ESPStack[ESP] * ACC;
		break;
	    case AATOperator.DIVIDE:
		ACC = ESPStack[ESP] / ACC;
		break;
	    case AATOperator.AND:
		if (ACC !=0 && ESPStack[ESP] != 0)
		    ACC = 1;
		else
		    ACC = 0;
		break;
	    case AATOperator.OR:
		if (ACC !=0 || ESPStack[ESP] != 0)
		    ACC = 1;
		else
		    ACC = 0;
		break;
	    case AATOperator.EQUAL:
		if (ESPStack[ESP] == ACC)
		    ACC = 1;
		else
		    ACC = 0;
		break;
	    case AATOperator.NOT_EQUAL:
		if (ESPStack[ESP] != ACC)
		    ACC = 1;
		else
		    ACC = 0;
		break;
	    case AATOperator.LESS_THAN:
		if (ESPStack[ESP] < ACC)
		    ACC = 1;
		else
		    ACC = 0;
		break;

	    case AATOperator.LESS_THAN_EQUAL:
		if (ESPStack[ESP] <= ACC)
		    ACC = 1;
		else
		    ACC = 0;
		break;
	    case AATOperator.GREATER_THAN:
		if (ESPStack[ESP] > ACC)
		    ACC = 1;
		else
		    ACC = 0;
		break;

	    case AATOperator.GREATER_THAN_EQUAL:
	    		if (ESPStack[ESP] >= ACC)
		    ACC = 1;
		else
		    ACC = 0;
		break;
	    default:
		System.out.println("ERROR: Bad Operator:" + opexp.operator());
		return true;
	    }
	} else if (exp instanceof AATRegister) {
	    ACC = GetRegister(((AATRegister) exp));
	} else {
	    System.out.println("ERROR:  Unknown expression!!");
	    return true;
	}
	return false;
    }

    boolean EvalCallStm(AATCallStatement stm) {
	int i;
	if (stm.label().toString().compareTo("Print") == 0) {
	    if (EvaluateExpression((AATExpression) stm.actuals().elementAt(0)))
		return true;
	    System.out.print(ACC + " ");
	    return false;
	}
	if (stm.label().toString().compareTo("Println") == 0) {
	    System.out.println("");
	    return false;
	}
	for (i=stm.actuals().size()-1;i>=0;i--) {
	    if (EvaluateExpression((AATExpression) stm.actuals().elementAt(i)))
		return true;
	    //    System.out.println("Parameter:"+ACC);
	    SetMemory(SP, ACC);
	    SP = SP - 4;
	}
	Integer jumpIndex = (Integer) labels.find(stm.label().toString());
	if (jumpIndex == null) {
	    System.out.println("ERROR:  CallStatement");
	    System.out.println("        Undefined label" + stm.label());
	    return true;
	}
	ReturnAddr = IP;
	IP = jumpIndex.intValue();
	if (RunFunction()) return true;
	SP = SP + 4 * stm.actuals().size();
	return false;
    }



    boolean execute(AATStatement s) {
	if (s instanceof AATCallStatement) {
	    return EvalCallStm((AATCallStatement) s);
	} else if (s instanceof AATConditionalJump) {
	    if (EvaluateExpression(((AATConditionalJump) s).test()))
		return true;
	    if (ACC != 0) {
		Integer jumpIndex = (Integer) labels.find(((AATConditionalJump) s).label().toString());
		if (jumpIndex == null) {
		    System.out.println("ERROR: in AATConditionalJump");
		    System.out.println("       Trying to jump to :" + ((AATConditionalJump) s).label());
		    System.out.println("       Label does not exist!");
		    return true;
		}
		IP = jumpIndex.intValue();
	    }

	} else if (s instanceof AATEmpty) {
	    /* do nothing */
	} else if (s instanceof AATHalt) {
	    return true;
	} else if (s instanceof AATJump) {
	    Integer jumpIndex = (Integer) labels.find(((AATJump) s).label().toString());
	    if (jumpIndex == null) {
		System.out.println("ERROR: in AATJump");
                System.out.println("       Trying to jump to :" + ((AATJump) s).label());
		System.out.println("       Label does not exist!");
		return true;
	    }
	    IP = jumpIndex.intValue();
	} else if (s instanceof AATLabel) {
	    /* do nothing */
	} else if (s instanceof AATMove) {
	    if (((AATMove) s).lhs() instanceof AATMemory) {
		if (EvaluateExpression(((AATMemory) ((AATMove) s).lhs()).mem()))
		    return true;
		ESPStack[ESP++] = ACC;
		if (EvaluateExpression(((AATMove) s).rhs()))
		    return true;
		temp1 = ESPStack[--ESP];
		SetMemory(temp1,ACC);
	    } else if (((AATMove) s).lhs() instanceof AATRegister) {
		if (EvaluateExpression(((AATMove) s).rhs()))
		    return true;
		SetRegister((AATRegister) (((AATMove) s).lhs()), ACC);
	    } else {
		System.out.println("ERROR: in AATMove");
                System.out.println("       LHS must be AATMemory or AATRegister!");
		return true;
	    }
	} else if (s instanceof AATReturn) {
	    System.out.println("Interpreter ERROR");
	    System.out.println("   It should not be possible to get here (AATReturn)!");
	    System.out.println("   Please report to Prof. Galles!");
	    return true;
	} else if (s instanceof AATSequential) {
	    System.out.println("Interpreter ERROR");
	    System.out.println("   It should not be possible to get here!");
	    System.out.println("   All AATSequentials should have been removed by flatten!");
	    System.out.println("   Please report to Prof. Galles!");
	    return true;
	} else {
	    System.out.println("ERROR: malformed AAT");
	    return true;
	}
	return false;
    }


    boolean RunFunction() {
	while(true) {
	    if ((IP >= codeLength) ||
		(IP < 0))
		return true;
	    //	    System.out.println("-- Executing statement " + IP);
	    //   code[IP].Accept(pt);
	    IP++;
	    if (code[IP-1] instanceof AATReturn) {
		IP = ReturnAddr;
		return false;
	    }

	    if (execute(code[IP-1]))
		return true;
	}
    }



    public void run() {
	SP = (STACKSIZE - 1) * 4;
	ESP = 0;
	heap = (HEAPSIZE  + STACKSIZE - 1) * 4;
	ReturnAddr = -1;
	IP = 0;
	Integer mainLab = (Integer) labels.find("main1");
	if (mainLab == null) {
	    System.out.println("ERROR:  no main!");
	    return;
	}
	IP = mainLab.intValue();
	if (!RunFunction()) {
	    System.out.println("Program Completed");
	} else {
	    System.out.println("Error occured -- system aborted");
	}
    }



    public AATInterpreter(AATStatement program) {
	codeLength = countStatements(program);
	code = new AATStatement[codeLength];
	labels = new HashTable(127);
	pt = new AATPrintTree();
	FlattenCode(program,0);
	stdin = new BufferedReader (new InputStreamReader(System.in));
        ESPStack = new int[ESPSTACKSIZE];
        Memory = new int[STACKSIZE + HEAPSIZE];
    }


    AATStatement code[];
    int IP;
    int codeLength;

    HashTable labels;

    int ESPStack[];
    int Memory[];
    int SP;
    int ESP;
    int FP;
    int ACC;
    int Result;
    int ReturnAddr;
    int temp1;
    int temp2;
    int temp3;
    int temp4;
    int heap;
    BufferedReader stdin;
    AATPrintTree pt;
}
