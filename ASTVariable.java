public abstract class ASTVariable {

    public abstract Object Accept(ASTVisitor V);
    public abstract int line();
}
