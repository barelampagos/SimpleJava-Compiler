import java.util.Vector;

public class AATBuildTree {
  private AATRegister FP = new AATRegister(Register.FP());
  private AATRegister SP = new AATRegister(Register.SP());
  private AATRegister RA = new AATRegister(Register.ReturnAddr());

    public AATStatement functionDefinition(AATStatement body, int framesize, Label start, Label end) {
      // Set up
      // Store frame pointer: oldFP = FP;
      // Store return address: oldRA = RA;
      // Store stack pointer: oldSP = SP;

      AATMove oldFP = new AATMove(                                              // Moving a mem into SP
                          new AATMemory(                                        // Getting memloc of SP
                              new AATOperator(                                  //
                                  SP,
                                  new AATOperator(
                                      new AATConstant(MachineDependent.WORDSIZE),
                                      new AATConstant(framesize),
                                      AATOperator.MULTIPLY),                    // Calculate offset
                                  AATOperator.MINUS)),
                          FP);

      // Store oldRA
      AATMove oldRA = new AATMove(
                          new AATMemory(
                              new AATOperator(
                                  SP,
                                  new AATOperator(
                                      new AATOperator(
                                          new AATConstant(framesize),
                                          new AATConstant(1),
                                          AATOperator.PLUS
                                      ),
                                      new AATConstant(MachineDependent.WORDSIZE),
                                      AATOperator.MULTIPLY
                                  ),
                                  AATOperator.MINUS
                              )
                          ),
                          RA
      );

      // Move FP to SP
      AATMove moveFPtoSP = new AATMove(FP, SP);

      // Move SP to end of activation record
      AATMove moveSPtoEnd = new AATMove(
                                SP,
                                new AATOperator(
                                    SP,
                                    new AATConstant(MachineDependent.WORDSIZE*(framesize + 2)),
                                    AATOperator.MINUS
                                )
      );

      // Body and End Labels happen here

      // Return SP
      AATMove returnSP = new AATMove(SP, FP);

      // Make new $RA = oldRA
      AATMove returnRA = new AATMove(
                            RA,
                            new AATMemory(
                                new AATOperator(
                                    SP,
                                    new AATOperator(
                                        new AATOperator(
                                            new AATConstant(framesize),
                                            new AATConstant(1),
                                            AATOperator.PLUS
                                        ),
                                        new AATConstant(MachineDependent.WORDSIZE),
                                        AATOperator.MULTIPLY
                                    ),
                                    AATOperator.MINUS
                                )
                            )
      );

      // Make new $FP = oldFP

      AATMove returnFP = new AATMove(
                              FP,
                              new AATMemory(
                                  new AATOperator(
                                      SP,
                                      new AATOperator(
                                          new AATConstant(MachineDependent.WORDSIZE),
                                          new AATConstant(framesize),
                                          AATOperator.MULTIPLY
                                      ),
                                      AATOperator.MINUS
                                  )
                              )
      );

      // Clean Up
      // Restore old FP: FP = oldFP;
      // Restore old SP: SP = oldSP;
      // Restore return address: RA = oldRA;
      // Warning: Do this in the right order
      // Return from the function with the return abstract assembly instruction: new AATReturn();
      AATStatement seq = sequentialStatement(returnFP, new AATReturn());
      seq = sequentialStatement(returnRA, seq);
      seq = sequentialStatement(returnSP, seq);
      seq = sequentialStatement(new AATLabel(end), seq);
      seq = sequentialStatement(body, seq);
      seq = sequentialStatement(moveSPtoEnd, seq);
      seq = sequentialStatement(moveFPtoSP, seq);
      seq = sequentialStatement(oldRA, seq);
      seq = sequentialStatement(oldFP, seq);
      seq = sequentialStatement(new AATLabel(start), seq);


      return seq;
    }

    public AATStatement ifStatement(AATExpression test, AATStatement ifbody, AATStatement elsebody) {
    	 Label iftrue = new Label("ifTrue");
       Label ifend = new Label("ifEnd");

       AATStatement seq = sequentialStatement(ifbody, new AATLabel(ifend));
       seq = sequentialStatement(new AATLabel(iftrue), seq);
       seq = sequentialStatement(new AATJump(ifend), seq);
       seq = sequentialStatement(elsebody, seq);
       seq = sequentialStatement(new AATConditionalJump(test, iftrue), seq);

       return seq;
    }

    // TODO:
    public AATExpression allocate(AATExpression size) {
        Vector actuals = new Vector();
        actuals.addElement(size);
        return new AATCallExpression(Label.AbsLabel("allocate"), actuals);
    }

    public AATStatement whileStatement(AATExpression test, AATStatement whilebody) {
	     Label whilestart = new Label("whileStart");
       Label whiletest = new Label("whileTest");

       AATStatement seq = sequentialStatement(new AATLabel(whiletest), new AATConditionalJump(test, whilestart));
       seq = sequentialStatement(whilebody, seq);
       seq = sequentialStatement(new AATLabel(whilestart), seq);
       seq = sequentialStatement(new AATJump(whiletest), seq);

       return seq;
    }

    public AATStatement dowhileStatement(AATExpression test, AATStatement dowhilebody) {
      	Label dowhileStart = new Label("dowhileStart");

        AATStatement seq = sequentialStatement(new AATConditionalJump(test, dowhileStart), null);
        seq = sequentialStatement(dowhilebody, seq);
        seq = sequentialStatement(new AATLabel(dowhileStart), seq);

        return seq;
    }

    public AATStatement forStatement(AATStatement init, AATExpression test, AATStatement increment, AATStatement body) {
      	Label forTest = new Label("forTest");
        Label forStart = new Label("forStart");

        AATStatement seq = sequentialStatement(new AATLabel(forTest), new AATConditionalJump(test, forStart));
        seq = sequentialStatement(increment, seq);
        seq = sequentialStatement(body, seq);
        seq = sequentialStatement(new AATLabel(forStart), seq);
        seq = sequentialStatement(new AATJump(forTest), seq);
        seq = sequentialStatement(init, seq);

        return seq;
    }

    public AATStatement emptyStatement() {
	     return new AATEmpty();
    }

    public AATStatement callStatement(Vector actuals, Label name) {
       return new AATCallStatement(name, actuals);
    }

    public AATStatement assignmentStatement(AATExpression lhs, AATExpression rhs) {
        return new AATMove(lhs, rhs);
    }

    public AATStatement sequentialStatement(AATStatement first, AATStatement second) {
        return new AATSequential(first, second);
    }

    public AATExpression baseVariable(int offset) {
	       return new AATMemory(new AATOperator(new AATRegister(Register.FP()), new AATConstant(offset), AATOperator.MINUS)) ;
    }

    public AATExpression arrayVariable(AATExpression base, AATExpression index, int elementSize) {

        AATExpression rhs = new AATOperator(index, new AATConstant(elementSize), AATOperator.MULTIPLY);
        AATExpression baseVar = new AATOperator(base, rhs, AATOperator.MINUS);

        return new AATMemory(baseVar);
    }

    public AATExpression classVariable(AATExpression base, int offset) {
        AATExpression address = base;
        if (offset != 0) {
            address = new AATOperator(address, new AATConstant(offset), AATOperator.PLUS);
        }
        return new AATMemory(address);
    }

    public AATExpression constantExpression(int value) {
	      return new AATConstant(value);
    }

    public AATExpression operatorExpression(AATExpression left, AATExpression right, int operator) {
	     return new AATOperator(left, right, operator);
    }

    public AATExpression callExpression(Vector actuals, Label name) {
      	return new AATCallExpression(name, actuals);
    }

    public AATStatement returnStatement(AATExpression value, Label functionend) {

       AATMove saveResult = new AATMove(new AATRegister(Register.Result()), value);
       AATJump jumpEnd = new AATJump(functionend);

       return sequentialStatement(saveResult, jumpEnd);
    }
}
