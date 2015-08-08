import java.io.*;

class CodeGenerator implements AATVisitor {
    private final int wordSize = MachineDependent.WORDSIZE;


    public CodeGenerator(String output_filename) {
	       try {
	          output = new PrintWriter(new FileOutputStream(output_filename));
         } catch (IOException e) {
	          System.out.println("Could not open file "+output_filename+" for writing.");
	       }
	       EmitSetupCode();
    }

    public Object VisitCallExpression(AATCallExpression expression) {
      // Outputs same code as AATCallStatement, just need to store result
      VisitCallStatement(new AATCallStatement(expression.label(), expression.actuals()));
      emit("move " + Register.ACC() + ", " + Register.Result());

      return null;
    }

    public Object VisitMemory(AATMemory expression) {
      expression.mem().Accept(this);
      emit("lw " + Register.ACC() + ", 0(" + Register.ACC() + ")");
      return null;
    }


    public Object VisitOperator(AATOperator expression) {
      // All operators begin expressions with this code
      expression.left().Accept(this);
      emit("sw " + Register.ACC() +", 0(" + Register.ESP() + ")");
      emit("addi " + Register.ESP() +"," + Register.ESP() + ", " + -wordSize);
      expression.right().Accept(this);
      emit("addi " + Register.ESP() +"," + Register.ESP() + "," + wordSize);
      emit("lw " + Register.Tmp1() +", 0(" + Register.ESP() + ")");

      switch(expression.operator()) {
        case AATOperator.PLUS: {
          emit("add " + Register.ACC() +", " + Register.Tmp1() + "," + Register.ACC());
          return null;
        }
        case AATOperator.MINUS: {
          emit("sub " + Register.ACC() +", " + Register.Tmp1() + "," + Register.ACC());
          return null;
        }
        case AATOperator.MULTIPLY: {
          emit("mult " + Register.ACC() +", " + Register.Tmp1());
          emit("mflo " + Register.ACC());
          return null;
        }
        case AATOperator.DIVIDE: {
          emit("div " + Register.Tmp1() + "," + Register.ACC());
          emit("mflo " + Register.ACC());
          return null;
        }
        case AATOperator.AND: {
          emit("and " + Register.ACC() +", " + Register.Tmp1() + "," + Register.ACC());
          return null;
        }
        case AATOperator.OR: {
          emit("or " + Register.ACC() +", " + Register.Tmp1() + "," + Register.ACC());
          return null;
        }
        case AATOperator.EQUAL: {
          Label equalBegin = new Label("EqualBegin");
          // if t1 & ACC are equal, jump to beginning
          emit("beq " + Register.Tmp1() + "," + Register.ACC() + "," + equalBegin);
          emit("li " + Register.ACC() + ", 0");
          Label equalEnd = new Label("EqualEnd");
          emit("j " + equalEnd);
          emit(equalBegin + ":");
          emit("addi " + Register.ACC() + ", " + Register.Zero() + ", 1");
          emit(equalEnd + ":");
          return null;
        }
        case AATOperator.NOT_EQUAL: {
          Label notEqualBegin = new Label("NotEqualBegin");
          // if t1 & ACC are not equal, jump to beginning
          emit("bne " + Register.Tmp1() + "," + Register.ACC() + "," + notEqualBegin);
          emit("li " + Register.ACC() + ", 0");
          Label notEqualEnd = new Label("NotEqualEnd");
          emit("j " + notEqualEnd);
          emit(notEqualBegin + ":");
          emit("addi " + Register.ACC() + ", " + Register.Zero() + ", 1");
          emit(notEqualEnd + ":");
          return null;
        }
        case AATOperator.GREATER_THAN: {
          emit("slt " + Register.ACC() + "," + Register.ACC() + "," + Register.Tmp1());
          return null;
        }
        case AATOperator.GREATER_THAN_EQUAL: {
          emit("addi " + Register.ACC() +"," + Register.ACC() + ", 1");
          emit("slt " + Register.ACC() + "," + Register.ACC() + "," + Register.Tmp1());
          return null;
        }
        case AATOperator.LESS_THAN: {
          emit("slt " + Register.ACC() + "," + Register.Tmp1() + "," + Register.ACC());
          return null;
        }
        case AATOperator.LESS_THAN_EQUAL: {
          emit("addi " + Register.ACC() +"," + Register.ACC() + ", 1");
          emit("slt " + Register.ACC() + "," + Register.Tmp1() + "," + Register.ACC());
          return null;
        }
        case AATOperator.NOT: {
          emit("li " + Register.Tmp1() + ", 1");
          emit("slt " + Register.ACC() + "," + Register.ACC() + "," + Register.Tmp1());
          return null;
        }
        default: {
          System.out.println("Bad Operator");
          return null;
        }
      }
    }

    public Object VisitRegister(AATRegister expression) {
      emit("addi " + Register.ACC() + ", " + expression.register() + ", " + 0);
      return null;
    }

    public Object VisitCallStatement(AATCallStatement statement) {
      int actuals = statement.actuals().size();

      // Go through actuals, storing on 0($SP), update SP
      for (int i = 0; i < actuals; i++) {
        ((AATExpression) statement.actuals().elementAt(i)).Accept(this);
        emit("sw " + Register.ACC() + ", 0(" + Register.SP() + ")");
        emit("addi " + Register.SP() + ", " + Register.SP() + ", " + (-wordSize));
      }

      // Calls function
      emit("jal " + statement.label());

      // Reset SP
      if (actuals > 0) {
        emit("addi " + Register.SP() + "," + Register.SP() + "," + ((wordSize)*actuals));
      }

      emit("addi " + Register.ACC() + "," + Register.Result() + "," + 0);

      return null;
    }

    public Object VisitConditionalJump(AATConditionalJump statement) {
      statement.test().Accept(this);
      emit("bgtz "+ Register.ACC() + " " + statement.label());
      return null;

    }

    public Object VisitEmpty(AATEmpty statement) {
      // This is empty because we are visiting an empty statement
      return null;
    }

    public Object VisitJump(AATJump statement) {
    	// j (label)
      emit("j " + statement.label());
    	return null;
    }

    public Object VisitLabel(AATLabel statement) {
    	emit(statement.label() + ":");
    	return null;
    }

    public Object VisitMove(AATMove statement) {
        // From the text
        if (statement.lhs() instanceof AATRegister) {
          statement.rhs().Accept(this);
          emit("addi " + ((AATRegister) statement.lhs()).register() + "," + Register.ACC() + ", 0");
        } else {
          ((AATMemory) statement.lhs()).mem().Accept(this);
          emit("sw " + Register.ACC() + ", 0(" + Register.ESP() + ")");
          emit("addi " + Register.ESP() + "," + Register.ESP() + "," + -wordSize);
          statement.rhs().Accept(this);
          emit("addi " + Register.ESP() + "," + Register.ESP() + "," + wordSize);
          emit("lw " + Register.Tmp1() + ", 0(" + Register.ESP() + ")");
          emit("sw " + Register.ACC() + ", 0(" + Register.Tmp1() + ")");
        }

        return null;
    }

    public Object VisitReturn(AATReturn statement) {
    	// jr $RA
      emit("jr " + Register.ReturnAddr());
    	return null;
    }

    public Object VisitHalt(AATHalt halt) {
    	return null;
    }

    public Object VisitSequential(AATSequential statement) {
      // Accept both sides of the sequential
      statement.left().Accept(this);
      statement.right().Accept(this);
      return null;
    }

    public Object VisitConstant(AATConstant expression) {
      // addi $ACC, $zero, constant
      emit("addi " + Register.ACC() + "," + Register.Zero() + "," + expression.value());
      return null;
    }

    private void emit(String assem) {
    	assem = assem.trim();
    	if (assem.charAt(assem.length()-1) == ':')
          output.println(assem);
    	else
    	    output.println("\t" + assem);
    }

    public void GenerateLibrary() {
    	emit("Print:");
    	emit("lw $a0, 4(" + Register.SP() + ")");
    	emit("li $v0, 1");
    	emit("syscall");
    	emit("li $v0,4");
    	emit("la $a0, sp");
    	emit("syscall");
    	emit("jr $ra");
    	emit("Println:");
    	emit("li $v0,4");
    	emit("la $a0, cr");
    	emit("syscall");
    	emit("jr $ra");
    	emit("Read:");
    	emit("li $v0,5");
    	emit("syscall");
    	emit("jr $ra");
    	emit("allocate:");
    	emit("la " + Register.Tmp1() + ", HEAPPTR");
    	emit("lw " + Register.Result() + ",0(" + Register.Tmp1() + ")");
    	emit("lw " + Register.Tmp2() + ", 4(" + Register.SP() + ")");
    	emit("sub " + Register.Tmp2() + "," + Register.Result() + "," + Register.Tmp2());
    	emit("sw " + Register.Tmp2() + ",0(" + Register.Tmp1() + ")");
    	emit("jr $ra");
    	emit(".data");
    	emit("cr:");
    	emit(".asciiz \"\\n\"");
    	emit("sp:");
    	emit(".asciiz \" \"");
            emit("HEAPPTR:");
    	emit(".word 0");
    	output.flush();
    }

    private void EmitSetupCode() {
    	emit(".globl main");
    	emit("main:");
    	emit("addi " + Register.ESP() + "," + Register.SP() + ",0");
    	emit("addi " + Register.SP() + "," + Register.SP() + "," +
    	     - wordSize * STACKSIZE);
    	emit("addi " + Register.Tmp1() + "," + Register.SP() + ",0");
    	emit("addi " + Register.Tmp1() + "," + Register.Tmp1() + "," +
    	     - wordSize * STACKSIZE);
    	emit("la " + Register.Tmp2() + ", HEAPPTR");
    	emit("sw " + Register.Tmp1() + ",0(" + Register.Tmp2() + ")");
      emit("sw " + Register.ReturnAddr() + "," + wordSize  + "("+ Register.SP() + ")");
     	emit("jal main1");
    	emit("li $v0, 10");
      emit("syscall");
    }

    private final int STACKSIZE = 1000;
    private PrintWriter output;
}
