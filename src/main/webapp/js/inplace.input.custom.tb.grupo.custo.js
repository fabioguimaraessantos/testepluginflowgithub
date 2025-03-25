if(!window.LOG){window.LOG={warn:function(){}}
}if(!window.Richfaces){window.Richfaces={}
}Richfaces.InplaceInput=Class.create();
Richfaces.InplaceInput.prototype={initialize:function(A,B,I,H,D,G,F,E,C){this.inplaceInput=$(A);
this.inplaceInput.component=this;
this.tempValueKeeper=$(B);
this.valueKeeper=$(I);
this.attributes=D;
this.events=G;
this.currentText=this.getCurrentText();
this.value=this.valueKeeper.value;
this.currentState=Richfaces.InplaceInput.STATES[0];
this.prevState=Richfaces.InplaceInput.STATES[0];
if(this.attributes.showControls){this.bar=new Richfaces.InplaceInputBar(C[0],C[1],C[2],C[3],C[4],this.attributes.verticalPosition,this.attributes.horizontalPosition)
}this.editEvent=this.attributes.editEvent.substring(2,this.attributes.editEvent.length);
this.tempValueKeeper.style.top=Richfaces.getBorderWidth(this.tempValueKeeper,"t")+"px";
this.initHandlers();
this.initEvents();
this.classes=Richfaces.mergeStyles(F,E.getCommonStyles());
this["rich:destructor"]="destroy";
this.skipSwitching=false
},destroy:function(){this.inplaceInput.component=null
},initHandlers:function(){this.inplaceInput.observe(this.editEvent,this.switchingStatesHandler.bindAsEventListener(this));
this.inplaceInput.observe("mouseout",this.inplaceMouseOutHandler.bindAsEventListener(this));
this.inplaceInput.observe("mouseover",this.inplaceMouseOverHandler.bindAsEventListener(this));
this.tempValueKeeper.observe("focus",function(A){this.switchingStatesHandler(A)
}.bindAsEventListener(this));
this.tempValueKeeper.observe("blur",function(A){this.tmpValueBlurHandler(A)
}.bindAsEventListener(this));
this.tempValueKeeper.observe("keydown",function(A){this.tmpValueKeyDownHandler(A)
}.bindAsEventListener(this));
if(this.bar){if(this.bar.ok){this.bar.ok.observe("mousedown",function(A){this.okHandler(A)
}.bindAsEventListener(this));
if(Richfaces.browser.isOpera){this.bar.ok.observe("click",Event.stop)
}}if(this.bar.cancel){this.bar.cancel.observe("mousedown",function(A){this.cancelHandler(A)
}.bindAsEventListener(this));
if(Richfaces.browser.isOpera){this.bar.cancel.observe("click",Event.stop)
}}}},initEvents:function(){for(var A in this.events){if(A){this.inplaceInput.observe("rich:"+A,this.events[A])
}}},inplaceMouseOverHandler:function(B){var A=this.inplaceInput.className;
if(this.currentState==Richfaces.InplaceInput.STATES[0]){if(A.indexOf(this.classes.component.view.hovered)==-1){A+=" "+this.classes.component.view.hovered
}}else{if(this.currentState==Richfaces.InplaceInput.STATES[2]){if(A.indexOf(this.classes.component.changed.hovered)==-1){A+=" "+this.classes.component.changed.hovered
}}}this.inplaceInput.className=A
},inplaceMouseOutHandler:function(A){if(this.currentState==Richfaces.InplaceInput.STATES[0]){this.inplaceInput.className=this.classes.component.view.normal
}else{if(this.currentState==Richfaces.InplaceInput.STATES[2]){this.inplaceInput.className=this.classes.component.changed.normal
}}},switchingStatesHandler:function(B){if(this.skipSwitching){this.skipSwitching=false;
return 
}var A=(B.srcElement)?B.srcElement:B.target;
if(this.currentState!=Richfaces.InplaceInput.STATES[1]){this.edit()
}if(A.id==this.inplaceInput.id){this.skipSwitching=true;
this.tempValueKeeper.focus()
}},edit:function(){if(Richfaces.invokeEvent(this.events.oneditactivation,this.inplaceInput,"rich:oneditactivation",{oldValue:this.valueKeeper.value,value:this.tempValueKeeper.value})){this.startEditableState();
if(this.events.oneditactivated){this.inplaceInput.fire("rich:oneditactivated",{oldValue:this.valueKeeper.value,value:this.tempValueKeeper.value})
}}},tmpValueBlurHandler:function(A){if(this.skipBlur){this.skipBlur=false;
return 
}if(this.clickOnBar){this.clickOnBar=false;
return 
}if(!this.attributes.showControls){this.save(A)
}},tmpValueKeyDownHandler:function(A){
	switch(A.keyCode){
		case Event.KEY_ESC:
			this.cancel(A);
		
			if(!this.attributes.showControls){
				this.skipBlur=true;
				this.tempValueKeeper.blur()
			}
			
			Event.stop(A);
			break;
		case Event.KEY_RETURN:
			if(this.attributes.showControls){
				this.okHandler(A)
			}else{
				Event.stop(A);
				this.tempValueKeeper.blur()
			}
			break;
		case Event.KEY_TAB:
			if(this.attributes.showControls){
				this.save(A)
			}
			break
	}
},
okHandler:function(A){
	this.save(A);
	Event.stop(A)
},
cancelHandler:function(A){
	this.cancel(A);
	Event.stop(A);
},
endEditableState:function(){if(this.bar){this.bar.hide()
}this.tempValueKeeper.style.clip="rect(0px 0px 0px 0px)";
this.tempValueKeeper.blur()
},startEditableState:function(){if(this.currentState==Richfaces.InplaceInput.STATES[1]){return 
}this.changeState(Richfaces.InplaceInput.STATES[1]);
var B=this.inplaceInput.offsetWidth;
var A=this.setInputWidth(B);
this.tempValueKeeper.style.clip="rect(auto auto auto auto)";
this.inplaceInput.className=this.classes.component.editable;
if(this.bar){this.bar.show(A,this.tempValueKeeper.offsetHeight)
}if(this.attributes.selectOnEdit){Richfaces.InplaceInput.textboxSelect(this.tempValueKeeper,0,this.tempValueKeeper.value.length)
}},startViewState:function(){this.deleteViewArtifacts();
this.changeState(Richfaces.InplaceInput.STATES[0]);
this.createNewText(this.currentText);
this.inplaceInput.className=this.classes.component.view.normal;
this.inplaceInput.observe("mouseover",function(A){this.inplaceMouseOverHandler(A)
}.bindAsEventListener(this))
},startChangedState:function(){this.deleteViewArtifacts();
this.changeState(Richfaces.InplaceInput.STATES[2]);
this.createNewText(this.currentText);
this.inplaceInput.className=this.classes.component.changed.normal
},setInputWidth:function(D){if(this.currentState!=1){return 
}var C=parseInt(this.attributes.inputWidth);
if(!C){var A=parseInt(this.attributes.maxInputWidth);
var B=parseInt(this.attributes.minInputWidth);
if(D>A){C=A
}else{if(D<B){C=B
}else{C=D
}}}this.tempValueKeeper.style.width=C+"px";
return C
},changeState:function(A){this.prevState=this.currentState;
this.currentState=A
},
cancel:function(B,A){
	if(Richfaces.invokeEvent(this.events.onviewactivation,this.inplaceInput,"rich:onviewactivation",{oldValue:this.valueKeeper.value,value:this.tempValueKeeper.value})){
		this.endEditableState();

		if(!A){
			A=this.valueKeeper.value
		}
		
		this.tempValueKeeper.value=A;
		this.currentText=A;
		
		if(this.tempValueKeeper.value==""){
			this.setDefaultText()
		}
		
		switch(this.prevState){
			case Richfaces.InplaceInput.STATES[0]:
				this.startViewState();
				break;
			case Richfaces.InplaceInput.STATES[2]:
				this.startChangedState();
				break
		}
		
		if(this.events.onviewactivated){
			this.inplaceInput.fire("rich:onviewactivated",{oldValue:this.valueKeeper.value,value:this.tempValueKeeper.value})
		}
	}
},
save:function(A){
	if(Richfaces.invokeEvent(this.events.onviewactivation,this.inplaceInput,"rich:onviewactivation",{oldValue:this.valueKeeper.value,value:this.tempValueKeeper.value})){
		var B=this.tempValueKeeper.value;
		
		this.setValue(B);
	
		if(this.events.onviewactivated){
			this.inplaceInput.fire("rich:onviewactivated",{oldValue:this.valueKeeper.value,value:this.tempValueKeeper.value});
		}
		
		var codigoOrcamentoDespesa = this.inplaceInput.parentNode.childNodes[0].value;
		var nomeOrcamentoDespesa = this.inplaceInput.parentNode.childNodes[1].value;
		
		updateNomeOrcamentoDespesa(codigoOrcamentoDespesa, nomeOrcamentoDespesa);
	}
},
getParameters:function(A,C,B){if(!C){C=A
}if(C&&C[B]){return C[B]
}return C
},setValue:function(C,E){var B=this.valueKeeper.value;
var D=this.getParameters(C,E,"value");
var A=true;
if(B!=D){A=Richfaces.invokeEvent(this.events.onchange,this.inplaceInput,"onchange",D)
}if(A){this.tempValueKeeper.value=D;
this.endEditableState();
if(D.blank()){this.setDefaultText();
this.valueKeeper.value=""
}else{this.currentText=D;
this.valueKeeper.value=D
}if(D!=this.value){this.startChangedState()
}else{this.startViewState()
}}else{this.cancel()
}},getValue:function(){return this.valueKeeper.value
},setDefaultText:function(){this.currentText=this.attributes.defaultLabel
},deleteViewArtifacts:function(){var A=this.inplaceInput.childNodes[3];
if(A){this.inplaceInput.removeChild(A)
}},getCurrentText:function(){return this.inplaceInput.childNodes[3]
},createNewText:function(A){if(!this.getCurrentText()){this.inplaceInput.appendChild(document.createTextNode(A.nodeValue||A))
}}};
Richfaces.InplaceInputBar=Class.create();
Richfaces.InplaceInputBar.prototype={initialize:function(E,G,F,D,B,A,C){this.bar=$(E);
this.ok=$(G);
this.cancel=$(F);
this.bsPanel=$(D);
this.buttonsShadow=$(B);
this.verticalPosition=A;
this.horizontalPosition=C;
this.initDimensions()
},initDimensions:function(){this.BAR_WIDTH=26;
this.BAR_HEIGHT=12
},show:function(B,A){this.positioning(B,A);
if(Richfaces.browser.isIE6&&this.buttonsShadow){this.buttonsShadow.style.visibility="hidden"
}this.bar.show()
},hide:function(){this.bar.hide()
},positioning:function(C,B){if(this.bsPanel){this.bsPanel.style.height=B+"px"
}this.bar.style.position="absolute";
var A=this.bar.style;
if(this.verticalPosition=="top"||this.verticalPosition=="bottom"){switch(this.horizontalPosition){case"left":A.left="0px";
break;
case"right":A.left=C+"px";
break;
case"center":A.left=parseInt(C/2-this.BAR_WIDTH/2)+"px";
break
}if(this.verticalPosition=="top"){A.top=-this.BAR_HEIGHT+"px"
}else{A.top=B+"px"
}}else{if(this.verticalPosition=="center"){A.top="0px";
switch(this.horizontalPosition){case"left":A.left=-this.BAR_WIDTH+"px";
break;
case"right":A.left=C+"px";
break;
case"center":A.left=C+"px";
break
}}}}};
Richfaces.InplaceInput.STATES=[0,1,2];
Richfaces.InplaceInput.textboxSelect=function(C,B,A){if(Prototype.Browser.IE){var D=C.createTextRange();
D.moveStart("character",B);
D.moveEnd("character",-C.value.length+A);
D.select()
}else{if(Prototype.Browser.Gecko){C.setSelectionRange(B,A)
}else{C.setSelectionRange(B,A)
}}};