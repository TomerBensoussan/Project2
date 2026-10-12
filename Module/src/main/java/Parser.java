public class Parser {

    private static final String NUMBERS = "0123456789";
    private static final String OPERANDS = "+-*/^";
    private static final int[] OPERAND_PRIORITY = {3, 3, 2, 2, 1};

    static boolean validOp(String op){
        return op.length() == 1 && OPERANDS.contains(op);
    }
    static boolean validNum(String num){
        return num.length() == 1 && NUMBERS.contains(num);
    }
    private static void applyOp(ArrayStack<AST> stack, String op){
        if (stack.size() <2){throw new IllegalArgumentException("Insufficient operands");}
        AST right = stack.pop();
        AST left = stack.pop();
        stack.push(new BinopNode(op, left, right));
    }
    public static boolean leftPriority(String leftOp, String rightOp) {
        if (!validOp(leftOp) || !validOp(rightOp)){
            throw new IllegalArgumentException("operand does not exist");
        } else {
            return OPERAND_PRIORITY[OPERANDS.indexOf(leftOp)] <= OPERAND_PRIORITY[OPERANDS.indexOf(rightOp)]
                    && !(leftOp.equals("^") && rightOp.equals("^"));
        }
    }

    static AST parsePostFix(String str){
        if(str.isEmpty()){throw new IllegalArgumentException("Empty input");}
        String[] tokens = str.split(" ");
        ArrayStack<AST> stack = ArrayStack.emptyStack();
        for (int i = 0; i< tokens.length; i++){
            try{
                stack.push(new NumNode(Double.parseDouble(str)));
            } catch (NumberFormatException e) {
                if (!validOp(tokens[i])) {
                    throw new IllegalArgumentException("Invalid token");
                }
                else{
                    applyOp(stack, tokens[i]);
                }
            }
        }
        if(stack.size() != 1) throw new IllegalArgumentException("insufficient operands");
        return stack.peek();
    }
    static AST parseInfix(String str){

        String[] token = str.split(" ");
        if (token.length == 0) throw new IllegalArgumentException("empty string");

        ArrayStack<AST> numStack = ArrayStack.emptyStack();
        ArrayStack<String> opStack = ArrayStack.emptyStack();
        int openBracket = 0;

        for (int i = 0; i < token.length; i++){
            try {
                AST num = new NumNode(Double.parseDouble(str));
                if ((i%2 == 1)) { //if operator was expected
                    throw new IllegalArgumentException("too many operands");
                } else{ //push num to numStack
                    numStack.push(num);
                }
            } catch (NumberFormatException e){// not valid number, so op
                if (!validOp(str) && !"()".contains(token[i])){ //if not valid op
                    throw new IllegalArgumentException("invalid token");
                }
                else if ((i - openBracket)%2 == 1) {//if op was expected
                    if (token[i].equals("(")){//if found open paren
                        throw new IllegalArgumentException("too many operands");
                    }
                    else if (token[i].equals(")")){ //if found close bracket
                        try {//popping till finding next start paren
                            while (!opStack.peek().equals("(")){
                                applyOp(numStack, opStack.pop());
                            }
                        } catch (IndexOutOfBoundsException f){ //to many close parens
                            throw new IllegalArgumentException("mismatch close paren");
                        }
                        opStack.pop();
                        openBracket--;
                    }
                    else{//popping till operation standings are done
                        while (!opStack.isEmpty() && !opStack.peek().equals("(") &&
                        leftPriority(opStack.peek(), token[i])){
                            applyOp(numStack, opStack.pop());
                        }
                    }
                } else if(token[i].equals("(")){
                    opStack.push("(");
                    openBracket++;
                } else { //if number was expected
                    throw new IllegalArgumentException("insufficient operands");
                }
            }
        }
        if (openBracket != 0) throw new IllegalArgumentException("mismatch open paren");

        //finish off opStack
        while (!opStack.isEmpty()){
            applyOp(numStack, opStack.pop());
        }
        return numStack.peek();
    }
}
