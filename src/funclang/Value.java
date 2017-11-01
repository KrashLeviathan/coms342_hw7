package funclang;

import java.util.List;

import funclang.AST.Exp;

@SuppressWarnings("ALL")
public interface Value {
	public String tostring();
	public boolean equals(Value other);
    static class RefVal implements Value {
		private int _loc;
		public RefVal(int location) {
			_loc = location;
		}
		public int loc() { return _loc; }
		@Override
		public String tostring() {
			return "loc:" + _loc;
		}
		// https://www.sitepoint.com/implement-javas-equals-method-correctly/
		@Override
		public boolean equals(Value other) {
			if (this == other) {
				return true;
			}
			if (other == null) {
				return false;
			}
			if (getClass() != other.getClass()) {
				return false;
			}
			RefVal casted = (RefVal) other;
			return this.loc() == casted.loc();
		}
	}
	static class FunVal implements Value { //New in the funclang
		private Env _env;
		private List<String> _formals;
		private Exp _body;
		private Exp _optLastExp;
		public FunVal(Env env, List<String> formals, Exp body, Exp optLastExp) {
			_env = env;
			_formals = formals;
			_body = body;
			_optLastExp = optLastExp;
		}
		public Env env() { return _env; }
		public List<String> formals() { return _formals; }
		public Exp body() { return _body; }
		public Exp optLastExp() { return _optLastExp; }
	    public String tostring() {
			String result = "(lambda ( ";
			if (_optLastExp == null) {
				for (String formal : _formals) {
					result += formal + " ";
				}
			} else {
				int i = 0;
				while (i < _formals.size()-1) {
					result += _formals.get(i) + " ";
					i++;
				}
				result += "( " + _formals.get(i) + " = " + _optLastExp.accept(new Printer.Formatter(), _env) + " )";
			}
			result += ") ";
			result += _body.accept(new Printer.Formatter(), _env);
			return result + ")";
	    }
		// https://www.sitepoint.com/implement-javas-equals-method-correctly/
		public boolean equals(Value other) {
			return (this == other);
		}
	}
	static class NumVal implements Value {
	    private double _val;
	    public NumVal(double v) { _val = v; }
	    public double v() { return _val; }
	    public String tostring() {
	    	int tmp = (int) _val;
	    	if(tmp == _val) return "" + tmp;
	    	return "" + _val;
	    }
		// https://www.sitepoint.com/implement-javas-equals-method-correctly/
		public boolean equals(Value other) {
			if (this == other) {
				return true;
			}
			if (other == null) {
				return false;
			}
			if (getClass() != other.getClass()) {
				return false;
			}
			NumVal casted = (NumVal) other;
			return this.v() == casted.v();
		}
	}
	static class BoolVal implements Value {
		private boolean _val;
	    public BoolVal(boolean v) { _val = v; }
	    public boolean v() { return _val; }
	    public String tostring() { if(_val) return "#t"; return "#f"; }
		// https://www.sitepoint.com/implement-javas-equals-method-correctly/
		public boolean equals(Value other) {
			if (this == other) {
				return true;
			}
			if (other == null) {
				return false;
			}
			if (getClass() != other.getClass()) {
				return false;
			}
			BoolVal casted = (BoolVal) other;
			return (this.v() && casted.v()) || (!this.v() && !casted.v());
		}
	}
	static class StringVal implements Value {
		private java.lang.String _val;
	    public StringVal(String v) { _val = v; }
	    public String v() { return _val; }
	    public java.lang.String tostring() { return "" + _val; }
		// https://www.sitepoint.com/implement-javas-equals-method-correctly/
		public boolean equals(Value other) {
			if (this == other) {
				return true;
			}
			if (other == null) {
				return false;
			}
			if (getClass() != other.getClass()) {
				return false;
			}
			StringVal casted = (StringVal) other;
			return this.v().equals(casted.v());
		}
	}
	static class PairVal implements Value {
		protected Value _fst;
		protected Value _snd;
	    public PairVal(Value fst, Value snd) { _fst = fst; _snd = snd; }
		public Value fst() { return _fst; }
		public Value snd() { return _snd; }
	    public java.lang.String tostring() {
	    	if(isList()) return listToString();
	    	return "(" + _fst.tostring() + " " + _snd.tostring() + ")";
	    }
	    protected boolean isList() {
	    	if(_snd instanceof Value.Null) return true;
	    	if(_snd instanceof Value.PairVal &&
	    		((Value.PairVal) _snd).isList()) return true;
	    	return false;
	    }
	    private java.lang.String listToString() {
	    	String result = "(";
	    	result += _fst.tostring();
	    	Value next = _snd;
	    	while(!(next instanceof Value.Null)) {
	    		result += " " + ((PairVal) next)._fst.tostring();
	    		next = ((PairVal) next)._snd;
	    	}
	    	return result + ")";
	    }
		// https://www.sitepoint.com/implement-javas-equals-method-correctly/
		public boolean equals(Value other) {
			if (this == other) {
				return true;
			}
			if (other == null) {
				return false;
			}
			if (getClass() != other.getClass()) {
				return false;
			}
			PairVal casted = (PairVal) other;
			return this.fst().equals(casted.fst()) && this.snd().equals(casted.snd());
		}
	}
	static class Null implements Value {
		public Null() {}
	    public String tostring() { return "()"; }
		// https://www.sitepoint.com/implement-javas-equals-method-correctly/
		public boolean equals(Value other) {
			return this == other || other != null && getClass() == other.getClass();
		}
	}
	static class UnitVal implements Value {
		public static final UnitVal v = new UnitVal();
	    public String tostring() { return ""; }
		// https://www.sitepoint.com/implement-javas-equals-method-correctly/
		public boolean equals(Value other) {
			return this == other || other != null && getClass() == other.getClass();
		}
	}
	static class DynamicError implements Value {
		private String message = "Unknown dynamic error.";
		public DynamicError(String message) { this.message = message; }
	    public String tostring() { return "" + message; }
		// https://www.sitepoint.com/implement-javas-equals-method-correctly/
		public boolean equals(Value other) {
			return this == other || other != null && getClass() == other.getClass();
		}
	}
}
