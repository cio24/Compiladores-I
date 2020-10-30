package usefulClassesPackage;

public class ProcedureData {
    private String id;
    private int NA;
    private boolean shadowing;

    public ProcedureData(String id, int NA, boolean shadowing) {
        this.id = id;
        this.NA = NA;
        this.shadowing = shadowing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void sumNA() {
        this.NA++;
    }

    public int subtractNA() {
        return --this.NA;
    }

    public boolean isShadowing() {
        return shadowing;
    }

    public void setShadowing(boolean shadowing) {
        this.shadowing = shadowing;
    }
    
    public int getNA() {
    	return NA;
    }
    
    public String getShadowing() {
    	if (shadowing) {
    		return "TRUE";
    	}else {
    		return "FALSE";
    	}
    }
}
