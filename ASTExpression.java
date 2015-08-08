public abstract class ASTExpression {

    public abstract Object Accept(ASTVisitor V);
    public abstract int line();
}
