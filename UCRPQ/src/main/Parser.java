package main;

import java.util.List;
import java.util.LinkedList;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.RuntimeException;
import java.util.stream.Collectors;

public class Parser {

  private static class Make {
    static RPQ rpq(String o, String d, RegExp e) { return new RPQ(o,e,d); }
    static CRPQ crpq(List<RPQ> l) { return new CRPQ(l); }
    static UCRPQ ucrpq(List<CRPQ> l) { return new UCRPQ(l); }
    static RegExp concatenation(List<RegExp> l) { return new Concatenation(l.toArray(new RegExp[l.size()]));  }
    static RegExp union(List<RegExp> l) { return new Union(l.toArray(new RegExp[l.size()]));  }
    static RegExp star(RegExp e) {  return new Star(e);  }
    static RegExp epsilon() { return new Epsilon(); }
    static RegExp atom_f(String s) { return new Atom(s, true); }
    static RegExp atom_b(String s) { return new Atom(s, false); }
  }

  static List<Character> operators = Arrays.asList('|','&',',','+','.','*');


  String input;

  List<Integer> parenthesisMatching; /* Contains offset to matching parenthesis
                                        For all i, element at position i
                                        Positive if input[i] is opening parenthesis
                                        Negative if input[i] is closing parenthesis
                                        0 if input[i] is not a parenthesis */
  int start;
  int end;

  private Parser(String input) {
    this.input = input;
    this.start = 0;
    this.end = input.length();
    initializeParenthesisMatching();
  }

  private void initializeParenthesisMatching() {
    parenthesisMatching= new ArrayList<Integer>(end);
    for (int i=0; i<end; i++)
      parenthesisMatching.add(0);
    Stack<Integer> stack = new Stack<Integer>();
    for (int i=0; i<end; i++) {
      char c = input.charAt(i);
      if (c == '(')
        stack.push(i);
      else if (c == ')') {
        if (stack.isEmpty())
          throw new RuntimeException("Parentheses in \""+input+"\" are not well balanced");
        int j = stack.pop();
        parenthesisMatching.set(j, i-j);
        parenthesisMatching.set(i, j-i);
      }
    }
    if (! stack.isEmpty())
      throw new RuntimeException("Parentheses in \""+input+"\" are not well balanced");
  }

  private List<Integer> lvlZOperators() {
    List<Integer> result = new LinkedList<Integer>();
    int depth = 0;
    for (int i = start; i<end; i++) {
      if (parenthesisMatching.get(i) != 0)
        i += parenthesisMatching.get(i);
      else if ((operators.contains(input.charAt(i))) && depth==0)
        result.add(i);
    }
    return result;
  }

  private void trimSubstring() {
    boolean stop = false;
    while (!stop) {
      stop = true;
      if (Character.isSpaceChar(input.charAt(start))) {
        start++;
        if (start != end)
          stop = false;
      } else if (Character.isSpaceChar(input.charAt(end-1))) {
        end--;
        if (start != end)
          stop = false;
      } else if ((parenthesisMatching.get(start)!= 0) &&
                 ((start+parenthesisMatching.get(start)) == (end-1))) {
        start++;
        end--;
        if (start != end)
          stop = false;
      }
    }
  }


  private String extract() {
    return input.substring(start,end);
  }

  private RegExp _parseRegExp() {
    int oldStart = start;
    int oldEnd = end;
    trimSubstring();
    List<Integer> op_positions = lvlZOperators();
    if (op_positions.isEmpty()) {
      if (start == end)
        return Make.epsilon();

      if (isValidAtomString())
        return Make.atom_f(extract());
      end--;
      boolean b = isValidAtomString() && input.charAt(end) == '-';
      String str = extract();
      end++;
      if (b)
        return Make.atom_b(str);
      throw new RuntimeException("Substring \""+extract()+"\" is not a valid atom and does not contain any top-level operator.");
    }
    char c = '\0';
    for (int i=0 ; i<=operators.size(); i++) {
      c = operators.get(i);
      final char d = c;
      if (op_positions.stream().anyMatch(x -> (input.charAt(x) == d) ))
        break;
    }
    final char d = c;
    op_positions.removeIf(p->input.charAt(p)!=d);
    List<RegExp> children = new ArrayList<RegExp>(op_positions.size()+1);
    op_positions.add(end);
    for (int i = 0; i<op_positions.size(); i++) {
      end = op_positions.get(i);
      children.add(_parseRegExp());
      start = op_positions.get(i)+1;
    }
    start = oldStart;
    end = oldEnd;
    switch (d) {
      case '.': return Make.concatenation (children);
      case '+': return Make.union (children);
      case '*':
      {
        if ( (children.size() != 2) || !(children.get(1) instanceof Epsilon))
          throw new RuntimeException("Star is a unary operator.");
        return Make.star(children.get(0));
      }
      default: throw new Error("Unreachable Statement");
    }
  }

  private RPQ _parseRPQ() {
    int oldEnd = end;
    int oldStart = start;
    trimSubstring();
    List<Integer> op_positions = lvlZOperators();
    op_positions.removeIf(p->input.charAt(p)!=',');
    if (op_positions.size() != 2)
      throw new RuntimeException("Expected exactly two top-level commas in \""
                                 +extract()
                                 +"\", got "+op_positions.size()+".");
    String var1 = input.substring(start, op_positions.get(0));
    String var2= input.substring(op_positions.get(1)+1,end);
    start = op_positions.get(0)+1;
    end = op_positions.get(1);
    RegExp re = _parseRegExp();
    end = oldEnd;
    start = oldStart;
    return Make.rpq(var1,var2,re);
  }

  private CRPQ _parseCRPQ() {
    int oldEnd = end;
    int oldStart = start;
    trimSubstring();
    List<Integer> op_positions = lvlZOperators();
    op_positions.removeIf(p->input.charAt(p)!='&');
    op_positions.add(end);
    List<RPQ> l = new ArrayList<RPQ>();
    for(int i : op_positions) {
      end = i;
      l.add(_parseRPQ());
      start = end+1;
    }
    end = oldEnd;
    start = oldStart;
    return Make.crpq(l);
  }

  private UCRPQ _parseUCRPQ() {
    int oldEnd = end;
    int oldStart = start;
    trimSubstring();
    List<Integer> op_positions = lvlZOperators();
    System.out.println(op_positions);
    op_positions.removeIf(p->input.charAt(p)!='|');
    op_positions.add(end);
    List<CRPQ> l = new ArrayList<CRPQ>();
    for(int i : op_positions) {
      end = i;
      l.add(_parseCRPQ());
      start = end+1;
    }
    end = oldEnd;
    start = oldStart;
    return Make.ucrpq(l);
  }

  private boolean isValidAtomString() {
    if ( (start != end) && !Character.isLetter(input.charAt(start)))
      return false;
    for (int i = start; i < end; i++)
      if (!Character.isLetterOrDigit(input.charAt(i)) && !(input.charAt(i)== '_'))
        return false;
    return true;
  }

  public static void main(String[] args) {
    Parser parser = new Parser(args[0]);
    System.out.println(parser._parseUCRPQ());
  }






  public static RegExp parseRegExp(String str) {
    return (new Parser(str)._parseRegExp());
  }

  public static RPQ parseRPQ(String str) {
    return (new Parser(str)._parseRPQ());
  }

  public static CRPQ parseCRPQ(String str) {
    return (new Parser(str)._parseCRPQ());
  }

  public static UCRPQ parseUCRPQ(String str) {
    return (new Parser(str)._parseUCRPQ());
  }
}
