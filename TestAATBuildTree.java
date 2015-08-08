import java.util.Vector;

class TestAATBuildTree {

    public static void main(String args[]) {
	AATBuildTree bt = new AATBuildTree();
	AATPrintTree pt = new AATPrintTree();
	AATExpression exptree;
	AATStatement stmtree, functiondefs;
	Vector actuals;

	System.out.println("=================== ");
	System.out.println("Testing Expressions ");
	System.out.println("=================== ");
	System.out.println("----------------------------");
	System.out.println("Testing literal constat : 15 ");
	System.out.println("----------------------------");
	exptree =  bt.constantExpression(15);
	exptree.Accept(pt);
	System.out.println("--------------------------");
	System.out.println("Testing expression : 4 + 5 ");
	System.out.println("--------------------------");
	exptree = bt.operatorExpression(bt.constantExpression(4),bt.constantExpression(5),
					AATOperator.PLUS);
	exptree.Accept(pt);
	System.out.println("================= ");
	System.out.println("Testing Variables ");
	System.out.println("================= ");
	System.out.println("-------------------------------");
	System.out.println("Testing base variable, offset 4 ");
	System.out.println("-------------------------------");
	exptree = bt.baseVariable(4);
	exptree.Accept(pt);
	System.out.println("----------------------------------------");
	System.out.println("Assingment:  x := 3 (x local, offset = 4)");
	System.out.println("----------------------------------------");
	stmtree = bt.assignmentStatement(bt.baseVariable(4), bt.constantExpression(3));
	stmtree.Accept(pt);
	System.out.println("-----------------------------------------------------------");
	System.out.println("For: for(i=0; i < 10; i++) x := 0;   x offset 4, i offset 8 ");
	System.out.println("------------------------------------------------------------");
	stmtree = bt.forStatement(bt.assignmentStatement(bt.baseVariable(8), bt.constantExpression(0)),
				  bt.operatorExpression(bt.baseVariable(8), bt.constantExpression(10), AATOperator.LESS_THAN),
				  bt.assignmentStatement(bt.baseVariable(8),
							 bt.operatorExpression(bt.baseVariable(8),
									       bt.constantExpression(1),
									       AATOperator.PLUS)),
				  bt.assignmentStatement(bt.baseVariable(4),
							 bt.constantExpression(0)));
	stmtree.Accept(pt);
	/* Add more tests !!! */

    }
}





