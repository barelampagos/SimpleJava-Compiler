import java.util.Vector;

public interface AATVisitor {

    public Object VisitCallExpression(AATCallExpression call);
    public Object VisitCallStatement(AATCallStatement call);
    public Object VisitConditionalJump(AATConditionalJump cjump);
    public Object VisitConstant(AATConstant constant);
    public Object VisitEmpty(AATEmpty empty);
    public Object VisitHalt(AATHalt halt);
    public Object VisitJump(AATJump jump);
    public Object VisitLabel(AATLabel label);
    public Object VisitMemory(AATMemory mem);
    public Object VisitMove(AATMove move);
    public Object VisitOperator(AATOperator oper);
    public Object VisitRegister(AATRegister reg);
    public Object VisitReturn(AATReturn ret);
    public Object VisitSequential(AATSequential seq);

}
