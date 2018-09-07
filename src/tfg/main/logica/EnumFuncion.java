package tfg.main.logica;

public enum EnumFuncion {
	FC1,FC2,FC3,FC4,FC5,FC6,FC7,FC8,FC9;
	
	public String toString(){
		switch (this){
			case FC1: return "Data of a tree";
			case FC2: return "Homogeneous intervals";
			case FC3: return "Data comparison";
			case FC4: return "Search for discrepancies between two trees";
			case FC5: return "Growth of a tree trunk";
			case FC6: return "Comparison of trunks";
			case FC7: return "Contraction in a tree";
			case FC8: return "Comparison of contractions";
			default: return "Patterns in a tree";
		}
	}
}
