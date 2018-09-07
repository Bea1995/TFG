package tfg.main.logica;

public enum EnumPatron {
	FuerBrut,BoyMor,ShAdd;
	
	public String toString(){
		switch (this){
			case FuerBrut: return "Brute Force";
			case BoyMor: return "Boyer Moore";
			default: return "Shift-Add";
		}
	}
}
