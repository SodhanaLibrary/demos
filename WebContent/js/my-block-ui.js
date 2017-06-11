var blockUiVar = 0;

function blockUi() {
	if(blockUiVar == 0) {
		$("#blockUiDiv").show();
		$("#blockUiBackdrop").show();	
	}
	blockUiVar++;
}

function unBlockUi() {
	if(blockUiVar == 1) {
		$("#blockUiDiv").hide();
	    $("#blockUiBackdrop").hide();
	}
	blockUiVar--;
}
