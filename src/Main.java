import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Main {

	private StringBuilder output = new StringBuilder();

	public static Set<String> readInput() throws IOException {
		Set<String> set = new HashSet<String>();
		HashMap<String, String> map = new HashMap<String, String>();
		File file = new File("input.txt");
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String line, key, value = "";
		while ((line = bufferedReader.readLine()) != null) {
			key = line.substring(0, line.indexOf(":"));
			if (map.containsKey(key))
				value = map.get(key);
			else
				value = "";
			value += line.substring(line.indexOf("task") + 5, line.length());
			map.put(key, value);
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			set.add(entry.getValue());
		}
		return set;
	}

	static ArrayList<String> getSubsets(String s) {

		int n = s.length();
		ArrayList<String> subsets = new ArrayList<String>();

		for (int i = 0; i < (1 << n); i++) {
			String sub = "";
			for (int j = 0; j < n; j++) {
				if ((i & (1 << j)) > 0) {
					sub += s.charAt(j);
				}
			}
			subsets.add(sub);
		}
		return subsets;
	}
	

	public static void main(String[] args) {

		try {
			Set<String> W = readInput();
			Set<String> Tw = new HashSet<String>();
			Set<String> Ti = new HashSet<String>();
			Set<String> To = new HashSet<String>();
			Set<Pair> Xw = new HashSet<Pair>();
			Set<Pair> Yw = new HashSet<Pair>();
			Set<String> directFollower = new HashSet<String>();
			Set<String> causal = new HashSet<String>();
			Set<String> parallel = new HashSet<String>();

			for (String s : W) {
				for (int i = 0; i < s.length(); ++i) {
					if (i == 0)
						Ti.add(s.charAt(i) + "");
					else if (i == s.length() - 1)
						To.add(s.charAt(i) + "");
					Tw.add(s.charAt(i) + "");
				}
			}
			for (String s : W) {
				for (int i = 0; i < s.length() - 1; i++) {
					directFollower.add(s.charAt(i) + ">" + s.charAt(i + 1));
				}
			}
			for (String s : directFollower) {
				boolean flag = false;
				for (String t : directFollower) {
					if (s.charAt(0) == t.charAt(2) && s.charAt(2) == t.charAt(0)) {
						parallel.add(s.charAt(0) + "||" + s.charAt(2));
						parallel.add(t.charAt(0) + "||" + t.charAt(2));
						flag = true;
					}
				}
				if (flag == false) {
					causal.add(s.charAt(0) + "->" + s.charAt(2));
				}
			}
			String res = "";
			for (String s : Tw) {
				res += s;
			}
			ArrayList<String> subsets = getSubsets(res);
//			System.out.println(subsets);
			for (int i = 1; i < subsets.size(); ++i) {
				String A = subsets.get(i);
				for (int j = 1; j < subsets.size(); ++j) {
					String B = subsets.get(j);
					boolean causality = true;
					for (int k = 0; k < A.length(); ++k) {
						boolean flag = true;
						for (int l = 0; l < B.length(); ++l) {
							if (!causal.contains(A.charAt(k) + "->" + B.charAt(l))) {
								flag = false;
								break;
							}
						}
						if (flag == false) {
							causality = false;
							break;
						}
					}
					if (causality) {
						boolean execlusive1 = true, execlusive2 = true;
						for (int k = 0; k < A.length(); ++k) {
							
							for (int l = 0; l < A.length(); ++l) {
								if (causal.contains(A.charAt(k) + "->" + A.charAt(l))
										|| parallel.contains(A.charAt(k) + "||" + A.charAt(l))
										|| directFollower.contains(A.charAt(k) + ">" + A.charAt(l))) {
									execlusive1 = false;
									break;
								}
							}
							if(execlusive1 == false)
								break;
							else{
								execlusive1 = true;
							}
						}
						for (int k = 0; k < B.length(); ++k) {
							for (int l = 0; l < B.length(); ++l) {
								if (causal.contains(B.charAt(k) + "->" + B.charAt(l))
										|| parallel.contains(B.charAt(k) + "||" + B.charAt(l))
										|| directFollower.contains(B.charAt(k) + ">" + B.charAt(l))) {
									execlusive2 = false;
									break;
								}
							}
							if(execlusive2 == false)
								break;
							else{
								execlusive2 = true;
							}
						}
						if(execlusive1 && execlusive2){
							Set<String> setA = new HashSet<String>();
							Set<String> setB = new HashSet<String>();
							
							for(int k = 0; k < A.length(); ++k){
								setA.add(A.charAt(k) + "");
							}
							for(int k = 0; k < B.length(); ++k){
								setB.add(B.charAt(k) + "");
							}
							Pair p = new Pair(setA, setB);
							Xw.add(p);
							
						}

					}
				}
			}
			
			for(Pair p : Xw){
				boolean flag = true;
				for(Pair pp : Xw){
					boolean a = pp.getLeft().containsAll(p.getLeft()) && pp.getRight().containsAll(p.getRight());
					boolean b = p.equal(pp);
					if(!((a && b) || (!a && !b) || (!a && b))){
						flag = false;
						break;
					}
				}
				if(flag){
					Yw.add(p);
				}
				
			}
			//printing W
			String out = "W = { ";
			for(String s: W){
				out += s + ", ";
			}
			out += "}";
			out = out.replace(", }", " }");
			System.out.println(out);
			//printing Tw
			out = "Tw = { ";
			for(String s: Tw){
				out += s + ", ";
			}
			out += "}";
			out = out.replace(", }", " }");
			System.out.println(out);
			
			//printing Ti
			out = "Ti = { ";
			for(String s: Ti){
				out += s + ", ";
			}
			out += "}";
			out = out.replace(", }", " }");
			System.out.println(out);
			
			//printing To
			out = "To = { ";
			for(String s: To){
				out += s + ", ";
			}
			out += "}";
			out = out.replace(", }", " }");
			System.out.println(out);
			
			//printing Xw
			out = "Xw = { ";
			for(Pair p: Xw){
				out += p.getPair() + ",\n";
			}
			out += "}";
			out = out.replace("),\n}", " }");
			System.out.println(out + "\n");
			
			//printing Yw
			out = "Yw = { ";
			for(Pair p: Yw){
				out += p.getPair() + ",\n";
			}
			out += "}";
			out = out.replace("),\n}", " }");
			System.out.println(out + "\n");
			
			//printing Pw
			out = "Pw = { ";
			for(Pair p: Yw){
				out += "P" + p.getPair() + ",\n";
			}
			out += "Iw" + ", ";
			out += "Ow";
			out += " }";
			System.out.println(out + "\n");
			
			//printing Fw
			out = "Fw = {\n";
			for(Pair p: Yw){
				for(String pp: p.getLeft()){
					out += "(" +pp + ", P" + p.getPair() + ")\n";
				}
			}
			for(Pair p: Yw){
				for(String pp: p.getRight()){
					out += "(P" +p.getPair() + ", " + pp + ")\n";
				}
			}
			for(String s: Ti){
				out += "(Iw," + s + "),\n";
			}
			for(String s: To){
				out += "(Iw," + s + "),\n";
			}
			out += "}";
			out = out.replace("),\n}", ")\n}");
			System.out.println(out);
			

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
