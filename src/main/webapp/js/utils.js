/**
 * Marca como checado todos os inputs do tipo checkbox da tela.
 * 
 */
function checkAllCheckbox() {
	var inputRefArray = document.getElementsByTagName('input');

	for ( var i = 0; i < inputRefArray.length; i++) {
		var inputRef = inputRefArray[i];

		if (inputRef.type.substr(0, 8) == 'checkbox') {
			inputRef.checked = true;
		}
	}
}

/**
 * Desmarca todos os inputs do tipo checkbox da tela.
 * 
 */
function uncheckAllCheckbox() {
	var inputRefArray = document.getElementsByTagName('input');

	for ( var i = 0; i < inputRefArray.length; i++) {
		var inputRef = inputRefArray[i];

		if (inputRef.type.substr(0, 8) == 'checkbox') {
			inputRef.checked = false;
		}
	}
}

/**
 * Seleciona/Deseleciona todos os checkbox.
 * 
 * @param checkBox -
 *            id do checkbox de controle
 * 
 * @param element -
 *            elemento que emgloba os checkbox
 * 
 */
function checkUncheckAll(checkBox, elementId) {
	var element = document.getElementById(elementId);

	var inputRefArray;
	if (element == null) {
		inputRefArray = document.getElementsByTagName('input');
	} else {
		inputRefArray = element.getElementsByTagName('input');
	}

	for ( var i = 0; i < inputRefArray.length; i++) {
		var inputRef = inputRefArray[i];

		if (inputRef.type.substr(0, 8) == 'checkbox' && !inputRef.disabled) {
			inputRef.checked = checkBox.checked;
		}
	}

}

/**
 * Seta o foco no campo passado por parametro.
 * 
 */
function setFocusOnField(field) {
	var inputRefArray = document.getElementsByTagName('input');

	for ( var i = 0; i < inputRefArray.length; i++) {
		var inputRef = inputRefArray[i];

		if (inputRef.name.indexOf(field) > 0) {
			inputRef.focus();
		}
	}
}

/**
 * Marca ou Desmarca todos os inputs do tipo checkbox que est�o aninhados na
 * estrutura do elemento base (baseElement) passado por parametro. Se o
 * elemento base nao for passado, o valor � settado em todos os inputs do
 * documento.
 * 
 * @param baseElement -
 *            ID do elemento base que contem os inputs a serem "checkados"
 * 
 * @param newValue -
 *            valor (true/false) a ser settado nos inputs
 */
function checkUncheckAllByGroup(baseElement, newValue) {
	var inputRefArray;
	if (baseElement == null) {
		inputRefArray = document.getElementsByTagName('input');
	} else {
		inputRefArray = baseElement.getElementsByTagName('input');
	}

	for (var i = 0; i < inputRefArray.length; i++) {
		var inputRef = inputRefArray[i];

		if (inputRef.type.substr(0, 8) == 'checkbox') {
			inputRef.checked = newValue;
		}
	}
}

/**
 * Verifica se o evento foi o clique de uma tecla num�rica.
 *
 * @param evt
 * @returns {Boolean}
 */
function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    console.log(charCode);
    // Teclas "Home", "End" e direcionais s�o permitidas.
    if (charCode >= 35 && charCode <= 38) {
    	return true;
    }

    if (charCode > 31 && (charCode < 48 || charCode > 57)) {

    	return false;
    }

    return true;
}