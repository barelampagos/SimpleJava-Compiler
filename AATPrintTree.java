import java.util.Vector;

public class AATPrintTree implements  AATVisitor {

    static final int indentstep  = 3;

    int indentlevel = 0;

    void Print(String word) {
 	int i;
	for (i=0; i < indentstep * indentlevel; i++)
	    System.out.print(" ");
	System.out.println(word);
    }


    public Object VisitCallExpression(AATCallExpression call) {
	int i;
	Print("Call Expression: "+call.label());
	indentlevel++;
	for (i=0; i<call.actuals().size(); i++) 
	    ((AATExpression) call.actuals().elementAt(i)).Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitCallStatement(AATCallStatement call) {
	Print("Call Statement: "+call.label());
	indentlevel++;
	for (int i=0; i<call.actuals().size(); i++) 
	    ((AATExpression) call.actuals().elementAt(i)).Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitConditionalJump(AATConditionalJump cjump) {
	Print("Conditional Jump: " + cjump.label());
	indentlevel++;
	cjump.test().Accept(this);
	indentlevel--;
	return null;
    }
    
    public Object VisitConstant(AATConstant constant) {
	Print(Integer.toString(constant.value()));
	return null;
    }

    public Object VisitEmpty(AATEmpty empty) {
	Print("Empty");
	return null;
    }

    public Object VisitHalt(AATHalt halt) {
	Print("Halt");
	return null;
    }

    public Object VisitJump(AATJump jump) {
	Print("Jump: " + jump.label());
	return null;
    }

    public Object VisitLabel(AATLabel label) {
	Print("Label: " + label.label());
	return null;
    }

    public Object VisitMemory(AATMemory mem) {
	Print("Memory");
	indentlevel++;
	mem.mem().Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitMove(AATMove move) {
	Print("Move");
	indentlevel++;
	move.lhs().Accept(this);
	move.rhs().Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitOperator(AATOperator oper) {
	Print(AATOperator.names[oper.operator()]);
	indentlevel++;
	oper.left().Accept(this);
	oper.right().Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitRegister(AATRegister reg) {
	Print("Register: " + reg.register());
	return null;
    }

    public Object VisitReturn(AATReturn ret) {
	Print("Return");
	return null;
    }

    public Object VisitSequential(AATSequential seq) {
	seq.left().Accept(this);
	seq.right().Accept(this);
	return null;
    }

}
