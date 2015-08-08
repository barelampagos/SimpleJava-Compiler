import java.util.Vector;

public class ASTPrintTree implements  ASTVisitor {

    static final int indentstep = 3;

    int indentlevel = 0;

    void Print(String word) {
 	int i;
	for (i=0; i < indentstep * indentlevel; i++)
	    System.out.print(" ");
	System.out.println(word);
    }

    public Object VisitArrayVariable(ASTArrayVariable array) {
	Print("Array Variable (base/index)");
	indentlevel++;
	array.base().Accept(this);
	array.index().Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitAssignmentStatement(ASTAssignmentStatement assign) {
	Print("Assign (lhs/rhs)");
	indentlevel++;
	assign.variable().Accept(this);
	assign.value().Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitBaseVariable(ASTBaseVariable base) {
	Print("Base Variable " + base.name());
	return null;
    }

    public Object VisitBooleanLiteral(ASTBooleanLiteral boolliteral) {
	if (boolliteral.value() == true) 
	    Print("TRUE");
	else
	    Print("FALSE");
	return null;
    }

    public Object VisitClass(ASTClass classs) {
	Print("Class: " + classs.name());
	indentlevel++;
	if (classs.variabledefs() != null)
	    classs.variabledefs().Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitClasses(ASTClasses classes) { 
	int i;

	for (i=0; i<classes.size();i++) 
	    classes.elementAt(i).Accept(this);
	return null;
    }

    public Object VisitClassVariable(ASTClassVariable classvariable) {
	Print("Class Variable:");
	indentlevel++;
	classvariable.base().Accept(this);
	Print(classvariable.variable());
	indentlevel--;
	return null;	
    }

    public Object VisitDoWhileStatement(ASTDoWhileStatement dowhile) {
	Print("Do-while (test/body)");
	indentlevel++;
	dowhile.test().Accept(this);
	dowhile.body().Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitEmptyStatement(ASTEmptyStatement empty) {
	Print("Empty Statement");
	return null;
    }
    
    public Object VisitForStatement(ASTForStatement forstmt) {
	Print("For (initialize/test/increment/body)");
	indentlevel++;
	forstmt.initialize().Accept(this);
	forstmt.test().Accept(this);
	forstmt.increment().Accept(this);
	forstmt.body().Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitFormal(ASTFormal formal) {
	String array = "";
	for (int i = 0; i < formal.arraydimension();i++) {
	    array = array + "[]";
	}
	Print(formal.type() + " " + formal.name() + array);
	return null;
    }
    
    public Object VisitFormals(ASTFormals formals) { 
	int i;
	if ((formals == null) || formals.size() == 0)
	    Print("No formal parameters");
	else
	    Print("Formals:");
	indentlevel++;
	for (i=0; i<formals.size(); i++) {
	    formals.elementAt(i).Accept(this);
	}
	indentlevel--;
	return null;
    }
    
    public Object VisitFunction(ASTFunction function) {
	Print("Function Definition: " + function.name() + " Return type: " + function.type());
	// Print return type
	indentlevel++;
	if (function.formals() != null) 
	    function.formals().Accept(this);
	function.body().Accept(this);
	indentlevel--;
	return null;
    }

    public Object  VisitFunctionDefinitions(ASTFunctionDefinitions functiondefs) { 
	int i;
	for (i=0; i < functiondefs.size(); i++)
	    functiondefs.elementAt(i).Accept(this);
	return null;
    }

    public Object VisitFunctionCallExpression(ASTFunctionCallExpression functioncall) { 
	int i;
	Print("Function Call: "+functioncall.name());
	indentlevel++;
	for (i=0; i<functioncall.size(); i++)
		functioncall.elementAt(i).Accept(this);
	indentlevel--;
	return null;
    }
    

    public Object VisitFunctionCallStatement(ASTFunctionCallStatement functioncall) {
	int i;
	Print("Function Call: "+functioncall.name());
	indentlevel++;
	for (i=0; i<functioncall.size(); i++)
	    functioncall.elementAt(i).Accept(this);
	indentlevel--;
	return null;
    }



    public Object VisitIfStatement(ASTIfStatement ifstmt) {
	Print("If (test/if body/else body)");
	indentlevel++;
	ifstmt.test().Accept(this);
	ifstmt.thenstatement().Accept(this);
	if (ifstmt.elsestatement() != null)
	    ifstmt.elsestatement().Accept(this);
	else
	    Print("No else statement");
	indentlevel--;
	return null;
    }


    public Object VisitIntegerLiteral(ASTIntegerLiteral literal) {
	Print(Integer.toString(literal.value()));
	return null;
    }

    public Object VisitNewArrayExpression(ASTNewArrayExpression newarray) {
	String suffix = "";
	int i;
	for(i=0; i< newarray.arraydimension(); i++)
	    suffix = suffix + "[]";
	Print("New Array of "+newarray.type() + suffix);
	// print out dimensionality of element  
	indentlevel++;
	newarray.elements().Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitNewClassExpression(ASTNewClassExpression newclass) {
	Print("New " + newclass.type());
	return null;
    }

    public Object VisitOperatorExpression(ASTOperatorExpression opexpr) {
	Print(ASTOperatorExpression.names[opexpr.operator()]);
	indentlevel++;
	opexpr.left().Accept(this);
	opexpr.right().Accept(this);
	indentlevel--;
	return null;
    }


    public Object VisitProgram(ASTProgram program) { 
	if (program.classes() != null) 
	    program.classes().Accept(this);
	if (program.functiondefinitions() != null)
	    program.functiondefinitions().Accept(this);
	return null;
    }

    public Object VisitPrototype(ASTPrototype prototype) {
	int i;
	Print("Prototype: " + prototype.type() +" " +  prototype.name());
	indentlevel++;
	if (prototype.formals() != null) {
	    for (i=0; i < prototype.formals().size(); i++)
		prototype.formals().elementAt(i).Accept(this);
	    if (prototype.formals().size() == 0)
		Print("No Formals");

	} else {
	    Print("No Formals");
	}
	indentlevel--;
	return null;
    }

    public Object VisitReturnStatement(ASTReturnStatement ret) {
	Print("Return");
	indentlevel++;
	if (ret.value() != null)
	    ret.value().Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitStatements(ASTStatements statements) { 
	int i;
	Print("{");
	indentlevel++;
	for (i=0; i<statements.size(); i++)
	    statements.elementAt(i).Accept(this);
	indentlevel--;
	Print("}");
	return null;
    }

    public Object VisitUnaryOperatorExpression(ASTUnaryOperatorExpression operator) {
	Print(ASTUnaryOperatorExpression.names[operator.operator()]);
	indentlevel++;
	operator.operand().Accept(this);
	indentlevel--;
	return null;
    }

    public Object VisitInstanceVariableDef(ASTInstanceVariableDef variabledef) {
	String array = "";
	for (int i = 0; i < variabledef.arraydimension();i++) {
	    array = array + "[]";
	}
	Print(variabledef.type() + " " + variabledef.name() + array);
	return null;
    }


    public Object VisitInstanceVariableDefs(ASTInstanceVariableDefs variabledefs) { 
	int i;
	for (i=0; i<variabledefs.size(); i++) {
	    variabledefs.elementAt(i).Accept(this);
	}
	return null;
    }

    public Object VisitVariableExpression(ASTVariableExpression variableexpression) { 
	variableexpression.variable().Accept(this);
	return null;
    }

    public Object VisitWhileStatement(ASTWhileStatement whilestatement) {
	Print("While (test/body)");
	indentlevel++;
	whilestatement.test().Accept(this);
	whilestatement.body().Accept(this);
	indentlevel--;
	return null;
    }   
    public Object VisitVariableDefStatement(ASTVariableDefStatement variabledef) {
	String array = "";
	for (int i = 0; i < variabledef.arraydimension();i++) {
	    array = array + "[]";
	}
	Print(variabledef.type() + " " + variabledef.name() + array);
	indentlevel++;
	if (variabledef.init() != null) {
	    variabledef.init().Accept(this);
	}
	indentlevel--;
	return null;
	

    }


 
}

