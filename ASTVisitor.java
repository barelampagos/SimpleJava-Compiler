import java.util.Vector;

public interface ASTVisitor {

    public Object VisitArrayVariable(ASTArrayVariable array);
    public Object VisitAssignmentStatement(ASTAssignmentStatement assign);
    public Object VisitBaseVariable(ASTBaseVariable base);
    public Object VisitBooleanLiteral(ASTBooleanLiteral boolliteral);
    public Object VisitClass(ASTClass classs);
    public Object VisitClasses(ASTClasses classes);
    public Object VisitClassVariable(ASTClassVariable classvariable);
    public Object VisitDoWhileStatement(ASTDoWhileStatement dowhile);
    public Object VisitEmptyStatement(ASTEmptyStatement empty);
    public Object VisitForStatement(ASTForStatement forstmt);
    public Object VisitFormal(ASTFormal formal);
    public Object VisitFormals(ASTFormals formals);
    public Object VisitFunction(ASTFunction function);
    public Object VisitFunctionCallExpression(ASTFunctionCallExpression functioncall);
    public Object VisitFunctionCallStatement(ASTFunctionCallStatement functioncall);
    public Object VisitIfStatement(ASTIfStatement ifsmt);
    public Object VisitIntegerLiteral(ASTIntegerLiteral literal);
    public Object VisitInstanceVariableDef(ASTInstanceVariableDef variabledef);
    public Object VisitInstanceVariableDefs(ASTInstanceVariableDefs variabledefs);
    public Object VisitNewArrayExpression(ASTNewArrayExpression newarray);
    public Object VisitNewClassExpression(ASTNewClassExpression newclass);
    public Object VisitOperatorExpression(ASTOperatorExpression opexpr);
    public Object VisitProgram(ASTProgram program);
    public Object VisitFunctionDefinitions(ASTFunctionDefinitions functiondefinitions);
    public Object VisitPrototype(ASTPrototype prototype);
    public Object VisitReturnStatement(ASTReturnStatement ret);
    public Object VisitStatements(ASTStatements statements);
    public Object VisitUnaryOperatorExpression(ASTUnaryOperatorExpression operator);
    public Object VisitVariableDefStatement(ASTVariableDefStatement vardef);
    public Object VisitVariableExpression(ASTVariableExpression variableexpression); 
    public Object VisitWhileStatement(ASTWhileStatement whilestatement);
}
