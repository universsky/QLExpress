package com.ql.util.express.instruction.opdata;

import com.ql.util.express.ExpressUtil;
import com.ql.util.express.InstructionSetContext;

public class OperateDataField extends OperateDataAttr {
	Object fieldObject;
	String orgiFieldName;
	
	public OperateDataField(Object aFieldObject,String aFieldName) {
		super(null,null);
		this.name = aFieldObject.getClass().getName() + "." + aFieldName;
		this.fieldObject = aFieldObject;
		this.orgiFieldName =aFieldName;
	}
	
    public String getName(){
    	return name;
    }
	public String toString() {
		try {			
			return name;
		} catch (Exception ex) {
			return ex.getMessage();
		}
	}

    public Object transferFieldName(InstructionSetContext<String,Object> context,String oldName){
		if (context.isSupportDynamicFieldName() == false) {
			return oldName;
		} else {
			try {
				OperateDataAttr o = (OperateDataAttr) context
						.findAliasOrDefSymbol(oldName);
				if (o != null) {
					return o.getObject(context);
				} else {
					return oldName;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
    }
	public Object getObjectInner(InstructionSetContext<String,Object> context) throws Exception {
		//如果能找到aFieldName的定义,则再次运算
		if(this.fieldObject instanceof OperateDataVirClass){
			return ((OperateDataVirClass)this.fieldObject).getValue(transferFieldName(context,this.orgiFieldName));
		}else{
		  return ExpressUtil.getProperty(this.fieldObject,transferFieldName(context,this.orgiFieldName));
		}
	}
    
	public Class<?> getType(InstructionSetContext<String,Object> context) throws Exception {
		if(this.fieldObject instanceof OperateDataVirClass){
			return ((OperateDataVirClass)this.fieldObject).getValueType(transferFieldName(context,this.orgiFieldName));
		}else{
		    return ExpressUtil.getPropertyClass(this.fieldObject,transferFieldName(context,this.orgiFieldName));
		}
	}

	public void setObject(InstructionSetContext<String,Object> context, Object value) throws Exception{
		if(this.fieldObject instanceof OperateDataVirClass){
			((OperateDataVirClass)this.fieldObject).setValue(transferFieldName(context,this.orgiFieldName).toString(),value);
		}else{
			ExpressUtil.setProperty(fieldObject, transferFieldName(context,this.orgiFieldName), value);
		}
		
	}
}