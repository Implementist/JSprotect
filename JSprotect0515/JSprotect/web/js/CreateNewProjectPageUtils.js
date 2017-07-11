/**
 * Created by Implementist on 2017/6/23.
 */

function setDisabledOfComputationParam() {
    if (document.getElementsByName("chbComputation")[0].value === "unchecked") {
        document.getElementsByName("txtComputationParam")[0].removeAttribute("readonly");
        document.getElementsByName("chbComputation")[0].value = "checked";
    } else {
        document.getElementsByName("txtComputationParam")[0].setAttribute("readonly", "readonly");
        document.getElementsByName("chbComputation")[0].value = "unchecked";
    }
}

function setDisabledOfFlattenParams() {
    if (document.getElementsByName("chbControlFlowFlatten")[0].value === "unchecked") {
        document.getElementsByName("txtThresholdValue")[0].removeAttribute("readonly");
        document.getElementsByName("txtBlockSize")[0].removeAttribute("readonly");
        document.getElementsByName("chbControlFlowFlatten")[0].value = "checked";
    } else {
        document.getElementsByName("txtThresholdValue")[0].setAttribute("readonly", "readonly");
        document.getElementsByName("txtBlockSize")[0].setAttribute("readonly", "readonly");
        document.getElementsByName("chbControlFlowFlatten")[0].value = "unchecked";
    }
}

function reverseValue(element) {
    if (element.value === "unchecked")
        element.value = "checked";
    else
        element.value = "unchecked";
}

function changeMaxValue() {
    document.getElementsByName('txtBlockSize')[0].max = document.getElementsByName('txtThresholdValue')[0].value - 1;
}

function addReserveName() {
    var reserveName = document.getElementsByName('txtReserveName')[0].value;
    var elements = document.getElementsByClassName('lblReserveName');

    //如果已存在当前要添加的保留字，就直接返回
    for (var i = 0; i < elements.length; i++) {
        if (elements.item(i).textContent === reserveName) {
            return;
        }
    }

    //向最后一列后面添加保留字块
    $("#lastColumn").append('<div><label class="lblReserveName" style="border: 1px solid #B0B0B0;background: #FFFFFF;padding: 3px 5px;margin-top: 5px;margin-left: 105px;float: left">' + reserveName + '</label>' +
        '<img src="img/close.png" style="width: 25px; height: 25px; margin-left: 5px;margin-bottom:5px;float: left;margin-top: 5px;cursor: pointer" onclick="removeReserveName(this)"/><br></div>');
}

function removeReserveName(currentElement) {
    var parentNode = currentElement.parentNode;
    parentNode.parentNode.removeChild(parentNode);
}
