//package hello;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class hw4 {
	
	public static void hw4(String input, String output) throws IOException{
	
		PrintStream myconsole=new PrintStream(new File(output));
		System.setOut(myconsole);
		
		BufferedReader in = new BufferedReader(new FileReader(input));
		
		ArrayList stack = new ArrayList();
		HashMap<String,String> hm = new HashMap<String,String>();
		HashMap funHm = new HashMap<String,doFun>();
		HashMap environs = new HashMap<String,HashMap>();
		String line = in.readLine();
		while(line!=null){

			if(line.equals("let")) {
				stack.add(0,parseLet(in,hm,funHm,environs,myconsole));
				
			}
			else if(line.startsWith("fun")){
		//	    stack.add(0,"hello");
		        String[] delim = line.split(" ");
                String funName = delim[1];
                String Args = delim[2];
			    doFun fun= new doFun(in,myconsole,funName,Args,hm);
			    funHm.put(funName,fun);
			    stack.add(0,":unit:");
			    //if (funHm.containsKey("myname")){
			   //  String s = ((doFun) funHm.get("myname")).getarg();
			    //    System.out.println(s);
			   // }
			}
			else if(line.startsWith("inOut")){
			     String[] delim = line.split(" ");
                String funName = delim[1];
                String Args = delim[2];
			    doFun fun= new doFun(in,myconsole,funName,Args,hm);
			    fun.setflag();
			    funHm.put(funName,fun);
			    stack.add(0,":unit:");
			    
			}
			else if(Character.isLetter(line.charAt(0))){
				stack = parsePrimitive(line, stack, hm,funHm,environs, myconsole);
			}
			else if(line.charAt(0)==':'){
				stack = parseBooleanOrError(line, stack, hm);
			}
		
			else{
				myconsole.println("Error command!");
			}
			
			line = in.readLine();
		}
		in.close();
	}
	
	public static Object parseLet(BufferedReader in, HashMap hm_parent,HashMap funHm,HashMap environs, PrintStream myconsole) throws IOException {
		ArrayList stack_let = new ArrayList();
		HashMap hm_let = new HashMap(hm_parent);
		HashMap funHmlet = new HashMap<String,doFun>();
		String line = in.readLine();
		while(!line.equals("end")){
			
			if(line.equals("let")) {
				stack_let.add(0,parseLet(in,hm_let,funHmlet,environs,myconsole));
			}
			else if(line.startsWith("fun")){
		//	    stack.add(0,"hello");
		        String[] delim = line.split(" ");
                String funName = delim[1];
                String Args = delim[2];
			    doFun fun= new doFun(in,myconsole,funName,Args,hm_let);
			    funHmlet.put(funName,fun);
			    stack_let.add(0,":unit:");
			    //if (funHm.containsKey("myname")){
			   //  String s = ((doFun) funHm.get("myname")).getarg();
			    //    System.out.println(s);
			   // }
			   
			}
			else if(line.startsWith("inOut")){
			     String[] delim = line.split(" ");
                String funName = delim[1];
                String Args = delim[2];
			    doFun fun= new doFun(in,myconsole,funName,Args,hm_let);
			    fun.setflag();
			    funHmlet.put(funName,fun);
			    stack_let.add(0,":unit:");
			    
			}
			else if(Character.isLetter(line.charAt(0))){
				stack_let = parsePrimitive(line, stack_let, hm_let,funHmlet,environs, myconsole);
			}
			else if(line.charAt(0)==':'){
				stack_let = parseBooleanOrError(line, stack_let, hm_let);
			}
			else{
				myconsole.println("Error command!");
			}
			
			line = in.readLine();
		}
		return stack_let.get(0);
	}
	
	 static class doFun {
	   
	    //String[] delim = line.split(" ");
        //String funName = delim[0];
        
       // String arg = delim[0];
       //System.out.println("this is arg:"+arg);
        Boolean inOut = false;
        List<String> funBody = new ArrayList<String>();
         String args;
        HashMap hmFun;
        String name=null;
       // stack_let.add(0,funName);
       // stack_let.add(0,arg);
       public doFun(BufferedReader in, PrintStream myconsole,String funName,String Arg,HashMap hmParent)throws IOException{
        ArrayList stack_let = new ArrayList();
	    String line = in.readLine();
	    hmFun = new HashMap(hmParent);
	    this.args = Arg;
	    this.name = funName;
        String function = funName;
        while(!line.equals("funEnd")){
           funBody.add(line);
           line = in.readLine();
         }
       
         // hm.put(function,funBody);  
        // System.out.println(Arrays.toString(funBody.toArray()));
       }
       public void setflag(){
           inOut = true;
       }
       public Boolean isIn(){
           return inOut;
       }
	  public String getarg(){
	      return this.args;
	  }
	  public String getname(){
	      return this.name;
	  }
	  public List<String> getBody(){
	      return this.funBody;
	  }
	  public HashMap getEnv(){
	      return this.hmFun;
	  }
	    //return list.get(0);
	    //System.out.println(function);
	   // System.out.println(list.get(0));
	 
	  
	    
	}
	
	public static ArrayList parseBooleanOrError(String line, ArrayList stack, HashMap hm) {
		if (line.startsWith(":e")){
			stack.add(0, ":error:");
		}
		else if (line.startsWith(":t")){
			stack.add(0, ":true:");
		}
		else if (line.startsWith(":f")){
			stack.add(0, ":false:");
		}
		
		return stack;
	}

	public static ArrayList doMul(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			stack.remove(0);
			stack.remove(0);
			Integer newTop = x*y;
			stack.add(0, newTop.toString());
		}
		return stack;
	}

	public static ArrayList doSub(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			stack.remove(0);
			stack.remove(0);
			Integer newTop = x-y;
			stack.add(0, newTop.toString());
		}
		return stack;
	}

	public static ArrayList doAdd(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("(.*)[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("(.*)[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			stack.remove(0);
			stack.remove(0);
			Integer newTop = x+y;
			stack.add(0, newTop.toString());
		}
		return stack;
	}

	public static ArrayList parsePrimitive(String line, ArrayList stack, HashMap hm, HashMap funHm,HashMap environs, PrintStream myconsole) throws IOException{
		if (line.startsWith("add")){
			stack = doAdd(stack,hm);
		}
		else if (line.startsWith("sub")){
			stack = doSub(stack,hm);
		}
		else if (line.startsWith("mul")){
			stack = doMul(stack,hm);
		}
		else if (line.startsWith("div")){
			stack = doDiv(stack,hm);
		}
		else if (line.startsWith("rem")){
			stack = doRem(stack, hm);
		}
		else if (line.startsWith("pop")){
			stack = doPop(stack);
		}
		else if (line.startsWith("push")){
			stack = doPush(stack, line,hm);
		}
		else if (line.startsWith("swap")){
			stack = doSwap(stack);
		}
		else if (line.startsWith("neg")){
			stack = doNeg(stack,hm);
		}
		else if (line.startsWith("quit")){
			doQuit(stack, myconsole);
		}
		else if (line.startsWith("if")) {
			doIf(stack,hm);
		}
		else if (line.startsWith("not")) {
			doNot(stack,hm);
		}
		else if (line.startsWith("and")) {
			doAnd(stack,hm);
		}
		else if (line.startsWith("or")) {
			doOr(stack,hm);
		}			
		else if (line.startsWith("equal")) {
			doEqual(stack,hm);
		}
		else if (line.startsWith("lessThan")) {
			doLessThan(stack,hm);
		}
		else if (line.startsWith("bind")) {
			doBind(stack,hm);
		}
		else if (line.startsWith("call")){
		    doCall(stack,hm,funHm,environs,myconsole);
		}

		return stack;
	}

public static Object getKeyFromValue(Map hm, Object value) {
    for (Object o : hm.keySet()) {
      if (hm.get(o).equals(value)) {
        return o;
      }
    }
    return null;
  }

   public static Object doCall(ArrayList stack, HashMap hm,HashMap funHm,HashMap environs,PrintStream myconsole) throws IOException{
        ArrayList functionstack = new ArrayList();
        ArrayList<String> body = new ArrayList<String>();
        HashMap localfun = null;
        HashMap local = new HashMap<String,String>();
        String x=null;String fun=null;String k=null;String arg=null;String copy = null;String namevalue =null;
        int s=0;
        
      
        if (stack.size() < 2){
			stack.add(0, ":error:");
		}
		else {
			// copy = stack.pop();
			if(hm.containsKey((String) stack.get(0))){
			    fun = (String)hm.get((String)stack.get(0));
			}
			 else{fun = (String) stack.get(0);}
			 arg = (String) stack.get(1);
			 copy = arg;
			// System.out.println("called " + fun);
		//	System.out.println("this is arg" + copy + "and this is function" + fun);
		
			if (hm.containsKey(arg)){
				x = (String)hm.get(arg);
			}
				else{
					x = arg;
				}
			if(funHm.get(fun)==null){
		             stack.add(0, ":error:");
		             return stack;
			}
			if ((((String) stack.get(0)).startsWith(":e") || (((String) stack.get(1)).startsWith(":e")))){
			stack.add(0, ":error:");
			return stack;
	    	}
			else if(funHm.containsKey(fun)){
		            localfun = new HashMap(((doFun) funHm.get(fun)).getEnv());
		            localfun.put((((doFun) funHm.get(fun)).getarg()), x);
		      
		            environs.put(fun,localfun);
		            body = (ArrayList) ((doFun) funHm.get(fun)).getBody();
		            local =(HashMap) environs.get(fun);
		            FileWriter writer = new FileWriter("body.txt"); 
					for(String str: body) {
					  writer.write(str + '\n');
					}	
					writer.close();
						stack.remove(0);
			            stack.remove(0);
		    }
		}
//	 for (String l : body) { System.out.println("thisis" + l); }
			BufferedReader ins = new BufferedReader(new FileReader("body.txt"));
			 
			String lines = ins.readLine();
			while(lines!=null){
                //System.out.println("maped to " + (String)localfun.get((((doFun) funHm.get(fun)).getarg())));
              // System.out.println("key is " + localfun.containsKey(((doFun) funHm.get(fun)).getarg()));
              // System.out.println("empty? " + funHm.containsKey("y"));
              
                s++;
				if(lines.equals("let")) {
					functionstack.add(0,parseLet(ins,local,funHm,environs,myconsole));
				}
				else if(lines.equals("return")){
				   if (local.containsKey((String) functionstack.get(0))){
		            namevalue = (String) local.get(functionstack.get(0));
		            stack.add(0,namevalue);
		            //System.out.println("this is" +namevalue);
				   }
				   else{
				    stack.add(0,functionstack.get(0));
				   // System.out.println("this is" +(String) functionstack.get(0));
				   }
				  //  System.out.println("hi");
				}
				else if(lines.startsWith("fun")){
		//	    stack.add(0,"hello");
		        String[] delim = lines.split(" ");
                String funName = delim[1];
                String Args = delim[2];
			    doFun funs= new doFun(ins,myconsole,funName,Args,local);
			    funHm.put(funName,funs);
			    functionstack.add(0,":unit:");
			    //if (funHm.containsKey("myname")){
			   //  String s = ((doFun) funHm.get("myname")).getarg();
			    //    System.out.println(s);
			   // }
			   
			}
			else if(lines.startsWith("inOut")){
			     String[] delim = lines.split(" ");
                String funName = delim[1];
                String Args = delim[2];
			    doFun funs= new doFun(ins,myconsole,funName,Args,local);
			    funs.setflag();
			    funHm.put(funName,funs);
			    functionstack.add(0,":unit:");
			    
			}
				else if(Character.isLetter(lines.charAt(0))){
					functionstack = parsePrimitive(lines, functionstack, local,funHm,environs, myconsole);
				}
				else if(lines.charAt(0)==':'){
					functionstack = parseBooleanOrError(lines, functionstack, local);
				}
			
				else{
					myconsole.println("Error command!");
				}
			lines = ins.readLine();
			}
//			for (int i = 0; i < functionstack.size(); i++){
//			String p = (String) functionstack.get(i);
//			myconsole.println(p.replace("\"",""));
//		}
			ins.close();
			if(((doFun) funHm.get(fun)).isIn()){
			    
		/*	 String ans = (String) getKeyFromValue(hm,arg);
    
			    System.out.println("I BEEN LOOKING FOR" + ans);
			    
			    
			  Iterator iterator = hm.keySet().iterator();

                while (iterator.hasNext()) {
                   
                String key = iterator.next().toString();
                if(hm.containsKey(key)){
                    
                }
                String value = (String) localfun.get(key);
                System.out.println("key and value is " + copy + "and " + (String)functionstack.get(0));
                
                 hm.put(copy,functionstack.get(0));
                }*/
                
               HashMap temp = new HashMap();
                temp.put( arg,local.get((((doFun) funHm.get(fun)).getarg())));
                hm.putAll(temp);
                   
			  Iterator iterator = temp.keySet().iterator();

                while (iterator.hasNext()) {
                   
                String key = iterator.next().toString();
               
                String value = (String) localfun.get(key);
                //System.out.println("key and value is " + key + "and " + value);
                }
                
                
			}
		
//	System.out.println("this is" + s);
		return stack;
   }
        
		        
	//}
	public static void doQuit(ArrayList stack, PrintStream myconsole) {
		for (int i = 0; i < stack.size(); i++){
			String s = (String) stack.get(i);
			myconsole.println(s.replace("\"",""));
		}
		myconsole.close();
	}

	public static ArrayList doNeg(ArrayList stack, HashMap hm) {
		if (stack.isEmpty()){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
            int x;
            String s0 = (String) stack.get(0);
            if(s0.matches("(.*)[0-9]+")) x = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
                    stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s0_1);
            }
			Integer newTop = -1*x;
			stack.remove(0);
			stack.add(0, newTop.toString());
        }
		return stack;
	}

	private static ArrayList doSwap(ArrayList stack) {
		if (stack.size() < 2){
			stack.add(0, ":error:");
		}
		else{
			String x = (String) stack.get(1);
			String y = (String) stack.get(0);
			stack.remove(0);
			stack.remove(0);
			stack.add(0, y);
			stack.add(0, x);
		}
		return stack;
	}

	public static ArrayList doPush(ArrayList stack, String line,HashMap hm) {
        
		String getNum = line.substring(5);
	 
		/*if (hm.containsKey(getNum)){
		    getNum = (String) hm.get(getNum);
		    System.out.println("hallo");
		}*/
		if (getNum.charAt(0) == '-'){
			if (getNum.substring(1).equals("0")){
				stack.add("0");
			}
			else if (getNum.substring(1).matches("[0-9]+")){
				stack.add(0, getNum);
			}
			else{
				stack.add(0, ":error:");
			}
		}
		else if (getNum.matches("[0-9]+")){
			stack.add(0, getNum);
		}
		else if (getNum.matches("^[a-zA-Z].*")){
			stack.add(0, getNum);
		}
		else if (getNum.matches("^\".+\"$")){
			stack.add(0, getNum);
		}
		else{
			stack.add(0, ":error:");
		}
		return stack;
	}

	public static ArrayList doPop(ArrayList stack) {
		if (stack.size() < 1){
			stack.add(0, ":error:");
		}
		else{
			stack.remove(0);
		}
		return stack;
	}

	public static ArrayList doRem(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			if (y == 0){
				stack.add(0, ":error:");
			}
			else{
				stack.remove(0);
				stack.remove(0);
				Integer newTop = x%y;
				stack.add(0, newTop.toString());
			}
		}
		return stack;
	}

	public static ArrayList doDiv(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			if (y == 0){
				stack.add(0, ":error:");
			}
			else{
				stack.remove(0);
				stack.remove(0);
				Integer newTop = x/y;
				stack.add(0, newTop.toString());
			}
		}
		return stack;
	}
	
	public static ArrayList doIf(ArrayList stack, HashMap hm) {
		if (stack.size()<3){
			stack.add(0, ":error:");
		}
		else {
            String s2 = (String) stack.get(2);
            
			if(!s2.equals(":true:") && !s2.equals(":false:")) s2 = (String) hm.get(s2);
            if (s2 != null && s2.equals(":true:")) {
                stack.remove(2);
                stack.remove(1);
				
            }
            else if (s2 != null && s2.equals(":false:")) {
                stack.remove(2);
                stack.remove(0);
				
            }
            else{
                stack.add(0, ":error:");
            }
        }
		return stack;
	}
	
	public static ArrayList doNot(ArrayList stack, HashMap hm) {
		if (stack.size()<1){
			stack.add(0, ":error:");
		}
        else {
            String s0 = (String) stack.get(0);
            if(!s0.equals(":true:") && !s0.equals(":false:")) s0 = (String) hm.get(s0);
            if(s0 == null){
                stack.add(0, ":error:");
            }
            else if (s0.equals(":true:")) {
                stack.remove(0);
                stack.add(0, ":false:");
            }
            else if (s0.equals(":false:")) {
                stack.remove(0);
                stack.add(0, ":true:");
            }
            else{
                stack.add(0, ":error:");
            }
        }
		return stack;
	}
	
	public static ArrayList doAnd(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else {
			String s0 = (String) stack.get(0);
            if(!s0.equals(":true:") && !s0.equals(":false:")) s0 = (String) hm.get(s0);
			String s1 = (String) stack.get(1);
            if(!s1.equals(":true:") && !s1.equals(":false:")) s1 = (String) hm.get(s1);
            if(s0 == null || s1 == null) {
                stack.add(0, ":error:");
            }
			else if (s0.equals(":false:") && s1.equals(":false:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":false:");
			}
			else if (s0.equals(":false:") && s1.equals(":true:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":false:");
			}
			else if (s0.equals(":true:") && s1.equals(":false:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":false:");
			}
			else if (s0.equals(":true:") && s1.equals(":true:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":true:");
			}
			else{
				stack.add(0, ":error:");
			}
		}
		return stack;
	}
	
	public static ArrayList doOr(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else {
			String s0 = (String) stack.get(0);
            if(!s0.equals(":true:") && !s0.equals(":false:")) s0 = (String) hm.get(s0);
			String s1 = (String) stack.get(1);
            if(!s1.equals(":true:") && !s1.equals(":false:")) s1 = (String) hm.get(s1);
            if(s0 == null || s1 == null) {
                stack.add(0, ":error:");
            }
			else if (s0.equals(":false:") && s1.equals(":false:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":false:");
			}
			else if (s0.equals(":false:") && s1.equals(":true:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":true:");
			}
			else if (s0.equals(":true:") && s1.equals(":false:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":true:");
			}
			else if (s0.equals(":true:") && s1.equals(":true:")) {
				stack.remove(0);
				stack.remove(0);
				stack.add(0, ":true:");
			}
			else{
				stack.add(0, ":error:");
			}
		}
		return stack;
	}
	
	public static ArrayList doEqual(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			stack.remove(0);
			stack.remove(0);
            if(x == y) stack.add(0, ":true:");
            else stack.add(0, ":false:");
		}
		return stack;
	}
	
	public static ArrayList doLessThan(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else if (((String) stack.get(0)).charAt(0) == ':' || ((String) stack.get(1)).charAt(0) == ':'){
			stack.add(0, ":error:");
		}
		else{
			String s1 = (String) stack.get(1);
			String s0 = (String) stack.get(0);
			int x,y;
			if(s1.matches("[0-9]+")) x = Integer.parseInt(s1);
			else {
				String s1_1 = (String) hm.get(s1);
				if(s1_1 == null || !s1_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				x = Integer.parseInt(s1_1);
			}
			if(s0.matches("[0-9]+")) y = Integer.parseInt(s0);
			else {
				String s0_1 = (String) hm.get(s0);
				if(s0_1 == null || !s0_1.matches("[0-9]+")) {
					stack.add(0, ":error:");
					return stack;
				}
				y = Integer.parseInt(s0_1);
			}
			stack.remove(0);
			stack.remove(0);
            if(x < y) stack.add(0, ":true:");
            else stack.add(0, ":false:");
		}
		return stack;
	}
	
	public static ArrayList doBind(ArrayList stack, HashMap hm) {
		if (stack.size()<2){
			stack.add(0, ":error:");
		}
		else {
			String s0 = (String) stack.get(0);
			String s1 = (String) stack.get(1);
			if(s1.matches("^[a-zA-Z].*") && !s0.equals(":error:")) {
				stack.remove(0);
				stack.remove(0);
				Object o = hm.get(s0);
				if(o!=null) hm.put(s1,o);
				else hm.put(s1,s0);
				stack.add(0, ":unit:");
			}
			else {
				stack.add(0, ":error:");
			}
		}
		return stack;
	}
	/*	public static void main(String[] args){
	    try{
	    hw4("input.txt","output.txt");  
	}catch (IOException ie){
	    System.out.println("his");
	}
	}*/
}
