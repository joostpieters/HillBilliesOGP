package hillbillies.model.EsotERICScript.Statements;

import hillbillies.model.EsotERICScript.Expressions.BooleanExpression;
import hillbillies.model.EsotERICScript.Expressions.Expression;
import hillbillies.model.EsotERICScript.Expressions.PositionExpression;
import hillbillies.model.EsotERICScript.Expressions.UnitExpression;
import hillbillies.model.Task;
import hillbillies.model.Unit;
import hillbillies.model.exceptions.SyntaxError;

import java.util.List;

public class Statement {

    public Statement(Task task) {
        this.beingExcecuted = false;
        this.executed = false;
        this.task = task;
    }
    
    private boolean beingExcecuted;
    private boolean executed;
    public Task task;
    public Unit executor;
    private PartStatement partStatement;
    
    public void execute(Unit unit, double dt) throws SyntaxError {
        this.executor = unit;
        this.partStatement.execute(unit, dt);
    }

    public void finishExecuting() {
        this.beingExcecuted = false;
        this.executed = true;
        //TODO Execute next?
    }
    
    public void reExecutePrepare() {
        this.beingExcecuted = false;
        this.executed = false;
    }

    // x := e
    // TODO: name the variable by given string
    class AssignmentPartStatement extends PartStatement {

        public AssignmentPartStatement(String variableName, Expression value) {
            this.variableName = variableName;
            this.value = value;
        }
        
        private final String variableName;
        private final Expression value;

        @Override
        public void execute(Unit unit, double dt) throws SyntaxError {
            if (value instanceof BooleanExpression) {
                boolean booleanVar = ((BooleanExpression) value).value(unit);
            } else if (value instanceof PositionExpression) {
            	int[] intVar = ((PositionExpression) value).value(unit);
            } else if (value instanceof UnitExpression) {
            	Unit unitVar = ((UnitExpression) value).value(unit);
            }
            	
        }
        
    }
    
    // while e do s done
    class WhilePartStatement extends PartStatement {

        public WhilePartStatement(BooleanExpression condition, Statement body) {
            this.condition = condition;
            this.body = body;
        }
        
        private final BooleanExpression condition;
        private final Statement body;
        private Boolean flag;
        private Boolean conditionEvaluated = false;

        @Override
        public void execute(Unit unit, double dt) throws SyntaxError {
            if (! conditionEvaluated) {
                flag = condition.value(unit);
                conditionEvaluated = true;
            }
            while (flag)
            	body.execute(unit, dt);
        }
        
    }
    
    // if e then s [ else s ] fi
    class IfPartStatement extends PartStatement {

        public IfPartStatement(BooleanExpression condition, Statement ifPart, Statement elsePart) {
            this.condition = condition;
            this.ifPart = ifPart;
            this.elsePart = elsePart;
        }
        
        private final BooleanExpression condition;
        private final Statement ifPart;
        private final Statement elsePart;
        private Boolean flag;
        private Boolean conditionEvaluated = false;

        @Override
        public void execute(Unit unit, double dt) throws SyntaxError {
            if (! conditionEvaluated) {
                flag = condition.value(unit);
                conditionEvaluated = true;
            }
            if (flag)
            	ifPart.execute(unit, dt);
            else
            	elsePart.execute(unit, dt);
        }
        
    }
    
    // break
    // TODO
    
    // print e
    // TODO
    
    // {s}
    class SequencePartStatement extends PartStatement {

        public SequencePartStatement(List<Statement> statements) {
            this.statementList = statements;
        }
        
        private final List<Statement> statementList;

        @Override
        public void execute(Unit unit, double dt) throws SyntaxError {
        	if (statementList.size() == 0)
        		throw new SyntaxError("The list of statements is empty.");
            for (Statement statement: statementList)
            	statement.execute(unit, dt);
        }
        
    }	
    		
    // moveTo e
    class MoveToPartStatement extends PartStatement {

        public MoveToPartStatement(PositionExpression destination) {
            this.argExpr1 = destination;
        }
        
        PositionExpression argExpr1;
        
        @Override
        public void execute(Unit unit, double dt) throws SyntaxError {
            if (! beingExcecuted)
            	unit.moveTo(argExpr1.value(executor));
            //TODO: keep information about the completion of this statement
        }
    }
    
    // work e
    class WorkPartStatement extends PartStatement {

        public WorkPartStatement(PositionExpression position) {
            this.argExpr1 = position;
        }
        
        PositionExpression argExpr1;
        
        @Override
        public void execute(Unit unit, double dt) throws SyntaxError {
            unit.work(argExpr1.value(executor));
            //TODO: keep information about the completion of this statement
        }
        
    }
    
    // follow e
    // TODO: bc Arthur likes to procrastinate
    class FollowPartStatement extends PartStatement {

        public FollowPartStatement(UnitExpression unitToFollow) {
            this.argExpr1 = unitToFollow;
        }
        
        UnitExpression argExpr1;
        
        @Override
        public void execute(Unit unit, double dt) throws SyntaxError {
            //unit.moveTo(argExpr1.value());
            // TODO: keep information about the completion of this statement
        }
        
    }
    
    // attack e
    class AttackPartStatement extends PartStatement {
        public AttackPartStatement(UnitExpression defender) {
            this.argExpr1 = defender;
        }
        
        UnitExpression argExpr1;
        
        @Override
        public void execute(Unit unit, double dt) throws SyntaxError {
            unit.attack(argExpr1.value(executor));
            //TODO: keep information about the completion of this statement
        }
    }

    /**
     * Static methods for creating new statements
     */
    public static Statement newWorkAt(PositionExpression loc, Task task) {
        Statement statement = new Statement(task);
        statement.partStatement = statement.new WorkPartStatement(loc);
        return statement;
    }
    
    public static Statement newAttack(UnitExpression unit, Task task){
        Statement statement = new Statement(task);
        statement.partStatement = statement.new AttackPartStatement(unit);
        return statement;
    }
    
    public static Statement newFollow(UnitExpression unit, Task task){
        Statement statement = new Statement(task);
        statement.partStatement = statement.new FollowPartStatement(unit);
        return statement;
    }
    
    public static Statement newMoveTo(PositionExpression pos, Task task){
        Statement statement = new Statement(task);
        statement.partStatement = statement.new MoveToPartStatement(pos);
        return statement;
    }

}