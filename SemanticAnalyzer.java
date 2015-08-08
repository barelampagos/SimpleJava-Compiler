import java.util.Vector;
import java.util.Set;

public class SemanticAnalyzer implements ASTVisitor{

	private VariableEnvironment variableEnv;
	private FunctionEnvironment functionEnv;
	private TypeEnvironment typeEnv;
	private Type returnType;
	private AATBuildTree bt;
	private int offset;
	private final int sizeWord = MachineDependent.WORDSIZE;
	private Label funcEndLabel;

	public SemanticAnalyzer() {
		variableEnv = new VariableEnvironment();
		functionEnv = new FunctionEnvironment();
		typeEnv = new TypeEnvironment();
		functionEnv.addBuiltinFunctions();
		returnType = null;
		bt = new AATBuildTree();
	}

	public Type checkType(String type, String name, int arraydim, int line) {
		Type varType = typeEnv.find(type);

		if (arraydim > 0)  {
			varType = checkArray(type, name, arraydim, line, varType);
		} else {
			if (varType == null) {
				CompError.message(line, type + " is an undefined type.");
			} else {
				return varType;
			}
		}

		return varType;
	}

	public Type checkArray(String type, String name, int arraydim, int line, Type varType) {

		String newType = type;
		for (int i = 0; i < arraydim; i++) {
			// debug System.out.println("Looking for:  " + type);
			newType += "[]";
			// debug System.out.println("Creating new Array: " + type);
		}

			varType = typeEnv.find(newType);
			if (varType == null) {
				// Create new Type of "type"
				varType = new ArrayType(checkType(type, name, arraydim - 1, line));
				//System.out.println("[ARRAY TYPE] name: " + type + ", New Array type: " + arrayType);
				typeEnv.insert(newType, varType);
			}


		return varType;
	}

	@Override
	public Object VisitArrayVariable(ASTArrayVariable array) {
		//Analyze index --> int
		//Analyze base --> array type
		//Return type
		TypeClass index = (TypeClass) array.index().Accept(this);
		Type indexType = index.type();

		TypeClass base = (TypeClass) array.base().Accept(this);

		if (base != null) {
			Type baseType = base.type();
			// System.out.println("[ARRAY VARIABLE BASE TYPE] " + baseType);

			if (indexType != IntegerType.instance())
				CompError.message(array.line(), "Array Index - Type must be an integer.");

			if (baseType instanceof ArrayType) {
				Type t = ((ArrayType) baseType).type();

				return new TypeClass(t, bt.arrayVariable(base.tree(), index.tree(), sizeWord));

			} else {
				CompError.message(array.line(), "Array Type - Base Variable must be an array type.");
			}
		}

		return new TypeClass(IntegerType.instance(), null);
	}

	@Override
	public Object VisitAssignmentStatement(ASTAssignmentStatement assignSt) {
		TypeClass lhs = (TypeClass) assignSt.variable().Accept(this);
		TypeClass rhs = (TypeClass) assignSt.value().Accept(this);

		if (lhs != null && rhs != null) {
			// System.out.println("LHS: " +  lhs.type() + ", RHS: " + rhs.type());
			if (lhs.type() != rhs.type()) {
				CompError.message(assignSt.line(), "Assignment Statement - types differ.");
			}

			return bt.assignmentStatement(lhs.tree(), rhs.tree());
		}

		return bt.emptyStatement();
	}

	@Override
	public Object VisitBaseVariable(ASTBaseVariable base) {
		VariableEntry entry = variableEnv.find(base.name());
		TypeClass typeClass = new TypeClass(null, null);

		if (entry == null) {
			CompError.message(base.line(), "Variable "  + base.name() + " not declared");
		} else {
			// System.out.println("[BASE VARIABLE] " + base.name() + " - " + entry.type());
			typeClass = new TypeClass(entry.type(), bt.baseVariable(entry.offset()));
		}

		return typeClass;
	}

	@Override
	public Object VisitBooleanLiteral(ASTBooleanLiteral blit){
  		int booleanVal = 0;
			if (blit.value() == true) {
					booleanVal = 1;
			}

			return new TypeClass(BooleanType.instance(), bt.constantExpression(booleanVal));
  	}

	@Override
	public Object VisitClass(ASTClass classs) {

		VariableEnvironment classEnv = VisitInstanceVariableDefs(classs.variabledefs());

		ClassType className = new ClassType(classEnv);
		typeEnv.insert(classs.name(), className);

		return new TypeClass(IntegerType.instance(), (AATExpression) null);
	}

	@Override
	public Object VisitClasses(ASTClasses classes) {
		for (int i = 0; i < classes.size(); i++) {
			classes.elementAt(i).Accept(this);
		}

		return new TypeClass(null, null);
	}

	@Override
	public Object VisitClassVariable(ASTClassVariable classvariable) {
		TypeClass base = (TypeClass) classvariable.base().Accept(this);

		if (base != null) {
			Type baseType = base.type();

			if (!(baseType instanceof ClassType)) {
				CompError.message(classvariable.line(), "Class Variable - Instance variable " + classvariable.variable() + " never defined.");

			} else {

				ClassType classVarType = (ClassType) baseType;

				VariableEnvironment env = classVarType.variables();
				VariableEntry entry = env.find(classvariable.variable());

				if (entry != null) {
					Type t = entry.type();
					return new TypeClass(t, bt.classVariable(base.tree(), entry.offset()));
				}
				else {
					CompError.message(classvariable.line(), "Instance variable never created.");
				}
			}
		}

		return new TypeClass(IntegerType.instance(), (AATExpression) null);
	}

	@Override
	public Object VisitDoWhileStatement(ASTDoWhileStatement doWhileSt) {
		TypeClass test = (TypeClass) doWhileSt.test().Accept(this);

		if (test.type() != BooleanType.instance())
			CompError.message(doWhileSt.line(), "Do While Statement - test must be a boolean");

		return bt.dowhileStatement(test.tree(), (AATStatement) doWhileSt.body().Accept(this));
	}

	@Override
	public Object VisitEmptyStatement(ASTEmptyStatement empty) {

		return bt.emptyStatement();
	}

	@Override
	public Object VisitForStatement(ASTForStatement forstmt) {

		AATStatement init = (AATStatement) forstmt.initialize().Accept(this);
		AATStatement incr = (AATStatement) forstmt.increment().Accept(this);
		TypeClass test = (TypeClass) forstmt.test().Accept(this);

		if (test.type() != BooleanType.instance()) {
			CompError.message(forstmt.line(), "For Statement - test must be a boolean");
		}
		else {
			return bt.forStatement(init, test.tree(), incr, (AATStatement) forstmt.body().Accept(this));
		}

		return bt.emptyStatement();
	}

	@Override
	public Object VisitFormal(ASTFormal formal) {
		VariableEntry formalVarEntry = variableEnv.find(formal.name());

		//Check if variable exists in variableEnv

		//Type formalVarType = typeEnv.find(formal.type());
		Type formalVarType = checkType(formal.type(), formal.name(), formal.arraydimension(), formal.line());
		if (formalVarEntry == null) {
			variableEnv.insert(formal.name(), new VariableEntry(formalVarType, offset));
			offset -= sizeWord;
		}
		//System.out.println("FORMAL TYPE OF " + formal.name() + ": " + formalVarType);

		//Check if formal is valid type
		if (formalVarType == null) {
			CompError.message(formal.line(), "Formal parameter has invalid type.");
			formalVarType = IntegerType.instance();
		}

		return formalVarType;
	}

	@Override
	public Object VisitFormals(ASTFormals formals) {
		Vector formaltypes = new Vector(formals.size());
		offset = -4;

		for (int i = 0; i < formals.size(); i++) {
			formaltypes.addElement(formals.elementAt(i).Accept(this));
		}

		return formaltypes;
	}

	@Override
	public Object VisitFunction(ASTFunction function) {

		FunctionEntry fEntry = functionEnv.find(function.name());
		Type retType = typeEnv.find(function.type());
		returnType = retType;
		Label startlabel;

		// If prototype exists in functionEnv
		if (fEntry != null) {

			// Check if return type exists & is the same as in the typeEnv
			if (retType != fEntry.result()) {
					CompError.message(function.line(), "Invalid Return Type.");
			}

			// Check for:
			// # of parameters is equal to # in prototype
			// type of parameters equal to those in prototype
			Vector formals = fEntry.formals();
			if (formals.size() != function.formals().size()) {
					CompError.message(function.line(), "Number of formal parameters does not equal number from prototype.");
			} else {
					for (int i = 0; i < formals.size(); i++) {

							//System.out.println("formals.elementAt(i): " + ((ASTFormal) formals.elementAt(i)).type() + "\nfunction.formals().elementAt(i): " + function.formals().elementAt(i).type());
							ASTFormal f = (ASTFormal)formals.elementAt(i);

							if (!(f.type().equals(function.formals().elementAt(i).type()))){
								CompError.message(function.line(), "Formal Parameter types differ from prototype.");
							}

					}
			}

			startlabel = fEntry.startlabel();
			funcEndLabel = fEntry.endlabel();

		}
		// Function does not have prototype
		else {
			// Create a new function entry and insert into funcEnv
			//		- Get formals & types
			Vector<ASTFormal> formals = new Vector<ASTFormal>();
			for (int i = 0; i < function.formals().size(); i++) {
				formals.addElement(function.formals().elementAt(i));
			}

			startlabel = new Label(function.name());
			funcEndLabel = new Label(function.name());

			fEntry = new FunctionEntry(retType, formals, startlabel, funcEndLabel);
			functionEnv.insert(function.name(), fEntry);

		}

		variableEnv.beginScope();

		function.formals().Accept(this);

		AATStatement functionBody = (AATStatement) function.body().Accept(this);
		int frameSize = variableEnv.size();
		//System.out.println("[FRAMESIZE]: " + frameSize);

		variableEnv.endScope();

		// Reset returnType
		returnType = null;

		return bt.functionDefinition(functionBody, frameSize, startlabel, funcEndLabel);
	}


	@Override
	public Object VisitFunctionCallExpression(ASTFunctionCallExpression functioncall) {

		FunctionEntry fEntry = functionEnv.find(functioncall.name());
		Label startLabel = new Label(functioncall.name());
		Type retType = null;

		if (fEntry == null) {
			CompError.message(functioncall.line(), functioncall.name() + " function has not been declared");

			return new TypeClass(null, null);
		}
		else {

			Vector<Type> formalTypes = new Vector<Type>();
			Vector<AATExpression> formalTrees = new Vector<AATExpression>();

			for (int i = 0; i < functioncall.size(); i++) {
					TypeClass formal = (TypeClass) functioncall.elementAt(i).Accept(this);
					formalTypes.addElement(formal.type());
					formalTrees.addElement(formal.tree());
			}

			startLabel = fEntry.startlabel();
			retType = fEntry.result();

			if (fEntry.formals().size() != functioncall.size()) {
					CompError.message(functioncall.line(), "Number of formal parameters does not equal number from prototype.");
			}
			else {

				for (int j = 0; j < functioncall.size(); j++) {
					ASTFormal f = (ASTFormal) fEntry.formals().elementAt(j);
					Type t = checkType(f.type(), f.name(), f.arraydimension(), f.line());

					if (t != formalTypes.elementAt(j)) {
						CompError.message(functioncall.line(), "Type of parameter in function: " + functioncall.name() + " does not match function prototype.");
					}
				}

			}

			return new TypeClass(retType, bt.callExpression(formalTrees, startLabel));
		}
	}

	@Override
	public Object VisitFunctionCallStatement(ASTFunctionCallStatement functioncall) {

		FunctionEntry function = functionEnv.find(functioncall.name());
		if (function == null) {
				CompError.message(functioncall.line(), functioncall.name() + " has not been declared.");
		} else {

				Vector<AATExpression> formalTrees = new Vector<AATExpression>();

				for (int i = 0; i < functioncall.size(); i++) {
						TypeClass formal = (TypeClass) functioncall.elementAt(i).Accept(this);
						formalTrees.addElement(formal.tree());
				}

				return bt.callStatement(formalTrees, function.startlabel());
		}

		return bt.callStatement(null, null);
	}

	@Override
	public Object VisitIfStatement(ASTIfStatement ifstmt) {
		TypeClass test = (TypeClass) ifstmt.test().Accept(this);
		Type testType = test.type();

		if (testType != BooleanType.instance())
			CompError.message(ifstmt.line(), "If Statement - test must be a boolean");

		AATStatement thenbody = (AATStatement) ifstmt.thenstatement().Accept(this);
		AATStatement elsebody = bt.emptyStatement();

		if (ifstmt.elsestatement() != null)
				elsebody = (AATStatement) ifstmt.elsestatement().Accept(this);

		return bt.ifStatement(test.tree(), thenbody, elsebody);
	}

	@Override
	public Object VisitIntegerLiteral(ASTIntegerLiteral lit) {
  		return new TypeClass(typeEnv.find("int"), bt.constantExpression(lit.value()));
  }

	@Override
	public Type VisitInstanceVariableDef(ASTInstanceVariableDef variabledef) {

		Type type = checkType(variabledef.type(), variabledef.name(), variabledef.arraydimension(), variabledef.line());

		if (type == null) {
			CompError.message(variabledef.line(), "Variable type does not exist.");
		}

		offset += sizeWord;

		return type;
	}

	@Override
	public VariableEnvironment VisitInstanceVariableDefs(ASTInstanceVariableDefs variabledefs) {

		VariableEnvironment instanceVars = new VariableEnvironment();

		//System.out.println("Instance variable environment: ");

		for (int i = 0; i < variabledefs.size(); i++) {
			//variabledefs.elementAt(i).Accept();
			Type type = VisitInstanceVariableDef(variabledefs.elementAt(i));

			if (type != null) {
				VariableEntry vEntry = new VariableEntry(type);
				instanceVars.insert(variabledefs.elementAt(i).name(), vEntry);
			}
		}

		return instanceVars;
	}

	@Override
	public Object VisitNewArrayExpression(ASTNewArrayExpression newarray) {
		TypeClass elements = (TypeClass) newarray.elements().Accept(this);

		Type array = checkType(newarray.type(), newarray.type(), newarray.arraydimension(), newarray.line());

		if (!(array instanceof ArrayType)) {
			CompError.message(newarray.line(), newarray.type() + "has not not been defined.");
		}
		return new TypeClass(array, bt.allocate(new AATOperator(elements.tree(), new AATConstant(sizeWord), AATOperator.MULTIPLY)));
	}


	public Object VisitNewClassExpression(ASTNewClassExpression newclass) {
		Type t = typeEnv.find(newclass.type());
		int size = sizeWord;

		if (!(t instanceof ClassType)) {
			CompError.message(newclass.line(), newclass.type() + "has not been defined.");

			// If not instance of Class Type --> Default to int type
			t = typeEnv.find("int");
		} else {
			size = (((ClassType) t).variables().size()) * sizeWord;
		}

		return new TypeClass(t, bt.allocate(bt.constantExpression(size)));
	}

	@Override
	public Object VisitOperatorExpression(ASTOperatorExpression opexpr)
	{
		TypeClass lhs = (TypeClass) opexpr.left().Accept(this);
		TypeClass rhs = (TypeClass) opexpr.right().Accept(this);

		if (lhs != null && rhs != null) {
				AATExpression leftTree = lhs.tree();
				AATExpression rightTree = rhs.tree();

				switch (opexpr.operator())
				{
				case ASTOperatorExpression.PLUS:
						if (lhs.type() != IntegerType.instance() || rhs.type() != IntegerType.instance()) {
							CompError.message(opexpr.line(),
									"+ operator requires integer operands");
						}
						return new TypeClass(IntegerType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.PLUS));

				case ASTOperatorExpression.MINUS:
						if (lhs.type() != IntegerType.instance() || rhs.type() != IntegerType.instance())
						{
							CompError.message(opexpr.line(), "- operator requires integer operands");
						}
						return new TypeClass(IntegerType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.MINUS));

				case ASTOperatorExpression.MULTIPLY:
						if (lhs.type() != IntegerType.instance() || rhs.type() != IntegerType.instance()) {
							CompError.message(opexpr.line(),
									"* operator requires integer operands");
						}
						return new TypeClass(IntegerType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.MULTIPLY));

				case ASTOperatorExpression.DIVIDE:
						if (lhs.type() != IntegerType.instance() || rhs.type() != IntegerType.instance()) {
							CompError.message(opexpr.line(),
									"/ operator requires integer operands");
						}
						return new TypeClass(IntegerType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.DIVIDE));

				case ASTOperatorExpression.AND:
						if (lhs.type() != BooleanType.instance() || rhs.type() != BooleanType.instance()) {
							CompError.message(opexpr.line(),
									"&& operator requires boolean operands");
						}
						return new TypeClass(BooleanType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.AND));

				case ASTOperatorExpression.OR:
						if (lhs.type() != BooleanType.instance() || rhs.type() != BooleanType.instance()) {
							CompError.message(opexpr.line(),
									"|| operator requires boolean operands");
						}
						return new TypeClass(BooleanType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.OR));

				case ASTOperatorExpression.EQUAL:
						return new TypeClass(BooleanType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.EQUAL));

				case ASTOperatorExpression.NOT_EQUAL:
						return new TypeClass(BooleanType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.NOT_EQUAL));
				case ASTOperatorExpression.LESS_THAN:
						if (lhs.type() != IntegerType.instance() || rhs.type() != IntegerType.instance()) {
							CompError.message(opexpr.line(),
									"< operator requires integer operands");
						}
						return new TypeClass(BooleanType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.LESS_THAN));

				case ASTOperatorExpression.LESS_THAN_EQUAL:
						if (lhs.type() != IntegerType.instance() || rhs.type() != IntegerType.instance()) {
							CompError.message(opexpr.line(),
									"<= operator requires integer operands");
						}
						return new TypeClass(BooleanType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.LESS_THAN_EQUAL));

				case ASTOperatorExpression.GREATER_THAN:
						if (lhs.type() != IntegerType.instance() || rhs.type() != IntegerType.instance()) {
							CompError.message(opexpr.line(),
									"> operator requires integer operands");
						}
						return new TypeClass(BooleanType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.GREATER_THAN));

				case ASTOperatorExpression.GREATER_THAN_EQUAL:
						if (lhs.type() != IntegerType.instance() || rhs.type() != IntegerType.instance()) {
							CompError.message(opexpr.line(),
									">= operator requires integer operands");
						}
						return new TypeClass(BooleanType.instance(), bt.operatorExpression(leftTree, rightTree, AATOperator.GREATER_THAN_EQUAL));

				default:
					return new TypeClass(null, bt.operatorExpression(leftTree, rightTree, AATOperator.BAD_OPERATOR));
				}
		}

		return new TypeClass(null, bt.operatorExpression(null, null, AATOperator.BAD_OPERATOR));
	}


	@Override
	public Object VisitProgram(ASTProgram program) {

		program.classes().Accept(this);
		AATStatement prog = (AATStatement) program.functiondefinitions().Accept(this);

		return prog;
	}

	@Override
	public Object VisitFunctionDefinitions(
			ASTFunctionDefinitions functiondefinitions) {

		Vector functionDefs = new Vector();
		for (int i = 0; i < functiondefinitions.size(); i++) {
			functionDefs.addElement(functiondefinitions.elementAt(i).Accept(this));
		}

		AATStatement seq = (AATStatement) functionDefs.elementAt(functionDefs.size() - 1);


		for (int j = functionDefs.size() - 2; j >= 0; j--){
			seq = bt.sequentialStatement((AATStatement) functionDefs.elementAt(j), seq);
		}


		return seq;
	}

	@Override
	public Object VisitPrototype(ASTPrototype prototype) {
		Type ret = typeEnv.find(prototype.type());
		Vector<ASTFormal> formals = new Vector<ASTFormal>();
		for (int i = 0; i < prototype.formals().size(); i++) {
				formals.addElement(prototype.formals().elementAt(i));
		}

		if (ret == null) {
			CompError.message(prototype.line(), "Type " + prototype.type() + " is not defined");

			// Default to integer
			ret = typeEnv.find("int");
		}

		functionEnv.insert(prototype.name(), new FunctionEntry(ret, formals, new Label(prototype.name() + "Start"), new Label(prototype.name() + "End")));

		return bt.emptyStatement();
	}

	@Override
	public Object VisitReturnStatement(ASTReturnStatement ret) {

		TypeClass retType = (TypeClass) ret.value().Accept(this);

		if (returnType != retType.type()) {
			CompError.message(ret.line(), "Return value does not match function return type.");
		}

		return bt.returnStatement(retType.tree(), funcEndLabel);
	}

	@Override
	public Object VisitStatements(ASTStatements statements) {
		offset = 0;

		Vector statementList = new Vector();
		for (int i = 0; i < statements.size(); i++) {
			statementList.addElement(statements.elementAt(i).Accept(this));
		}

		AATStatement seq = (AATStatement) statementList.elementAt(statementList.size() - 1);

		for (int i = statementList.size() - 2; i >= 0; i--) {
				seq = bt.sequentialStatement((AATStatement) statementList.elementAt(i), seq);
		}
		return seq;
	}

	@Override
	public Object VisitUnaryOperatorExpression(ASTUnaryOperatorExpression operator) {

		TypeClass opType = (TypeClass) operator.operand().Accept(this);

		if (opType.type() != BooleanType.instance()) {
				CompError.message(operator.line(), "Unary Operator problem.");

				// Defaults to boolean if not valid type
				return new TypeClass(BooleanType.instance(), bt.operatorExpression(opType.tree(), bt.constantExpression(1), AATOperator.MINUS));
		}
		return new TypeClass(opType.type(), bt.operatorExpression(opType.tree(), bt.constantExpression(1), AATOperator.MINUS));
	}

	@Override
	public Object VisitVariableDefStatement(ASTVariableDefStatement vardef) {
		Type varType = checkType(vardef.type(), vardef.name(), vardef.arraydimension(), vardef.line());
		AATStatement variableInit = bt.emptyStatement();
		AATExpression baseVar = bt.baseVariable(offset);

		if (vardef.init() != null) {

			TypeClass init = (TypeClass) vardef.init().Accept(this);

			if (varType != init.type()) {
				CompError.message(vardef.line(), "The left hand type: " + varType + ", does not equal right hand side " + init.type());
			}

			variableInit = bt.assignmentStatement(baseVar, init.tree());

		}

		variableEnv.insert(vardef.name(), new VariableEntry(varType, offset));
		offset += sizeWord;

		return variableInit;
	}

	@Override
	public Object VisitVariableExpression(ASTVariableExpression variableexpression) {

		TypeClass var = (TypeClass) variableexpression.variable().Accept(this);
		if (var == null) {
			CompError.message(variableexpression.line(), "Variable invalid.");
		}

		return var;
	}

	@Override
	public Object VisitWhileStatement(ASTWhileStatement whileSt) {
		TypeClass test = (TypeClass) whileSt.test().Accept(this);

		if (test.type() != BooleanType.instance())
			CompError.message(whileSt.line(), "While Statement - test must be a boolean");

		AATStatement body = (AATStatement) whileSt.body().Accept(this);

		return bt.whileStatement(test.tree(), body);
	}

	class TypeClass {

			private Type type_;
			private AATExpression tree_;

			public TypeClass(Type type, AATExpression tree) {
					type_ = type;
					tree_ = tree;
			}

			public Type type() {
					return type_;
			}

			public AATExpression tree() {
					return tree_;
			}

			public void settype(Type type) {
					type_ = type;
			}

			public void settree(AATExpression tree) {
					tree_ = tree;
			}

	}
}
